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
            [landing-page.home.app-bar :as app-bar]))

(def ^:const ^:private company-description
  "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Iaculis eu non diam phasellus vestibulum lorem sed risus ultricies.")

(defn- text-btn [{:keys [label]}]
  [button {:color "secondary"}
   label])

(defn- pwd-visibility-icon []
  [input-adornment {:position "end"}
   [visibility]])

(defn- login-inputs []
  [paper {:sx {:p 2
               :display "flex"
               :flex-direction "column"}
          :elevation 2}
   [text-field {:sx {:py 1}
                :variant "filled"
                :label "Email or phone"}]
   [text-field {:sx {:py 1}
                :variant "filled"
                :InputProps {:end-adornment (r/as-element [pwd-visibility-icon])}
                :label "Password"}]
   ])

(defn- continue-btn []
  [button
   {:variant "contained"
    :full-width true}
   "Log in"])

(defn- create-account-box []
  [box {:display "flex"
        :align-items "center"}
   "Don't have an account?"
   [text-btn {:label "Create an account"}]])

(defn- left-container []
  [stack {:direction "column"
          :use-flex-gap true
          :spacing 2
          :width 1
          :height 1
          :justify-content "center"
          :px 8}
   [box {:component "img"
         :alt "company logo"
         :sx {:height "4rem"
              :width "4rem"}
         :src "assets/company_logo.png"}]
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
        "COMPANY NAME"]
       [typography
        [typography {:component "span"
                     :sx {:color "common.black"}}
         (:description @description-state)]
        [typography {:component "span"
                     :color "secondary"
                     :sx {:font-size "5rem" :line-height 0}}
         "."]]])))

(defn main []
  [stack {:height 1}
   [:> app-bar/main]
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
     [right-container]]]])