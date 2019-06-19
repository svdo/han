(ns staff
  (:require ["react-native" :as rn]
            ["react-native-view-overflow" :default ViewOverflow]
            [reagent.core :as r]
            [styles :refer (styles)]
            [images :refer (images)]
            ["react-native-svg" :refer (Line Rect) :default Svg]))

(defn show-measure-num? [num]
  (or (= 1 num)
      (= 0 (mod num 5))))

(def show-double-bar? show-measure-num?) ;; todo temporary

(defn bar-lines [width]
  (take 5 (map (fn [y] ^{:key (str y)}
                 [:> Line {:x1 0.5 :y1 y :x2 (+ 0.5 (dec width)) :y2 y
                           :stroke-width 1 :stroke "black"
                           :stroke-linecap "square"}])
               (iterate (partial + 8) 0.5))))

(defn cell [details]
  (let [item (details "item")
        num (item "measureNumber")]
    [:> ViewOverflow {:style {:width 68 :height 65}}
     [:> rn/View {:style {:margin-top 32 :width 68 :height 33 :display "flex" :flex-direction "column"}}
      [:> rn/View {:style {:width 68 :height 8 :background-color "white" :border-top-width 1 :border-top-color "black"}}]
      [:> rn/View {:style {:width 68 :height 8 :background-color "white" :border-top-width 1 :border-top-color "black"}}]
      [:> rn/View {:style {:width 68 :height 8 :background-color "white" :border-top-width 1 :border-top-color "black"}}]
      [:> rn/View {:style {:width 68 :height 9 :background-color "white" :border-top-width 1 :border-top-color "black" :border-bottom-width 1 :border-bottom-color "black"}}]]]))

(defn single-line-separator []
  [:> ViewOverflow {:style {:width 1 :height 65}}
   [:> rn/View {:style {:margin-top 32 :width 1 :height 33 :border-left-width 1 :border-left-color "black"}}]])

(defn double-line-separator []
  [:> ViewOverflow {:style {:width 3 :height 65}}
   [:> rn/View {:style {:margin-top 32 :width 3 :height 33 :background-color "black"}}
    [:> rn/View {:style {:margin-left 1 :width 1 :height 8 :background-color "white" :border-top-width 1 :border-top-color "black"}}]
    [:> rn/View {:style {:margin-left 1 :width 1 :height 8 :background-color "white" :border-top-width 1 :border-top-color "black"}}]
    [:> rn/View {:style {:margin-left 1 :width 1 :height 8 :background-color "white" :border-top-width 1 :border-top-color "black"}}]
    [:> rn/View {:style {:margin-left 1 :width 1 :height 9 :background-color "white" :border-top-width 1 :border-top-color "black" :border-bottom-width 1 :border-bottom-color "black"}}]]])

(defn separator [props]
  (if (show-double-bar? (.-measureNumber (.-leadingItem props)))
    (double-line-separator)
    (single-line-separator)))

(defn header []
  [:> rn/View {:style {:width 12 :height 65}}
   [:> rn/View {:style {:margin-top 32 :width 12 :height 33 :border-right-width 1 :border-right-color "black"}}
    [:> rn/View {:style {:width 11 :height 8 :background-color "white" :border-top-width 1 :border-top-color "black"}}]
    [:> rn/View {:style {:width 11 :height 8 :background-color "white" :border-top-width 1 :border-top-color "black"}}]
    [:> rn/View {:style {:width 11 :height 8 :background-color "white" :border-top-width 1 :border-top-color "black"}}]
    [:> rn/View {:style {:width 11 :height 9 :background-color "white" :border-top-width 1 :border-top-color "black" :border-bottom-width 1 :border-bottom-color "black"}}]]])

(defn footer []
  [:> rn/View {:style {:width 11 :height 65}}
   [:> rn/View {:style {:margin-top 32 :width 6 :height 33 :background-color "black"}}
    [:> rn/View {:style {:margin-left 1 :width 2 :height 8 :background-color "white" :border-top-width 1 :border-top-color "black"}}]
    [:> rn/View {:style {:margin-left 1 :width 2 :height 8 :background-color "white" :border-top-width 1 :border-top-color "black"}}]
    [:> rn/View {:style {:margin-left 1 :width 2 :height 8 :background-color "white" :border-top-width 1 :border-top-color "black"}}]
    [:> rn/View {:style {:margin-left 1 :width 2 :height 9 :background-color "white" :border-top-width 1 :border-top-color "black" :border-bottom-width 1 :border-bottom-color "black"}}]]])

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