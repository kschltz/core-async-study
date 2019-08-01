(ns core-async-lesson.pub-sub
  (:require [clojure.core.async :as a :refer [<! put!]]
            [clojure.pprint :as p]
            [clojure.spec.alpha :as s]
            [clojure.spec.gen.alpha :as g]
            [clojure.test.check :as tc]
            [clojure.test.check.properties :as prop]))


(s/def ::message-type #{::success ::fail})
(s/def ::message-body string?)
(s/def ::message (s/keys :req [::message-type ::message-body]))

(def message-channel (a/chan 2))

(def message-publisher (a/pub message-channel ::message-type))

(as-> (a/chan) sub-channel
      (a/sub message-publisher ::success sub-channel)
      (a/go-loop [message-log {}]
        (as-> message-log ml
              (assoc ml (System/currentTimeMillis)
                        (<! sub-channel))
              (do (p/pprint ml) ml)
              (recur ml))))

(defn send-msg [msg]
  (put! message-channel msg))

(def msg-gen (s/gen ::message))

(g/generate msg-gen)

(def send-prop (prop/for-all [msg msg-gen]
                             (send-msg msg)))

(tc/quick-check 1 send-prop)