(ns demo-chat.subs
  (:require
   [re-frame.core :as rf]
   [demo-chat.db :as db]))

(rf/reg-sub
 ::history
 (fn [db]
   (let [user (::db/user db)]
     (->> (::db/history db)
          (map #(assoc % ::own (= (::db/from %) user)))))))

(rf/reg-sub
 ::user
 (fn [db]
   (::db/user db)))
