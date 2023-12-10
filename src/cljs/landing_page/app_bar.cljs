(ns landing-page.app-bar
  (:require [landing-page.context.i18n :as i18n]
            [landing-page.settings.views :as settings.views]
            [landing-page.util :as util]
            [reagent-mui.icons.account-circle-outlined :refer [account-circle-outlined]]
            [reagent-mui.icons.app-settings-alt :refer [app-settings-alt]]
            [reagent-mui.icons.arrow-back :refer [arrow-back]]
            [reagent-mui.icons.business :refer [business]]
            [reagent-mui.icons.emoji-people :refer [emoji-people]]
            [reagent-mui.icons.home :refer [home]]
            [reagent-mui.icons.logout :refer [logout]]
            [reagent-mui.icons.menu :as icons.menu]
            [reagent-mui.icons.notifications-outlined :refer [notifications-outlined]]
            [reagent-mui.icons.pets :refer [pets]]
            [reagent-mui.material.app-bar :refer [app-bar]]
            [reagent-mui.material.avatar :refer [avatar]]
            [reagent-mui.material.badge :refer [badge]]
            [reagent-mui.material.box :refer [box]]
            [reagent-mui.material.divider :refer [divider]]
            [reagent-mui.material.drawer :refer [drawer]]
            [reagent-mui.material.icon-button :refer [icon-button]]
            [reagent-mui.material.list :refer [list]]
            [reagent-mui.material.list-item :refer [list-item]]
            [reagent-mui.material.list-item-button :refer [list-item-button]]
            [reagent-mui.material.list-item-icon :refer [list-item-icon]]
            [reagent-mui.material.list-item-text :refer [list-item-text]]
            [reagent-mui.material.menu :refer [menu]]
            [reagent-mui.material.menu-item :refer [menu-item]]
            [reagent-mui.material.stack :refer [stack]]
            [reagent-mui.material.toolbar :refer [toolbar]]
            [reagent-mui.material.tooltip :refer [tooltip]]
            [reagent-mui.material.use-scroll-trigger :refer [use-scroll-trigger]]
            [reagent-mui.styles :as styles]
            [reagent.core :as r]
            [reitit.frontend.easy :as rfe]))

(def ^:private drawer-width 240)

(def styled-avatar
  (styles/styled
   avatar
   (fn [{:keys [theme]}]
     {":hover" {:cursor "pointer"
                :box-shadow (get-in theme [:shadows 5])}})))

