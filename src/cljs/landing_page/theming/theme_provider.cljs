(ns landing-page.theming.theme-provider
  (:require [landing-page.theming.subs :as subs]
            [landing-page.util :as util]
            [reagent-mui.styles :as styles]))

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

(defn main []
  (styles/responsive-font-sizes (styles/create-theme (theme))))