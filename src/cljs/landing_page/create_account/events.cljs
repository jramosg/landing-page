(ns landing-page.create-account.events
  (:require [landing-page.forms.constants :as constants]
            [re-frame.core :as rf]
            [landing-page.forms.events :as forms.events]))


(rf/reg-event-fx
  ::set-initial-submit-dispatched
  (fn [_ [_]]
    {:dispatch [::forms.events/set-initial-submit-dispatched constants/create-account-form-id]}))

(rf/reg-event-fx
  ::create-account
  (fn [{:keys [db]} [_]]
    {:db (assoc db :loading? true)
     :dispatch-later [{:ms 3000
                       :dispatch [::create-account-success]}]}))

(rf/reg-event-fx
  ::create-account-success
  (fn [{:keys [db]}]
    {:navigate :user-profile
     :db (dissoc db :loading?)}))