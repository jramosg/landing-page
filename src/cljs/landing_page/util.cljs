(ns landing-page.util
  (:require [re-frame.core :as rf]
            [reitit.frontend.easy :as rfe]))

(def ^:const company-name "LOREM IPSUM COOP.")

(defn field-value [e]
  (-> e .-target .-value))

(def listen (comp deref rf/subscribe))
(def >evt rf/dispatch)

(defn dispatch-n [evts]
  (>evt [:dispatch-n evts]))

(rf/reg-fx
 :navigate
 (fn [loc & [opts]]
   (rfe/navigate loc opts)))

(rf/reg-event-fx
 :dispatch-n
 (fn [_ [_ evts]]
   {:fx (map
         (fn [evt]
           [:dispatch evt])
         evts)}))
