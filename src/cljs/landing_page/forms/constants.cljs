(ns landing-page.forms.constants)

(def root-db-path [:forms])
(def values-db-path (conj root-db-path :values))
(def flags-db-path (conj root-db-path :flags))
(def flag-initial-submit-dispatched? :initial-submit-dispatched?)

;IDS
(def create-account-form-id :create-account-form)