(ns reagent-tutorial.core
  (:require [reagent.core :as reagent :refer [atom]]))

(enable-console-print!)

(println "Edits to this text should show up in your developer console.")

;; define your app data so that it doesn't get over-written on reload

(defonce app-state
  (atom
   {:contacts 
    [{:first "Ben" :last "Bitdiddle" :email "benb@mit.edu"}
     {:first "Alyssa" :middle-initial "P" :last "Hacker" :email "aphacker@mit.edu"}
     {:first "Eval" :middle "Lu" :last "Ator" :email "eval@mit.edu"}
     {:first "Louis" :last "Reasoner" :email "prolog@mit.edu"}
     {:first "Cy" :middle-initial "D" :last "Effect" :email "bugs@mit.edu"}
     {:first "Lem" :middle-initial "E" :last "Tweakit" :email "morebugs@mit.edu"}]}))


(defn middle-name [contact]
  (cond 
    (:middle contact) (:middle contact)
    (:middle-initial contact) (str (:middle-initial contact) ".")))

(defn display-name [{:keys [first, last] :as contact}]
  (str last ", " first " " (middle-name contact)))

(defn contacts []
  [:div 
   [:h2 "Contact List"]
   [:ul
    (map #(with-meta (vector :li (display-name %)) {:key %}) (:contacts @app-state))]])

(reagent/render-component [contacts]
                          (. js/document (getElementById "contacts")))

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)
