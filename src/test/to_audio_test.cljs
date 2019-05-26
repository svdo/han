(ns to-audio-test
  (:require [cljs.test :refer (deftest is testing use-fixtures)]
            [note]))

(def simple-3-4-measure {:time-signature/beats 3
                         :time-signature/duration note/quarter
                         :tempo/tempo 96
                         :tempo/multiplier note/quarter})

(def simple-7-8-measure {:time-signature/beats 7
                         :time-signature/duration note/eighth
                         :tempo/tempo 140
                         :tempo/multiplier note/eighth})

(def fast-7-8-measure {:time-signature/beats 7
                       :time-signature/duration note/eighth
                       :tempo/tempo 140
                       :tempo/multiplier note/quarter})

(defn sound-for [beat] (if (= 0 beat) :first :normal))

(defn measure-to-audio
  "Converts a measure into a series of time sequences that can be sent
   to the audio player"
  [{:keys [time-signature/duration time-signature/beats tempo/tempo tempo/multiplier]}]
  (let [beat-duration (/ 60 tempo (/ multiplier duration))]
    (mapv (fn [beat] [(* beat beat-duration)
                      (sound-for beat)]) (range 0 beats))))

(deftest simple-3-4
  (is (= [[0.0 :first] [0.625 :normal] [1.25 :normal]]
         (measure-to-audio simple-3-4-measure))))

(deftest simple-7-8
  (is (= (measure-to-audio simple-7-8-measure)
         (map (fn [beat] [(* (/ 60.0 140.0) beat) (sound-for beat)]) (range 0 7)))))

(deftest fast-7-8
  (is (= (measure-to-audio fast-7-8-measure)
         (map (fn [beat] [(* (/ 60.0 140.0 2.0) beat)
                          (sound-for beat)]) (range 0 7)))))