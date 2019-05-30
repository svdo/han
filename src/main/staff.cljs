(ns staff
  (:require ["react-native" :as rn]
            ["react-native-view-overflow" :default ViewOverflow]
            [reagent.core :as r]
            [styles :refer [styles]]))

(defonce staff-img (js/require "../img/staff.png"))
(defonce measure-separator (js/require "../img/separator.png"))
(defonce staff-header (js/require "../img/staff-header.png"))
(defonce staff-footer (js/require "../img/staff-end.png"))

(defn show-measure-num? [num]
  (or (= 1 num)
      (= 0 (mod num 5))))

(defn cell [details]
  (let [item (details "item")
        num (item "measure-number")]
    [:> ViewOverflow {:style {:width 68}}
     [:> rn/ImageBackground {:source staff-img
                             :resize-mode "stretch"
                             :style {:width "100%" :height 66}}
      (if (show-measure-num? num) [:> rn/Text {:style (:measure-number styles)} num])
      [:> rn/View {:style (:measure-contents styles)}
       [:> rn/Text {:style (:time-signature styles)} "3"]
       [:> rn/Text {:style (:time-signature styles)} "4"]]]]))

(defn staff-component [width bg-image]
  [:> rn/View {:style {:width width}}
   [:> rn/ImageBackground {:source bg-image
                           :resize-mode "center"
                           :style {:width width :height 66}}]])

(defn header    [] (staff-component 15 staff-header))
(defn separator [] (staff-component 1 measure-separator))
(defn footer    [] (staff-component 20 staff-footer))

(defn Staff [styles]
  [:> rn/View styles
   [:> rn/FlatList
    {:data (clj->js (mapv (fn [n] {:key (str n) :measure-number n}) (range 1 11)))
     :horizontal true
     :Cell-renderer-component ViewOverflow
     :List-header-component #(r/as-element [header])
     :List-footer-component #(r/as-element [footer])
     :Item-separator-component #(r/as-element [separator])
     :render-item (fn [details] (r/as-element [cell (js->clj details)]))}]])