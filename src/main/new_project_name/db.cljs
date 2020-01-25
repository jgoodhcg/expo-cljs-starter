(ns new-project-name.db
  (:require
   ["expo-constants" :as expo-constants]

   [clojure.spec.alpha :as s]
   [spec-tools.data-spec :as ds]
   [spec-tools.core :as st]))

(def version (-> expo-constants
                 (.-default)
                 (.-manifest)
                 (.-version)))

(def app-db-spec
  (ds/spec {:spec {:settings {:theme (s/spec #{:light :dark})}
                   :version  string?}
            :name ::app-db}))

(def default-app-db
  {:settings {:theme :dark}
   :version  version})
