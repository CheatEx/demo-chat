(ns demo-chat.redis
  (:require [config.core :refer [env]]
            [taoensso.carmine :as car :refer (wcar)]
            [demo-chat.room :as room]))
  
(defmacro wcar* [& body] `(car/wcar server1-conn ~@body))

(def redis-uri (or (env :redis-url) "redis://localhost:6379/"))

(def server1-conn {:pool {} :spec {:uri redis-uri}})

(def history-key "demo-chat:history:chat")

(def store
  (reify room/Store
    
    (room/save! [this message]
      (wcar* (car/rpush history-key message)))

    (room/load-history! [this]
      (wcar* (car/lrange history-key 0 -1)))))
