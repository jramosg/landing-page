(ns landing-page.context.i18n
  (:require [clojure.string :as str]
            [landing-page.dicts.main :as dicts]
            [landing-page.util :as util]
            [landing-page.settings.events :as settings.events]
            [landing-page.settings.subs :as settings.sus]))


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
  (when-not (util/listen [::settings.sus/prefered-language])
    (util/>evt [::settings.events/set-prefered-language (fetch-local-language)])))