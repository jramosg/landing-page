(ns landing-page.forms.events
  (:require [landing-page.forms.constants :as constants]
            [landing-page.forms.db :as db]
            [re-frame.core :as rf]))

(rf/reg-event-db
 ::set-flag-value
 (fn [db [_ form-id flag new-value]]
   (assoc-in db (conj constants/flags-db-path form-id flag) new-value)))

(rf/reg-event-fx
 ::set-initial-submit-dispatched
 (fn [_ [_ form-id]]
   {:dispatch [::set-flag-value form-id constants/flag-initial-submit-dispatched? true]}))

(rf/reg-event-db
 ::clean-form-state
 (fn [db [_ form-id]]
   (-> db
       (update-in constants/values-db-path dissoc form-id)
       (update-in constants/flags-db-path dissoc form-id))))

(rf/reg-event-db
 ::set-field-value
 (fn [db [_ form-id field-path new-value]]
   (assoc-in db (vec (concat constants/values-db-path
                             (cons form-id (db/get-field-path field-path))))
             new-value)))