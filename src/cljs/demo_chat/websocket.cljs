(ns demo-chat.websocket
  (:require
   [re-frame.core :as rf]
   [pneumatic-tubes.core :as tubes]
   
   [demo-chat.events :as events]))

(defn ws-protocol [http-protocol]
  (case http-protocol
    "http:"  "ws:"
    "https:" "wss:"
    :else   nil))

(def ws-url (let [protocol (-> js/window .-location .-protocol)
                  host (-> js/window .-location .-host)]
              (str (ws-protocol protocol) "//" host "/chat")))

(defn on-receive [event-v]
  (rf/dispatch event-v))

(defn on-connect []
  (rf/dispatch-sync [::events/connected true]))

(defn on-disconnect []
  (rf/dispatch-sync [::events/connected false]))

(def tube (tubes/tube ws-url on-receive on-connect on-disconnect (fn []) {}))

(rf/reg-fx :ws
           (fn [[operation & data]]
             (case operation
               ::transfer (tubes/dispatch tube data)
               ::open (tubes/create! tube))))
