(ns landing-page.shop.subs
  (:require [landing-page.shop.constants :as constants]
            [re-frame.core :as rf]))

(rf/reg-sub
 ::filters
 (fn [db]
   (get-in db constants/filters-path)))

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