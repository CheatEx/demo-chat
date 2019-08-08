(ns demo-chat.handler
  (:require [compojure.core :refer [GET defroutes]]
            [compojure.route :refer [resources]]
            [ring.util.response :refer [resource-response]]
            [ring.middleware.reload :refer [wrap-reload]]
            [pneumatic-tubes.core :refer [receiver transmitter dispatch]]
            [pneumatic-tubes.httpkit :refer [websocket-handler]]
            
            [demo-chat.room :as room]
            [demo-chat.redis :as redis]
            [demo-chat.events :as events]))

(def tx (transmitter))
(def dispatch-to (partial dispatch tx))

(defonce chat (room/create-room redis/store))

(def rx
  (receiver
   {::events/join
    (fn [tube [_ user]]
      (room/join! chat user (fn [evt] (dispatch tx tube evt)))
      (assoc tube ::user user))
    
    ::events/send
    (fn [tube [_ message]]
      (room/send! chat message)
      tube)
    
    :tube/on-destroy
    (fn [tube _]
      (room/leave! chat (::user tube))
      tube)}))

(def ws-handler (websocket-handler rx))

(defroutes routes
  (GET "/" [] (resource-response "index.html" {:root "public"}))
  (GET "/chat" [] ws-handler)
  (resources "/"))

(def dev-handler (-> #'routes wrap-reload))

(def handler routes)
