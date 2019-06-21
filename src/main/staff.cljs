(ns staff
  (:require ["react-native" :as rn]
            ["react-native-view-overflow" :default ViewOverflow]
            [reagent.core :as r]
            [styles :refer (styles)]
            [images :refer (images)]))

(defn show-measure-num? [num]
  (or (= 1 num)
      (= 0 (mod num 5))))

(def show-double-bar? show-measure-num?) ;; todo temporary

(defn bar-lines [width margin-left]
  (map (fn [i] ^{:key (str i)} [:> rn/View {:style {:margin-left margin-left
                                                    :width width
                                                    :height (if (= i 5) 9 8)
                                                    :background-color "white"
                                                    :border-top-width 1
                                                    :border-top-color "black"}}])
       (range 1 6)))

(defn cell [details]
  (let [item (details "item")
        num (item "measureNumber")]
    [:> ViewOverflow {:style {:width 68 :height 65}}

     ;; variant with cursor between measures
     [:> rn/View {:style {:position "absolute" :top 0 :width 68 :height 32}}
      [:> rn/Text {:style (:measure-number styles)} num]
      [:> rn/View {:style {:position "absolute"
                           :bottom 16 :left -5 :width 10 :height 10 :background-color "transparent"
                           :border-top-width 10 :border-top-color "blue"
                           :border-left-width 5 :border-left-color "transparent"
                           :border-right-width 5 :border-right-color "transparent"
                           :border-bottom-width 0 :border-bottom-color "transparent"}}]]

     ;; variant with cursor on measure
     [:> rn/View {:style {:position "absolute" :top 0 :width 68 :height 32}}
      ; [:> rn/Text {:style (:measure-number styles)} num]
      [:> rn/View {:style {:display "flex" :width 68 :height 32 :flex-direction "row" :justify-content "center"}}
       [:> rn/View {:style {:position "absolute" :bottom 2 :width 10 :height 10
                            :background-color "transparent"
                            :border-top-width 10 :border-top-color "blue"
                            :border-left-width 5 :border-left-color "transparent"
                            :border-right-width 5 :border-right-color "transparent"
                            :border-bottom-width 0 :border-bottom-color "transparent"}}]]]

     ;; tempo
     [:> rn/View {:style {:display "flex" :flex-direction "row" :justify-content "flex-start" :align-items "flex-end" :margin-left 2}}
      (let [note (:note/dotted-sixteenth images)]
        [:> rn/Image {:source (:img note)
                      :style {:tint-color "black"
                              :width (* 0.7 (:width note))
                              :height (* 0.7 (:height note))}}])
      [:> rn/Text {:style (:tempo-text styles)} "=160"]]

     [:> rn/View {:style {:position "absolute" :top 32 :width 68 :height 33 :display "flex" :flex-direction "column"}}
      (bar-lines 68 0)]
     [:> rn/View {:style {:position "absolute" :top 32 :width 68 :height 33 :display "flex" :flex-direction "column" :justify-content "space-around" :align-items "center"}}
      [:> rn/Text {:style (:time-signature styles)} "3"]
      [:> rn/Text {:style (:time-signature styles)} "4"]]]))

(defn single-line-separator []
  [:> ViewOverflow {:style {:width 1 :height 65}}
   [:> rn/View {:style {:margin-top 32 :width 1 :height 33 :border-left-width 1 :border-left-color "black"}}]])

(defn double-line-separator []
  [:> ViewOverflow {:style {:width 3 :height 65}}
   [:> rn/View {:style {:margin-top 32 :width 3 :height 33 :background-color "black"}}
    (bar-lines 1 1)]])

(defn separator [props]
  (if (show-double-bar? (.-measureNumber (.-leadingItem props)))
    (double-line-separator)
    (single-line-separator)))

(defn header []
  [:> rn/View {:style {:width 12 :height 65}}
   [:> rn/View {:style {:margin-top 32 :width 12 :height 33 :border-right-width 1 :border-right-color "black"}}
    (bar-lines 11 0)]])

(defn footer []
  [:> rn/View {:style {:width 11 :height 65}}
   [:> rn/View {:style {:margin-top 32 :width 6 :height 33 :background-color "black"}}
    (bar-lines 2 1)]])

(defn Staff [styles]
  [:> rn/View styles
   [:> rn/FlatList
    {:data (clj->js (mapv (fn [n] {:key (str n) :measureNumber n}) (range 1 4)))
     :horizontal true
     :Cell-renderer-component ViewOverflow
     :List-header-component #(r/as-element [header])
     :List-footer-component #(r/as-element [footer])
     :Item-separator-component #(r/as-element [separator %])
     :render-item (fn [details] (r/as-element [cell (js->clj details)]))}]])

(comment
  ;; if needed for performance, consider https://github.com/Flipkart/recyclerlistview
  )