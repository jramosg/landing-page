(ns landing-page.theming.subs
  (:require [re-frame.core :as rf]))

(rf/reg-sub
  ::theme
  (fn [db]
    (:theme db)))

(rf/reg-sub
  ::mode
  :<- [::theme]
  (fn [theme]
    (get  theme :mode "light")))