(ns landing-page.landing.view
  (:require [landing-page.components.text-field :refer [my-text-field]]
            [landing-page.context.i18n :as i18n]
            [landing-page.util :as util]
            [reagent-mui.icons.visibility :refer [visibility]]
            [reagent-mui.material.box :refer [box]]
            [reagent-mui.material.button :refer [button]]
            [reagent-mui.material.container :refer [container]]
            [reagent-mui.material.grid :refer [grid]]
            [reagent-mui.material.grow :refer [grow]]
            [reagent-mui.material.input-adornment :refer [input-adornment]]
            [reagent-mui.material.paper :refer [paper]]
            [reagent-mui.material.stack :refer [stack]]
            [reagent-mui.material.typography :refer [typography]]
            [reagent.core :as r]
            [reitit.frontend.easy :as rfe]))

(defn- text-btn [{:keys [label] :as props}]
  [button (merge {:color "secondary"}
                 (dissoc props :label))
   label])

(defn- pwd-visibility-icon []
  [input-adornment {:position "end"}
   [visibility]])

(defn- login-text-field [props]
  [my-text-field (merge {:margin "dense"}
                        props)])

(defn- login-inputs []
  [paper {:sx {:p 2
               :display "flex"
               :flex-direction "column"}
          :elevation 5}
   [login-text-field {:label (i18n/t :email)}]
   [login-text-field {:label (i18n/t :password)}]])

(defn- continue-btn []
  [button
   {:variant "contained"
    :full-width true
    :on-click #(rfe/navigate :route/home)}
   (i18n/t :login/btn)])

(defn- create-account-box []
  [box {:display "flex"
        :align-items "center"}
   (i18n/t :login/not-account?)
   [text-btn {:label (i18n/t :create-account)
              :on-click (fn [] (rfe/navigate :route/create-account))}]])

(defn- left-container []
  [container {:max-width "sm"}
   [stack {:direction "column"
           :use-flex-gap true
           :spacing 2
           :width 1
           :height 1
           :justify-content "center"}
    [typography {:variant "h4"} (i18n/t :login/desc)]
    [login-inputs]
    [box {:display "flex" :justify-content "flex-end" :mt -2}
     [text-btn {:label (i18n/t :forgot-password?)}]]
    [continue-btn]
    [create-account-box]]])

(defn- write-effect [description-state]
  (let [{:keys [remaining-desc]}
        (swap! description-state (fn [{[next-char] :remaining-desc :as m}]
                                   (-> m
                                       (update :description str next-char)
                                       (update :remaining-desc rest))))]
    (if (seq remaining-desc)
      (js/setTimeout #(write-effect description-state) 30)
      (swap! description-state assoc :finished? true))))

(defn- right-container []
  (when (util/listen [:landing-page.settings.subs/prefered-language])
    (let [description-state (r/atom {:description ""
                                     :remaining-desc (seq (i18n/t :company-details/landing-desc))})]
      (write-effect description-state)
      (fn []
        [grow {:in true :timeout 2000}
         [:div#landing-company-details
          [typography {:variant "h3"
                       :color "primary"}
           util/company-name]
          [typography
           [typography {:component "span"
                        :sx {:color "common.black"}}
            (if-not (:finished? @description-state)
              (:description @description-state)
              (i18n/t :company-details/landing-desc))]
           [typography {:component "span"
                        :color "secondary"
                        :sx {:font-size "6rem" :line-height 0}}
            "."]]]]))))

(defn main []
  [grid {:container true
         :height 1}
   [grid {:item true :sm 12 :md 6 :height 1
          :align-items "center"
          :justify-content "center"
          :width 1
          :display "flex"}
    [left-container]]
   [grid {:sm 12
          :md 6
          :width 1
          :bgcolor "primary.light"
          :justify-content "center"
          :display "flex"
          :flex-direction "column"
          :p 4
          :height 1
          :item true}
    [right-container]]])