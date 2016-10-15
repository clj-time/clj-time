(ns clj-time.core-test
  (:refer-clojure :exclude [extend second])
  (:require [clojure.test :refer :all]
            [clj-time.core :refer :all])
  (:import java.util.Date
           org.joda.time.DateTime))

(deftest test-now
  (is (= (date-time 2010 1 1)
         (do-at (date-time 2010 1 1)
                (now)))))

(deftest test-today-at-midnight
  (is (= (date-midnight 2010 1 1)
         (do-at (date-midnight 2010 1 1)
                (today-at-midnight))))
  (let [midnight (do-at (date-time 2010 1 15 2)
                        (today-at-midnight (time-zone-for-offset -3)))]
    (is (= 14 (day midnight)))))


(deftest test-with-time-at-start-of-day
  (let [d (date-time 2010 1 1)]
    (is (= d (with-time-at-start-of-day d))))
  (let [start-of-day (-> (date-time 2010 1 15 2)
                         (to-time-zone (time-zone-for-offset -3))
                         (with-time-at-start-of-day))]
    (is (= 14 (day start-of-day))))
  (let [d (-> (date-time 2015 3 27 23)
              (from-time-zone (time-zone-for-id "Asia/Gaza")))
        d-plus-1h (-> d
                      (plus (hours 1)))]
    (is (= (hour d-plus-1h) (hour (with-time-at-start-of-day d-plus-1h))))
    (is (not (zero? (hour d-plus-1h))))))

(deftest test-epoch
  (let [e (epoch)]
    (is (= 1970 (year e)))
    (is (= 0 (second e)))))

(deftest test-date-time-and-accessors
  (let [d (date-time 1986)]
    (is (= 1986 (year   d)))
    (is (= 1    (month  d)))
    (is (= 1    (day    d)))
    (is (= 0    (hour   d)))
    (is (= 0    (minute d)))
    (is (= 0    (second d)))
    (is (= 0    (milli  d))))
  (let [d (date-time 1986 10 14 4 3 2 1)]
    (is (= 1986 (year   d)))
    (is (= 10   (month  d)))
    (is (= 14   (day    d)))
    (is (= 4    (hour   d)))
    (is (= 3    (minute d)))
    (is (= 2    (second d)))
    (is (= 1    (milli  d)))))

(deftest test-date-midnight-and-accessors
  (let [d (date-midnight 1986)]
    (is (= 1986 (year   d)))
    (is (= 1    (month  d)))
    (is (= 1    (day    d)))
    (is (= 0    (hour   d)))
    (is (= 0    (minute d)))
    (is (= 0    (second    d)))
    (is (= 0    (milli  d))))
  (let [d (date-midnight 1986 10 14)]
    (is (= 1986 (year   d)))
    (is (= 10   (month  d)))
    (is (= 14   (day    d)))
    (is (= 0    (hour   d)))
    (is (= 0    (minute d)))
    (is (= 0    (second d)))
    (is (= 0    (milli  d)))))

(deftest test-min-and-max
  (let [ds [(date-time 1986 10 14 4 3 2 1)
            (date-time 1986 12 14 4 3 2 1)
            (date-time 1987 10 14 11 3 2 5)
            (date-time 2014 10 14 4 3 2 1)
            (date-time 1986 10 14 4 3 2 2)
            (date-time 1985 10 14 4 3 2 1)
            (date-time 1985 10 14 4 3 2 2)]]
    (is (= (date-time 1985 10 14 4 3 2 1)
           (apply min-date ds)))
    (is (= (date-time 2014 10 14 4 3 2 1)
           (apply max-date ds))))
  (is (= (date-time 1986 10 14 4 3 2 1)
         (apply min-date [(date-time 1986 10 14 4 3 2 1)])))
  (is (= (date-time 1986 10 14 4 3 2 1)
         (apply max-date [(date-time 1986 10 14 4 3 2 1)]))))

(deftest test-local-date-time-and-accessors
  (let [d (local-date-time 1986)]
    (is (= 1986 (year   d)))
    (is (= 1    (month  d)))
    (is (= 1    (day    d)))
    (is (= 0    (hour   d)))
    (is (= 0    (minute d)))
    (is (= 0    (second d)))
    (is (= 0    (milli  d))))
  (let [d (local-date-time 1986 10 14 4 3 2 1)]
    (is (= 1986 (year   d)))
    (is (= 10   (month  d)))
    (is (= 14   (day    d)))
    (is (= 4    (hour   d)))
    (is (= 3    (minute d)))
    (is (= 2    (second d)))
    (is (= 1    (milli  d)))))

