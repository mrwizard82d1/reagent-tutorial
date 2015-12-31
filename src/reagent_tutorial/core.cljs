(ns reagent-tutorial.core
  (:require [reagent.core :as rc :refer [atom]]))

(enable-console-print!)

(println "Edits to this text should show up in your developer console.")

;; define your app data so that it doesn't get over-written on reload

(def app-state
  (rc/atom
   {:contacts 
    [{:first "Ben" :last "Bitdiddle" :email "benb@mit.edu"}
     {:first "Alyssa" :middle-initial "P" :last "Hacker" :email "aphacker@mit.edu"}
     {:first "Eval" :middle "Lu" :last "Ator" :email "eval@mit.edu"}
     {:first "Louis" :last "Reasoner" :email "prolog@mit.edu"}
     {:first "Cy" :middle-initial "D" :last "Effect" :email "bugs@mit.edu"}
     {:first "Lem" :middle-initial "E" :last "Tweakit" :email "morebugs@mit.edu"}]}))


(defn middle-name [{:keys [middle middle-initial]}]
  (cond 
    middle middle
    middle-initial (str middle-initial ".")))

(defn display-name [{:keys [first, last] :as contact}]
  (str last ", " first " " (middle-name contact)))

(defn remove-contact! [c]
  (println "Remove contact" c)
  (swap! app-state update-in [:contacts] (fn [contacts]
                                           (vec (remove #(= c %) contacts)))))

(defn contact [c]
  "Returns the component presenting a single contact."
  (with-meta (vector :li
                     [:span (display-name c)]
                     [:button
                      {:on-click #(remove-contact! c)}
                      "Delete"]) {:key (:last c)}))

(defn contacts []
  [:div 
   [:h2 "Contact List"]
   [:ul 
    (map contact (:contacts @app-state))]])

(rc/render-component [contacts]
                     (. js/document (getElementById "contacts")))

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)
