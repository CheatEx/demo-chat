(ns demo-chat.events
  (:require
   [clojure.string :as str]
   [re-frame.core :as rf]
   [pneumatic-tubes.core :as tubes]
   [demo-chat.db :as db]
   [demo-chat.websocket :as ws]))

(rf/reg-event-db ::initialize-db
                 (fn [_ _]
                   db/initial-db))

(rf/reg-event-fx ::logged-in
                 (fn [cofx [_ user]]
                   {:db (assoc (:db cofx) ::db/user user)
                    :ws [::ws/transfer ::join user]}))

(rf/reg-event-fx ::connect
                 (fn [cofx _]
                   {:ws [::ws/open]}))

(rf/reg-event-fx ::send-message
                 (fn [cofx [_ text]]
                   (if-not (str/blank? text)
                     (let [message (db/make-message (get-in cofx [:db ::db/user]) text)]
                       {:ws [::ws/transfer ::send message]})
                     {})))

(rf/reg-event-db ::connected
                 (fn [db [_ value]]
                   (assoc db ::db/connected value)))

(rf/reg-event-db ::received
                 (fn [db [_ message]]
                   (update db ::db/history conj message)))

(rf/reg-event-db ::history
                 (fn [db [_ history]]
                   (update db ::db/history into history)))

(rf/reg-event-db ::logged-out
                 (fn [db _]
                   (dissoc db ::db/user)))
