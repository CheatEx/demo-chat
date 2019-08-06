(ns demo-chat.core-test
  (:require [cljs.test :refer-macros [deftest testing is]]
            [demo-chat.core :as core]))

(deftest fake-test
  (testing "fake description"
    (is (= 1 2))))
