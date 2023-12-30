(ns landing-page.shop.shop-item.view
  (:require [reagent-mui.material.container :refer [container]]
            [reagent-mui.material.grow :refer [grow]]
            [reagent-mui.material.stack :refer [stack]]
            [reagent-mui.styles :as styles]))

(def ^:private styled-img (styles/styled "img" {:height "100%"
                                                :max-height "400px"}))

(defn main []
  [stack {:justify-content "center"
          :align-items "center"
          :height 1}
   [grow {:in true :timeout 3000}
    [styled-img {:src "assets/fantasma-1.gif"}]]])