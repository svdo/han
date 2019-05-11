(ns audio.engine)

(def audioContext (atom nil))
(def unlocked (atom false))

(defn add-to-body [html]
  (goog.object/set js/document.body "innerHTML"
                   (str js/document.body.innerHTML html)))

(defn init []
  (println "init player (println)")
  (add-to-body "<p>init player</p>")
  (js/console.log "init player (console.log)")
  (if (and (js/window.hasOwnProperty "webkitAudioContext")
           (not (js/window.hasOwnProperty "AudioContext")))
    (reset! audioContext (js/window.webkitAudioContext.))
    (reset! audioContext (js/window.AudioContext.)))
  (println "audioContext (println)" @audioContext)
  (js/console.log "audioContext (console.log)" @audioContext))

(defn play []
  (add-to-body "<p>play?</p>")
  (when (not @unlocked)
    (let [buffer (.createBuffer @audioContext 1 1 22050)
          node (.createBufferSource @audioContext)]
      (add-to-body (str "<p>play: " node "</p>"))
      (goog.object/set node "buffer" buffer)
      (.start node 0)
      (reset! unlocked true))))

(defn schedule-note []
  (let [attack 0.01
        sustain 0.03
        time (.-currentTime @audioContext)
        note-length 0.5
        gain (.createGain @audioContext)]
    (add-to-body (str "<p>schedule-note at time " time "</p>"))
    (.setValueAtTime (.-gain gain) 0 time)
    (add-to-body "1.")
    (.linearRampToValueAtTime (.-gain gain) 1 (+ time attack))
    (add-to-body "2.")
    (.setValueAtTime (.-gain gain) 1 (+ time attack sustain))
    (add-to-body "3.")
    (.linearRampToValueAtTime (.-gain gain) 0 (+ time note-length))

    (add-to-body "4.")
    (try
      (let [osc (.createOscillator @audioContext)]
        ; (.connect (.connect osc gain) (.-destination @audioContext))
        ; (.connect osc (.-destination @audioContext))
        (-> osc
            ; (.connect gain)
            (.connect (.-destination @audioContext)))
        (goog.object/set (.-frequency osc) "value" 880)
        (add-to-body "5.")
        (add-to-body "6.")
        (.start osc time)
        (add-to-body "7.")
        (.stop osc (+ time note-length))
        (add-to-body "8."))
      (catch js/Error e
        (add-to-body e)))))

(init)
(schedule-note)
(play)