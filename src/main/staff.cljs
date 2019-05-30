(ns staff
  (:require ["react-native" :as rn]
            ["react-native-view-overflow" :default ViewOverflow]
            [reagent.core :as r]))

(defonce staff-img (js/require "../img/staff.png"))
(defonce measure-separator (js/require "../img/separator.png"))
(defonce staff-header (js/require "../img/staff-header.png"))

(defn cell [details]
  (let [item (details "item")
        key (item "key")
        num (item "measure-number")]
    [:> ViewOverflow {:style {:width 80
                              :background-color (if (= 0 (mod num 2)) "#dd666611" "#ffffff33")}}
     [:> rn/Text {:style {:left -15 :width 30 :text-align "center"}} num]]))

(defn header []
  [:> rn/View {:style {:width 15}}
   [:> rn/ImageBackground {:source staff-header
                           :resize-mode "center"
                           :style {:width 15 :height 66}}]])

(defn separator []
  [:> rn/View {:style {:width 1}}
   [:> rn/ImageBackground {:source measure-separator
                           :resize-mode "center"
                           :style {:width 3 :height 66}}]])

(defn Staff [styles]
  [:> rn/View styles
   [:> rn/ImageBackground {:source staff-img
                           :resize-mode "stretch"
                           :style {:width "100%" :height 66}}
    [:> rn/FlatList
     {:data (clj->js (mapv (fn [n] {:key (str n) :measure-number n}) (range 1 11)))
      :horizontal true
      :Cell-renderer-component ViewOverflow
      :List-header-component #(r/as-element [header])
      :Item-separator-component #(r/as-element [separator])
      :render-item (fn [details] (r/as-element [cell (js->clj details)]))}]]])