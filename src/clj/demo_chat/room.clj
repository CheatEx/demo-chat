(ns demo-chat.room
  (:require [demo-chat.events :as events]))

(defn new [] {::receivers {} ::history []})

(defn send [room message]
  (let [updated (update room ::history conj message)]
    (doseq [kv (::receivers room)]
      ((second kv) [::events/received message]))
    updated))

(defn join [room id receiver]
  (let [updated (assoc-in room [::receivers id] receiver)]
    (receiver [::events/history (::history room)])
    updated))

(defn leave [room id]
  (update room ::receivers dissoc id))