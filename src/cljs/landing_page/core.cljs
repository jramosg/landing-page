(ns landing-page.core
  (:require
    [landing-page.home.app-bar :as app-bar]
    [landing-page.util :as util]
    [reagent.core :as r]
    [reagent.core :as reagent :refer [atom]]
    [reagent.dom :as rdom]
    [reagent.session :as session]
    [reitit.frontend :as reitit]
    [landing-page.home.view :as home.view]
    [reagent-mui.material.css-baseline :refer [css-baseline]]
    [reagent-mui.styles :as styles]
    [landing-page.theming.theme-provider :as theme-provider]
    [reitit.frontend.controllers :as rfc]
    [reitit.frontend.easy :as rfe]
    [landing-page.create-account.view :as create-account.view]
    [reagent-mui.material.box :refer [box]]
    [landing-page.loading :as loading]
    [landing-page.context.i18n :as i18n]))

(defonce match (r/atom nil))

(defn current-page []
  [:<>
   [css-baseline]
   [styles/theme-provider (theme-provider/main)
    [css-baseline]
    [loading/main]
    [box {:id "main-container" :height 1}
     [:> app-bar/main]
     (when @match
       (let [view (:view (:data @match))]
         [view @match]))]]])

(defn mount-root []
  (rdom/render
    [current-page]
    (.getElementById js/document "app")))

(defn log-fn [& params]
  (fn [_]
    (apply js/console.log params)))

(def routes
  (reitit/router
    ["/"
     [""
      {:name ::index
       :view home.view/main
       :controllers [{:start (log-fn "start" "frontpage controller")
                      :stop (log-fn "stop" "frontpage controller")}]}]
     ["create-account" {:name :create-account
                        :view create-account.view/main
                        :controllers [{:start (log-fn "start" "create-account")
                                       :stop (log-fn "stop" "create-account")}]}]
     ["user-profile" {:name :user-profile
                      :view (fn [] [:h1 "LOGGED IN!"])
                      :controllers [{:start (log-fn "start" "user-profile")
                                     :stop (log-fn "stop" "user-profile")}]}]]))

(defn init! []
  (rfe/start!
    routes
    (fn [new-match]
      (swap! match (fn [old-match]
                     (if new-match
                       (assoc new-match :controllers (rfc/apply-controllers (:controllers old-match) new-match))))))
    {:use-fragment true})
  (i18n/start)
  (mount-root))

(init!)
