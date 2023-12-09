(ns landing-page.theming.theme-provider
  (:require [clojure.string :as str]
            [re-frame.core :as rf]
            [reagent-mui.styles :as styles]
            [reagent-mui.colors :as colors]
            [landing-page.theming.subs :as subs]
            [landing-page.util :as util]))

(defn- theme [mode]
  {:palette {:tonal-offset {:light (util/listen [::subs/light-offset-0-1])
                            :dark (util/listen [::subs/dark-offset-0-1])}
             :primary {:main (util/listen [::subs/primary-color])}
             :secondary {:main (util/listen [::subs/secondary-color])
                         }
             :text-on-light {:main "rgba(0, 0, 0, 0.87)"}
             :mode @mode}
   :typography {:font-size (util/listen [::subs/font-size])
                :font-family (util/listen [::subs/font-family])
                }
   :components {:MuiButton {:style-overrides {:root {:text-transform "none"}}}}})

(defn main []
  (styles/create-theme (theme (rf/subscribe [::subs/mode]))))