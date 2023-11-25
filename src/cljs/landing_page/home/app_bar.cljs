(ns landing-page.home.app-bar
  (:require [landing-page.util :as util]
            [reagent-mui.components :as mui.components]
            [reagent-mui.material.stack :refer [stack]]
            [reagent.core :as r]
            [reagent-mui.icons.dark-mode :refer [dark-mode]]
            [reagent-mui.icons.light-mode :refer [light-mode]]
            [re-frame.core :as rf]
            [landing-page.theming.events :as theming.events]
            [landing-page.theming.subs :as theming.subs]
            [reagent-mui.icons.business :refer [business]]
            [landing-page.settings.views :as settings.views]))

(defn- app-bar-content []
  [:<>
   [mui.components/tooltip {:title util/company-name}
    [business]]
   [stack {:direction "row" :spacing 2}
    [settings.views/language-selector-icon-btn]
    [settings.views/theme-mode-switch]]])

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