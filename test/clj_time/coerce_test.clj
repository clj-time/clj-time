(ns clj-time.coerce-test
  (:refer-clojure :exclude [extend])
  (:use clojure.test)
  (:use (clj-time core coerce))
  (:import java.util.Date java.sql.Timestamp))

(deftest test-from-date
  (let [dt (from-long 893462400000)
        d  (to-date dt)]
    (is (instance? Date d))
    (is (= dt (from-date d)))))

(deftest test-from-sql-date
  (let [dt (from-long 893462400000)
        d  (to-sql-date dt)]
    (is (instance? java.sql.Date d))
    (is (= dt (from-sql-date d)))))

(deftest test-from-long
  (is (= (date-time 1998 4 25) (from-long 893462400000))))

(deftest test-from-string
  (is (= (from-string "1998-04-25T00:00:00.000Z")
         (date-time 1998 4 25))))

(deftest test-to-date
  (is (nil? (to-date nil)))
  (is (nil? (to-date "")))
  (is (nil? (to-date "x")))
  (is (= (Date. 893462400000) (to-date (date-time 1998 4 25))))
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
  (is (= (java.sql.Date. 893462400000) (to-sql-date (Date. 893462400000))))
  (is (= (java.sql.Date. 893462400000) (to-sql-date (java.sql.Date. 893462400000))))
  (is (= (java.sql.Date. (long 0)) (to-date 0)))
  (is (= (java.sql.Date. 893462400000) (to-sql-date 893462400000)))
  (is (= (java.sql.Date. 893462400000) (to-sql-date (Timestamp. 893462400000))))
  (is (= (java.sql.Date. 893462400000) (to-sql-date "1998-04-25T00:00:00.000Z"))))

(deftest test-to-date-time
  (is (nil? (to-date-time nil)))
  (is (nil? (to-date-time "")))
  (is (nil? (to-date-time "x")))
  (is (= (date-time 1998 4 25) (to-date-time (date-time 1998 4 25))))
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
  (is (= 893462400000 (to-long (Date. 893462400000))))
  (is (= 893462400000 (to-long (java.sql.Date. 893462400000))))
  (is (= (long 0) (to-long 0)))
  (is (= 893462400000 (to-long 893462400000)))
  (is (= 893462400000 (to-long (Timestamp. 893462400000))))
  (is (= 893462400000 (to-long "1998-04-25T00:00:00.000Z"))))

(deftest test-to-string
  (is (nil? (to-string nil)))
  (is (nil? (to-string "")))
  (is (nil? (to-string "x")))
  (is (= "1998-04-25T00:00:00.000Z" (to-string (date-time 1998 4 25))))
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
  (is (= (Timestamp. 893462400000) (to-timestamp (Date. 893462400000))))
  (is (= (Timestamp. 893462400000) (to-timestamp (java.sql.Date. 893462400000))))
  (is (= (Timestamp. (long 0)) (to-timestamp 0)))
  (is (= (Timestamp. 893462400000) (to-timestamp 893462400000)))
  (is (= (Timestamp. 893462400000) (to-timestamp (Timestamp. 893462400000))))
  (is (= (Timestamp. 893462400000) (to-timestamp "1998-04-25T00:00:00.000Z"))))
