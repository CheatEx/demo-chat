(ns demo-chat.effects
  (:require
   [re-frame.core :as rf]
   [pneumatic-tubes.core :as tubes]
   
   [demo-chat.events :as events]))

(defn on-receive [event-v]
  (.log js/console "received from server:" (str event-v))
  (rf/dispatch event-v))

(def tube (tubes/tube (str "ws://localhost:3449/chat") on-receive))

(rf/reg-fx :ws
 (fn [data]
   (println "dispatching" data)
   (tubes/dispatch tube data)))

(tubes/create! tube)
