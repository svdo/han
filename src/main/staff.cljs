(ns staff
  (:require ["react-native" :as rn]
            ["react-native-view-overflow" :default ViewOverflow]
            [reagent.core :as r]
            [styles :refer [styles]]
            [images :refer [images]]))

(defn show-measure-num? [num]
  (or (= 1 num)
      (= 0 (mod num 5))))

(def show-double-bar? show-measure-num?) ;; todo temporary

(defn cell [details]
  (let [item (details "item")
        num (item "measureNumber")]
    [:> ViewOverflow {:style {:width 68}}
     [:> rn/ImageBackground {:source (:img (:staff/bar images))
                             :resize-mode "repeat"
                             :style {:width "100%" :height 66}}
      ; [:> rn/View {:style {:display "flex" :justify-content "flex-start"}}
      ;    [:> rn/Image {:source (:img (:note/dotted-sixteenth images))
      ;                  :style {:display "flex"
      ;                          :tint-color "black"
      ;                          :width (:width (:note/dotted-sixteenth images))
      ;                          :height (:height (:note/dotted-sixteenth images))}}]
      ;    [:> rn/Text "= 128"]]
      ; (if (show-measure-num? num) [:> rn/Text {:style (:measure-number styles)} num])
      ; [:> rn/View {:style (:measure-contents styles)}
      ;  [:> rn/Text {:style (:time-signature styles)} "3"]
      ;  [:> rn/Text {:style (:time-signature styles)} "4"]]
      [:> rn/View {:style {:flex-direction "column"}}
       [:> rn/View {:style {:background-color "#ff000033" :height 32}}
        (if (show-measure-num? num)
          [:> rn/Text {:style {:position "absolute" :bottom 0 :width 32 :left -16 :text-align "center"}} num])]
       [:> rn/View {:style {:background-color "#00ff0033" :height 33 :align-items "center" :justify-content "space-around"}}
        [:> rn/Text {:numberOfLines 1 :ellipsizeMode "tail" :style (merge {:width 68} (:time-signature styles))} "2+2+3+2+2"]
        [:> rn/Text {:style (:time-signature styles)} "8"]]]]]))

(defn staff-component [image]
  (let [width (:width image)]
    [:> rn/View {:style {:width width}}
     [:> rn/ImageBackground {:source (:img image)
                             :resize-mode "center"
                             :style {:width width :height 66}}]]))

(defn header    [] (staff-component (:staff/start images)))
(defn separator [props]
  (if (show-double-bar? (.-measureNumber (.-leadingItem props)))
    (staff-component (:staff/double-separator images))
    (staff-component (:staff/measure-separator images))))
(defn footer    [] (staff-component (:staff/end images)))

(defn Staff [styles]
  [:> rn/View styles
   [:> rn/FlatList
    {:data (clj->js (mapv (fn [n] {:key (str n) :measureNumber n}) (range 1 11)))
     :horizontal true
     :Cell-renderer-component ViewOverflow
     :List-header-component #(r/as-element [header])
     :List-footer-component #(r/as-element [footer])
     :Item-separator-component #(r/as-element [separator %])
     :render-item (fn [details] (r/as-element [cell (js->clj details)]))}]])

(comment
  ;; if needed for performance, consider https://github.com/Flipkart/recyclerlistview
  )