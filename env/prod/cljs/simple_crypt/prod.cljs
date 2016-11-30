(ns simple-crypt.prod
  (:require [simple-crypt.core :as core]))

;;ignore println statements in prod
(set! *print-fn* (fn [& _]))

(core/init!)
