(ns player
  (:require ["react-native-webview" :as rnwv :refer [WebView]]
            [shadow.resource :as rc]))

(defonce webview-ref (atom nil))
(defonce javascript-injected (atom false))

(defn initial-js []
  (def audio-engine-source (rc/inline "audio-engine-compiled-js-source.txt"))
  (def replace-console-log (rc/inline "replace-console-log.js"))
  (str replace-console-log audio-engine-source ";true"))

(defn inject-javascript [ref]
  (when (and (not (nil? ref))
             (false? @javascript-injected))
    (reset! javascript-injected true)
    (reset! webview-ref ref)
    (js/setTimeout
     #(.injectJavaScript ref (initial-js)))))

(defn message-from-webview [event]
  (let [native-event (.-nativeEvent event)
        data (.-data native-event)
        parsed (js/JSON.parse data)
        logType (.-logType parsed)]
    (when (not (nil? logType))
      (let [log-fn (goog.object/get js/console logType)]
        (.apply log-fn js/console (goog.object/getValues (.-args parsed)))))))

(defn Player [styles]
  [:> WebView (merge styles
                     {:ref (fn [r] (inject-javascript r))
                      :media-playback-requires-user-action false
                      :java-script-enabled true
                      :allow-file-access true
                      :allow-universal-access-from-file-URLs true
                      :origin-whitelist ["*"]
                      :source {:html "<body style='font-size: 200%'><h1>Hi!</h1></body>"}
                      :injected-java-script "true"
                      :on-message message-from-webview})])

(defn play-a-note []
  (when (not (nil? @webview-ref))
    (.injectJavaScript @webview-ref "audio.engine.schedule_note();true")))
