(ns app
  (:require [reagent.core :as r :refer [atom]]
            ["react-native" :as rn :refer [AppRegistry, Platform]]
            [player :refer [Player]]))

(def styles
  {:container
   {:flex 1
    :justify-content "center"
    :background-color "#F5FCFF"}

   :welcome
   {:font-size 20
    :text-align "center"
    :margin 10}

   :instructions
   {:text-align "center"
    :color "#333333"
    :margin-bottom 5}})

(def instructions
  (.select Platform
           #js {:ios "Press Cmd+R to reload,\nCmd+D or shake for dev menu"
                :android "Double tap R on your keyboard to reload,\nShake or press menu button for dev menu"}))

(defn app-root []
  [:> rn/View {:style (:container styles)}
   [:> rn/Text {:style (:welcome styles)} "Welcome to React Native!"]
   [:> rn/Text {:style (:instructions styles)} "To get started, edit app.cljs"]
   [:> rn/Text {:style (:instructions styles)} instructions]
   [Player]])

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
  (.forceUpdate @app-root-ref))
