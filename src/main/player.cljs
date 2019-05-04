(ns player
  (:require [reagent.core :as r]
            ["react-native-webview" :as rnwv :refer [WebView]]))

(defn Player []
  [:> WebView {:origin-whitelist ["*"]
               :source {:html "<h1>Hi!</h1>"}
               :injected-java-script "document.body.style.backgroundColor='red';true"}])