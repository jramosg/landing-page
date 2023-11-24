(ns landing-page.home.app-bar
  (:require [landing-page.util :as util]
            [reagent-mui.components :as mui.components]
            [reagent.core :as r]
            [reagent-mui.icons.dark-mode :refer [dark-mode]]
            [reagent-mui.icons.light-mode :refer [light-mode]]
            [re-frame.core :as rf]
            [landing-page.theming.events :as theming.events]
            [landing-page.theming.subs :as theming.subs]
            [reagent-mui.icons.business :refer [business]]))

(defn- on-palette-mode-click [mode]
  (rf/dispatch [::theming.events/change-theme-kw :mode mode]))

(defn- palette-mode-icon-btn []
  [mui.components/stack
   {:align-items "center"
    :direction "row"}
   [light-mode {:sx {:color "common.white"}}]
   [mui.components/switch
    {:checked (= @(rf/subscribe [::theming.subs/mode]) "dark")
     :color "secondary"
     :on-change (fn [_ v]
                  (on-palette-mode-click (if v "dark" "light")))}]
   [dark-mode {:sx {:color "common.black"}}]])

(defn- app-bar-content []
  [:<>
   [mui.components/tooltip {:title util/company-name}
    [business]]
   [palette-mode-icon-btn]])

(defn main []
  (let [scroll-trigger (mui.components/use-scroll-trigger {:threshold 0})]
    (r/as-element
      [:<>
       [mui.components/app-bar
        {:elevation (if scroll-trigger 6 0)
         :enable-color-on-dark true}
        [mui.components/toolbar
         {:sx {:justify-content "space-between"}}
         [app-bar-content]]]
       [mui.components/toolbar]])))