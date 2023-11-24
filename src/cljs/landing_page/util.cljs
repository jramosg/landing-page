(ns landing-page.util
  (:require [re-frame.core :as rf]
            [reitit.frontend.easy :as rfe]))

(def ^:const company-name "Company name")

(defn field-value [e]
  (-> e .-target .-value))

(def listen (comp deref rf/subscribe))
(def >evt rf/dispatch)

(rf/reg-fx
  :navigate
  (fn [loc & [opts]]
    (rfe/navigate loc opts)))
