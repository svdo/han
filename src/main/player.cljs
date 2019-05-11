(ns player
  (:require ["react-native-webview" :as rnwv :refer [WebView]]
            [shadow.resource :as rc]))

(defonce javascript-injected (atom false))

(defn inject-javascript [ref]
  (when (and (not (nil? ref))
             (false? @javascript-injected))
    (reset! javascript-injected true)
    (def replace-console-log
      (str "var console={};"
           "console.log=function(message){"
           "  document.body.innerHTML+='<p>'+JSON.stringify(arguments)+'</p>';"
           "  if (arguments.length > 2 && arguments[2].message){"
           "    document.body.innerHTML+='<p>'+arguments[2].message+'<br/><pre>'+arguments[2].stack+'</pre></p>'"
           "  };"
           "};"
           "console.error=console.log;"
           "console.exception=console.error;"
           "console.warn=console.log;"
           "window.console=console;"))
    (def audio-engine-source (str replace-console-log
                                  (rc/inline "audio-engine-compiled-js-source.txt")))
    (def javascript (str replace-console-log audio-engine-source ";true"))

    (js/setTimeout
     #(.injectJavaScript ref javascript) 1000)))

(defn Player []
  [:> WebView {:ref (fn [r] (inject-javascript r))
               :media-playback-requires-user-action false
               :java-script-enabled true
               :allow-file-access true
               :allow-universal-access-from-file-URLs true
               :origin-whitelist ["*"]
               :source {:html "<body style='font-size: 200%'><h1>Hi!</h1></body>"}}])