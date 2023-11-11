(ns landing-page.theme-provider
  (:require [reagent-mui.styles :as styles]))

(def ^:const ^:private theme
  {:palette {:tonal-offset {:light 0.7}
             :primary {:main "#0B664D"}
             :secondary {:main "#FD9CCA"}}
   :components {:MuiButton {:style-overrides {:root {:text-transform "none"}}}}})

(defn main []
  (styles/create-theme theme))