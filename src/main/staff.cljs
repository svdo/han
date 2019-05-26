(ns staff
  (:require ["react-native" :as rn]
            [reagent.core :as r]))

(defonce staff-img (js/require "../img/staff.png"))

(defn cell [details]
  (let [item (details "item")
        key (item "key")]
    [:> rn/View {:style {:width 80
                         :background-color (if (= 0 (mod key 2)) "#dd666611" "#ffffff33")}}
     [:> rn/Text {:style {:left -15 :width 30 :text-align "center"}} key]]))

(defn Staff [styles]
  [:> rn/View styles
   [:> rn/ImageBackground {:source staff-img
                           :resize-mode "stretch"
                           :style {:width "100%" :height 66}}
    [:> rn/FlatList
     {:data (clj->js (mapv (fn [n] {:key n}) (range 0 100)))
      :horizontal true
      :render-item (fn [details] (r/as-element [cell (js->clj details)]))}]]])