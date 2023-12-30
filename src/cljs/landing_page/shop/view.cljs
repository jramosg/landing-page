(ns landing-page.shop.view
  (:require [landing-page.components.modals :as modals]
            [landing-page.components.text-field :refer [my-text-field]]
            [landing-page.context.i18n :as i18n]
            [landing-page.shop.constants :as constants]
            [landing-page.shop.events :as events]
            [landing-page.shop.filter-modal :as filter-modal]
            [landing-page.shop.subs :as subs]
            [landing-page.util :as util]
            [reagent-mui.icons.add :refer [add]]
            [reagent-mui.icons.fiber-manual-record :refer [fiber-manual-record]]
            [reagent-mui.icons.fiber-manual-record-outlined :refer [fiber-manual-record-outlined]]
            [reagent-mui.icons.filter-list :refer [filter-list]]
            [reagent-mui.icons.import-export :refer [import-export]]
            [reagent-mui.icons.search :refer [search] :rename {search search-icon}]
            [reagent-mui.material.badge :refer [badge]]
            [reagent-mui.material.box :refer [box]]
            [reagent-mui.material.button :refer [button]]
            [reagent-mui.material.card :refer [card]]
            [reagent-mui.material.card-action-area :refer [card-action-area]]
            [reagent-mui.material.card-actions :refer [card-actions]]
            [reagent-mui.material.card-content :refer [card-content]]
            [reagent-mui.material.card-media :refer [card-media]]
            [reagent-mui.material.chip :refer [chip]]
            [reagent-mui.material.container :refer [container]]
            [reagent-mui.material.grow :refer [grow]]
            [reagent-mui.material.icon-button :refer [icon-button]]
            [reagent-mui.material.menu :refer [menu]]
            [reagent-mui.material.menu-item :refer [menu-item]]
            [reagent-mui.material.paper :refer [paper]]
            [reagent-mui.material.stack :refer [stack]]
            [reagent-mui.material.toggle-button :refer [toggle-button]]
            [reagent-mui.material.toggle-button-group :refer [toggle-button-group]]
            [reagent-mui.material.typography :refer [typography]]
            [reagent-mui.styles :as styles]
            [reagent-mui.util :as mui.util]
            [reagent.core :as r]
            [reitit.frontend.easy :as rfe]))

(def ^:private photo-url "https://api-prod-minimal-v510.vercel.app/assets/images/m_product/product_%s.jpg")

(def ^:private styled-img
  (styles/styled
   "img"
   {:width "100%"}))

(def ^:private img-box
  (styles/styled
   box
   {:position "relative"
    "& .image-selector" {:display "none"
                         :justify-content "center"
                         :bottom "6px"
                         :left 0
                         :right 0
                         :position "absolute"
                         :margin-left "auto"
                         :margin-right "auto"}
    ":hover .image-selector" {:display "flex"}}))

(defn- image-toogle-btns [images active-img-atm]
  [toggle-button-group {:class "image-selector"
                        :sx {:bgcolor "rgba(245, 245, 245, 0.3)"
                             :width "fit-content"}
                        :aria-label "Select image"
                        :value @active-img-atm
                        :exclusive true
                        :on-change (fn [_ v] (when v
                                               (reset! active-img-atm v)))}
   (doall
    (for [image images]
      [toggle-button {:key (str "selector-" image)
                      :sx {:p 0}
                      :aria-pressed (= @active-img-atm image)
                      :value image}
       (if (= @active-img-atm image)
         [fiber-manual-record {:sx {:color "common.white"}}]
         [fiber-manual-record-outlined {:sx {:color "common.black"}}])]))])

(def ^:const ^:private add-sx
  {:background-color "primary.main"
   :color "primary.contrastText"
   ":hover" {:background-color "primary.dark"}})

(def ^:const ^:private discount-box-style
  {:position "absolute"
   :top 1
   :left 1
   :bgcolor "error.main"
   :color "error.contrastText"
   :border-radius "50%"
   :height 40
   :px 0.25
   :width 40
   :display "flex"
   :align-items "center"
   :justify-content "center"
   :font-weight "bold"})

