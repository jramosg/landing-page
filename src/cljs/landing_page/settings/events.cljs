(ns landing-page.settings.events
  (:require [re-frame.core :as rf]
            [landing-page.settings.constants :as constants]))

(rf/reg-event-db
  ::set-prefered-language
  (fn [db [_ kw]]
    (assoc-in db constants/prefered-language-path kw)))
