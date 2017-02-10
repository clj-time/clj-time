(ns clj-time.spec
  "This namespace requires Clojure 1.9 or later. It defines a set of predicates plus a set of spec defs with associated generators."
  (:require [clojure.spec :as spec]
            [clojure.spec.gen :as gen]
            [clj-time.core :refer [date-time]]
            [clj-time.coerce :refer [to-date-time to-long]])
  (:import [org.joda.time DateTime DateTimeZone LocalDate LocalDateTime]
           [org.joda.time.base BaseDateTime]
           [java.util TimeZone]))

(defn date-time?
  "This includes DateTime, MutableDateTime and DateMidnight. The time zone is not specified."
  [x]
  (instance? BaseDateTime x))

(defn utc-date-time? [x]
  (and (date-time? x)
       (= (.getZone ^BaseDateTime x) DateTimeZone/UTC)))

(defn local-date-time? [x]
  (instance? LocalDateTime x))

(defn local-date? [x]
  (instance? LocalDate x))

(defn time-zone? [x]
  (instance? DateTimeZone x))

(def all-time-zones
  (delay
    (set
      (keep #(try (DateTimeZone/forTimeZone (TimeZone/getTimeZone ^String %))
                  (catch Throwable t nil))
            (TimeZone/getAvailableIDs)))))

(defn ^:dynamic *time-zones*
  "Dynamically bind this to choose which time zones to use in generators."
  []
  (gen/one-of [(gen/return DateTimeZone/UTC)
               (spec/gen @all-time-zones)]))

(spec/def ::modern-era (spec/int-in (to-long (date-time 2006 3 24 14 49 31))
                                    (to-long (date-time 2017 1 20 16 00 00))))

(spec/def ::questionable-future (spec/int-in (to-long (date-time 2017 1 20 16 00 00))
                                             (to-long (date-time 2030 12 31 23 59 59))))

(defn ^:dynamic *period*
  "Dynamically bind this to choose the range of your generated dates."
  []
  (gen/one-of [(spec/gen ::modern-era) (spec/gen ::questionable-future)]))

(spec/def ::time-zone
          (spec/with-gen time-zone? *time-zones*))

; FIXME: despite specifying a time zone, this seems to always produce UTC. I don't know what I'm doing wrong.
(spec/def ::date-time
          (spec/with-gen utc-date-time?
                         #(gen/fmap (fn [[ms tz]]
                                      (DateTime. ms ^DateTimeZone tz))
                                    (gen/tuple (*period*)
                                               (*time-zones*)))))

(spec/def ::utc-date-time
          (spec/with-gen utc-date-time?
                         #(gen/fmap (fn [ms] (DateTime. ms DateTimeZone/UTC))
                                    (*period*))))

(spec/def ::local-date
          (spec/with-gen local-date?
                         #(gen/fmap (fn [ms] (LocalDate. ms))
                                    (*period*))))

(spec/def ::local-date-time
          (spec/with-gen local-date-time?
                         #(gen/fmap (fn [ms] (LocalDateTime. ms))
                                    (*period*))))


(comment
  (gen/sample (spec/gen ::utc-date-time))
  (gen/sample (spec/gen ::local-date))
  (gen/sample (spec/gen ::local-date-time))
  (binding [*period* #(spec/gen int?)]
    (gen/sample (spec/gen ::local-date)))
  (gen/sample (spec/gen ::time-zone))
  (map #(.getZone ^DateTime %) (gen/sample (spec/gen ::date-time))))
