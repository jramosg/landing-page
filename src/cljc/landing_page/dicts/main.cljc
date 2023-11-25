(ns landing-page.dicts.main
  (:require [landing-page.dicts.basque :as basque]
            [landing-page.dicts.spanish :as spanish]
            [landing-page.dicts.english :as english]))

(def ^:large-vars/data-var dicts
  {:en english/dicts
   :eu basque/dicts
   :es spanish/dicts
   :tongue/fallback :en})