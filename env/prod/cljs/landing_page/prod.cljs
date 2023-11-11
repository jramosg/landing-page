(ns landing-page.prod
  (:require [landing-page.core :as core]))

;;ignore println statements in prod
(set! *print-fn* (fn [& _]))

(core/init!)
