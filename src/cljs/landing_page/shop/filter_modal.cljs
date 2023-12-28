(ns landing-page.shop.filter-modal
  (:require [landing-page.components.modals :as modals]
            [landing-page.context.i18n :as i18n]
            [landing-page.shop.constants :as constants]
            [landing-page.shop.events :as events]
            [landing-page.shop.subs :as subs]
            [landing-page.util :as util]
            [reagent-mui.colors :as colors]
            [reagent-mui.icons.check :refer [check]]
            [reagent-mui.material.box :refer [box]]
            [reagent-mui.material.button :refer [button]]
            [reagent-mui.material.chip :refer [chip]]
            [reagent-mui.material.dialog-actions :refer [dialog-actions]]
            [reagent-mui.material.dialog-content :refer [dialog-content]]
            [reagent-mui.material.divider :refer [divider]]
            [reagent-mui.material.stack :refer [stack]]
            [reagent-mui.material.typography :refer [typography]]
            [reagent-mui.styles :as styles]
            [reagent.core :as r]))

(defn- title+content-wrapper [title content]
  [box {:px 2.5}
   [typography {:variant "subtitle2" :gutter-bottom true}
    title]
   content])

(def ^:private color-size-grid
  (styles/styled
   "div"
   (fn [{{:keys [spacing]} :theme}]
     {:grid-template-columns "repeat(6, auto)"
      :grid-gap (spacing 1)
      :display "grid"
      :justify-content "space-between"})))

(def ^:private circle-size "40px")

(def ^:private styled-button
  (styles/styled
   button
   {:shouldForwardProp (fn [props] (not (contains? #{"bgcolor" "selected?"} props)))}
   (fn [{:keys [theme bgcolor selected?]}]
     (cond->
      {:border-radius circle-size
       :height circle-size
       :border "2px solid"
       :width circle-size
       :min-width circle-size}
       bgcolor (assoc :background-color bgcolor
                      :color ((get-in theme [:palette :get-contrast-text]) bgcolor)
                      ":hover" {:background-color bgcolor}
                      :border-color (if selected?
                                      (get-in theme [:palette :secondary :main])
                                      (get-in theme [:palette :text :primary])))))))

(defn- update-filter-kw [state kw v]
  (swap! state update kw (fn [s]
                           ((if (s v) disj conj)
                            s v))))

(defn- color-selector [state]
  [title+content-wrapper
   (i18n/t :color)
   [color-size-grid
    (doall
     (for [color [colors/green colors/red colors/blue colors/yellow colors/grey {500 "#000"} {500 "#fff"} colors/orange]
           :let [color (color 500)
                 selected? ((:colors @state) color)]]
       [styled-button {:key color
                       :bgcolor color
                       :selected? ((:colors @state) color)
                       :on-click #(update-filter-kw state :colors color)} ""
        (when selected? [check])]))]])

(defn- size-selector [state]
  [title+content-wrapper
   (i18n/t :size)
   [color-size-grid
    (doall
     (for [size (range 36 46 0.5)]
       [styled-button {:key size
                       :color (if ((:sizes @state) size)
                                "secondary" "inherit")
                       :on-click #(update-filter-kw state :sizes size)}
        size]))]])

(defn- modal-content []
  (let [state (r/atom (or (util/listen [::subs/filters])
                          {constants/sizes-kw #{}
                           constants/colors-kw #{}}))]
    (fn []
      [:<>
       [dialog-content
        [stack {:spacing 2}
         [color-selector state]
         [divider]
         [size-selector state]]]
       [dialog-actions {:sx {:justify-content "center"}}
        [button {:variant "outlined"
                 :on-click #(util/>evt [::modals/kill])}
         (i18n/t :close-modal)]
        [button {:variant "contained"
                 :on-click #(util/dispatch-n [[::modals/kill]
                                              [::events/apply-filters @state]])}
         (i18n/t :filter)]]])))

(defn- props []
  {:modal-type "drawer"
   :dialog-title (i18n/t :filter)
   :dialog-content+actions-render-fn modal-content})