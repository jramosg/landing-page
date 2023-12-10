(ns landing-page.dicts.main
  (:require [landing-page.dicts.basque :as basque]
            [landing-page.dicts.english :as english]
            [landing-page.dicts.spanish :as spanish]))

(def ^:large-vars/data-var dicts
  {:en english/dicts
   :eu basque/dicts
   :es spanish/dicts
   :tongue/fallback :en})