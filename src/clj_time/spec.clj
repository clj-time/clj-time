(ns clj-time.spec
  "This namespace requires Clojure 1.9 or later. It defines a set of predicates plus a set of spec defs with associated generators."
  (:require [clojure.spec :as spec]
            [clojure.spec.gen :as gen]
            [clj-time.types :refer [date-time? local-date-time? local-date? time-zone?]]
            [clj-time.core :refer [date-time]]
            [clj-time.coerce :refer [to-date-time to-long]])
  (:import [org.joda.time DateTime DateTimeZone LocalDate LocalDateTime]
           [org.joda.time.base BaseDateTime]
           [java.util TimeZone]))

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

(spec/def ::past (spec/int-in (to-long (date-time 2001 1 1 00 00 00))
                              (to-long (date-time 2010 12 31 00 00 00))))

(spec/def ::past-and-future (spec/int-in (to-long (date-time 2011 1 1 00 00 00))
                                         (to-long (date-time 2030 12 31 23 59 59))))

(spec/def ::future (spec/int-in (to-long (date-time 2031 1 1 0 00 00))
                                (to-long (date-time 2040 12 31 23 59 59))))

(defn ^:dynamic *period*
  "Dynamically bind this to choose the range of your generated dates."
  []
  (spec/gen ::past-and-future))

(spec/def ::time-zone
          (spec/with-gen time-zone? *time-zones*))

(spec/def ::date-time
          (spec/with-gen date-time?
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
