(ns simple-crypt.core
    (:require [reagent.core :as reagent :refer [atom]]
              [reagent.session :as session]
              [secretary.core :as secretary :include-macros true]
              [accountant.core :as accountant]
              [io.github.theasp.simple-encryption :as crypt]))

;; -------------------------
;; Views

(def to-encrypt (atom "Hello, World!"))

(defn home-page []
  (println)
  [:div [:h2 "Welcome to simple-crypt"]
   [:input {:type "text"
            :value @to-encrypt
            :on-change #(reset! to-encrypt (-> % .-target .-value))}]
   (let [key (crypt/new-random-key :aes-256-cbc)
         input @to-encrypt
         encrypted (crypt/encrypt-with key @to-encrypt)
         decrypted (crypt/decrypt-with key encrypted)]
     [:ul
      [:li "key: " [:code (:key key)]]
      [:li "encrypted: " [:code (:data encrypted)]]
      [:li "decrypted: " decrypted]])])

(defn current-page []
  [:div [(session/get :current-page)]])

;; -------------------------
;; Routes

(secretary/defroute "/" []
  (session/put! :current-page #'home-page))

;; -------------------------
;; Initialize app

(defn mount-root []
  (reagent/render [current-page] (.getElementById js/document "app")))

(defn init! []
  (accountant/configure-navigation!
    {:nav-handler
     (fn [path]
       (secretary/dispatch! path))
     :path-exists?
     (fn [path]
       (secretary/locate-route path))})
  (accountant/dispatch-current!)
  (mount-root))
