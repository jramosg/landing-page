(ns landing-page.components.modals
  (:require [landing-page.util :as util]
            [re-frame.core :as rf]
            [reagent-mui.icons.close :refer [close]]
            [reagent-mui.material.dialog :refer [dialog]]
            [reagent-mui.material.dialog-actions :refer [dialog-actions]]
            [reagent-mui.material.dialog-content :refer [dialog-content]]
            [reagent-mui.material.dialog-title :refer [dialog-title] :rename {dialog-title mui-dialog-title}]
            [reagent-mui.material.drawer :refer [drawer]]
            [reagent-mui.material.icon-button :refer [icon-button]]
            [reagent-mui.material.stack :refer [stack]]
            [reagent-mui.styles :as styles]))

(rf/reg-event-db
 ::open
 (fn [db [_ {:keys [_modal-type _modal-props _content-render-fn _dialog-title _dialog-title-render-fn _dialog-actions-render-fn] :as modal}]]
   (assoc db :modal modal)))

(rf/reg-event-db
 ::kill
 (fn [db [_]]
   (dissoc db :modal)))

(rf/reg-sub
 ::modal
 (fn [db] (:modal db)))

(defn- dialog-title-comp [{:keys [dialog-title dialog-title-render-fn]}]
  (cond
    dialog-title [mui-dialog-title dialog-title]
    dialog-title-render-fn [dialog-title-render-fn]))

(def ^:private drawer-as-modal
  (styles/styled
   drawer
   (fn [{:keys [theme]}]
     {:z-index (+ (get-in theme [:z-index :drawer]) 2)
      "& .MuiDrawer-paper" {((get-in theme [:breakpoints :down]) "xs") {:width "100vw"}
                            :width "400px"}})))

(defn- drawer-comp []
  (let [{:keys [modal-type modal-props dialog-content+actions-render-fn] :as modal}
        (util/listen [::modal])]
    [drawer-as-modal
     (merge (merge {:open (= modal-type "drawer")
                    :anchor "right"
                    :variant "temporary"
                    :on-close #(util/>evt [::kill])}
                   modal-props))
     [stack {:direction "row" :spacing 1 :justify-content "space-between" :align-items "center"}
      [dialog-title-comp modal]
      [icon-button {:on-click #(util/>evt [::kill])} [close]]]
     (when dialog-content+actions-render-fn
       [dialog-content+actions-render-fn])]))

(defn modal-comp []
  [drawer-comp])