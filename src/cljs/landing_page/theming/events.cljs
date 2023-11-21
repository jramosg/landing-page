(ns landing-page.theming.events
  (:require [re-frame.core :as rf]))

(rf/reg-event-db
  ::change-theme-kw
  (fn [db [_ kw v]]
    (assoc-in db [:theme kw] v)))
