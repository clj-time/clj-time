(ns clj-time.core-test
  (:refer-clojure :exclude [extend])
  (:use clojure.test
        clj-time.core)
  (:import java.util.Date
           org.joda.time.DateTime))

(deftest test-now
  (is (= (date-time 2010 1 1)
         (do-at (date-time 2010 1 1) 
                (now)))))

(deftest test-today-at-midnight
  (is (= (date-midnight 2010 1 1)
         (do-at (date-midnight 2010 1 1)
                (today-at-midnight)))))

(deftest test-epoch
  (let [e (epoch)]
    (is (= 1970 (year e)))
    (is (= 0 (sec e)))))

(deftest test-date-time-and-accessors
  (let [d (date-time 1986)]
    (is (= 1986 (year   d)))
    (is (= 1    (month  d)))
    (is (= 1    (day    d)))
    (is (= 0    (hour   d)))
    (is (= 0    (minute d)))
    (is (= 0    (sec    d)))
    (is (= 0    (milli  d))))
  (let [d (date-time 1986 10 14 4 3 2 1)]
    (is (= 1986 (year   d)))
    (is (= 10   (month  d)))
    (is (= 14   (day    d)))
    (is (= 4    (hour   d)))
    (is (= 3    (minute d)))
    (is (= 2    (sec    d)))
    (is (= 1    (milli  d)))))

(deftest test-date-midnight-and-accessors
  (let [d (date-midnight 1986)]
    (is (= 1986 (year   d)))
    (is (= 1    (month  d)))
    (is (= 1    (day    d)))
    (is (= 0    (hour   d)))
    (is (= 0    (minute d)))
    (is (= 0    (sec    d)))
    (is (= 0    (milli  d))))
  (let [d (date-midnight 1986 10 14)]
    (is (= 1986 (year   d)))
    (is (= 10   (month  d)))
    (is (= 14   (day    d)))
    (is (= 0    (hour   d)))
    (is (= 0    (minute d)))
    (is (= 0    (sec    d)))
    (is (= 0    (milli  d)))))

(deftest test-local-date-time-and-accessors
  (let [d (local-date-time 1986)]
    (is (= 1986 (year   d)))
    (is (= 1    (month  d)))
    (is (= 1    (day    d)))
    (is (= 0    (hour   d)))
    (is (= 0    (minute d)))
    (is (= 0    (sec    d)))
    (is (= 0    (milli  d))))
  (let [d (date-time 1986 10 14 4 3 2 1)]
    (is (= 1986 (year   d)))
    (is (= 10   (month  d)))
    (is (= 14   (day    d)))
    (is (= 4    (hour   d)))
    (is (= 3    (minute d)))
    (is (= 2    (sec    d)))
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
           (secs 2)
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
           (secs 2)
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
    (is (= 1763822 (in-secs p)))
    (is (= 1763822000 (in-msecs p)))))

(deftest test-interval-in-bigger
  (let [p (interval (date-time 1986 10 14 12 5 4) (date-time 1987 11 3  22 2 6))]
    (is (= 1       (in-years p)))
    (is (= 12      (in-months p)))
    (is (= 55      (in-weeks p)))
    (is (= 385     (in-days p)))
    (is (= 9249    (in-hours p)))))

(deftest test-within?
  (let [d1 (date-time 1985)
        d2 (date-time 1986)
        d3 (date-time 1987)
        ld1 (local-date 2013 1 1)
        ld2 (local-date 2013 2 28)
        ld3 (local-date 2013 10 5)]
    (is (within? (interval d1 d3) d2))
    (is (not (within? (interval d1 d2) d3)))
    (is (not (within? (interval d1 d2) d2)))
    (is (not (within? (interval d2 d3) d1)))
    (is (within? ld1 ld3 ld2))
    (is (not (within? ld1 ld2 ld3)))
    (is (not (within? ld3 ld2 ld1)))
    (is (not (within? ld2 ld3 ld1)))))

(deftest test-overlaps?
  (let [d1 (date-time 1985)
        d2 (date-time 1986)
        d3 (date-time 1987)
        d4 (date-time 1988)]
    (is (overlaps? (interval d1 d3) (interval d2 d4)))
    (is (overlaps? (interval d1 d3) (interval d2 d3)))
    (is (not (overlaps? (interval d1 d2) (interval d2 d3))))
    (is (not (overlaps? (interval d1 d2) (interval d3 d4))))))

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
  (is (secs? (secs 2))))

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
        d8 (date-time 2016 2 29)]
    (is (= d1 (last-day-of-the-month 2012 1)))
    (is (= d1 (last-day-of-the-month (date-time 2012 1 13))))
    (is (= d2 (last-day-of-the-month 2012 2)))
    (is (= d2 (last-day-of-the-month (date-time 2012 2 8))))
    (is (= d3 (last-day-of-the-month 2012 3)))
    (is (= d4 (last-day-of-the-month 2012 4)))
    (is (= d5 (last-day-of-the-month 2012 5)))
    (is (= d6 (last-day-of-the-month 2012 6)))
    (is (= d7 (last-day-of-the-month 2013 2)))
    (is (= d8 (last-day-of-the-month 2016 2)))))

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
        d8 (date-time 2016 2 1)]
    (is (= d1 (first-day-of-the-month 2012 1)))
    (is (= d1 (first-day-of-the-month (date-time 2012 1 24))))
    (is (= d2 (first-day-of-the-month 2012 2)))
    (is (= d2 (first-day-of-the-month (date-time 2012 2 24))))
    (is (= d3 (first-day-of-the-month 2012 3)))
    (is (= d4 (first-day-of-the-month 2012 4)))
    (is (= d5 (first-day-of-the-month 2012 5)))
    (is (= d6 (first-day-of-the-month 2012 6)))
    (is (= d7 (first-day-of-the-month 2013 2)))
    (is (= d8 (first-day-of-the-month 2016 2)))))


(deftest test-today-at
  (let [^DateTime n  (now)
        y  (.getYear n)
        m  (.getMonthOfYear n)
        d  (.getDayOfMonth n)
        d1 (date-time y m d 13 0)]
    (is (= d1 (today-at 13 0)))
    (is (= d1 (today-at 13 0 0)))))
