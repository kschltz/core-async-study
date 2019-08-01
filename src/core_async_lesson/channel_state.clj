(ns core-async-lesson.channel_state
  (:require [clojure.core.async :as a :refer :all]))

(def main-channel (a/chan))

(go-loop [state {:value 0}]
  (let [new-state {}]
    (as-> new-state ns
          (let [new-val (eval ((<! main-channel) (:value state)))]
            (println "new value: " new-val)
            (assoc ns :value new-val))
          (recur ns))))

(defn operate!
  "Sends a function to operate on current state,
  as of: (operate #(+10 %))"
  [fn]
  (put! main-channel fn))


















