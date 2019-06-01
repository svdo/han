(ns staff
  (:require ["react-native" :as rn]
            ["react-native-view-overflow" :default ViewOverflow]
            [reagent.core :as r]
            [styles :refer [styles]]))

(defonce staff-img         {:img (js/require "../img/staff.png")        :width 10})
(defonce measure-separator {:img (js/require "../img/separator.png")    :width  1})
(defonce staff-header      {:img (js/require "../img/staff-header.png") :width 15})
(defonce staff-footer      {:img (js/require "../img/staff-end.png")    :width 11})

(defn show-measure-num? [num]
  (or (= 1 num)
      (= 0 (mod num 5))))

(defn cell [details]
  (let [item (details "item")
        num (item "measure-number")]
    [:> ViewOverflow {:style {:width 68}}
     [:> rn/ImageBackground {:source (:img staff-img)
                             :resize-mode "stretch"
                             :style {:width "100%" :height 66}}
      (if (show-measure-num? num) [:> rn/Text {:style (:measure-number styles)} num])
      [:> rn/View {:style (:measure-contents styles)}
       [:> rn/Text {:style (:time-signature styles)} "3"]
       [:> rn/Text {:style (:time-signature styles)} "4"]]]]))

(defn staff-component [image]
  (let [width (:width image)]
    [:> rn/View {:style {:width width}}
     [:> rn/ImageBackground {:source (:img image)
                             :resize-mode "center"
                             :style {:width width :height 66}}]]))

(defn header    [] (staff-component staff-header))
(defn separator [] (staff-component measure-separator))
(defn footer    [] (staff-component staff-footer))

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