(ns core-async-lesson.tic-tac-toe
  (:require [clojure.core.async :as a :refer [go go-loop <! >!]]))


(def initial-board [[nil nil nil]
                    [nil nil nil]
                    [nil nil nil]])

(defn transpose [matrix]
  (apply map vector matrix))

(defn horizontal-win? [board]
  (->> board
       (map (fn [row] (reduce conj #{} row)))
       (some #(and (= (count %)1) (not (nil? (first %)))))
       ))

(defn straight-win? [board]
  (or (horizontal-win? board)
      (horizontal-win? (transpose board))))

(defn traverse-win? [board]
  (loop [x 0
         y 0
         values #{}]
    (if (< x (count board))
      (recur (inc x) (inc y) (conj values (get-in board [x y])))
      (and (= (count values) 1) (not (nil? (first values)))))))

(defn diagonal-win? [board]
  (or (traverse-win? board)
      (traverse-win? (reverse board))))


(defn win? [board]
  (or (diagonal-win? board)
      (straight-win? board)))


(defn valid-move? [x y board]
  (nil? (get-in board [x y])))


(defn play? [x y board marker]
  (if (valid-move? x y board)
    (assoc-in board [x y] marker)
    false))

(defn get-mark [round]
  (if (even? round)
    "X"
    "O"))

(defn get-input []
  (as->(read-line) rl
     {:x (Character/digit (first rl) 10)
      :y (Character/digit (second rl) 10)}))



(defn wait-move! [board round]
  (loop [r false]
    (if r
      r
      (let [input (get-input)]
        (recur (play? (:x input) (:y input) board (get-mark round)))))))

(defn start-game []
  (loop [board initial-board
         round 0]
    (println board)
    (let [new-board (wait-move! board round)]

      (if (win? new-board)
        (do (println "GAME-OVER" (get-mark round) "WON")
            new-board)
        (recur new-board (inc round))))))








