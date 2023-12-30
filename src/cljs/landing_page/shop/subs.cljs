(ns landing-page.shop.subs
  (:require [landing-page.shop.constants :as constants]
            [re-frame.core :as rf]))

(def ^:private ^:const shop-items-mocked
  #_(let [colors ["green" "red" "blue" "yellow" "grey" "white" "black" "orange"]
          sizes (range 36 46 0.5)
          photo-url "https://api-prod-minimal-v510.vercel.app/assets/images/m_product/product_%s.jpg"]
      (vec (map-indexed (fn [id i]
                          (let [images [(format photo-url i) (format photo-url (inc i))]
                                discount (rand-nth (into (repeat 5 0) [10 20 30 40]))
                                price (rand-nth [50.99 60.99 99.99 105.99 40.99 180.00 ])]
                            {:images images
                             :price price
                             :discount discount
                             :price-with-discount (if (pos? discount)
                                                    (format "%.2f" (- price (/ (* price discount) 100)))
                                                    (str price))
                             :id id
                             :available-colors (vec (take (rand-nth [1 (inc (count colors))]) (shuffle colors)))
                             :available-sizes (vec (take (rand-int (count sizes)) (shuffle sizes)))}))
                        (range 1 25 2))))
  [{:images ["https://api-prod-minimal-v510.vercel.app/assets/images/m_product/product_1.jpg"
             "https://api-prod-minimal-v510.vercel.app/assets/images/m_product/product_2.jpg"],
    :price 105.99,
    :discount 40,
    :price-with-discount "63.59",
    :id 0,
    :available-colors ["green"],
    :available-sizes [37.5 39.5 43.5 45.5 43.0 38.0 42.5 39.0 41.0 44.0 37.0]}
   {:images ["https://api-prod-minimal-v510.vercel.app/assets/images/m_product/product_3.jpg"
             "https://api-prod-minimal-v510.vercel.app/assets/images/m_product/product_4.jpg"],
    :price 180.0,
    :discount 0,
    :price-with-discount "180.0",
    :id 1,
    :available-colors ["blue"],
    :available-sizes [37.5]}
   {:images ["https://api-prod-minimal-v510.vercel.app/assets/images/m_product/product_5.jpg"
             "https://api-prod-minimal-v510.vercel.app/assets/images/m_product/product_6.jpg"],
    :price 60.99,
    :discount 20,
    :price-with-discount "48.79",
    :id 2,
    :available-colors ["yellow"],
    :available-sizes [37.5 43.5 41.0 45.5 43.0 38.5 37.0 38.0]}
   {:images ["https://api-prod-minimal-v510.vercel.app/assets/images/m_product/product_7.jpg"
             "https://api-prod-minimal-v510.vercel.app/assets/images/m_product/product_8.jpg"],
    :price 50.99,
    :discount 10,
    :price-with-discount "45.89",
    :id 3,
    :available-colors ["green" "orange" "black" "blue" "grey" "red" "white" "yellow"],
    :available-sizes [41.0 44.5 37.5 36.5 38.5 38.0 41.5 43.5 40.0 45.0 40.5 39.0 45.5 42.5 39.5]}
   {:images ["https://api-prod-minimal-v510.vercel.app/assets/images/m_product/product_9.jpg"
             "https://api-prod-minimal-v510.vercel.app/assets/images/m_product/product_10.jpg"],
    :price 180.0,
    :discount 10,
    :price-with-discount "162.00",
    :id 4,
    :available-colors ["red"],
    :available-sizes [41.5 36.5 41.0 39.5 42.5 39.0]}
   {:images ["https://api-prod-minimal-v510.vercel.app/assets/images/m_product/product_11.jpg"
             "https://api-prod-minimal-v510.vercel.app/assets/images/m_product/product_12.jpg"],
    :price 180.0,
    :discount 40,
    :price-with-discount "108.00",
    :id 5,
    :available-colors ["black" "blue" "green" "grey" "red" "orange" "yellow" "white"],
    :available-sizes [36.5 37.0]}
   {:images ["https://api-prod-minimal-v510.vercel.app/assets/images/m_product/product_13.jpg"
             "https://api-prod-minimal-v510.vercel.app/assets/images/m_product/product_14.jpg"],
    :price 105.99,
    :discount 0,
    :price-with-discount "105.99",
    :id 6,
    :available-colors ["red"],
    :available-sizes [36.5 41.0 37.0 40.5 43.0 36 38.0 44.0 45.5 41.5 45.0 42.5 37.5 43.5 40.0 39.5 38.5 44.5]}
   {:images ["https://api-prod-minimal-v510.vercel.app/assets/images/m_product/product_15.jpg"
             "https://api-prod-minimal-v510.vercel.app/assets/images/m_product/product_16.jpg"],
    :price 40.99,
    :discount 0,
    :price-with-discount "40.99",
    :id 7,
    :available-colors ["white"],
    :available-sizes [44.5 36 45.0 36.5 43.0 39.5 37.5]}
   {:images ["https://api-prod-minimal-v510.vercel.app/assets/images/m_product/product_17.jpg"
             "https://api-prod-minimal-v510.vercel.app/assets/images/m_product/product_18.jpg"],
    :price 60.99,
    :discount 40,
    :price-with-discount "36.59",
    :id 8,
    :available-colors ["red"],
    :available-sizes [39.5 44.0 42.0 36.5 39.0 41.5 40.5 37.0 37.5 45.5 45.0]}
   {:images ["https://api-prod-minimal-v510.vercel.app/assets/images/m_product/product_19.jpg"
             "https://api-prod-minimal-v510.vercel.app/assets/images/m_product/product_20.jpg"],
    :price 105.99,
    :discount 40,
    :price-with-discount "63.59",
    :id 9,
    :available-colors ["yellow" "green" "blue" "white" "orange" "grey" "red" "black"],
    :available-sizes []}
   {:images ["https://api-prod-minimal-v510.vercel.app/assets/images/m_product/product_21.jpg"
             "https://api-prod-minimal-v510.vercel.app/assets/images/m_product/product_22.jpg"],
    :price 99.99,
    :discount 40,
    :price-with-discount "59.99",
    :id 10,
    :available-colors ["green"],
    :available-sizes [37.0 39.5 40.5 44.5 42.5 41.5 39.0 38.5 44.0 45.0 43.0 38.0 40.0 37.5 36.5 42.0]}
   {:images ["https://api-prod-minimal-v510.vercel.app/assets/images/m_product/product_23.jpg"
             "https://api-prod-minimal-v510.vercel.app/assets/images/m_product/product_24.jpg"],
    :price 99.99,
    :discount 0,
    :price-with-discount "99.99",
    :id 11,
    :available-colors ["green"],
    :available-sizes [36 38.0 36.5 43.5 44.0 44.5 40.0 41.0 39.0 42.0 38.5 37.5 45.0 42.5]}])

(rf/reg-sub
  ::shop-items
  (fn [_]
    shop-items-mocked))

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
  (prn "sort-m " sort-m)
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