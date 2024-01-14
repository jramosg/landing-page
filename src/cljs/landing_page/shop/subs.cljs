(ns landing-page.shop.subs
  (:require [landing-page.shop.constants :as constants]
            [re-frame.core :as rf]))

(rf/reg-sub
 ::loading?
 (fn [db]
   (get-in db constants/loading-path)))

(rf/reg-sub
 ::shop-items
 (fn [db]
   (get-in db constants/items-path)))

(rf/reg-sub
 ::filters
 (fn [db]
   (get-in db constants/filters-path)))

(rf/reg-sub
 ::sort-by
 (fn [db]
   (get-in db constants/sort-by-path)))

(rf/reg-sub
 ::sort-by-label
 :<- [::sort-by]
 (fn [sort-m] (:sort-label sort-m)))

(defn- filter-items [items {:keys [colors sizes]}]
  (filter
   (fn [{:keys [available-colors available-sizes]}]
     (and
      (or (empty? colors) (some colors available-colors))
      (or (empty? sizes) (some sizes available-sizes))))
   items))

(defn- desc [a b] (compare b a))

(defn- sort-items [items {:keys [sort-kw sorting-order] :as sort-m}]
  (sort-by
   (fn [item]
     (cond-> (sort-kw item)
       (= sort-kw :price-with-discount)
       js/parseFloat))
   (if (= sorting-order "desc")
     desc compare)
   items))

(rf/reg-sub
 ::filter+sorted-items
 :<- [::shop-items]
 :<- [::filters]
 :<- [::sort-by]
 (fn [[items filters sort-m]]
   (cond-> items
     (seq filters) (filter-items filters)
     sort-m (sort-items sort-m))))

(rf/reg-sub
 ::filter-count
 :<- [::filters]
 (fn [filters]
   (reduce-kv
    (fn [acc _ v]
      (cond
        (coll? v)
        (+ acc (count v))
        v
        (inc acc)))
    0 filters)))

(rf/reg-sub
 ::colors
 (fn [db]
   (get-in db constants/filter-color-path)))

(rf/reg-sub
 ::sorted-colors
 :<- [::colors]
 (fn [colors] (sort colors)))

(rf/reg-sub
 ::sizes
 (fn [db]
   (get-in db constants/filter-size-path)))

(rf/reg-sub
 ::sorted-sizes
 :<- [::sizes]
 (fn [sizes] (sort sizes)))