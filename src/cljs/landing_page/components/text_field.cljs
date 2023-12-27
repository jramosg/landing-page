(ns landing-page.components.text-field
  (:require [landing-page.context.i18n :as i18n]
            [landing-page.forms.events :as forms.events]
            [landing-page.forms.subs :as forms.subs]
            [landing-page.util :as util]
            [reagent-mui.icons.visibility :refer [visibility]]
            [reagent-mui.icons.visibility-off :refer [visibility-off]]
            [reagent-mui.material.icon-button :refer [icon-button]]
            [reagent-mui.material.text-field :refer [text-field]]
            [reagent-mui.material.tooltip :refer [tooltip]]
            [reagent.core :as r]))

(defn pwd-visible-icon-btn [state]
  (let [visible? (:pwd-visible? @state)]
    [tooltip {:title (i18n/t (if visible?
                               :hide-pwd
                               :show-pwd))}
     [icon-button
      {:on-click #(swap! state update :pwd-visible? not)}
      (if (:pwd-visible? @state)
        [visibility-off]
        [visibility])]]))

(defn- password-props [state]
  {:InputProps {:end-adornment
                (r/as-element
                 [pwd-visible-icon-btn state])}})

(defn- get-form-props [{:keys [form-id field-path field-spec validation-error-msg] :as form-opts}]
  (let [v (util/listen [::forms.subs/field-value form-id field-path])
        error? (util/listen [::forms.subs/show-validation-errors? form-id field-path field-spec])]
    (cond-> {:value v
             :callback-fn #(util/>evt [::forms.events/set-field-value form-id field-path %])
             :error error?}
      error? (assoc :helper-text (i18n/t validation-error-msg)))))

(defn my-text-field [{:keys [type _callback-fn] :as _props}
                     & [{:keys [_form-id _field-path _field-spec validation-error-msg] :as form-props}]]
  (let [pwd? (= type "password")
        state (r/atom (cond-> {}
                        pwd? (assoc :pwd-visible? false)))]
    (fn [props]
      (let [{:keys [callback-fn] :as props} (cond-> props
                                              form-props (merge (get-form-props form-props)))]
        [text-field
         (cond-> {:variant "outlined"
                  :full-width true
                  :on-change (fn [e]
                               (when callback-fn
                                 (callback-fn (util/field-value e))))}

           true
           (-> (merge props)
               (dissoc :callback-fn))

           pwd?
           (merge (password-props state))

           (and pwd? (:pwd-visible? @state))
           (assoc :type "text"))]))))