(ns player
  (:require ["react-native-webview" :as rnwv :refer [WebView]]
            [shadow.resource :as rc]))

(defn Player []
  (def audio-engine-source (str (rc/inline "audio-engine-compiled-js-source.txt") ";true"))
  [:> WebView {:origin-whitelist ["*"]
               :source {:html "<h1>Hi!</h1>"}
               :injected-java-script audio-engine-source}])