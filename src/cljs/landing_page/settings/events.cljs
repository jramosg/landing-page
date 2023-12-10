(ns landing-page.settings.events
  (:require [landing-page.settings.constants :as constants]
            [re-frame.core :as rf]))

(rf/reg-event-db
 ::set-prefered-language
 (fn [db [_ kw]]
   (assoc-in db constants/prefered-language-path kw)))
