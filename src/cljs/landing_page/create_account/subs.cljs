(ns landing-page.create-account.subs
  (:require [landing-page.forms.subs :as forms.subs]
            [landing-page.forms.constants :as forms.constants]
            [re-frame.core :as rf]))

(rf/reg-sub
  ::inital-submit-dispatched?
  :<- [::forms.subs/flags]
  (fn [flags]
    (get-in flags [forms.constants/create-account-form-id :initial-submit-dispatched?])))

(rf/reg-sub
  ::values
  :<- [::forms.subs/values]
  (fn [values]
    (forms.constants/create-account-form-id values)))

(rf/reg-sub
  ::email
  :<- [::values]
  (fn [values]
    (:email values)))

(rf/reg-sub
  ::password
  :<- [::values]
  (fn [values]
    (:password values)))

(rf/reg-sub
  ::accepted-terms?
  :<- [::values]
  (fn [values]
    (:accepted-terms? values false)))