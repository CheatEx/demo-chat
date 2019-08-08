(ns demo-chat.room-test
  (:require [clojure.test :refer [deftest testing is]]
            [demo-chat.events :as events]
            [demo-chat.room :as room :refer :all]))

(defn store [initial]
  (let [history (atom initial)]
    (reify room/Store
      (room/save! [this message]
        (swap! history conj message))

      (room/load-history! [this]
        @history))))

(defn msg [val] {:text val})

(defn room-setup 
  ([recv-count] (room-setup [] recv-count))
  ([history recv-count] (into [(create-room (store history))]
                            (repeatedly recv-count #(atom [])))))

(defn receive-to [recv]
  (fn [e] (swap! recv conj e)))

(deftest room-test
  (testing "join and send"
    (let [[room recv1 recv2] (room-setup 2)]
      (do (join! room 1 (receive-to recv1))
          (join! room 2 (receive-to recv2))
          (send! room (msg 1)))
      (is (= [[::events/history []] [::events/received (msg 1)]] @recv1))
      (is (= [[::events/history []] [::events/received (msg 1)]] @recv2))))
  (testing "history rewind"
    (let [[room recv1 recv2] (room-setup 2)]
      (do (join! room 1 #(swap! recv1 conj %))
          (send! room (msg 1))
          (send! room (msg 2))
          (join! room 2 #(swap! recv2 conj %)))
      (is (= @recv2 [[::events/history [(msg 1) (msg 2)]]])))))

(deftest store-test
  (testing "history loaded"
    (let [history [(msg 1) (msg 2)]
          [room recv1 recv2] (room-setup history 2)]
      (do (join! room 1 (receive-to recv1))
          (send! room (msg 3))
          (join! room 2 #(swap! recv2 conj %)))
      (is (= [[::events/history history] [::events/received (msg 3)]] @recv1))
      (is (= [[::events/history (conj history (msg 3))]] @recv2)))))
