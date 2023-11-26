(ns landing-page.forms.db)

(defn get-field-path [x]
  (cond-> x (keyword? x) vector))