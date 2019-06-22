(ns staff
  (:require ["react-native" :as rn]
            ["react-native-view-overflow" :default ViewOverflow]
            [reagent.core :as r]
            [cljs-bean.core :refer [bean]]
            [styles :refer (styles style)]
            [images :refer (images)]))

(def sample-data (atom (mapv (fn [n] {:measureNumber n
                                      :showMeasureNumber true
                                      :isSelected (= n 2)
                                      :beats (if (= 2 n) "2+2+3+2+2" 3)
                                      :duration "4"
                                      :nextMeasureHasDifferentTempo (= 1 n)
                                      :showTempo (= 2 n)
                                      :tempoMultiplier :note/dotted-sixteenth
                                      :tempoNumber 160})
                             (range 1 4))))
(def view-model (r/atom (clj->js @sample-data)))

(defn reset-view-model! []
  (reset! view-model (clj->js @sample-data)))

(defn bar-lines
  ([width margin-left]
   (bar-lines width margin-left false))
  ([width margin-left selected]
   (map (fn [i] ^{:key (str i)} [:> rn/View {:style {:margin-left margin-left
                                                     :width width
                                                     :height (if (= i 4) 9 8)
                                                     :background-color (if selected "#ddf" "white")
                                                     :border-top-width 1
                                                     :border-top-color "black"
                                                     :border-bottom-width (if (= i 4) 1 0)
                                                     :border-bottom-color "black"}}])
        (range 1 5))))

(defn- cursor [extra-styles]
  [:> rn/View {:style (merge {:position "absolute"
                              :width 10 :height 10
                              :background-color "transparent"
                              :border-top-width 10 :border-top-color "blue"
                              :border-left-width 5 :border-left-color "transparent"
                              :border-right-width 5 :border-right-color "transparent"
                              :border-bottom-width 0 :border-bottom-color "transparent"}
                             extra-styles)}])

(defn set-cursor-on [measure-number view-model]
  (map (fn [measure]
         (merge measure {:cursorOn (= measure-number (:measureNumber measure))
                         :cursorBefore false}))
       view-model))

(defn set-cursor-before [measure-number view-model]
  (map (fn [measure]
         (merge measure {:cursorOn false
                         :cursorBefore (= measure-number (:measureNumber measure))}))
       view-model))

(defn cell [details]
  (let [item (.-item details)
        {:keys [measureNumber showMeasureNumber isSelected
                beats duration showTempo tempoMultiplier tempoNumber
                cursorOn cursorBefore]} (bean item)]
    [:> ViewOverflow (style :measure/cell)
     [:> rn/View {:style {:height 32}}

      [:> rn/View (style :measure/number-and-cursors)
       (when showMeasureNumber
         [:> rn/Text (style :measure/measure-number) measureNumber])
         ;; variant with cursor between measures
       (when cursorBefore
         (cursor {:bottom (if showMeasureNumber 16 2) :left -5}))

         ;; variant with cursor on measure
       (when cursorOn
         [:> rn/View (style :measure/cursor-on)
          (cursor {:bottom 2})])]

        ;; tempo
      (when showTempo
        [:> rn/View (style :measure/tempo)
         (let [note ((keyword (str "note/" tempoMultiplier)) images)]
           [:> rn/Image {:source (:img note)
                         :style {:tint-color "black"
                                 :width (* 0.7 (:width note))
                                 :height (* 0.7 (:height note))}}])
         [:> rn/Text (style :measure/tempo-text) (str "=" tempoNumber)]])]

     [:> rn/View (style :measure/bar-lines)
      (bar-lines "100%" 0 isSelected)]

     [:> rn/View (style :measure/time-signature)
      [:> rn/Text (style :measure/time-signature-text) beats]
      [:> rn/Text (style :measure/time-signature-text) duration]]

     [:> rn/View {:style {:position "absolute" :width "100%" :height "100%"
                          :display "flex" :flex-direction "row"}}
      [:> rn/TouchableWithoutFeedback
       {:on-press (fn []
                    (swap! sample-data (partial set-cursor-before measureNumber))
                    (reset-view-model!))}
       [:> rn/View {:style {:width 15 :height "100%"}}]]
      [:> rn/TouchableWithoutFeedback
       {:on-press (fn []
                    (swap! sample-data (partial set-cursor-on measureNumber))
                    (reset-view-model!))}
       [:> rn/View {:style {:flex 1 :height "100%"}}]]
      [:> rn/TouchableWithoutFeedback
       {:on-press (fn []
                    (swap! sample-data (partial set-cursor-before (inc measureNumber)))
                    (reset-view-model!))}
       [:> rn/View {:style {:width 15 :height "100%"}}]]]]))

(defn single-line-separator [measureNumber]
  [:> ViewOverflow {:style {:width 1 :height 65}}
   [:> rn/TouchableWithoutFeedback
    {:on-press (fn []
                 (swap! sample-data (partial set-cursor-before (inc measureNumber)))
                 (reset-view-model!))}
    [:> rn/View {:style {:margin-top 32 :width 1 :height 33 :border-left-width 1 :border-left-color "black"}}]]])

(defn double-line-separator [measureNumber]
  [:> ViewOverflow {:style {:width 3 :height 65}}
   [:> rn/TouchableWithoutFeedback
    {:on-press (fn []
                 (swap! sample-data (partial set-cursor-before (inc measureNumber)))
                 (reset-view-model!))}
    [:> rn/View {:style {:margin-top 32 :width 3 :height 33 :background-color "black"}}
     (bar-lines 1 1)]]])

(defn separator [props]
  (let [item (.-leadingItem props)
        measureNumber (.-measureNumber item)]
    (if (.-nextMeasureHasDifferentTempo item)
      (double-line-separator measureNumber)
      (single-line-separator measureNumber))))

(defn header []
  [:> rn/View {:style {:width 12 :height 65}}
   [:> rn/TouchableWithoutFeedback
    {:on-press (fn []
                 (swap! sample-data (partial set-cursor-before 1))
                 (reset-view-model!))}
    [:> rn/View {:style {:margin-top 32 :width 12 :height 33 :border-right-width 1 :border-right-color "black"}}
     (bar-lines 11 0)]]])

(defn footer []
  [:> rn/View {:style {:width 11 :height 65}}
   [:> rn/View {:style {:position "absolute" :height 32}}
    [:> rn/View (style :measure/number-and-cursors)
     (cursor {:bottom 2 :left -5})]]
   [:> rn/View {:style {:margin-top 32 :width 6 :height 33 :background-color "black"}}
    (bar-lines 2 1)]])

(defn Staff [styles]
  [:> rn/View styles
   [:> rn/FlatList
    {:data ^js @view-model
     :key-extractor (fn [item _] (str (.-measureNumber item)))
     :horizontal true
     :Cell-renderer-component ViewOverflow
     :List-header-component #(r/as-element [header])
     :List-footer-component #(r/as-element [footer])
     :Item-separator-component #(r/as-element [separator %])
     :render-item #(r/as-element [cell %])}]])

(comment)
  ;; if needed for performance, consider https://github.com/Flipkart/recyclerlistview
