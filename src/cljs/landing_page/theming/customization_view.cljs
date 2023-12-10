(ns landing-page.theming.customization-view
  (:require [landing-page.context.i18n :as i18n]
            [landing-page.theming.constants :as constants]
            [landing-page.theming.events :as events]
            [landing-page.theming.subs :as subs]
            [landing-page.util :as util]
            [reagent-mui.material.box :refer [box]]
            [reagent-mui.material.container :refer [container]]
            [reagent-mui.material.grid :refer [grid]]
            [reagent-mui.material.menu-item :refer [menu-item]]
            [reagent-mui.material.paper :refer [paper]]
            [reagent-mui.material.select :refer [select]]
            [reagent-mui.material.slider :refer [slider]]
            [reagent-mui.material.stack :refer [stack]]
            [reagent-mui.material.text-field :refer [text-field]]
            [reagent-mui.material.typography :refer [typography]]
            [reagent-mui.styles :as styles]))

(def my-paper
  (styles/styled
   paper
   (fn [{{:keys [spacing]} :theme}]
     {:padding (spacing 2)})))

(def ^:const min-font-size 10)
(def ^:const max-font-size 30)

(def ^:const ^:private font-size-marks
  (for [i (range 12 (inc max-font-size) 4)]
    {:label (str i "px")
     :value i}))

(def ^:const ^:private offset-marks
  (for [i (range 10 91 10)]
    {:label (str i "%")
     :value i}))

(defn- font-size-selector []
  [:div
   [typography {:variant "subtitle1"} (i18n/t :size)]
   [box {:p 2}
    [slider {:on-change (fn [_ e] (util/>evt [::events/change-font-size e]))
             :value (util/listen [::subs/font-size])
             :min min-font-size
             :max max-font-size
             :value-label-display "on"
             :valueLabelFormat (fn [e]
                                 (str e "px"))
             :marks font-size-marks}]]])

(defn font-style-selector []
  [:div
   [typography {:variant "subtitle1"} (i18n/t :font-family)]
   [select {:value (util/listen [::subs/prefered-font-family])
            :auto-width true
            :on-change (fn [e]
                         (util/>evt [::events/change-font-family (util/field-value e)]))}
    (doall (for [v constants/font-family-opts]
             [menu-item {:value v
                         :key v}
              [typography {:font-family v} (if (= v "Fashion Fetish")
                                             (i18n/t :nereas-fav)
                                             v)]]))]])

(defn- font-customization []
  [my-paper
   [typography {:variant "h3" :gutter-bottom true} (i18n/t :typography)]
   [stack {:direction "column" :spacing 2}
    [font-size-selector]
    [font-style-selector]
    [typography (i18n/t :text-view-desc)]]])

(defn- color-sample [s]
  [grid {:item true :xs 12 :sm 6}
   [box {:height "50px"
         :width 1
         :display "flex"}
    [box {:bgcolor (str s ".light")
          :flex 1}]
    [box {:bgcolor (str s ".main")
          :flex 1}]
    [box {:bgcolor (str s ".dark")
          :flex 1}]]])

(defn- offset-slider [props]
  [slider (merge {:min 0
                  :max 100
                  :step 5
                  :value-label-display "auto"
                  :marks offset-marks
                  :valueLabelFormat (fn [e] (str e "%"))}
                 props)])

(defn- color-customization []
  [my-paper
   [typography {:variant "h3"} (i18n/t :color)]
   [grid {:container true :direction "row" :spacing 2 :mt 1}
    [grid {:item true :xs 12 :sm 6}
     [text-field {:type "color"
                  :full-width true
                  :label (i18n/t :primary-color)
                  :on-change #(util/>evt [::events/change-primary-color (util/field-value %)])
                  :value (util/listen [::subs/primary-color])}]]
    [grid {:item true :xs 12 :sm 6}
     [text-field {:type "color"
                  :full-width true
                  :label (i18n/t :secondary-color)
                  :on-change #(util/>evt [::events/change-secondary-color (util/field-value %)])
                  :value (util/listen [::subs/secondary-color])}]]
    [grid {:item true :xs 12}
     [:div
      [typography {:variant "subtitle1"}
       "Light offset"]
      [offset-slider {:on-change (fn [_ e]
                                   (util/>evt [::events/change-light-offset e]))
                      :value (util/listen [::subs/light-offset])}]]]
    [grid {:item true :xs 12}
     [:div
      [typography {:variant "subtitle1"}
       "Dark offset"]
      [offset-slider {:on-change (fn [_ e]
                                   (util/>evt [::events/change-dark-offset e]))
                      :value (util/listen [::subs/dark-offset])}]]]
    [grid {:item true :xs 12}
     [box (i18n/t :color-examples)]
     [grid {:container true :spacing 2}
      [color-sample "primary"]
      [color-sample "secondary"]]]]])

(defn main []
  [box {:height 1
        :p 2}
   [container
    [stack {:direction "column" :spacing 2}
     [color-customization]
     [font-customization]]]])