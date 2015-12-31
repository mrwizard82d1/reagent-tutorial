(ns reagent-tutorial.core
  (:require [reagent.core :as rc :refer [atom]]
            [goog.crypt :as crypt]
            [goog.crypt.base64 :as base64]
            [clojure.string :as str]))

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

(defn parse-contact [contact-as-text]
  (let [[first middle last] (str/split contact-as-text #"\s+")]
    (cond
      (every? nil? [first middle last])
      nil
      (and (nil? middle) (nil? last))
      {:first first}
      (nil? last)
      {:first first :last middle}
      (str/ends-with? middle ".")
      {:first first :middle-initial (str/replace middle "." "") :last last}
      :else
      {:first first :middle middle :last last})))

(defn remove-contact! [c]
  (println "Remove contact" c)
  (swap! app-state update-in [:contacts] (fn [contacts]
                                           (vec (remove #(= c %) contacts)))))

(defn add-contact! [c]
  (println "Add contact" c)
  (swap! app-state update-in [:contacts] (fn [contacts]
                                           (conj contacts c))))

(defn gen-key-map [c]
  "Generate a key map for a contact used to identify the contact in the application state."
  (let [hasher (crypt/Sha256.)]
    (.update hasher (display-name c))
    {:key (base64/encodeByteArray (.digest hasher))}))

(defn contact [c]
  "Returns the component presenting a single contact."
  (with-meta (vector :li
                     [:span (display-name c)]
                     [:button
                      {:on-click #(remove-contact! c)}
                      "Delete"]) (gen-key-map c)))

(defn new-contact []
  (let [value (rc/atom "")]
    (fn []
      [:div
       [:input {:type "text"
                :placeholder "Contact Name"
                :value @value
                :on-change #(reset! value (-> % .-target .-value))}]
       [:button {:on-click #(when-let [c (parse-contact @value)]
                              (add-contact! c)
                              (reset! value ""))}
        "Add"]])))

(defn contacts []
  [:div 
   [:h2 "Contact List"]
   [:ul 
    (map contact (:contacts @app-state))]
   [new-contact]])

(rc/render-component [contacts]
                     (. js/document (getElementById "contacts")))

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)
