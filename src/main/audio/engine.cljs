(ns audio.engine)

(def audioContext (atom nil))
(def unlocked (atom false))

(defn init []
  (println "init player (println)")
  (js/console.log "init player (console.log)")
  (if (and (js/window.hasOwnProperty "webkitAudioContext")
           (not (js/window.hasOwnProperty "AudioContext")))
    (reset! audioContext (js/window.webkitAudioContext.))
    (reset! audioContext (js/window.AudioContext.)))
  (println "audioContext (println)" @audioContext)
  (js/console.log "audioContext (console.log)" @audioContext))
