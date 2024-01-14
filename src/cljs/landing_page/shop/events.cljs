(ns landing-page.shop.events
  (:require [landing-page.shop.constants :as constants]
            [re-frame.core :as rf]))

(rf/reg-event-fx
 ::get-shop-items
 (fn [{:keys [db]}]
   {:db (-> db
            (assoc-in constants/loading-path true)
            (assoc-in constants/items-path (repeat 10 [])))
    :dispatch-later [{:ms 2000                             ;simulate server request
                      :dispatch [::on-get-shop-item-success]}]}))

(rf/reg-event-db
 ::on-get-shop-item-success
 (fn [db]
   (-> db
       (assoc-in constants/loading-path false)
       (assoc-in constants/items-path constants/shop-items-mocked))))

(rf/reg-event-db
 ::clear-appdb
 (fn [db]
   (dissoc db (first constants/shop-root-path))))

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

(rf/reg-event-db
 ::add-sort-by
 (fn [db [_ v]]
   (assoc-in db constants/sort-by-path v)))