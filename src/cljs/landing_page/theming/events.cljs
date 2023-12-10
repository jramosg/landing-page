(ns landing-page.theming.events
  (:require [landing-page.theming.constants :as constants]
            [re-frame.core :as rf]))

(rf/reg-event-db
 ::change-theme-kw
 (fn [db [_ kw v]]
   (assoc-in db (conj constants/theme-root-path kw) v)))

(rf/reg-event-db
 ::change-font-size
 (fn [db [_ v]]
   (assoc-in db constants/font-size-path v)))

(rf/reg-event-db
 ::change-light-offset
 (fn [db [_ v]]
   (assoc-in db constants/light-offset-path v)))

(rf/reg-event-db
 ::change-dark-offset
 (fn [db [_ v]]
   (assoc-in db constants/dark-offset-path v)))

(rf/reg-event-db
 ::change-font-family
 (fn [db [_ v]]
   (assoc-in db constants/font-family-path v)))

(rf/reg-event-db
 ::change-primary-color
 (fn [db [_ v]]
   (assoc-in db constants/primary-color-path v)))

(rf/reg-event-db
 ::change-secondary-color
 (fn [db [_ v]]
   (assoc-in db constants/secondary-color-path v)))