(deftest test-year-month-and-accessors
  (let [d (year-month 1986)]
    (is (= 1986 (year   d)))
    (is (= 1    (month  d))))
  (let [d (date-time 1986 10)]
    (is (= 1986 (year   d)))
    (is (= 10   (month  d)))))

(deftest test-local-date-and-accessors
  (let [d (local-date 2013 3 19)]
    (is (= 2013 (year   d)))
    (is (= 3    (month  d)))
    (is (= 19   (day    d)))
    (is (= 2    (day-of-week d)))))

(deftest test-local-time-and-accessors
  (let [t (local-time 12 11 10 9)]
    (is (= 12 (hour t)))
    (is (= 11 (minute t)))
    (is (= 10 (second t)))
    (is (= 9 (milli t)))
    ))

(deftest test-today
  (is (= (local-date 2013 4 20) (do-at (from-time-zone (date-time 2013 4 20) (default-time-zone))
                                  (today)))))

(deftest test-day-of-week
  (let [d (date-time 2010 4 24)]
    (is (= 6 (day-of-week d))))
  (let [d (date-time 1918 11 11)]
    (is (= 1 (day-of-week d)))))

(deftest test-day-of-week-local
  (let [d (local-date-time 2010 4 24)]
    (is (= 6 (day-of-week d))))
  (let [d (date-time 1918 11 11)]
    (is (= 1 (day-of-week d)))))

(deftest test-time-zone-for-offset
  (is (= utc (time-zone-for-offset 0)))
  (is (= (time-zone-for-offset 10 0) (time-zone-for-offset 10))))

(deftest test-time-zone-for-id
  (is (= utc (time-zone-for-id "UTC"))))

