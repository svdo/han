(ns player
  (:require ["react-native-webview" :as rnwv :refer [WebView]]
            [shadow.resource :as rc]))

(defonce javascript-injected (atom false))

(def replace-console-log
  (str "var console={};"
       "console.log=function(message){"
       "  var output=JSON.stringify(arguments);"
       "  if (arguments.length > 2 && arguments[2].message){"
       "    output+=arguments[2].message+'\\n'+arguments[2].stack;"
       "  };"
       "  document.body.innerHTML+='<pre>'+output+'</pre>';"
       "};"
       "console.info=console.log;"
       "console.error=console.log;"
       "console.exception=console.error;"
       "console.warn=console.log;"
       "window.console=console;"))

(defn initial-js []
  (def audio-engine-source (rc/inline "audio-engine-compiled-js-source.txt"))
  (str replace-console-log audio-engine-source ";true"))

(defn inject-javascript [ref]
  (when (and (not (nil? ref))
             (false? @javascript-injected))
    (reset! javascript-injected true)
    (js/setTimeout
     #(.injectJavaScript ref (initial-js)) 1000)))

(defn Player [styles]
  [:> WebView (merge styles
                     {:ref (fn [r] (inject-javascript r))
                      :media-playback-requires-user-action false
                      :java-script-enabled true
                      :allow-file-access true
                      :allow-universal-access-from-file-URLs true
                      :origin-whitelist ["*"]
                      :source {:html "<body style='font-size: 200%'><h1>Hi!</h1></body>"}})])