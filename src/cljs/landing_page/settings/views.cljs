(ns landing-page.settings.views
  (:require [landing-page.components.menu-item :refer [menu-item]]
            [landing-page.context.i18n :as i18n]
            [landing-page.settings.events :as events]
            [landing-page.settings.subs :as subs]
            [landing-page.theming.events :as theming.events]
            [landing-page.theming.subs :as theming.subs]
            [landing-page.util :as util]
            [re-frame.core :as rf]
            [reagent-mui.icons.dark-mode :refer [dark-mode]]
            [reagent-mui.icons.language :refer [language]]
            [reagent-mui.icons.light-mode :refer [light-mode]]
            [reagent-mui.material.icon-button :refer [icon-button]]
            [reagent-mui.material.menu :refer [menu]]
            [reagent-mui.material.stack :refer [stack]]
            [reagent-mui.material.switch :refer [switch]]
            [reagent-mui.material.tooltip :refer [tooltip]]
            [reagent.core :as r]))

(def ^:const ^:private languages
  [{:label "English" :value :en}
   {:label "Euskera" :value :eu}
   {:label "Español" :value :es}])

(defn- on-palette-mode-click [mode]
  (util/>evt [::theming.events/change-theme-kw :mode mode]))

(defn theme-mode-switch []
  [stack
   {:align-items "center"
    :direction "row"}
   [light-mode {:sx {:color "common.white"}}]
   [switch
    {:checked (= @(rf/subscribe [::theming.subs/mode]) "dark")
     :color "secondary"
     :on-change (fn [_ v]
                  (on-palette-mode-click (if v "dark" "light")))}]
   [dark-mode {:sx {:color "common.black"}}]])

(defn- on-language-select [value]
  (util/>evt [::events/set-prefered-language value]))

(def language-selector-icon-btn
  (let [anchor-element (r/atom nil)
        close! #(reset! anchor-element nil)]
    (fn []
      (let [prefered-language (util/listen [::subs/prefered-language])]
        [:div
         [tooltip {:title (i18n/t :change-language)}
          [icon-button {:color "inherit"
                        :on-click #(reset! anchor-element (.-target %))}
           [language]]]
         [menu {:id "language-selector-menu"
                :anchor-el @anchor-element
                :open (boolean @anchor-element)
                :on-close close!
                :anchor-origin {:vertical "bottom" :horizontal "right"}
                :transform-origin {:vertical "top" :horizontal "right"}}
          (doall
           (for [{:keys [label value]} languages]
             [menu-item {:on-click (fn []
                                     (on-language-select value)
                                     (close!))
                         :selected (= value prefered-language)
                         :key (name value)
                         :sx {"&.Mui-selected" {:background-color "primary.light"
                                                "&:hover" {:background-color "primary.light"}
                                                :color "textOnLight.main"}}}
              label]))]]))))