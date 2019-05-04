(ns player
  (:require ["react-native-webview" :as rnwv :refer [WebView]]
            [shadow.resource :as rc]))

(defn Player []
  (def audio-engine-source (str (rc/inline "audio-engine-compiled-js-source.txt") ";true"))
  [:> WebView {:media-playback-requires-user-action false
               :origin-whitelist ["*"]
               :source {:html "<body style='font-size: 200%'><h1>Hi!</h1></body>"}
               :injected-java-script audio-engine-source}])