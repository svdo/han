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
     [:> Svg {:height 34 :width 68 :view-box "0 0 68 33"
              :style {:position "absolute" :bottom 0}}
      (bar-lines 68)]]))

(defn single-line-separator []
  [:> ViewOverflow {:style {:width 1 :height 65}}
   [:> Svg {:height 34 :width 1 :view-box "0 0 1 33"
            :style {:position "absolute" :bottom 0}}
    [:> Line {:x1 0.5 :y1 0.5 :x2 0.5 :y2 32.5 :stroke-linecap "square" :stroke-width 1 :stroke "black"}]]])

(defn double-line-separator []
  [:> ViewOverflow {:style {:width 3 :height 65}}
   [:> Svg {:height 34 :width 3 :view-box "0 0 3 33"
            :style {:position "absolute" :bottom 0}}
    [:> Line {:x1 0.5 :y1 0.5 :x2 0.5 :y2 32.5 :stroke-linecap "square" :stroke-width 1 :stroke "black"}]
    [:> Line {:x1 2.5 :y1 0.5 :x2 2.5 :y2 32.5 :stroke-linecap "square" :stroke-width 1 :stroke "black"}]
    (bar-lines 3)]])

(defn separator [props]
  (if (show-double-bar? (.-measureNumber (.-leadingItem props)))
    (double-line-separator)
    (single-line-separator)))

(defn header []
  [:> rn/View {:style {:width 15 :height 65}}
   [:> Svg {:height 34 :width 15 :view-box "0 0 15 33"
            :style {:position "absolute" :bottom 0}}
    (bar-lines 15)
    [:> Line {:x1 14.5 :y1 0.5 :x2 14.5 :y2 32.5 :stroke-linecap "square" :stroke-width 1 :stroke "black"}]]])

(defn footer []
  [:> rn/View {:style {:width 11 :height 65}}
   [:> Svg {:height 34 :width 11 :view-box "0 0 11 33"
            :style {:position "absolute" :bottom 0}}
    [:> Line {:x1 0.5 :y1 0.5 :x2 0.5 :y2 32.5 :stroke-linecap "square" :stroke-width 1 :stroke "black"}]
    [:> Line {:x1 4.5 :y1 1.5 :x2 4.5 :y2 31.5 :stroke-linecap "square" :stroke-width 3 :stroke "black"}]
    (bar-lines 3)]])

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