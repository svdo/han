(ns images)

(def images
  {:staff/bar               {:img (js/require "../img/staff.png")        :width 10 :height 66}
   :staff/measure-separator {:img (js/require "../img/separator.png")    :width  1 :height 66}
   :staff/start             {:img (js/require "../img/staff-header.png") :width 15 :height 66}
   :staff/end               {:img (js/require "../img/staff-end.png")    :width 11 :height 66}

   :note/whole              {:img (js/require "../img/notes/whole.png")         :width 18 :height 29}
   :note/dotted-whole       {:img (js/require "../img/notes/whole-dot.png")     :width 18 :height 29}
   :note/half               {:img (js/require "../img/notes/half.png")          :width 18 :height 29}
   :note/dotted-half        {:img (js/require "../img/notes/half-dot.png")      :width 18 :height 29}
   :note/quarter            {:img (js/require "../img/notes/quarter.png")       :width 18 :height 29}
   :note/dotted-quarter     {:img (js/require "../img/notes/quarter-dot.png")   :width 18 :height 29}
   :note/eighth             {:img (js/require "../img/notes/eighth.png")        :width 18 :height 29}
   :note/dotted-eighth      {:img (js/require "../img/notes/eighth-dot.png")    :width 18 :height 29}
   :note/sixteenth          {:img (js/require "../img/notes/sixteenth.png")     :width 18 :height 29}
   :note/dotted-sixteenth   {:img (js/require "../img/notes/sixteenth-dot.png") :width 18 :height 29}})