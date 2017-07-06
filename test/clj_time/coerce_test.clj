(ns clj-time.coerce-test
  (:refer-clojure :exclude [extend second])
  (:require [clojure.test :refer :all]
            [clj-time [core :refer :all] [coerce :refer :all]])
  (:import java.util.Date java.sql.Timestamp
           [org.joda.time LocalDate LocalDateTime]))

(deftest test-from-date
  (let [dt (from-long 893462400000)
        d  (to-date dt)]
    (is (instance? Date d))
    (is (= dt (from-date d)))
    (is (nil? (from-date nil)))))

(deftest test-from-sql-date
  (let [dt (from-long 893462400000)
        d  (to-sql-date dt)]
    (is (instance? java.sql.Date d))
    (is (= dt (from-sql-date d)))
    (is (nil? (from-sql-date nil)))))

(deftest test-from-sql-time
  (let [dt (from-long 893462400000)
        d  (to-sql-time dt)]
    (is (instance? java.sql.Timestamp d))
    (is (= dt (from-sql-time d)))
    (is (nil? (from-sql-time nil)))))

(deftest test-from-long
  (is (= (date-time 1998 4 25) (from-long 893462400000))))

(deftest test-from-epoch
  (is (= (date-time 1998 4 25)
         (from-epoch 893462400))))

(deftest test-from-string
  (is (= (from-string "1998-04-25T00:00:00.000Z")
         (date-time 1998 4 25))))

(deftest test-from-year-month
  (is (= (to-date-time (year-month 1998 4))
         (date-time 1998 4))))

(deftest test-from-local-date
  (is (= (to-date-time (local-date 2013 03 20))
         (date-time 2013 03 20))))

(deftest test-from-local-date-time
  (is (= (to-date-time (local-date-time 2013 03 20 14 00 34 16))
         (date-time 2013 03 20 14 00 34 16))))

(deftest test-to-date
  (is (nil? (to-date nil)))
  (is (nil? (to-date "")))
  (is (nil? (to-date "x")))
  (is (= (Date. 893462400000) (to-date (date-time 1998 4 25))))
  (is (= (Date. 893462400000) (to-date (date-midnight 1998 4 25))))
  (is (= (Date. 893462400000) (to-date (Date. 893462400000))))
  (is (= (Date. 893462400000) (to-date (java.sql.Date. 893462400000))))
  (is (= (Date. (long 0)) (to-date 0)))
  (is (= (Date. 893462400000) (to-date 893462400000)))
  (is (= (Date. 893462400000) (to-date (Timestamp. 893462400000))))
  (is (= (Date. 893462400000) (to-date "1998-04-25T00:00:00.000Z"))))

(deftest test-to-sql-date
  (is (nil? (to-sql-date nil)))
  (is (nil? (to-sql-date "")))
  (is (nil? (to-sql-date "x")))
  (is (= (java.sql.Date. 893462400000) (to-sql-date (date-time 1998 4 25))))
  (is (= (java.sql.Date. 893462400000) (to-sql-date (date-midnight 1998 4 25))))
  (is (= (java.sql.Date. 893462400000) (to-sql-date (Date. 893462400000))))
  (is (= (java.sql.Date. 893462400000) (to-sql-date (java.sql.Date. 893462400000))))
  (is (= (java.sql.Date. (long 0)) (to-sql-date 0)))
  (is (= (java.sql.Date. 893462400000) (to-sql-date 893462400000)))
  (is (= (java.sql.Date. 893462400000) (to-sql-date (Timestamp. 893462400000))))
  (is (= (java.sql.Date. 893462400000) (to-sql-date "1998-04-25T00:00:00.000Z"))))

