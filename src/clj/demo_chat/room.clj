(ns demo-chat.room)

(defn new [] [])

(defn send [room message]
  (doseq [r room]
    (r message)))

(defn join [room receiver]
  (conj room receiver))
