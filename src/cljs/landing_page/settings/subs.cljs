(ns landing-page.settings.subs
  (:require [landing-page.settings.constants :as constants]
            [re-frame.core :as rf]))

(rf/reg-sub
 ::prefered-language
 (fn [db]
   (get-in db constants/prefered-language-path)))