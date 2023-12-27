(ns landing-page.shop.view
  (:require [landing-page.components.modals :as modals]
            [landing-page.components.text-field :refer [my-text-field]]
            [landing-page.context.i18n :as i18n]
            [landing-page.shop.filter-modal :as filter-modal]
            [landing-page.util :as util]
            [goog.string :as gs]
            [reagent-mui.icons.add :refer [add]]
            [reagent-mui.icons.fiber-manual-record :refer [fiber-manual-record]]
            [reagent-mui.icons.fiber-manual-record-outlined :refer [fiber-manual-record-outlined]]
            [reagent-mui.icons.filter-list :refer [filter-list]]
            [reagent-mui.icons.import-export :refer [import-export]]
            [reagent-mui.icons.search :refer [search] :rename {search search-icon}]
            [reagent-mui.material.box :refer [box]]
            [reagent-mui.material.button :refer [button]]
            [reagent-mui.material.card :refer [card]]
            [reagent-mui.material.card-actions :refer [card-actions]]
            [reagent-mui.material.card-content :refer [card-content]]
            [reagent-mui.material.card-media :refer [card-media]]
            [reagent-mui.material.container :refer [container]]
            [reagent-mui.material.grow :refer [grow]]
            [reagent-mui.material.icon-button :refer [icon-button]]
            [reagent-mui.material.stack :refer [stack]]
            [reagent-mui.material.toggle-button :refer [toggle-button]]
            [reagent-mui.material.toggle-button-group :refer [toggle-button-group]]
            [reagent-mui.material.typography :refer [typography]]
            [reagent-mui.styles :as styles]
            [reagent-mui.util :as mui.util]
            [reagent.core :as r]))

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

(defn- image-card [[img0 :as _images]]
  (let [active-img-atm (r/atom img0)]
    (fn [images]
      [card {:sx {:p 2 :max-width "400px"}
             :elevation 10}
       [card-media
        (doall
         (for [image images]
           [img-box {:key image
                     :height 1 :width 1
                     :display (if (= image @active-img-atm) "block" "none")}
            [grow {:in (= image @active-img-atm) :timeout 1000}
             [styled-img {:src image}]]
            [image-toogle-btns images active-img-atm]]))]
       [stack {:direction "row" :justify-content "space-between"}
        [card-content
         [:div "X €"]]
        [card-actions
         [icon-button {:sx add-sx}
          [add]]]]])))

(defn- search-bar []
  [my-text-field {:InputProps {:start-adornment (r/as-element [search-icon {:color "primary"}])}
                  :label "Buscar"
                  :full-width false}])

(defn- filter-btn []
  [button {:start-icon (r/as-element [filter-list])
           :variant "outlined"
           :on-click #(util/>evt [::modals/open filter-modal/props])}
   (i18n/t :filter)])

(defn- sort-by-btn []
  [button {:start-icon (r/as-element [import-export])
           :variant "outlined"}
   "Ordenar por"])

(defn- shop-header []
  [stack {:direction "row" :justify-content "space-between" :spacing 1 :align-items "center"}
   [search-bar]
   [stack {:direction "row" :spacing 1}
    [filter-btn]
    [sort-by-btn]]])

(defn main []
  [container {:max-width "xl" :sx {:padding-top 2 :padding-bottom 2}}
   [stack {:direction "column" :spacing 2}
    [typography {:variant "h4"} (i18n/t :shop)]
    [shop-header]
    [box {:sx {:display "grid"
               :mt 2
               :grid-template-columns {:xs "auto" :sm "repeat(2, auto)" :md "repeat(3, auto)" :lg "repeat(4, auto)" :xl "repeat(5, auto)"}
               :grid-gap (fn [theme] ((:spacing (mui.util/js->clj' theme)) 2))
               :justify-content "center"}}
     (doall
      (for [i (range 1 25 2)
            :let [images [(gs/format photo-url i) (gs/format photo-url (inc i))]]]
        ^{:key i}
        [image-card images]))]]])