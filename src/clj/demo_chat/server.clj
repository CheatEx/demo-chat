(ns demo-chat.server
  (:require [demo-chat.handler :refer [handler]]
            [config.core :refer [env]]
            [org.httpkit.server :refer [run-server]])
  (:gen-class))

 (defn -main [& args]
   (let [port (or (env :port) 3000)]
     (run-server handler {:port port :join? false})))
