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
    :height 132}

   :player
   {:position "absolute"
    :flex 0
    :top 0
    :height 0
    :width 0}

   :measure-number
   {:position "absolute"
    :left -15
    :width 30
    :top 15
    :font-family serif-font
    :text-align "center"}

   :measure-contents
   {:margin-top 32
    :height 32
    :display "flex"
    :align-items "center"
    :flex-direction "column"
    :justify-content "space-around"}

   :time-signature
   {:margin-top -4
    :margin-bottom -4
    :font-family serif-font
    :font-size 18
    :font-weight "bold"}})

