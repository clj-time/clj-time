(ns clj-time.types-test
  (:require [clojure.test :refer :all]
            [clj-time.types :as types]
            [clj-time.core :refer :all]))

(deftest test-predicates
  (is (types/date-time? (date-time 2018 8 22 7 12 58)))
  (is (not (types/local-date-time? (date-time 2018 8 22 7 12 58))))
  (is (not (types/local-date? (date-time 2018 8 22 7 12 58))))

  (is (not (types/date-time? (local-date-time 2018 8 22 7 12 58))))
  (is (types/local-date-time? (local-date-time 2018 8 22 7 12 58)))
  (is (not (types/local-date? (local-date-time 2018 8 22 7 12 58))))

  (is (not (types/date-time? (local-date 2018 8 22))))
  (is (not (types/local-date-time? (local-date 2018 8 22))))
  (is (types/local-date? (local-date 2018 8 22))))


