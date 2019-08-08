(ns demo-chat.room-test
  (:require [clojure.test :refer [deftest testing is]]
            [demo-chat.events :as events]
            [demo-chat.room :refer :all]))

(defn msg [val] {:text val})

(defn room-setup [recv-count]
  (into [(create-room)]
        (repeatedly recv-count #(atom []))))

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
