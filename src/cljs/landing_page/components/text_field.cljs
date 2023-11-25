(ns landing-page.components.text-field
  (:require [landing-page.context.i18n :as i18n]
            [landing-page.util :as util]
            [reagent-mui.material.text-field :refer [text-field]]
            [reagent-mui.material.tooltip :refer [tooltip]]
            [reagent-mui.icons.visibility :refer [visibility]]
            [reagent-mui.icons.visibility-off :refer [visibility-off]]
            [reagent.core :as r]
            [reagent-mui.material.icon-button :refer [icon-button]]))

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
                  [pwd-visible-icon-btn state ])}})

(defn my-text-field [{:keys [type on-change] :as mui-props}]
  (let [pwd? (= type "password")
        state (r/atom (cond-> {}
                              pwd? (assoc :pwd-visible? false)))]
    (fn [props]
      [text-field
       (merge (cond-> {:variant "outlined"
                       :full-width true
                       :on-change (fn [e]
                                    (prn "ja ")
                                    (when on-change
                                      (on-change (util/field-value e))))}
                      pwd?
                      (merge (password-props state)))
              (cond-> (dissoc props :on-change)
                      (and pwd? (:pwd-visible? @state))
                      (assoc :type "text")))])))