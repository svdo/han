(ns app
  (:require [devtools.core :as devtools]
            [reagent.core :as r :refer [atom]]
            ["react-native" :as rn :refer [AppRegistry, Platform]]
            [player :refer [Player]]
            [staff :refer [Staff]]
            [styles :refer [styles]]))

(def instructions
  (.select Platform
           #js {:ios "Press Cmd+R to reload,\nCmd+D or shake for dev menu"
                :android "Double tap R on your keyboard to reload,\nShake or press menu button for dev menu"}))

(defn BuildingBlocks []
  [:> rn/View {:style (:approot styles)}
   [:> rn/View {:style (:container styles)}
    [:> rn/Text {:style (:welcome styles)} "Welcome to React Native!"]
    [:> rn/Text {:style (:instructions styles)} "To get started, edit app.cljs"]
    [:> rn/Text {:style (:instructions styles)} instructions]
    [:> rn/Button {:title "Play a note"
                   :on-press player/play-a-note}]
    [Staff {:style (:staff styles)}]]
   [:> rn/View
    [Player {:style (:player styles)}]]])

(defn app-root []
  [BuildingBlocks])

(defonce app-root-ref (atom nil))

(def updatable-app-root
  (with-meta app-root
    {:component-did-mount
     (fn [this]
       (println "component did mount")
       (reset! app-root-ref this))}))

(defn init []
  (println "Hello!")
  (.registerComponent AppRegistry "Han" #(r/reactify-component updatable-app-root)))

(defn ^:dev/after-load reload []
  (when (some? @app-root-ref)
    (.forceUpdate ^js @app-root-ref)))
