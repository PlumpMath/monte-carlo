(ns monte-carlo.core
  (:gen-class)
  (:require [clojure.core.async
             :as a
             :refer [>! <! >!! <!! go chan buffer close! thread
                     alts! alts!! timeout]]))

(defn flip-coin []
  "Determines a coin flip. If a random number is even --> heads, odd --> tails"
  (let [n (rand-int 100)
        m (mod n 2)]
    (if(zero? m)
      :heads
      :tails
      )))

(defn winner-is [tests]
  "Read a seq of test flips. Return the winner"
  (let [n (count tests)
        headct (count (filter #(= % :heads) tests))
        tailct (- n headct)
        delta (- headct tailct)]
    (cond
      (pos? delta) :heads
      (neg? delta) :tails
      :else :tie
    )))

(defn run-sim [n]
   (take n (repeatedly flip-coin)))

(defn get-msg [channel]
  (go (<!! channel)))

(defn put-msg [msg channel]
  (go (>!! channel msg)))

(def echo-chan (chan 5))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (let [n 10
        mcs (run-sim n)]
    (println mcs)))
