(ns demo-chat.room
  (:require [demo-chat.events :as events]))

(defn new [] (atom {::receivers {} ::history []}))

(defn- join [room id receiver]
  (assoc-in room [::receivers id] receiver))

(defn- leave [room id]
  (update room ::receivers dissoc id))

(defn- send [room message]
  (update room ::history conj message))

(defn join! [room id receiver]
  (swap! room join id receiver)
  (receiver [::events/history (::history @room)]))

(defn leave! [room id]
  (swap! room leave id))

(defn send! [room message]
  (swap! room send message)
  (doseq [kv (::receivers @room)]
    ((second kv) [::events/received message])))
