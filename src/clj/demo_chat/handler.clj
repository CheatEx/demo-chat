(ns demo-chat.handler
  (:require [compojure.core :refer [GET defroutes]]
            [compojure.route :refer [resources]]
            [ring.util.response :refer [resource-response]]
            [ring.middleware.reload :refer [wrap-reload]]
            [pneumatic-tubes.core :refer [receiver transmitter dispatch]]
            [pneumatic-tubes.httpkit :refer [websocket-handler]]
            
            [demo-chat.room :as room]
            [demo-chat.events :as events]))

(def tx (transmitter))
(def dispatch-to (partial dispatch tx))

(defonce chat (atom (room/new)))

(def rx
  (receiver
   {::events/join
    (fn [tube [_ user]]
      (swap! chat room/join user (fn [message] (dispatch tx tube [::events/received message])))
      (assoc tube ::user user))
    
    ::events/send
    (fn [tube [_ message]]
      (room/send @chat message)
      tube)
    
    :tube/on-destroy
    (fn [tube _]
      (swap! chat room/leave (::user tube))
      tube)}))

(def ws-handler (websocket-handler rx))

(defroutes routes
  (GET "/" [] (resource-response "index.html" {:root "public"}))
  (GET "/chat" [] ws-handler)
  (resources "/"))

(def dev-handler (-> #'routes wrap-reload))

(def handler routes)
