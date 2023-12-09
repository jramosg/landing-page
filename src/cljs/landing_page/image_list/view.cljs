(ns landing-page.image-list.view
  (:require [reagent-mui.material.container :refer [container]]
            [reagent-mui.material.box :refer [box]]
            [reagent-mui.material.typography :refer [typography]]
            [reagent-mui.material.image-list :refer [image-list]]
            [reagent-mui.material.image-list-item :refer [image-list-item]]
            [reagent-mui.styles :as styles]))

(def ^:const ^:private images
  [{:img "assets/kixkur.jpg"}
   {:img "assets/kixkur5.jpg"
    :rows 2}
   {:img "assets/kixkur4.jpg"
    :rows 2}
   {:img "assets/kixkur-ondding.jpg"}
   {:img "assets/kixkur-bizkotxo.jpg"
    :rows 2}
   {:img "assets/kixkur3.jpg"
    :rows 2
    :cols 2}])

(def size 600)

(def ^:private image-comp
  (styles/styled
    "img"
    {:height "100%"
     :width "100%"
     :object-fit "cover"}))

(defn main []
  [box {:p 4}
   [typography {:variant "h1"
                :text-align "center"}
    "guau guau"]
   [image-list {:variant "quilted"
                :row-height size
                :cols 3
                :sx {:width 1}}
    (doall (for [{:keys [img title rows cols]
                  :or {cols 1 rows 1}}
                 images]
             [image-list-item {:key img
                               :rows rows
                               :cols cols
                               :sx {:height 1
                                    :width 1}}
              [image-comp {:alt title
                           :loading "lazy"
                           :src img #_(str img "?w=" (* size cols) "&h=" (* size rows) "&fit=crop&auto=format")
                           :src-set img}]]))]])