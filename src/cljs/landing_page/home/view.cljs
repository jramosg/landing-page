(ns landing-page.home.view
  (:require [reagent-mui.material.unstable-grid-2 :refer [unstable-grid-2]]
            [reagent-mui.material.typography :refer [typography]]
            [reagent-mui.material.paper :refer [paper]]
            [reagent-mui.material.text-field :refer [text-field]]
            [reagent-mui.icons.visibility :refer [visibility]]
            [reagent-mui.material.input-adornment :refer [input-adornment]]
            [reagent.core :as r]
            [reagent-mui.material.button :refer [button]]
            [reagent-mui.material.box :refer [box]]
            [reagent-mui.material.stack :refer [stack]]
            [landing-page.home.app-bar :as app-bar]
            [reagent-mui.icons.business :refer [business]]
            [landing-page.util :as util]
            [reagent-mui.material.collapse :refer [collapse]]
            [reitit.frontend.easy :as rfe]
            [landing-page.components.text-field :refer [my-text-field]]))

(def ^:const ^:private company-description
  "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Iaculis eu non diam phasellus vestibulum lorem sed risus ultricies.")

(defn- text-btn [{:keys [label] :as props}]
  [button (merge {:color "secondary"}
                 (dissoc props :label))
   label])

(defn- pwd-visibility-icon []
  [input-adornment {:position "end"}
   [visibility]])

(defn- login-text-field [props]
  [my-text-field (merge {:sx {:py 1}
                         :variant "filled"}
                        props)])

(defn- login-inputs []
  [paper {:sx {:p 2
               :display "flex"
               :flex-direction "column"}
          :elevation 2}
   [login-text-field {:label "Email or phone"}]
   [login-text-field {:label "Password"}]])

(defn- continue-btn []
  [button
   {:variant "contained"
    :full-width true
    :on-click #()}
   "Log in"])

(defn- create-account-box []
  [box {:display "flex"
        :align-items "center"}
   "Don't have an account?"
   [text-btn {:label "Create an account"
              :on-click (fn []
                          (rfe/navigate :create-account)) #_#(do (prn "repl")                 ;  (rfe/push-state :create-account)
                               (set! (.-location js/window) "create-account")
                               )
              ;:href "/create-account"
              }]])

(defn- left-container []
  [stack {:direction "column"
          :use-flex-gap true
          :spacing 2
          :width 1
          :height 1
          :justify-content "center"
          :px 8}
   [typography {:variant "h4"} "Login into your account"]
   [login-inputs]
   [box {:display "flex" :justify-content "flex-end" :mt -2}
    [text-btn {:label "Forgot password?"}]]
   [continue-btn]
   [create-account-box]])

(defn- write-effect [description-state]
  (let [{:keys [remaining-desc]}
        (swap! description-state (fn [{[next-char] :remaining-desc :as m}]
                                   (-> m
                                       (update :description str next-char)
                                       (update :remaining-desc rest))))]
    (when (seq remaining-desc)
      (js/setTimeout #(write-effect description-state) 10))))

(defn- right-container []
  (let [description-state (r/atom {:description ""
                                   :remaining-desc (seq company-description)})]
    (write-effect description-state)
    (fn []
      [:<>
       [typography {:variant "h3"
                    :color "primary"}
        util/company-name]
       [typography
        [typography {:component "span"
                     :sx {:color "common.black"}}
         (:description @description-state)]
        [typography {:component "span"
                     :color "secondary"
                     :sx {:font-size "6rem" :line-height 0}}
         "."]]])))

(defn main []

  [unstable-grid-2 {:container true
                    :columns 2
                    :height 1}
   [unstable-grid-2 {:sm 2 :md 1 :height 1}
    [left-container]]
   [unstable-grid-2 {:sm 2
                     :md 1
                     :bgcolor "primary.light"
                     :justify-content "center"
                     :display "flex"
                     :flex-direction "column"
                     :p 4
                     :height 1
                     :wrap "wrap"}
    [right-container]]])