(deftest test-to-sql-time
  (is (nil? (to-sql-time nil)))
  (is (nil? (to-sql-time "")))
  (is (nil? (to-sql-time "x")))
  (is (= (java.sql.Timestamp. 893462400000) (to-sql-time (date-time 1998 4 25))))
  (is (= (java.sql.Timestamp. 893462400000) (to-sql-time (date-midnight 1998 4 25))))
  (is (= (java.sql.Timestamp. 893462400000) (to-sql-time (Date. 893462400000))))
  (is (= (java.sql.Timestamp. 893462400000) (to-sql-time (java.sql.Timestamp. 893462400000))))
  (is (= (java.sql.Timestamp. (long 0)) (to-sql-time 0)))
  (is (= (java.sql.Timestamp. 893462400000) (to-sql-time 893462400000)))
  (is (= (java.sql.Timestamp. 893462400000) (to-sql-time (Timestamp. 893462400000))))
  (is (= (java.sql.Timestamp. 893462400000) (to-sql-time "1998-04-25T00:00:00.000Z"))))

(deftest test-to-date-time
  (is (nil? (to-date-time nil)))
  (is (nil? (to-date-time "")))
  (is (nil? (to-date-time "x")))
  (is (= (date-time 1998 4 25) (to-date-time (date-time 1998 4 25))))
  (is (= (date-midnight 1998 4 25) (to-date-time (date-time 1998 4 25))))
  (is (= (date-time 2000 1 1 1) (plus (to-date-time (date-midnight 2000 1 1)) (hours 1))))
  (is (= (date-time 1998 4 25) (to-date-time (Date. 893462400000))))
  (is (= (date-time 1998 4 25) (to-date-time (java.sql.Date. 893462400000))))
  (is (= (date-time 1970 1 1) (to-date-time 0)))
  (is (= (date-time 1998 4 25) (to-date-time 893462400000)))
  (is (= (date-time 1998 4 25) (to-date-time (Timestamp. 893462400000))))
  (is (= (date-time 1998 4 25) (to-date-time "1998-04-25T00:00:00.000Z"))))

(deftest test-to-long
  (is (nil? (to-long nil)))
  (is (nil? (to-long "")))
  (is (nil? (to-long "x")))
  (is (= 893462400000 (to-long (date-time 1998 4 25))))
  (is (= 0 (to-long (date-midnight 1970))))
  (is (= (to-long (date-time 1993 3 15)) (to-long (date-midnight 1993 3 15))))
  (is (= 893462400000 (to-long (Date. 893462400000))))
  (is (= 893462400000 (to-long (java.sql.Date. 893462400000))))
  (is (= (long 0) (to-long 0)))
  (is (= 893462400000 (to-long 893462400000)))
  (is (= 893462400000 (to-long (Timestamp. 893462400000))))
  (is (= 893462400000 (to-long "1998-04-25T00:00:00.000Z"))))

(deftest test-to-epoch
  (is (nil? (to-epoch nil)))
  (is (nil? (to-epoch "")))
  (is (nil? (to-epoch "x")))
  (is (= 893462400 (to-epoch (date-time 1998 4 25))))
  (is (= 0 (to-epoch (date-midnight 1970))))
  (is (= (to-epoch (date-time 1993 3 15)) (to-epoch (date-midnight 1993 3 15))))
  (is (= 893462400 (to-epoch (Date. 893462400000))))
  (is (= 893462400 (to-epoch (java.sql.Date. 893462400000))))
  (is (= (long 0) (to-epoch 0)))
  (is (= 893462400 (to-epoch 893462400000)))
  (is (= 893462400 (to-epoch (Timestamp. 893462400000))))
  (is (= 893462400 (to-epoch "1998-04-25T00:00:00.000Z")))
  (is (= 893462400 (to-epoch "1998-04-25T00:00:00.500Z"))))

(deftest test-to-string
  (is (nil? (to-string nil)))
  (is (nil? (to-string "")))
  (is (nil? (to-string "x")))
  (is (= "1998-04-25T00:00:00.000Z" (to-string (date-time 1998 4 25))))
  (is (= "1998-04-25T00:00:00.000Z" (to-string (date-midnight 1998 4 25))))
  (is (= "1998-04-25T00:00:00.000Z" (to-string (Date. 893462400000))))
  (is (= "1998-04-25T00:00:00.000Z" (to-string (java.sql.Date. 893462400000))))
  (is (= "1970-01-01T00:00:00.000Z" (to-string 0)))
  (is (= "1998-04-25T00:00:00.000Z" (to-string 893462400000)))
  (is (= "1998-04-25T00:00:00.000Z" (to-string (Timestamp. 893462400000))))
  (is (= "1998-04-25T00:00:00.000Z" (to-string "1998-04-25T00:00:00.000Z"))))

