(ns demo-chat.runner
    (:require [doo.runner :refer-macros [doo-tests]]
              [demo-chat.core-test]))

(doo-tests 'demo-chat.core-test)
