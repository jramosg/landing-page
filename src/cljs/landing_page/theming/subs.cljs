(ns landing-page.theming.subs
  (:require [clojure.string :as str]
            [re-frame.core :as rf]
            [landing-page.theming.constants :as constants]))

(rf/reg-sub
  ::theme
  (fn [db]
    (:theme db)))

(rf/reg-sub
  ::mode
  :<- [::theme]
  (fn [theme]
    (get theme :mode "light")))

(rf/reg-sub
  ::font-size
  (fn [db]
    (get-in db constants/font-size-path constants/default-font-size)))

(rf/reg-sub
  ::prefered-font-family
  (fn [db]
    (get-in db constants/font-family-path (first constants/default-font-family))))

(rf/reg-sub
  ::font-family
  :<- [::prefered-font-family]
  (fn [prefered-font-family]
    (str/join ", "
              (if prefered-font-family
                (->> constants/default-font-family
                     (remove #{prefered-font-family})
                     (cons prefered-font-family))
                constants/default-font-family))))

(rf/reg-sub
  ::primary-color
  (fn [db]
    (get-in db constants/primary-color-path constants/default-primary-color)))

(rf/reg-sub
  ::secondary-color
  (fn [db]
    (get-in db constants/secondary-color-path constants/default-secondary-color)))

(rf/reg-sub
  ::light-offset
  (fn [db]
    (get-in db constants/light-offset-path constants/default-light-offset)))

(def ^:private offset-to-0-1 #(* 0.01 %))
(rf/reg-sub
  ::light-offset-0-1
  :<- [::light-offset]
  (comp offset-to-0-1))

(rf/reg-sub
  ::dark-offset
  (fn [db]
    (get-in db constants/dark-offset-path constants/default-dark-offset)))

(rf/reg-sub
  ::dark-offset-0-1
  :<- [::dark-offset]
  (comp offset-to-0-1))