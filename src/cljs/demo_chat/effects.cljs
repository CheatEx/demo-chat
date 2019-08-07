(ns demo-chat.effects
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

(def tube (tubes/tube ws-url on-receive))

(rf/reg-fx :ws
           (fn [data]
             (tubes/dispatch tube data)))

(tubes/create! tube)