(defn- user-menu [anchor-el-atom]
  (let [handle-close #(reset! anchor-el-atom nil)]
    [menu {:open (boolean @anchor-el-atom)
           :anchor-el @anchor-el-atom
           :id "user-profile-menu"
           :Menu-list-props {:aria-labelledby "user-avatar"}
           :on-close handle-close}
     [menu-item {:on-click handle-close}
      [list-item-icon [account-circle-outlined]]
      [list-item-text (i18n/t :my-profile)]]
     [menu-item {:on-click handle-close}
      [list-item-icon [badge {:badge-content 2 :color "secondary"} [notifications-outlined]]]
      [list-item-text (i18n/t :notifications)]]]))

(defn- avatar+menu []
  (let [anchor-el-atom (r/atom nil)]
    (fn []
      [:<>
       [badge {:badge-content 2 :color "secondary"}
        [styled-avatar {:src "assets/good_am.jpg"
                        :id "user-avatar"
                        :aria-controls (when @anchor-el-atom "user-profile-menu")
                        :aria-expanded (when @anchor-el-atom true)
                        :aria-haspopup true
                        :on-click #(reset! anchor-el-atom (.-target %))}]]
       [user-menu anchor-el-atom]])))

(defn- app-bar-content [sidebar-open?]
  [:<>
   (when (util/listen [:landing-page.core/logged-in?])
     [stack {:direction "row" :spacing {:xs 1 :sm 2} :align-items "center"}
      (when-not @sidebar-open?
        [box (cond-> {:sx {:display {:xs "none" :sm "block"}}}
               @sidebar-open? (assoc :visibility "hidden"))
         [icon-button {:on-click #(swap! sidebar-open? not)
                       :color "inherit"}
          [icons.menu/menu]]])
      [avatar+menu]])
   [tooltip {:title util/company-name}
    [business]]
   [stack {:direction "row" :spacing {:xs 1 :sm 2}}
    [settings.views/language-selector-icon-btn]
    [settings.views/theme-mode-switch]]])

(def ^:private offset
  (styles/styled "div"
                 (fn [{:keys [theme]}]
                   (get-in theme [:mixins :toolbar]))))

(def my-appbar
  (styles/styled
   app-bar
   {:shouldForwardProp (fn [props] (not= props "sidebarOpen?"))}
   (fn [{{{:keys [create duration] {:keys [sharp]} :easing} :transitions} :theme :keys [theme sidebar-open?] :as m}]
     (cond-> {:z-index (inc (get-in theme [:z-index :drawer]))
              :transition (create (clj->js '("width" "margin"))
                                  (clj->js {:easing sharp
                                            :duration (:leaving-screen duration)}))}
       sidebar-open?
       (assoc :margin-left drawer-width
              :width (str "calc(100% - " drawer-width "px)")
              :transition (create (clj->js '("width" "margin"))
                                  (clj->js {:easing sharp
                                            :duration (:entering-screen duration)})))))))

(defn- open-drawer-props [{{:keys [create duration] {:keys [sharp]} :easing} :transitions}]
  {:width drawer-width
   :transition (create "width" (clj->js {:easing sharp :duration (:entering-screen duration)}))
   :overflow-x "hidden"})

(defn- closed-drawer-props [{{:keys [create duration] {:keys [sharp]} :easing} :transitions
                             :keys [spacing breakpoints]}]
  (prn "break" breakpoints)
  {:width (str "calc(" (spacing 7) " + 1px)")
   ((:up breakpoints) "sm") {:width (str "calc(" (spacing 8) " + 1px)")}
   :transition (create "width" (clj->js {:easing sharp :duration (:leaving-screen duration)}))
   :overflow-x "hidden"})

(def my-drawer
  (styles/styled
   drawer
   {:shouldForwardProp (fn [props] (not= props "sidebarOpen?"))}
   (fn [{:keys [sidebar-open? theme]}]
     (let [open-closed-mixins (if sidebar-open?
                                (open-drawer-props theme)
                                (closed-drawer-props theme))]
       (merge
        {"& .MuiDrawer-paper" open-closed-mixins
         :box-sizing "border-box"
         :flex-shrink 0}
        open-closed-mixins)))))

(def drawer-header
  (styles/styled
   "div"
   (fn [{:keys [theme]}]
     (merge {:display "flex"
             :align-items "center"
             :justify-content "flex-end"}
            (get-in theme [:mixins :toolbar])))))

(defn- my-list-item [sidebar-open? {:keys [icon label selected? on-click]}]
  [list-item {:sx {:px 0
                   :py 0
                   "&.Mui-selected" {:background-color "primary.light"
                                     "&:hover" {:background-color "primary.light"}}}
              :selected selected?
              :on-click on-click}
   [list-item-button
    {:sx {:min-height 48
          ;:bgcolor (when selected? "primary.light")
          :justify-content (if @sidebar-open? "initial" "center")
          :px 2.5}}
    [list-item-icon
     {:sx {:min-width 0
           :mr (if @sidebar-open? 3 "auto")
           :justify-content "center"
           :color (if selected?
                    "textOnLight.main")}}
     [icon]]
    [list-item-text {:primary label
                     :sx {:opacity (if @sidebar-open? 1 0)
                          :white-space "nowrap"
                          :color (if selected?
                                   "textOnLight.main")}}]]])

(defn- drawer-comp [sidebar-open?]
  (let [list-item' (partial my-list-item sidebar-open?)]
    [my-drawer {:sidebar-open? @sidebar-open?
                :variant "permanent"
                :open @sidebar-open?}
     [drawer-header
      [icon-button {:color "inherit"
                    :on-click #(swap! sidebar-open? not)} [arrow-back]]]
     ;[divider]
     [list {:sx {:py 0}}
      [list-item' {:icon home
                   :on-click #(rfe/navigate :route/home)
                   :selected? (= :route/home (util/listen [:landing-page.core/route-name]))
                   :label (i18n/t :home)}]
      [list-item' {:icon emoji-people
                   :on-click #(rfe/navigate :route/about-me)
                   :selected? (= :route/about-me (util/listen [:landing-page.core/route-name]))
                   :label (i18n/t :about-me)}]
      [list-item' {:icon pets
                   :on-click #(rfe/navigate :route/images)
                   :selected? (= :route/images (util/listen [:landing-page.core/route-name]))
                   :label (i18n/t :pets)}]]
     [divider]
     [list-item' {:icon app-settings-alt
                  :on-click #(rfe/navigate :route/theme-customization)
                  :selected? (= :route/theme-customization (util/listen [:landing-page.core/route-name]))
                  :label (i18n/t :pets)}]
     [box {:flex 1 :align-items "flex-end" :display "flex" :pb 2}
      [list-item' {:icon logout
                   :on-click #(rfe/navigate :landing-page.core/index)
                   :label (i18n/t :logout)}]]]))

(defn- app-bar+sidebar [_scroll-trigger]
  (let [sidebar-open? (r/atom false)]
    (fn [scroll-trigger]
      [:<>
       [my-appbar
        {:elevation (if scroll-trigger 6 0)
         :enable-color-on-dark true
         :sidebar-open? @sidebar-open?}
        [toolbar {:sx {:justify-content "space-between"}}
         [app-bar-content sidebar-open?]]]
       (if-not (util/listen [:landing-page.core/logged-in?])
         [offset {:id "appbar-offset"}]
         [:<>
          [drawer-comp sidebar-open?]])])))

(defn main []
  (let [scroll-trigger (use-scroll-trigger {:threshold 0})]
    (r/as-element
     [app-bar+sidebar scroll-trigger])))