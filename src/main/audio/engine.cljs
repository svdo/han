(ns audio.engine
  (:require [oops.core :refer [oset!]]))

(def audioContext (atom nil))
(def unlocked (atom false))

(defn init []
  (js/console.debug "init player")
  (if (and (js/window.hasOwnProperty "webkitAudioContext")
           (not (js/window.hasOwnProperty "AudioContext")))
    (reset! audioContext (js/window.webkitAudioContext.))
    (reset! audioContext (js/window.AudioContext.)))
  (js/console.debug "audioContext" @audioContext))

(defn play []
  (when (not @unlocked)
    (let [buffer (.createBuffer @audioContext 1 1 22050)
          node (.createBufferSource @audioContext)]
      (oset! node :buffer buffer)
      (.start node 0)
      (reset! unlocked true))))

(defn schedule-note []
  (let [attack 0.01
        sustain 0.03
        time (.-currentTime @audioContext)
        note-length 0.5
        gain (.createGain @audioContext)]
    (js/console.debug (str "schedule-note at time " time))
    (.setValueAtTime (.-gain gain) 0 time)
    (.linearRampToValueAtTime (.-gain gain) 1 (+ time attack))
    (.setValueAtTime (.-gain gain) 1 (+ time attack sustain))
    (.linearRampToValueAtTime (.-gain gain) 0 (+ time note-length))

    (try
      (let [osc (.createOscillator @audioContext)]
        (-> osc
            (.connect gain)
            (.connect (.-destination @audioContext)))
        (oset! (.-frequency osc) :value 1660)
        (.start osc time)
        (.stop osc (+ time note-length)))
      (catch js/Error e
        (js/console.error #js {:error e
                               :message (.-message e)
                               :stack (.-stack e)})))))

(init)
(schedule-note)
(play)