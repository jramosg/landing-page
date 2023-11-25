(ns landing-page.create-account.view
  (:require [cljs.spec.alpha :as s]
            [landing-page.context.i18n :as i18n]
            [landing-page.util :as util]
            [reagent-mui.material.paper :refer [paper]]
            [reagent-mui.material.box :refer [box]]
            [reagent-mui.material.stack :refer [stack]]
            [reagent-mui.material.container :refer [container]]
            [landing-page.components.text-field :refer [my-text-field]]
            [reagent-mui.material.typography :refer [typography]]
            [reagent-mui.material.form-control-label :refer [form-control-label]]
            [reagent-mui.material.checkbox :refer [checkbox]]
            [reagent.core :as r]
            [reagent-mui.material.button :refer [button]]
            [landing-page.forms.events :as forms.events]
            [landing-page.forms.constants :as forms.constants]
            [landing-page.create-account.subs :as create-account.subs]
            [reagent-mui.icons.error :refer [error]]
            [landing-page.create-account.events :as create-account.events]))

(s/def ::email #(re-matches #"^[\w-\.]+@([\w-]+\.)+[\w-]{2,4}$" (or % "")))

(s/def ::one-number #(re-matches #"(.*\d.*)" (or % "")))
(s/def ::one-upper-case #(re-matches #"(.*[A-Z].*)" (or % "")))
(s/def ::count-8 #(>= (count %) 8))
(s/def ::password (s/and seq ::count-8 ::one-upper-case ::one-number))

(s/def ::accepted-terms? true?)

(s/def ::form (s/keys :req-un [::email ::password ::accepted-terms?]))

(def ^:const ^:private form-id forms.constants/create-account-form-id)

(defn- email-container []
  (let [v (util/listen [::create-account.subs/email])
        error? (and (util/listen [::create-account.subs/inital-submit-dispatched?])
                    (not (s/valid? ::email v)))]
    [my-text-field {:label (i18n/t :email)
                    :error error?
                    :on-change #(util/>evt [::forms.events/set-field-value form-id :email %])
                    :helper-text (when error? (i18n/t :wrong-email-format))}]))

(defn- error-row [error-text]
  [stack {:align-items "center" :direction "row" :spacing 1 :color "error.main"}
   [error]
   [typography {:component "span"
                :variant "body2"}
    error-text]])

(defn- password-container []
  (let [initial-submit-dispatched? (util/listen [::create-account.subs/inital-submit-dispatched?])
        v (util/listen [::create-account.subs/password])
        error? (and initial-submit-dispatched? (not (s/valid? ::password v)))]
    [:div
     [my-text-field {:label (i18n/t :password)
                     :type "password"
                     :error error?
                     :value (util/listen [::create-account.subs/password])
                     :on-change #(util/>evt [::forms.events/set-field-value form-id :password %])
                     :helper-text (when-not initial-submit-dispatched?
                                    (i18n/t :password-requirement/full-desc))}]
     (when initial-submit-dispatched?
       [:<>
        (when (not (s/valid? ::count-8 v))
          [error-row (i18n/t :password-requirement/min-8-char)])
        (when (not (s/valid? ::one-upper-case v))
          [error-row (i18n/t :password-requirement/an-upper-case)])
        (when (not (s/valid? ::one-number v))
          [error-row (i18n/t :password-requirement/a-number)])])]))

(defn- on-create-account-click []
  (when-not (util/listen [::create-account.subs/inital-submit-dispatched?])
    (util/>evt [::forms.events/set-initial-submit-dispatched form-id]))
  (when (s/valid? ::form (util/listen [::create-account.subs/values]))
    (util/>evt [::create-account.events/create-account])))

(defn terms-input []
  (let [v (util/listen [::create-account.subs/accepted-terms?])]
    [:div
     [form-control-label
      {:label (i18n/t :agree-terms)
       :control (r/as-element [checkbox {:checked v
                                         :color "secondary"}])
       :on-change (fn [_ v]
                    (util/>evt [::forms.events/set-field-value form-id :accepted-terms? v]))}]
     (when (and (util/listen [::create-account.subs/inital-submit-dispatched?])
                (not (s/valid? ::accepted-terms? v)))
       [error-row (i18n/t :must-accept-terms)])]))

(defn main [_]
  (r/create-class
    {:component-will-unmount #(util/>evt [::forms.events/clean-form-state form-id])
     :reagent-render
     (fn []
       (prn "values :accepted-terms?" (util/listen [::create-account.subs/values]))
       [box {:height 1 :display "flex" :align-items "center"}
        [container {:max-width "sm"}
         [stack {:spacing 1 :direction "column"}
          [paper {:elevation 5
                  :sx {:padding 2}}
           [stack {:spacing 2
                   :direction "column"}
            [email-container]
            [password-container]
            [terms-input]
            [button {:variant "contained"
                     :on-click on-create-account-click}
             (i18n/t :create-account)]]]]]])}))