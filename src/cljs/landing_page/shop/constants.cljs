(ns landing-page.shop.constants)

(def ^:const shop-root-path [:shop])

(def ^:const filters-path (conj shop-root-path :filters))
(def ^:const sort-by-path (conj shop-root-path :sort-by))

(def ^:const colors-kw :colors)
(def ^:const filter-color-path (conj filters-path colors-kw))

(def ^:const sizes-kw :sizes)
(def ^:const filter-size-path (conj filters-path sizes-kw))