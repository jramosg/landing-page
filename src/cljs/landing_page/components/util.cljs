(ns landing-page.components.util)

(defn selected-menu-list-item [{:keys [theme]}]
  (let [primary-light (get-in theme [:palette :primary :light])]
    {"&.Mui-selected" {:background-color primary-light
                       "&:hover" {:background-color primary-light}
                       :color ((get-in theme [:palette :get-contrast-text]) primary-light)}}))