(deftest test-to-timestamp
  (is (nil? (to-timestamp nil)))
  (is (nil? (to-timestamp "")))
  (is (nil? (to-timestamp "x")))
  (is (= (Timestamp. 893462400000) (to-timestamp (date-time 1998 4 25))))
  (is (= (Timestamp. 893462400000) (to-timestamp (date-midnight 1998 4 25))))
  (is (= (Timestamp. 893462400000) (to-timestamp (Date. 893462400000))))
  (is (= (Timestamp. 893462400000) (to-timestamp (java.sql.Date. 893462400000))))
  (is (= (Timestamp. (long 0)) (to-timestamp 0)))
  (is (= (Timestamp. 893462400000) (to-timestamp 893462400000)))
  (is (= (Timestamp. 893462400000) (to-timestamp (Timestamp. 893462400000))))
  (is (= (Timestamp. 893462400000) (to-timestamp "1998-04-25T00:00:00.000Z"))))

(deftest test-to-local-date
  (is (nil? (to-local-date nil)))
  (is (nil? (to-local-date "")))
  (is (nil? (to-local-date "x")))
  (is (= (LocalDate. 1998 4 25) (to-local-date (date-time 1998 4 25))))
  (is (= (LocalDate. 1998 4 25) (to-local-date (date-midnight 1998 4 25))))
  (is (= (LocalDate. 1998 4 25) (to-local-date (Date. 893462400000))))
  (is (= (LocalDate. 1998 4 25) (to-local-date (java.sql.Date. 893462400000))))
  (is (= (LocalDate. 1970 1 1) (to-local-date 0)))
  (is (= (LocalDate. 1998 4 25) (to-local-date 893462400000)))
  (is (= (LocalDate. 1998 4 25) (to-local-date (Timestamp. 893462400000))))
  (is (= (LocalDate. 1998 4 25) (to-local-date "1998-04-25T00:00:00.000Z"))))

(deftest test-to-local-date-time
  (is (nil? (to-local-date-time nil)))
  (is (nil? (to-local-date-time "")))
  (is (nil? (to-local-date-time "x")))
  (is (= (LocalDateTime. 1998 4 25 10 20) (to-local-date-time (date-time 1998 4 25 10 20))))
  (is (= (LocalDateTime. 1998 4 25 0 0) (to-local-date-time (date-midnight 1998 4 25))))
  (is (= (LocalDateTime. 1970 1 1 0 0) (to-local-date-time 0)))
  (is (= (LocalDateTime. 1998 4 25 0 0 55 0) (to-local-date-time 893462455000)))
  (is (= (LocalDateTime. 1998 4 25 10 20 30 400) (to-local-date-time "1998-04-25T10:20:30.400Z"))))

(deftest test-in-time-zone
  (testing "negative time zone offsets close to date boundary return previous day"
    (is (= (in-time-zone (date-time 2015 5 5 0 0)
                         (time-zone-for-offset 0 0))
           (local-date 2015 5 5)))

    (is (= (in-time-zone (date-time 2015 5 5 0 0)
                         (time-zone-for-offset -1 0))
           (local-date 2015 5 4)))

    (is (= (in-time-zone (date-time 2015 5 5 0 0)
                         (time-zone-for-offset 1 0))
           (local-date 2015 5 5))))

  (testing "positive time zone offsets close to date boundary return next day"
    (is (= (in-time-zone (date-time 2015 5 5 20 0)
                         (time-zone-for-offset 5 0))
           (local-date 2015 5 6)))

    (is (= (in-time-zone (date-time 2015 5 5 20 0)
                         (time-zone-for-offset 3 0))
           (local-date 2015 5 5)))))
