(ns styles
  (:require ["react-native" :as rn :refer [Platform]]))

(def serif-font
  (.select Platform
           #js {:ios "Times New Roman"
                :android "serif"}))

(def styles
  {:approot
   {:flex 1
    :justify-content "center"
    :background-color "green"}

   :container
   {:flex 1
    :justify-content "center"
    :background-color "#F5FCCC"}

   :welcome
   {:font-size 20
    :text-align "center"
    :margin 10}

   :instructions
   {:text-align "center"
    :color "#333333"
    :margin-bottom 5}

   :staff
   {:flex 0
    :background-color "white"
    :height 90}

   :player
   {:position "absolute"
    :flex 0
    :top 0
    :height 0
    :width 0}

   :measure/measure-number
   {:position "absolute"
    :left -15
    :width 30
    :bottom 0
    :font-family serif-font
    :text-align "center"}

  ;  :measure-contents
  ;  {:margin-top 32
  ;   :height 32
  ;   :display "flex"
  ;   :align-items "center"
  ;   :flex-direction "column"
  ;   :justify-content "space-around"}

   :measure/time-signature
   {:width "100%" :height 33
    :display "flex"
    :flex-direction "column"
    :justify-content "space-around"
    :align-items "center"}

   :measure/time-signature-text
   {:font-family serif-font
    :font-size 18
    :font-weight "bold"
    :padding-left 3
    :padding-right 3}

   :measure/tempo-text
   {:font-size 11}

   :measure/bar-lines
   {:position "absolute"
    :top 32
    :width "100%"
    :height 33
    :display "flex"
    :flex-direction "column"}

   :measure/tempo
   {:display "flex"
    :flex-direction "row"
    :justify-content "flex-start"
    :align-items "flex-end"
    :margin-left 2}

   :measure/cell
   {:min-width 60
    :height 65
    :display "flex"
    :flex-direction "column"}

   :measure/cursor-on
   {:display "flex"
    :width "100%"
    :height 32
    :flex-direction "row"
    :justify-content "center"}

   :measure/number-and-cursors
   {:position "absolute"
    :top 0
    :width "100%"
    :height 32}})

(defn style [key]
  {:style (key styles)})
