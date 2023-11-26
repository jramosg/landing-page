(ns landing-page.home.app-bar
  (:require [reagent-mui.components :as mui.components]
            [reagent-mui.material.stack :refer [stack]]
            [reagent.core :as r]
            [reagent-mui.icons.business :refer [business]]
            [reagent-mui.styles :as styles]

            [landing-page.util :as util]
            [landing-page.settings.views :as settings.views]))

(defn- app-bar-content []
  [:<>
   [mui.components/tooltip {:title util/company-name}
    [business]]
   [stack {:direction "row" :spacing 2}
    [settings.views/language-selector-icon-btn]
    [settings.views/theme-mode-switch]]])

(def ^:private offset (styles/styled "div"
                           (fn [{:keys [theme]}]
                             (get-in theme [:mixins :toolbar]))))
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
       [offset {:id "appbar-offset"}]])))