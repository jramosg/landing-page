(ns landing-page.loading
  (:require [landing-page.util :as util]
            [re-frame.core :as rf]
            [reagent-mui.material.backdrop :refer [backdrop]]
            [reagent-mui.material.circular-progress :refer [circular-progress]]))

(rf/reg-sub
  ::loading?
  (fn [db]
    (:loading? db false)))

(defn- backdrop-z-index [theme]
  (inc (get-in (js->clj theme :keywordize-keys true) [:zIndex :drawer])))

(defn main []
  (when (util/listen [::loading?])
    [backdrop {:open true
               :sx {:z-index backdrop-z-index}}
     [circular-progress {:thickness 6 :size 130}]]))