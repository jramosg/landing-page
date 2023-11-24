(ns landing-page.theming.theme-provider
  (:require [re-frame.core :as rf]
            [reagent-mui.styles :as styles]
            [landing-page.theming.subs :as subs]))

(defn- theme [mode]
  {:palette {:tonal-offset {:light 0.7}
             :primary {:main "#3D5B59"}
             :secondary {:main "#FCB5AC"}
             :mode @mode}
   :components {:MuiButton {:style-overrides {:root {:text-transform "none"}}}}})

(defn main []
  (styles/create-theme (theme (rf/subscribe [::subs/mode]))))