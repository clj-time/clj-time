(ns clj-time.format-test
  (:refer-clojure :exclude [extend second])
  (:use clojure.test
        (clj-time core format))
  (:import [org.joda.time DateTimeZone]
           java.util.Locale))

(deftest test-formatter
  (let [fmt (formatter "yyyyMMdd")]
    (is (= (date-time 2010 3 11)
           (parse fmt "20100311")))))

(deftest test-formatter-local
  (let [fmt (formatter-local "yyyyMMdd")]
    (is (= (local-date-time 2010 3 11)
           (parse-local fmt "20100311")))))

(deftest test-parse
  (is (= (date-time 2010 10 11)
         (parse "2010-10-11T00:00:00")))
  (let [fmt (formatters :date)]
    (is (= (date-time 2010 3 11)
           (parse fmt "2010-03-11"))))
  (let [fmt (formatters :basic-date-time)]
    (is (= (date-time 2010 3 11 17 49 20 881)
           (parse fmt "20100311T174920.881Z")))))

(deftest test-unparse
  (let [fmt (formatters :date)]
    (is (= "2010-03-11"
           (unparse fmt (date-time 2010 3 11)))))
  (let [fmt (formatters :basic-date-time)]
    (is (= "20100311T174920.881Z"
           (unparse fmt (date-time 2010 3 11 17 49 20 881))))
    (is (= "20100311T124920.881-0500"
           (unparse (formatter "yyyyMMdd'T'HHmmss.SSSZ"
                               (DateTimeZone/forOffsetHours -5))
                    (date-time 2010 3 11 17 49 20 881))))))

(deftest test-local-parse
  (is (= (local-date-time 2010 10 11)
         (parse-local "2010-10-11T00:00:00")))
  (let [fmt (formatters :date)]
    (is (= (local-date-time 2010 3 11)
           (parse-local fmt "2010-03-11"))))
  (let [fmt (formatters :basic-date-time)]
    (is (= (local-date-time 2010 3 11 17 49 20 881)
           (parse-local fmt "20100311T174920.881Z")))))

(deftest test-local-unparse
  (let [fmt (formatters :date)]
    (is (= "2010-03-11"
           (unparse-local fmt (local-date-time 2010 3 11)))))
  (let [fmt (formatters :basic-date-time)]
    (is (= "20100311T174920.881"
           (unparse-local fmt (local-date-time 2010 3 11 17 49 20 881))))
    (is (= "20100311T174920.881"
           (unparse-local (formatter-local "yyyyMMdd'T'HHmmss.SSS")
                    (local-date-time 2010 3 11 17 49 20 881))))))

(deftest test-local-date-parse
  (is (= (local-date 2010 10 11)
         (parse-local-date "2010-10-11T00:00:00")))
  (let [fmt (formatters :date)]
    (is (= (local-date 2010 3 11)
           (parse-local-date fmt "2010-03-11"))))
  (let [fmt (formatters :basic-date-time)]
    (is (= (local-date 2010 3 11)
           (parse-local-date fmt "20100311T000000.000Z")))))

(deftest test-local-date-unparse
  (let [fmt (formatters :date)]
    (is (= "2010-03-11"
           (unparse-local-date fmt (local-date 2010 3 11)))))
  (let [fmt (formatters :basic-date-time)]
    (is (= "20100311T000000.000"
           (unparse-local-date fmt (local-date-time 2010 3 11 00 00 00 000))))))

(deftest test-formatter-modifiers
  (let [fmt (formatter "YYYY-MM-dd hh:mm z" (time-zone-for-id "America/Chicago"))]
    (is (= "2010-03-11 11:49 CST"
           (unparse fmt (date-time 2010 3 11 17 49 20 881)))))
  (let [fmt (with-zone (formatters :basic-date-time) (time-zone-for-id "America/Chicago"))]
    (is (= "20100311T114920.881-0600"
           (unparse fmt (date-time 2010 3 11 17 49 20 881)))))
  (let [fmt (with-locale (formatters :rfc822) Locale/ITALY)]
    (is (= "gio, 11 mar 2010 17:49:20 +0000"
           (unparse fmt (date-time 2010 3 11 17 49 20 881)))))
  (let [fmt (with-pivot-year (formatter "YY") 2050)]
    (is (= (date-time 2075 1 1)
           (parse fmt "75")))))

(deftest test-multi-parser
  (let [fmt (formatter utc "YYYY-MM-dd HH:mm" "YYYY/MM/dd@HH:mm" "YYYYMMddHHmm")]
    (is (= "2012-02-01 22:15"
           (unparse fmt (parse fmt "2012-02-01 22:15"))))
    (is (= "2012-02-01 22:15"
           (unparse fmt (parse fmt "2012/02/01@22:15"))))
    (is (= "2012-02-01 22:15"
           (unparse fmt (parse fmt "201202012215"))))
    (is (= (date-time 2012 2 1 22 15)
           (parse fmt "201202012215")))
    (is (= "2012-02-01 22:15"
           (unparse fmt (date-time 2012 2 1 22 15))))))

(deftest test-mysql-format
  (are [expectation mysql] (= (parse mysql) expectation)
       (date-time 2013 1 1 0 0 0) "2013-01-01 00:00:00"
       (date-time 1991 1 13 11 30 45) "1991-1-13 11:30:45"
       (date-time 2013 8 3 12 11 13) "2013-08-03 12:11:13"))

(deftest test-instant->map-from-interval
  (let [it (interval (date-time 1986 9 2 0 0 2)  (date-time 1986 11 30 2 5 12))]
    (is (= (instant->map it)
      {:years 0
       :months 2
       :days 28
       :hours 2
       :minutes 5
       :seconds 10}))))

(deftest test-instant->map-from-date-time
  (let [dt (date-time 1986 9 2 0 0 2)]
    (is (= (instant->map dt)
      {:years 1986
       :months 9
       :days 2
       :hours 0
       :minutes 0
       :seconds 2}))))
