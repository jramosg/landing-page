(ns landing-page.shop.constants
  (:require [medley.core :as m]
            [reagent-mui.colors :as colors]))

(def ^:const shop-root-path [:shop])

(def ^:const filters-path (conj shop-root-path :filters))
(def ^:const sort-by-path (conj shop-root-path :sort-by))

(def ^:const colors-kw :colors)
(def ^:const filter-color-path (conj filters-path colors-kw))

(def ^:const sizes-kw :sizes)
(def ^:const filter-size-path (conj filters-path sizes-kw))

(def ^:const colors
  [{:color "green"
    :code (colors/green 500)}
   {:color "red"
    :code (colors/red 500)}
   {:color "blue"
    :code (colors/blue 500)}
   {:color "yellow"
    :code (colors/yellow 500)}
   {:color "orange"
    :code (colors/orange 500)}
   {:color "grey"
    :code (colors/grey 500)}
   {:color "black" :code "#000"}
   {:color "white" :code "#fff"}])

(def ^:const indexed-colors (m/index-by :color colors))