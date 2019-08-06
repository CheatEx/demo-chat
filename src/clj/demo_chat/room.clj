(ns demo-chat.room)

(defn new [] {})

(defn send [room message]
  (doseq [kv room]
    ((second kv) message)))

(defn join [room id receiver]
  (assoc room id receiver))

(defn leave [room id]
  (dissoc room id))