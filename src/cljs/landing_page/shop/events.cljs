(ns landing-page.shop.events
  (:require [landing-page.shop.constants :as constants]
            [re-frame.core :as rf]))

(rf/reg-event-db
 ::apply-filters
 (fn [db [_ filters]]
   (assoc-in db constants/filters-path filters)))

(rf/reg-event-db
 ::delete-color
 (fn [db [_ v]]
   (update-in db constants/filter-color-path disj v)))

(rf/reg-event-db
 ::delete-size
 (fn [db [_ v]]
   (update-in db constants/filter-size-path disj v)))