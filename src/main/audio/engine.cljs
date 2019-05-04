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
  (when (not @unlocked)
    (let [buffer (.createBuffer @audioContext 1 1 22050)
          node [(.createBufferSource @audioContext)]]
      (add-to-body "<p>play</p>")
      (goog.object/set node "buffer" buffer)
      (.start node 0)
      (reset! unlocked true))))

(defn schedule-note []
  (let [attack 0.01
        sustain 0.03
        time (.-currentTime @audioContext)
        note-length 0.5
        gain (.createGain @audioContext)
        osc (.createOscillator @audioContext)]
    (add-to-body "<p>schedule-note</p>")
    (.setValueAtTime (.-gain gain) 0 time)
    (.linearRampToValueAtTime (.-gain gain) 1 (+ time attack))
    (.setValueAtTime (.-gain gain) 1 (+ time attack sustain))
    (.linearRampToValueAtTime (.-gain gain) 0 (+ time note-length))

    (.connect (.connect osc gain) (.-destination @audioContext))
    (goog.object/set (.-value (.-frequency osc)) 880)
    (.start osc time)
    (.stop osc (+ time note-length))))

(init)
(schedule-note)
(play)