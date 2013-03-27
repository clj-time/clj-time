(ns clj-time.periodic-test
  (:use clojure.test
        [clj-time.core :only [date-time hours]]
        clj-time.periodic))


(deftest test-periodic-sequence
  (let [d0 (date-time 2012 3 3 20 0)
        d1 (date-time 2012 3 3 21 0)
        d2 (date-time 2012 3 3 22 0)
        d3 (date-time 2012 3 3 23 0)
        d4 (date-time 2012 3 4 0 0)
        d5 (date-time 2012 3 4 1 0)
        d6 (date-time 2012 3 4 2 0)
        uds (periodic-seq d0 (hours 1))]
    (are [a b] (= a b)
         d0 (first uds)
         d1 (second uds)
         d2 (nth uds 2)
         d3 (nth uds 3)
         d4 (nth uds 4)
         d5 (nth uds 5)
         d6 (nth uds 6))))
