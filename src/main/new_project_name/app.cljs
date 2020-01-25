(ns new-project-name.app
  (:require
    ["expo" :as ex]
    ["react-native" :as rn]
    ["react" :as react]
    ["react-native-router-flux" :as nav]
    ["react-native-paper" :as paper]
    [camel-snake-kebab.core :as csk]
    [camel-snake-kebab.extras :as cske]
    [reagent.core :as r]
    [re-frame.core :refer [subscribe dispatch dispatch-sync]]
    [shadow.expo :as expo]
    [new-project-name.handlers]
    [new-project-name.subscriptions]
    [new-project-name.helpers :refer [<sub >evt]]
    ))

;; must use defonce and must refresh full app so metro can fill these in
;; at live-reload time `require` does not exist and will cause errors
;; must use path relative to :output-dir

(defonce splash-img (js/require "../assets/shadow-cljs.png"))

(def styles
  ^js (-> {:surface
           {:flex           1
            :justify-content "center"}

           :theme-switch
           {:flex-direction  "row"
            :justify-content "space-between"}}
          (#(cske/transform-keys csk/->camelCase %))
          (clj->js)
          (rn/StyleSheet.create)))


(defn home-component []
  (r/as-element
   (let [version (<sub [:version])
         theme   (<sub [:theme])]
     [:> paper/Surface {:style (.-surface styles)}
      [:> rn/View
       [:> paper/Card
        [:> paper/Card.Title {:title    "A nice Expo/Shadow-cljs template"
                              :subtitle "For quick project startup"}]
        [:> paper/Card.Content
         [:> rn/View {:style (.-themeSwitch styles)}
          [:> paper/Text "Dark mode"]
          [:> paper/Switch {:value           (= theme :dark)
                            :on-value-change #(>evt [:set-theme (if (= theme :dark)
                                                                  :light
                                                                  :dark)])}]]
         [:> paper/Paragraph (str "Version: " version)]]]]])))

(defn root []
  (let [theme (<sub [:theme])]
    [:> paper/Provider {:theme (case theme
                                 :light paper/DefaultTheme
                                 :dark  paper/DarkTheme
                                 paper/DarkTheme)}
     [:> nav/Router
      [:> nav/Stack {:key "root"}
       [:> nav/Scene {:key          "home"
                      :title        "Home"
                      :hide-nav-bar true
                      :component    home-component}]]]]))

(defn start
  {:dev/after-load true}
  []
  (expo/render-root (r/as-element [root])))

(defn init []
  (dispatch-sync [:initialize-db])
  (start))

