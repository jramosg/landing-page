(ns landing-page.context.i18n
  (:require [landing-page.dicts.main :as dicts]
            [landing-page.settings.events :as settings.events]
            [landing-page.settings.subs :as settings.sus]
            [landing-page.util :as util]
            [clojure.string :as str]))

(defn t
  [& args]
  (let [preferred-language (util/listen [::settings.sus/prefered-language])]
    (get-in dicts/dicts (cons preferred-language args)
            (get-in dicts/dicts (cons (:tongue/fallback dicts/dicts) args)))))

(defn- fetch-local-language []
  (-> (.. js/window -navigator -language)
      (str/split #"-")
      first
      keyword))

(defn start []
  (util/>evt [::settings.events/set-prefered-language (fetch-local-language)]))