(ns demo-chat.room-test
  (:require [clojure.test :refer [deftest testing is]]
            [demo-chat.room :refer :all]))

(deftest fake-test
  (testing "fake description"
    (is (= 1 2))))