(defn- image-card [{[img0] :images id :id}]
  (let [active-img-atm (r/atom img0)
        to-item #(rfe/push-state :route/shop-item nil {:item id})]
    (fn [{:keys [images price price-with-discount discount available-colors]}]
      [card {:sx {:p 2 :max-width "400px"}
             :elevation 10}
       (doall
        (for [image images]
          [img-box {:key image
                    :display (if (= image @active-img-atm) "block" "none")}
           [grow {:in (= image @active-img-atm) :timeout 1000}
            [card-action-area
             {:on-click to-item}
             [card-media {:component "img" :image image :alt "Shop item"}]]]
           [image-toogle-btns images active-img-atm]
           (when (pos? discount)
             [box discount-box-style
              (str discount " %")])]))
       [stack {:direction "row" :justify-content "space-between"}
        [card-content
         [typography {:variant "h6"} (str "It" id)]
         (if (zero? discount)
           [:div price]
           [:<>
            [:del (str price " €")]
            [box {:color "error.main" :component "span"} (str " " price-with-discount " €")]])
         (if (seq available-colors)
           [stack {:direction "row"}
            (doall
              (for [color available-colors]
                [box {:key color
                      :height 15 :width 15
                      :border "2px solid"
                      :border-radius "50%"
                      :margin-left -0.6
                      :border-color "text.primary"
                      :bgcolor (get-in constants/indexed-colors [color :code])}]))])]
        [card-actions
         [icon-button {:sx add-sx
                       :on-click to-item}
          [add]]]]])))

(defn- search-bar []
  [my-text-field {:InputProps {:start-adornment (r/as-element [search-icon {:color "primary"}])}
                  :label (i18n/t :find)
                  :full-width false}])

(defn- filter-btn []
  [button {:start-icon (r/as-element
                        [filter-list])
           :variant "outlined"
           :on-click #(util/>evt [::modals/open (filter-modal/props)])}
   [badge {:badge-content (util/listen [::subs/filter-count])
           :show-zero false
           :color "secondary"}
    (i18n/t :filter)]])

(defn- sort-by-btn []
  (let [anchor-element (r/atom nil)
        close! (fn [] (reset! anchor-element nil))]
    (fn []
      (let [selected (util/listen [::subs/sort-by])]
        [:<>
         [button {:start-icon (r/as-element [import-export])
                  :variant "outlined"
                  :on-click #(reset! anchor-element (.-target %))}
          (if selected (i18n/t (util/listen [::subs/sort-by-label]))
                       (i18n/t :sort-by))]
         [menu {:id "language-selector-menu"
                :anchor-el @anchor-element
                :open (boolean @anchor-element)
                :on-close close!
                :anchor-origin {:vertical "bottom" :horizontal "right"}
                :transform-origin {:vertical "top" :horizontal "right"}}
          (doall
           (for [{:keys [sort-label] :as m} [#_{:sort-label :newest
                                              :sorting-order "desc"}
                                             {:sort-label :price-high-low
                                              :sort-kw :price-with-discount
                                              :sorting-order "desc"}
                                             {:sort-label :price-low-high
                                              :sort-kw :price-with-discount
                                              :sorting-order "asc"}
                                             {:sort-label :discount
                                              :sort-kw :discount
                                              :sorting-order "desc"}]]
             [menu-item {:on-click (fn []
                                     (util/>evt [::events/add-sort-by (when-not (= selected m)
                                                                        m)])
                                     (close!))
                         :selected (= selected m)
                         :key sort-label}
              (i18n/t sort-label)]))]]))))

(defn- shop-header []
  [stack {:direction "row" :justify-content "space-between" :spacing 1 :align-items "center"}
   [search-bar]
   [stack {:direction "row" :spacing 1 :align-items "center"}
    [filter-btn]
    [sort-by-btn]]])

(defn- show-filters-container [title items]
  [paper {:sx {:width "fit-content"
               :px 0.5 :py 1}
          :variant "outlined"}
   [stack {:direction "row" :spacing 1 :align-items "center" :flex-wrap "wrap" :use-flex-gap true}
    [typography {:variant "subtitle2"} (str title ": ")]
    items]])

(defn- colors-filters []
  (when-let [colors (seq (util/listen [::subs/sorted-colors]))]

    [show-filters-container
     (i18n/t :color)
     (doall
      (for [color colors]
        [chip {:key color
               :label (r/as-element [box {:width "20px"
                                          :height "20px"
                                          :border-radius "20px"
                                          :bgcolor (get-in constants/indexed-colors [color :code])}])
               :on-delete #(util/>evt [::events/delete-color color])}]))]))

(defn- size-filters []
  (when-let [sizes (seq (util/listen [::subs/sorted-sizes]))]
    [show-filters-container
     (i18n/t :size)
     (doall
       (for [size sizes]
         [chip {:key size :label size
                :on-delete #(util/>evt [::events/delete-size size])}]))]))

(defn- show-filters []
  [stack {:direction "row" :spacing 2 :align-items "center" :flex-wrap "wrap" :use-flex-gap true}
   [colors-filters]
   [size-filters]])

(defn main []
  [container {:max-width "xl" :sx {:padding-top 2 :padding-bottom 2}}
   [stack {:direction "column" :spacing 2}
    [typography {:variant "h3"} (i18n/t :shop)]
    [shop-header]
    [show-filters]
    [box {:sx {:display "grid"
               :mt 2
               :grid-template-columns {:xs "auto" :sm "repeat(2, auto)" :md "repeat(3, auto)" :lg "repeat(4, auto)" :xl "repeat(5, auto)"}
               :grid-gap (fn [theme] ((:spacing (mui.util/js->clj' theme)) 2))
               :justify-content "center"}}
     (doall
      (for [{:keys [id] :as item} (util/listen [::subs/filter+sorted-items])]
        ^{:key id} [image-card item]))]]])