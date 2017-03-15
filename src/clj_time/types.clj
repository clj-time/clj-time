(ns clj-time.types
  "This namespace defines a set of predicates for the various Joda Time types used by clj-time."
  (:import [org.joda.time DateTimeZone LocalDate LocalDateTime]
           [org.joda.time.base BaseDateTime]))

(defn date-time? [x]
  (and (instance? BaseDateTime x)
       (= (.getZone ^BaseDateTime x) DateTimeZone/UTC)))

(defn local-date-time? [x]
  (instance? LocalDateTime x))

(defn local-date? [x]
  (instance? LocalDate x))

(defn time-zone? [x]
  (instance? DateTimeZone x))
