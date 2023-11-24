(ns landing-page.forms.subs
  (:require [cljs.spec.alpha :as s]
            [landing-page.forms.constants :as constants]
            [re-frame.core :as rf]))

(rf/reg-sub
  ::flags
  (fn [db]
    (get-in db constants/flags-db-path)))

(rf/reg-sub
  ::values
  (fn [db]
    (get-in db constants/values-db-path)))

(rf/reg-sub
  ::show-validation-errors?
  :<- [::values]
  :<- [::flags]
  (fn [[form-values flags] [_ form-id field-path field-spec]]
    (and (get-in flags [form-id constants/flag-initial-submit-dispatched?])
         (not (s/valid? field-spec (get-in form-values field-path))))))