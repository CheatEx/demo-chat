(ns demo-chat.db)

(def test-db
  {::user {::name "me"}
   ::history [{::text "Hello" ::from "0"}
              {::text "Hey" ::from "2"}
              {::text "Wanna do some clojure?" ::from "0"}
              {::text "OMG not again!!!1111" ::from "1"}]})

(def initial-db {::history []
                 ::connected false})

(defn make-message [user text]
  {::text text ::from user})
