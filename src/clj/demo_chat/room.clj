(ns demo-chat.room)

(defn new [] {::receivers {} ::history []})

(defn send [room message]
  (let [updated (update room ::history conj message)]
    (doseq [kv (::receivers room)]
      ((second kv) message))
    updated))

(defn join [room id receiver]
  (assoc-in room [::receivers id] receiver))

(defn leave [room id]
  (update room ::receivers dissoc id))