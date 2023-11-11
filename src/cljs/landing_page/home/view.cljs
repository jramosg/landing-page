(ns landing-page.home.view
  (:require [reagent-mui.material.unstable-grid-2 :refer [unstable-grid-2]]
            [reagent-mui.material.typography :refer [typography]]
            [reagent-mui.material.paper :refer [paper]]
            [reagent-mui.material.text-field :refer [text-field]]
            [reagent-mui.icons.visibility :refer [visibility]]
            [reagent-mui.icons.visibility-off :refer [visibility-off]]
            [reagent-mui.material.input-adornment :refer [input-adornment]]
            [reagent.core :as r]
            [reagent-mui.material.button :refer [button]]
            [reagent-mui.material.box :refer [box]]
            [reagent-mui.styles :as styles]
            [reagent-mui.material.stack :refer [stack]]))

(defn- text-btn [{:keys [label]}]
  [button {:color "secondary"}
   label])

(defn- pwd-visibility-icon []
  [input-adornment {:position "end"}
   [visibility]])

(defn- login-inputs []
  [:<>
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
                 :label "Password"}]]
   [text-btn {:label "Forgot password?"}]])

(defn- continue-btn []
  [button
   {:variant "contained"
    :full-width true
    ;:sx {:width 1}
    }
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
          :min-height "100vh"
          :width 1
          :justify-content "center"
          :px 8}
   [box {:component "img"
         :alt "company logo"
         :sx {:height "4rem"
              :width "4rem"}
         :src "assets/company_logo.png"}]
   [typography {:variant "h4"} "Login into your account"]
   [login-inputs]
   [continue-btn]
   [create-account-box]])

(defn- right-container []
  [box
   [typography {:variant "h3"
                :color "primary"
                :component "span"}
    "COMPANY NAME"]

   [box {:color "black"}
    "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Iaculis eu non diam phasellus vestibulum lorem sed risus ultricies.\n\n"
    [typography {:variant "body1"
                 :color "secondary"
                 :component "span"
                 :sx {:font-size "5rem"
                      :line-height 0}}
     "."]]])

(defn main []
  [unstable-grid-2 {:container true
                    :columns 2
                    :min-height "100vh"}
   [unstable-grid-2 {:sm 2 :md 1}
    [left-container]]
   [unstable-grid-2 {:bgcolor "primary.light"
                     :justify-content "center"
                     :display "flex"
                     :sm 2
                     :min-height "100vh"
                     :md 1
                     :p 4
                     :wrap "wrap"
                     :align-items "center"}
    [right-container]]])