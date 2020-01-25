(ns new-project-name.handlers
  (:require
   [re-frame.core :refer [reg-event-db ->interceptor reg-event-fx reg-fx dispatch]]
   [com.rpl.specter :as sp :refer-macros [select select-one setval transform selected-any?]]
   [clojure.spec.alpha :as s]
   [new-project-name.db :as db :refer [default-app-db app-db-spec]]
   ))

(defn check-and-throw
  "Throw an exception if db doesn't have a valid spec."
  [spec db event]
  (when-not (s/valid? spec db)
    (let [explanation (s/explain-str spec db)]
      (throw (str "Spec check failed: " explanation))
      true)))

(def validate-spec
  (if goog.DEBUG
    (->interceptor
        :id :validate-spec
        :after (fn [context]
                 (let [db (-> context :effects :db)
                       old-db (-> context :coeffects :db)
                       event (-> context :coeffects :event)]

                   (if (some? (check-and-throw app-db-spec db event))
                     (assoc-in context [:effects :db] old-db) ;; put the old db back as the new db when check fails
                     context))))                              ;; otherwise return context unchanged
    ->interceptor))

(defn initialize-db [_ _]
  default-app-db)

(defn set-theme [db [_ theme]]
  (->> db
       (setval [:settings :theme] theme)))

(reg-event-db :initialize-db [validate-spec] initialize-db)
(reg-event-db :set-theme [validate-spec] set-theme)
