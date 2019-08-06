(ns demo-chat.subs
  (:require
   [re-frame.core :as rf]
   [demo-chat.db :as db]))

(rf/reg-sub
 ::history
 (fn [db]
   (::db/history db)))

(rf/reg-sub
 ::user
 (fn [db]
   (::db/user db)))
