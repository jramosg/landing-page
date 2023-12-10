(ns landing-page.forms.subs
  (:require [landing-page.forms.constants :as constants]
            [landing-page.forms.db :as db]
            [cljs.spec.alpha :as s]
            [re-frame.core :as rf]))

(rf/reg-sub
 ::flags
 (fn [db]
   (get-in db constants/flags-db-path)))

(rf/reg-sub
 ::values
 (fn [db]
   (get-in db constants/values-db-path)))

(defn- get-value [values form-id field-path]
  (get-in values (cons form-id (db/get-field-path field-path))))

(rf/reg-sub
 ::form-values
 :<- [::values]
 (fn [values [_ form-id]]
   (form-id values)))

(rf/reg-sub
 ::field-value
 :<- [::values]
 (fn [forms-data [_ form-id field-path]]
   (get-value forms-data form-id field-path)))

(rf/reg-sub
 ::show-validation-errors?
 :<- [::values]
 :<- [::flags]
 (fn [[forms-values flags] [_ form-id field-path field-spec]]
   (and (get-in flags [form-id constants/flag-initial-submit-dispatched?])
        (not (s/valid? field-spec (get-value forms-values form-id (db/get-field-path field-path)))))))