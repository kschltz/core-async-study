(ns core-async-lesson.core-test
  (:require [clojure.test :refer :all]
            [core-async-lesson.channel_state :refer :all]
            [core-async-lesson.pub-sub :as ps]
            [clojure.test.check :as tc]))


(deftest coverage-check
  (tc/quick-check 100 (ps/send-prop))
  (is true))

