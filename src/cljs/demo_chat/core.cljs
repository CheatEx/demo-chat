(ns demo-chat.core
  (:require
   [reagent.core :as ra]
   [re-frame.core :as rf]
   [demo-chat.events :as events]
   [demo-chat.effects :as effects]
   [demo-chat.views :as views]
   [demo-chat.config :as config]))

(defn dev-setup []
  (when config/debug?
    (enable-console-print!)
    (println "dev mode")))

(defn mount-root []
  (rf/clear-subscription-cache!)
  (ra/render [views/app]
             (.getElementById js/document "app")))

(defn ^:export init []
  (rf/dispatch-sync [::events/initialize-db])
  (dev-setup)
  (mount-root))
