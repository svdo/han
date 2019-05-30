(ns player
  (:require ["react-native-webview" :as rnwv :refer [WebView]]
            [oops.core :refer [oget+]]
            [clojure.spec.alpha :as s]
            [shadow.resource :as rc]))

(defonce webview-ref (atom nil))
(defonce javascript-injected (atom false))

(defn initial-js []
  (def audio-engine-source (rc/inline "audio-engine-compiled-js-source.txt"))
  (def replace-console-log (rc/inline "replace-console-log.js"))
  (str replace-console-log audio-engine-source ";true"))

(defn inject-javascript [ref]
  (when (and (some? ref)
             (false? @javascript-injected))
    (reset! javascript-injected true)
    (reset! webview-ref ref)
    (js/setTimeout
     #(.injectJavaScript ref (initial-js))
     1000)))

(defn message-from-webview [event]
  (let [native-event (.-nativeEvent event)
        data (.-data native-event)
        parsed (js/JSON.parse data)
        logType (.-logType parsed)]
    (when-not (nil? logType)
      (let [log-fn (oget+ js/console logType)]
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
                      :on-message message-from-webview})])

(defn play-a-note []
  (when (some? ^js @webview-ref)
    (.injectJavaScript ^js @webview-ref "audio.engine.schedule_note();true")))

(s/def :time-signature/duration #{1 2 4 8 16 32 64})
(s/def :time-signature/beats pos-int?)
(s/def :measure/time-signature (s/tuple :time-signature/beats :time-signature/duration))

(def note-lengths #{:dotted-whole :whole
                    :dotted-half :half
                    :dotted-quarter :quarter
                    :dotted-eighth :eighth
                    :dotted-sixteenth :sixteenth})

(s/def :measure/number pos-int?)
(s/def :measure/accents (s/coll-of (s/int-in 0 2)))

(s/def :piece/measure (s/and (s/keys :req [:time-signature/beats :time-signature/duration]
                                     :opt [:measure/accents])
                             #(or
                               (nil? (:measure/accents %))
                               (= (count (:measure/accents %))
                                  (:time-signature/beats %)))))

(comment
  (in-ns 'player)
  (play-a-note)

  (gen/sample (s/gen :measure/time-signature))
  (gen/sample (s/gen :measure/accents))
  (gen/sample (s/gen :piece/measure))

  (do
    (require '[clojure.test.check.generators])
    (require '[cljs.spec.gen.alpha :as gen])
    (require '[cljs.spec.test.alpha :as stest])
    (def sample-measure-1 {:time-signature/beats 3
                           :time-signature/duration 8})
    (def sample-measure-2 {:measure/accents [0 0]
                           :time-signature/beats 2
                           :time-signature/duration 4})
    (def sample-measure-3 {:measure/accents [1 0 0 1 0 1 0]
                           :time-signature/beats 7
                           :time-signature/duration 8})
    (def piece [sample-measure-1 sample-measure-2 sample-measure-3])

    (defn str-accent [accent]
      (if (= 1 accent) "o" "."))

    (defn str-measure [measure]
      "creates a printable version of the measure"
      (let [beats (:time-signature/beats measure)
            accents (:measure/accents measure)]
        (str (str beats
                  "/"
                  (:time-signature/duration measure))
             " "
             (str "O"
                  (if (some? accents)
                    (reduce str (map str-accent (rest accents)))
                    (reduce str (repeat (dec beats) ".")))))))

    (s/fdef str-measure
      :args (s/cat :measure :piece/measure)
      :ret string?)

    (defn str-piece [measures]
      (reduce str (interpose "|" (map str-measure measures)))))

  (s/conform :piece/measure sample-measure)
  (s/explain :piece/measure sample-measure)

  (stest/instrument `str-measure)

  (str-measure sample-measure-3)
  (str-measure {:time-signature/beats 3})

  (str-piece piece)

  ;
  )
