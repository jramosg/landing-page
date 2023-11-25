(ns landing-page.settings.subs
  (:require [re-frame.core :as rf]
            [landing-page.settings.constants :as constants]))

(rf/reg-sub
  ::prefered-language
  (fn [db]
    (get-in db constants/prefered-language-path)))