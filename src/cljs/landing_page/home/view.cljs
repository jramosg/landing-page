(ns landing-page.home.view
  (:require [landing-page.context.i18n :as i18n]
            [reagent-mui.material.drawer :refer [drawer]]
            [reagent-mui.material.box :refer [box]]
            [reagent-mui.styles :as styles]
            [reagent.core :as r]
            [reagent-mui.icons.home :refer [home]]
            [reagent-mui.material.container :refer [container]]
            [reagent-mui.material.paper :refer [paper]]
            [reagent-mui.material.typography :refer [typography]]))

(def drawer-width 240)

(def my-drawer
  (styles/styled
    drawer
    (fn [{:keys [open? theme]}]
      (prn "open? " open?)
      #_(merge

        (if @open?
          {:color "red"
           :width drawer-width}
          {:color "blue"
           :background-color "red"
           :width drawer-width}))
      {"& .MuiDrawer-paper" {:width drawer-width}})))

(defn main []
  [box {:bgcolor "primary.light"
        :height 1
        :width 1
        :p 2
        :color "textOnLight.main"
        :display "flex"
        :justify-content "center"}
   [container
    [typography {:variant "h1"} (i18n/t :home)]
    [paper {:sx {:height "200px"
                 :p 4}}
     [typography {:variant "h3"} "a b c d "]]]])