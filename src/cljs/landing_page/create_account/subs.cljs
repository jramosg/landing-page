(ns landing-page.create-account.subs
  (:require [landing-page.forms.constants :as forms.constants]
            [landing-page.forms.subs :as forms.subs]
            [re-frame.core :as rf]))

(rf/reg-sub
 ::inital-submit-dispatched?
 :<- [::forms.subs/flags]
 (fn [flags]
   (get-in flags [forms.constants/create-account-form-id :initial-submit-dispatched?])))