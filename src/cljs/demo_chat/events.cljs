(ns demo-chat.events
  (:require
   [re-frame.core :as rf]
   [pneumatic-tubes.core :as tubes]
   [demo-chat.db :as db]))

(rf/reg-event-db ::initialize-db
                 (fn [_ _]
                   db/initial-db))

(rf/reg-event-fx ::logged-in
                 (fn [cofx [_ user]]
                   {:db (assoc (:db cofx) ::db/user user)
                    :ws [::join user]}))

(rf/reg-event-fx ::send-message
                 (fn [cofx [_ text]]
                   (let [message (db/make-message (get-in cofx [:db ::db/user]) text)]
                     {:ws [::send message]})))

(rf/reg-event-db ::received
                 (fn [db [_ message]]
                   (update db ::db/history conj message)))