(deftest test-available-ids
  (is (some #{"UTC"} (available-ids))))

(deftest test-to-time-zone
  (let [tz  (time-zone-for-offset 2)
        dt1 (date-time 1986 10 14 6)
        dt2 (to-time-zone dt1 tz)]
    (is (= 8 (hour dt2)))
    (is (= (.getMillis dt1) (.getMillis dt2)))))

(deftest test-from-time-zone
  (let [tz  (time-zone-for-offset 2)
        dt1 (date-time 1986 10 14 6)
        dt2 (from-time-zone dt1 tz)]
    (is (= 6 (hour dt2)))
    (is (> (.getMillis dt1) (.getMillis dt2)))))

(deftest test-equal?
  (is (equal? (date-time 2013 01 01 00)
              (from-time-zone (date-time 2013 01 01 01)
                              (time-zone-for-offset 1))))
  (is (equal? (date-time 1987) (date-time 1987)))
  (is (not (equal? (date-time 1986) (date-time 1987))))
  (is (not (equal? (date-time 1987) (date-time 1986)))))

(deftest test-after?
  (is (after? (date-time 1987) (date-time 1986)))
  (is (not (after? (date-time 1986) (date-time 1987))))
  (is (not (after? (date-time 1986) (date-time 1986)))))

(deftest test-before?
  (is (before? (date-time 1986) (date-time 1987)))
  (is (not (before? (date-time 1987) (date-time 1986))))
  (is (not (before? (date-time 1986) (date-time 1986)))))

(deftest test-periods
  (is (= (date-time 1986 10 14 4 3 2 1)
         (plus (date-time 1984)
           (years 2)
           (months 9)
           (days 13)
           (hours 4)
           (minutes 3)
           (seconds 2)
           (millis 1))))
  (is (= (date-time 1986 1 8)
         (plus (date-time 1986 1 1) (weeks 1)))))

(deftest test-plus
  (is (= (date-time 1986 10 14 6)
         (plus (date-time 1986 10 14 4) (hours 2))))
  (is (= (date-time 1986 10 14 6 5)
         (plus (date-time 1986 10 14 4 2) (hours 2) (minutes 3)))))

(deftest test-minus
  (is (= (date-time 1986 10 14 4)
         (minus (date-time 1986 10 14 6) (hours 2))))
  (is (= (date-time 1986 10 14 4 2)
         (minus (date-time 1986 10 14 6 5) (hours 2) (minutes 3)))))

(deftest test-after?-local
  (is (after? (local-date-time 1987) (local-date-time 1986)))
  (is (not (after? (local-date-time 1986) (local-date-time 1987))))
  (is (not (after? (local-date-time 1986) (local-date-time 1986)))))

(deftest test-before?-local
  (is (before? (local-date-time 1986) (local-date-time 1987)))
  (is (not (before? (local-date-time 1987) (local-date-time 1986))))
  (is (not (before? (local-date-time 1986) (local-date-time 1986)))))

(deftest test-periods-local
  (is (= (local-date-time 1986 10 14 4 3 2 1)
         (plus (local-date-time 1984)
           (years 2)
           (months 9)
           (days 13)
           (hours 4)
           (minutes 3)
           (seconds 2)
           (millis 1))))
  (is (= (local-date-time 1986 1 8)
         (plus (local-date-time 1986 1 1) (weeks 1)))))

(deftest test-plus-local
  (is (= (local-date-time 1986 10 14 6)
         (plus (local-date-time 1986 10 14 4) (hours 2))))
  (is (= (local-date-time 1986 10 14 6 5)
         (plus (local-date-time 1986 10 14 4 2) (hours 2) (minutes 3)))))

(deftest test-minus-local
  (is (= (local-date-time 1986 10 14 4)
         (minus (local-date-time 1986 10 14 6) (hours 2))))
  (is (= (local-date-time 1986 10 14 4 2)
         (minus (local-date-time 1986 10 14 6 5) (hours 2) (minutes 3)))))

(defmacro when-available
  [sym & body]
  (try
    (and (resolve sym)
         (list* 'do body))
    (catch ClassNotFoundException _#)))

(defmacro when-not-available
  [sym & body]
  (when-not
    (try
      (resolve sym)
      (catch ClassNotFoundException _#
        nil))
    `(do ~@body)))

(deftest test-ago
  (when-available
    with-redefs
    (with-redefs [now #(date-time 2011 4 16 10 9 00)]
      (is (= (-> 10 years ago)
             (date-time 2001 4 16 10 9 00)))))
  (when-not-available
    with-redefs
    (binding [now #(date-time 2011 4 16 10 9 00)]
      (is (= (-> 10 years ago)
             (date-time 2001 4 16 10 9 00))))))

(deftest test-yesterday
  (is (= (date-time 2013 12 31)
         (do-at (date-time 2014 1 1)
                (yesterday)))))

(deftest test-from-now
  (when-available
    with-redefs
    (with-redefs [now #(date-time 2011 4 16 10 9 00)]
      (is (= (-> 30 minutes from-now)
             (date-time 2011 4 16 10 39 00)))))
  (when-not-available
    with-redefs
    (binding [now #(date-time 2011 4 16 10 9 00)]
      (is (= (-> 30 minutes from-now)
             (date-time 2011 4 16 10 39 00))))))

(deftest test-earliest
    (let [d1 (date-time 1990 1 1 23 1 1)
          d2  (date-time 2000 1 1 23 1 1)
          d3  (date-time 2010 1 1 23 1 1)
          d4  (date-time 2020 1 1 23 1 1)]
      (is (= d1 (earliest d1 d2)))
      (is (= d2 (earliest d2 d3)))
      (is (= d1 (earliest d1 d3)))
      (is (= d1 (earliest [d1 d2 d3 d4])))
      (is (= d1 (earliest [d4 d2 d3 d1])))
      (is (= d2 (earliest [d4 d3 d2])))
      (is (= d4 (earliest [d4])))
      (is (nil? (earliest [d1 d2 nil])))
      (is (nil? (earliest d2 nil)))))

(deftest test-latest
    (let [d1 (date-time 1990 1 1 23 1 1)
          d2  (date-time 2000 1 1 23 1 1)
          d3  (date-time 2010 1 1 23 1 1)
          d4  (date-time 2020 1 1 23 1 1)]
      (is (= d2 (latest d1 d2)))
      (is (= d3 (latest d2 d3)))
      (is (= d3 (latest d1 d3)))
      (is (= d4 (latest [d1 d2 d3 d4])))
      (is (= d4 (latest [d4 d2 d3 d1])))
      (is (= d3 (latest [d2 d3 d1])))
      (is (= d1 (latest [d1])))
      (is (= d2 (latest [d1 d2 nil])))
      (is (= d2 (latest d2 nil)))))

(deftest test-start-end
  (let [s (date-time 1986 10 14 12 5 4)
        e (date-time 1986 11 3  22 2 6)
        p (interval s e)]
    (is (= s (start p)))
    (is (= e (end p)))))

(deftest test-extend
  (is (= (interval (date-time 1986) (date-time 1988))
         (extend (interval (date-time 1986) (date-time 1987)) (years 1)))))

(deftest test-interval-in
  (let [p (interval (date-time 1986 10 14 12 5 4) (date-time 1986 11 3  22 2 6))]
    (is (= 0       (in-years p)))
    (is (= 0       (in-months p)))
    (is (= 2       (in-weeks p)))
    (is (= 20      (in-days p)))
    (is (= 489     (in-hours p)))
    (is (= 29397   (in-minutes p)))
    (is (= 1763822 (in-seconds p)))
    (is (= 1763822000 (in-millis p)))))

(deftest test-interval-in-bigger
  (let [p (interval (date-time 1986 10 14 12 5 4) (date-time 1987 11 3  22 2 6))]
    (is (= 1       (in-years p)))
    (is (= 12      (in-months p)))
    (is (= 55      (in-weeks p)))
    (is (= 385     (in-days p)))
    (is (= 9249    (in-hours p)))))

(deftest test-period-in-millis
  (is (= 30000      (-> 30 seconds in-millis)))
  (is (= 240000     (-> 4 minutes in-millis)))
  (is (= 43200000   (-> 12 hours in-millis)))
  (is (= 777600000  (-> 9 days in-millis)))
  (is (= 1814400000 (-> 3 weeks in-millis)))
  (is (thrown? UnsupportedOperationException (-> 2 months in-millis)))
  (is (thrown? UnsupportedOperationException (-> 2 years in-millis))))

(deftest test-period-in-seconds
  (is (= 30      (-> 30 seconds in-seconds)))
  (is (= 240     (-> 4 minutes in-seconds)))
  (is (= 43200   (-> 12 hours in-seconds)))
  (is (= 777600  (-> 9 days in-seconds)))
  (is (= 1814400 (-> 3 weeks in-seconds)))
  (is (thrown? UnsupportedOperationException (-> 2 months in-seconds)))
  (is (thrown? UnsupportedOperationException (-> 2 years in-seconds))))

(deftest test-period-in-minutes
  (is (= 0     (-> 30 seconds in-minutes)))
  (is (= 4     (-> 4 minutes in-minutes)))
  (is (= 720   (-> 12 hours in-minutes)))
  (is (= 12960 (-> 9 days in-minutes)))
  (is (= 30240 (-> 3 weeks in-minutes)))
  (is (thrown? UnsupportedOperationException (-> 2 months in-minutes)))
  (is (thrown? UnsupportedOperationException (-> 2 years in-minutes))))
 
(deftest test-period-in-hours
  (is (= 0   (-> 30 seconds in-hours)))
  (is (= 0   (-> 4 minutes in-hours)))
  (is (= 12  (-> 12 hours in-hours)))
  (is (= 216 (-> 9 days in-hours)))
  (is (= 504 (-> 3 weeks in-hours)))
  (is (thrown? UnsupportedOperationException (-> 2 months in-hours)))
  (is (thrown? UnsupportedOperationException (-> 2 years in-hours))))

(deftest test-period-in-days
  (is (= 0  (-> 30 seconds in-days)))
  (is (= 0  (-> 4 minutes in-days)))
  (is (= 0  (-> 12 hours in-days)))
  (is (= 9  (-> 9 days in-days)))
  (is (= 21 (-> 3 weeks in-days)))
  (is (thrown? UnsupportedOperationException (-> 2 months in-days)))
  (is (thrown? UnsupportedOperationException (-> 2 years in-days))))

(deftest test-period-in-weeks
  (is (= 0  (-> 30 seconds in-weeks)))
  (is (= 0  (-> 4 minutes in-weeks)))
  (is (= 0  (-> 12 hours in-weeks)))
  (is (= 1  (-> 9 days in-weeks)))
  (is (= 3  (-> 3 weeks in-weeks)))
  (is (thrown? UnsupportedOperationException (-> 2 months in-weeks)))
  (is (thrown? UnsupportedOperationException (-> 2 years in-weeks))))

(deftest test-period-in-months
  (is (thrown? UnsupportedOperationException (-> 2 seconds in-months)))
  (is (thrown? UnsupportedOperationException (-> 2 minutes in-months)))
  (is (thrown? UnsupportedOperationException (-> 2 hours in-months)))
  (is (thrown? UnsupportedOperationException (-> 2 days in-months)))
  (is (thrown? UnsupportedOperationException (-> 2 weeks in-months)))
  (is (= 7  (-> 7 months in-months)))
  (is (= 36 (-> 3 years in-months))))

(deftest test-period-in-years
  (is (thrown? UnsupportedOperationException (-> 2 seconds in-years)))
  (is (thrown? UnsupportedOperationException (-> 2 minutes in-years)))
  (is (thrown? UnsupportedOperationException (-> 2 hours in-years)))
  (is (thrown? UnsupportedOperationException (-> 2 days in-years)))
  (is (thrown? UnsupportedOperationException (-> 2 weeks in-years)))
  (is (= 1 (-> 14 months in-years)))
  (is (= 3 (-> 3 years in-years))))

(deftest test-within?
  (let [d1 (date-time 1985)
        d2 (date-time 1986)
        d3 (date-time 1987)
        d4 (date-time 2013 01 01 00)
        d5 (date-time 2013 01 01 01)
        ld1 (local-date 2013 1 1)
        ld2 (local-date 2013 2 28)
        ld3 (local-date 2013 10 5)
        dtz1 (from-time-zone (date-time 2013 01 01 02)
                             (time-zone-for-offset 1))]
    (is (within? (interval d1 d3) d2))
    (is (not (within? (interval d1 d2) d3)))
    (is (not (within? (interval d1 d2) d2)))
    (is (not (within? (interval d2 d3) d1)))
    (is (within? d4 d5 dtz1))
    (is (within? ld1 ld3 ld2))
    (is (not (within? ld1 ld2 ld3)))
    (is (not (within? ld3 ld2 ld1)))
    (is (not (within? ld2 ld3 ld1)))))

(deftest test-time-after?
  (let [t1 (local-time 11 12 13)
        t2 (local-time 12 13 14)
        t3 (local-time 13 14 15)]
    (is (after? t2 t1))
    (is (after? t3 t2))
    (is (after? t3 t1))
    (is (not (after? t2 t3)))
    (is (not (after? t1 t2)))
    (is (not (after? t1 t3)))
    ))

(deftest test-time-before?
  (let [t1 (local-time 11 12 13)
        t2 (local-time 12 13 14)
        t3 (local-time 13 14 15)]
    (is (before? t1 t2))
    (is (before? t2 t3))
    (is (before? t1 t3))
    (is (not (before? t3 t2)))
    (is (not (before? t2 t1)))
    (is (not (before? t3 t1)))
    ))

(deftest test-overlaps?
  (let [d1 (date-time 1985)
        d2 (date-time 1986)
        d3 (date-time 1987)
        d4 (date-time 1988)
        ld1 (local-date 2013 1 1)
        ld2 (local-date 2013 2 5)
        ld3 (local-date 2013 2 28)
        ld4 (local-date 2014 1 1)
        ld5 (local-date 2014 5 6)]
    (is (overlaps? (interval d1 d3) (interval d2 d4)))
    (is (overlaps? (interval d1 d3) (interval d2 d3)))
    (is (not (overlaps? (interval d1 d2) (interval d2 d3))))
    (is (not (overlaps? (interval d1 d2) (interval d3 d4))))
    (is (overlaps? ld1 ld3 ld2 ld4))
    (is (overlaps? ld2 ld4 ld3 ld5))
    (is (overlaps? ld1 ld5 ld1 ld5))
    (is (overlaps? ld1 ld5 ld2 ld4))
    (is (overlaps? ld2 ld4 ld1 ld5))
    (is (overlaps? ld1 ld2 ld2 ld3))
    (is (overlaps? ld2 ld3 ld1 ld2))
    (is (not (overlaps? ld1 ld2 ld3 ld4)))
    (is (not (overlaps? ld1 ld3 ld4 ld5)))))

(deftest test-overlap
  (let [d1 (date-time 1985)
        d2 (date-time 1986)
        d3 (date-time 1987)
        d4 (date-time 1988)
        n (now)
        n1 (minus n (minutes 5))
        n2 (plus n (minutes 5))]
    (is (nil? (overlap (interval d1 d2) nil)))
    (is (let [t (overlap (interval n1 n2) nil)]
          ;; nil is a zero length duration at 'now'
          (and
            (< (in-millis (interval n (start t))) 1000)
            (= 0 (in-millis t)))))
    (is (= (interval d2 d2) (overlap (interval d1 d3) (interval d2 d2))))
    (is (nil? (overlap (interval d1 d1) (interval d1 d1)))) ;; The intervals abut
    (is (nil? (overlap (interval d1 d2) (interval d2 d3)))) ;; The intervals abut
    (is (= (interval d2 d3) (overlap (interval d1 d3) (interval d2 d4))))
    (is (= (interval d2 d3) (overlap (interval d1 d3) (interval d2 d3))))
    (is (nil? (overlap (interval d1 d2) (interval d2 d3))))
    (is (nil? (overlap (interval d1 d2) (interval d3 d4))))))

(deftest test-abuts?
  (let [d1 (date-time 1985)
        d2 (date-time 1986)
        d3 (date-time 1987)
        d4 (date-time 1988)]
    (is (abuts? (interval d1 d2) (interval d2 d3)))
    (is (not (abuts? (interval d1 d2) (interval d3 d4))))
    (is (not (abuts? (interval d1 d3) (interval d2 d3))))
    (is (abuts? (interval d2 d3) (interval d1 d2)))))

(deftest test-years?
  (is (years? (years 2))))

(deftest test-months?
  (is (months? (months 2))))

(deftest test-weeks?
  (is (weeks? (weeks 2))))

(deftest test-days?
  (is (days? (days 2))))

(deftest test-hours?
  (is (hours? (hours 2))))

(deftest test-minutes?
  (is (minutes? (minutes 2))))

(deftest test-secs?
  (is (seconds? (seconds 2))))

(deftest mins-ago-test
  (is (= 5 (mins-ago (minus (now) (minutes 5))))))

;;
;; ported from quartzite.date-time
;;

(deftest test-last-day-of-the-month
  (let [d1 (date-time 2012 1 31)
        d2 (date-time 2012 2 29)
        d3 (date-time 2012 3 31)
        d4 (date-time 2012 4 30)
        d5 (date-time 2012 5 31)
        d6 (date-time 2012 6 30)
        d7 (date-time 2013 2 28)
        d8 (date-time 2016 2 29)
        d9 (local-date 2014 1 31)
        d10 (local-date 2014 1 5)
        d11 (local-date 2014 1 29)]
    (is (= d1 (last-day-of-the-month 2012 1)))
    (is (= d1 (last-day-of-the-month (date-time 2012 1 13))))
    (is (= d2 (last-day-of-the-month 2012 2)))
    (is (= d2 (last-day-of-the-month (date-time 2012 2 8))))
    (is (= d3 (last-day-of-the-month 2012 3)))
    (is (= d4 (last-day-of-the-month 2012 4)))
    (is (= d5 (last-day-of-the-month 2012 5)))
    (is (= d6 (last-day-of-the-month 2012 6)))
    (is (= d7 (last-day-of-the-month 2013 2)))
    (is (= d8 (last-day-of-the-month 2016 2)))
    (is (= d9 (last-day-of-the-month d9)))
    (is (= d9 (last-day-of-the-month d10)))
    (is (= d9 (last-day-of-the-month d11)))))

(deftest test-week-number-of-year
  (is (= 52 (week-number-of-year (date-time 2012 1 1))))
  (is (= 1 (week-number-of-year (date-time 2012 1 2))))
  (is (= 1 (week-number-of-year (date-time 2012 1 8))))
  (is (= 2 (week-number-of-year (date-time 2012 1 9))))
  (is (= 34 (week-number-of-year (date-time 2012 8 20))))
  (is (= 52 (week-number-of-year (date-time 2012 12 30))))
  (is (= 1 (week-number-of-year (date-time 2012 12 31))))
  (is (= 1 (week-number-of-year (date-time 2013 1 1)))))

(deftest test-number-of-days-in-the-month
  (is (= 31 (number-of-days-in-the-month 2012 1)))
  (is (= 31 (number-of-days-in-the-month (date-time 2012 1 3))))
  (is (= 29 (number-of-days-in-the-month 2012 2)))
  (is (= 28 (number-of-days-in-the-month 2013 2)))
  (is (= 30 (number-of-days-in-the-month 2012 11)))
  (is (= 31 (number-of-days-in-the-month 2012 3)))
  (is (= 30 (number-of-days-in-the-month 2012 4)))
  (is (= 31 (number-of-days-in-the-month 2013 12)))
  (is (= 28 (number-of-days-in-the-month 2013 2)))
  (is (= 29 (number-of-days-in-the-month 2016 2))))


(deftest test-first-day-of-the-month
  (let [d1 (date-time 2012 1 1)
        d2 (date-time 2012 2 1)
        d3 (date-time 2012 3 1)
        d4 (date-time 2012 4 1)
        d5 (date-time 2012 5 1)
        d6 (date-time 2012 6 1)
        d7 (date-time 2013 2 1)
        d8 (date-time 2016 2 1)
        d9 (local-date 2014 1 1)
        d10 (local-date 2014 1 2)
        d11 (local-date 2014 1 31)]
    (is (= d1 (first-day-of-the-month 2012 1)))
    (is (= d1 (first-day-of-the-month (date-time 2012 1 24))))
    (is (= d2 (first-day-of-the-month 2012 2)))
    (is (= d2 (first-day-of-the-month (date-time 2012 2 24))))
    (is (= d3 (first-day-of-the-month 2012 3)))
    (is (= d4 (first-day-of-the-month 2012 4)))
    (is (= d5 (first-day-of-the-month 2012 5)))
    (is (= d6 (first-day-of-the-month 2012 6)))
    (is (= d7 (first-day-of-the-month 2013 2)))
    (is (= d8 (first-day-of-the-month 2016 2)))
    (is (= d9 (first-day-of-the-month d9)))
    (is (= d9 (first-day-of-the-month d10)))
    (is (= d9 (first-day-of-the-month d11)))))

(deftest test-nth-day-of-the-month
  (let [july-1  (date-time 2012 7 1)
        july-10 (date-time 2012 7 10)
        july-15 (date-time 2012 7 15)
        july-31 (date-time 2012 7 31)]

    (is (= july-10 (nth-day-of-the-month july-1  10)))
    (is (= july-10 (nth-day-of-the-month july-10 10)))
    (is (= july-10 (nth-day-of-the-month july-15 10)))
    (is (= july-10 (nth-day-of-the-month july-31 10)))

    (is (= july-1 (nth-day-of-the-month july-1  1)))
    (is (= july-1 (nth-day-of-the-month july-10 1)))
    (is (= july-1 (nth-day-of-the-month july-15 1)))
    (is (= july-1 (nth-day-of-the-month july-31 1)))

    (is (= july-31 (nth-day-of-the-month july-1  31)))
    (is (= july-31 (nth-day-of-the-month july-10 31)))
    (is (= july-31 (nth-day-of-the-month july-15 31)))
    (is (= july-31 (nth-day-of-the-month july-31 31)))))

(deftest test-today-at
  (let [^DateTime n  (now)
        y  (.getYear n)
        m  (.getMonthOfYear n)
        d  (.getDayOfMonth n)
        d1 (date-time y m d 13 0)]
    (is (= d1 (today-at 13 0)))
    (is (= d1 (today-at 13 0 0)))))

(deftest test-floor
	(let [^DateTime t (date-time 0 1 2 3 4 5 6)]
		(is (= (floor t year)   (date-time 0)))
		(is (= (floor t month)  (date-time 0 1)))
		(is (= (floor t day)    (date-time 0 1 2)))
		(is (= (floor t hour)   (date-time 0 1 2 3)))
		(is (= (floor t minute) (date-time 0 1 2 3 4)))
		(is (= (floor t second) (date-time 0 1 2 3 4 5)))
		(is (= (floor t milli)  (date-time 0 1 2 3 4 5 6)))))

(deftest test-floor-with-timezones
  (are [tz-id floor-fn dt1-args dt2-args]
      (let [timezone (time-zone-for-id tz-id)
            [dt1 dt2] (map #(from-time-zone (apply date-time %)
                                            timezone)
                           [dt1-args dt2-args])
            ^DateTime floored-dt1 (floor dt1 floor-fn)]
        (and (= floored-dt1 dt2)
             (= (.getZone floored-dt1) timezone)))
    "Africa/Johannesburg" day [1 1 2 1]  [1 1 2]
    "Africa/Johannesburg" day [1 1 1 23] [1 1 1]
    "America/Los_Angeles" day [1 1 2 3]  [1 1 2]
    "America/Los_Angeles" day [1 1 1 20] [1 1 1]))
