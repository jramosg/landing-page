(ns landing-page.components.menu-item
  (:require [reagent-mui.material.menu-item :refer [menu-item] :rename {menu-item mui-menu-item}]
            [reagent-mui.styles :as styles]))

(def menu-item
  (styles/styled
   mui-menu-item
   (fn [{:keys [theme]}]
     (let [primary-light (get-in theme [:palette :primary :light])]
       {"&.Mui-selected" {:background-color primary-light
                          "&:hover" {:background-color primary-light}
                          :color (get-in theme [:palette :text-on-light :main])}}))))