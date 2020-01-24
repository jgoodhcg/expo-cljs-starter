(ns new-project-name.app
  (:require
    ["expo" :as ex]
    ["react-native" :as rn]
    ["react" :as react]
    ["react-native-router-flux" :as nav]
    [reagent.core :as r]
    [shadow.expo :as expo]
    ))

;; must use defonce and must refresh full app so metro can fill these in
;; at live-reload time `require` does not exist and will cause errors
;; must use path relative to :output-dir

(defonce splash-img (js/require "../assets/shadow-cljs.png"))

(def styles
  ^js (-> {:container
           {:flex 1
            :backgroundColor "#fff"
            :alignItems "center"
            :justifyContent "center"}
           :title
           {:fontWeight "bold"
            :fontSize 24
            :color "blue"}}
          (clj->js)
          (rn/StyleSheet.create)))


(defn home-component []
  (r/as-element [:> rn/View {:style (.-container styles)}
                 [:> rn/Text {:style (.-title styles)} "CLJS + Expo + Navigation"]
                 [:> rn/Image {:source splash-img :style {:width 200 :height 200}}]]))

(defn root []
  [:> nav/Router
   [:> nav/Stack {:key "root"}
    [:> nav/Scene {:key       "home"
                   :title     "Home"
                   :component home-component}]]])

(defn start
  {:dev/after-load true}
  []
  (expo/render-root (r/as-element [root])))

(defn init []
  (start))

