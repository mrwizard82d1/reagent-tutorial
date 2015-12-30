(ns reagent-tutorial.core
  (:require [reagent.core :as reagent :refer [atom]]))

(enable-console-print!)

(println "Edits to this text should show up in your developer console.")

;; define your app data so that it doesn't get over-written on reload

(defonce app-state (atom {:list ["Lion" "Zebra" "Buffalo" "Antelope"]}))

(defn hello-world []
  ;; The following code works; however, it generates a warning in the developer console:
  ;;     Every element in a seq should have a unique :key
  ;; This warning results from React expecting each element in the virtual DOM to have a unique key so that it can more
  ;; effectively determine changes to elements. My guess is that the Om library takes care of this issue for me.
  ;;
  ;; For more information, see the post on StackOverflow,
  ;; http://stackoverflow.com/questions/33446913/reagent-react-clojurescript-warning-every-element-in-a-seq-should-have-a-unique  
  ;; 
  #_[:ul (map #(vector :li %) (:list @app-state))]
  ;; 
  ;; To resolve this warning, the aforementioned post suggested adding meta data to each element in the list. The metadata is
  ;; a map between the symbol :key and a unique item (the list item if it is unique; other suggestions were using `gen-key`
  ;; or using a UUID generator).
  ;; 
  [:ul
   {:className "animals"}
   (map #(with-meta (vector :li %) {:key %}) (:list @app-state))]
  )

(reagent/render-component [hello-world]
                          (. js/document (getElementById "app")))

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)
