(ns landing-page.theming.theme-provider
  (:require [landing-page.theming.subs :as subs]
            [landing-page.util :as util]
            [reagent-mui.styles :as styles]
            [reagent-mui.util :as mui.util]))

(defn- theme []
  {:palette {:tonal-offset {:light (util/listen [::subs/light-offset-0-1])
                            :dark (util/listen [::subs/dark-offset-0-1])}
             :primary {:main (util/listen [::subs/primary-color])}
             :secondary {:main (util/listen [::subs/secondary-color])}
             :text-on-light {:main "rgba(0, 0, 0, 0.87)"}
             :mode (util/listen [::subs/mode])}
   :typography {:font-size (util/listen [::subs/font-size])
                :font-family (util/listen [::subs/font-family])}
   :components {:MuiButton {:style-overrides {:root {:text-transform "none"}}}}})

(defn- theme* [theme]
  (let [primary-light (get-in theme [:palette :primary :light])
        text-color-on-light ((get-in theme [:palette :get-contrast-text]) primary-light)
        menu+list-override {:background-color primary-light
                            "&:hover" {:background-color (get-in theme [:palette :primary :main])
                                       :color (get-in theme [:palette :primary :contrast-text])}
                            :color text-color-on-light}]
    (-> theme
        (update :components assoc
                :MuiListItem {:style-overrides {:root {"&.MuiListItem-root.Mui-selected" menu+list-override}}}
                :MuiMenuItem {:style-overrides {:root {"&.MuiMenuItem-root.Mui-selected" menu+list-override}}}))))

(defn main []
  (-> (styles/create-theme (theme))
      mui.util/js->clj'
      theme*
      styles/create-theme
      styles/responsive-font-sizes))