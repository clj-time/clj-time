(ns clj-time.local
  "Functions for working with local time without having to shift
   to/from utc, the preferred time zone of clj-time.core.

   Get the current local time with (local-now).

   (to-local-date-time obj) returns a local date-time instance
   retaining the time fields.

  The following all return 1986-10-14 04:03:27.246 with the
  local time zone.

  (to-local-date-time (clj-time.core/date-time 1986 10 14 4 3 27 246))
  (to-local-date-time \"1986-10-14T04:03:27.246\")
  (to-local-date-time \"1986-10-14T04:03:27.246Z\")

  The dynamic var *local-formatters* contains a map of local formatters
  for parsing and printing. It is initialized with all the formatters in
  clj-time.format localized.

  to-local-date-time for strings uses *local-formatters* to parse.

  (format-local-time (local-now) :basic-date-time) formats an obj using
  a formatter in *local-formatters* corresponding to the  format-key
  passed in.
"
  (:require [clj-time.core :as time]
            [clj-time.coerce :as coerce]
            [clj-time.format :as fmt])
  (:import (org.joda.time DateTime DateTimeZone)
           (org.joda.time.format DateTimeFormatter)))

(def ^{:doc "Map of local formatters for parsing and printing." :dynamic true}
  *local-formatters*
  (into {} (map
            (fn [[k #^DateTimeFormatter f]] [k (.withZone f #^DateTimeZone (time/default-time-zone))])
            fmt/formatters)))

(defn local-now
  "Returns a DateTime for the current instant in the default time zone."
  []
  (DateTime/now #^DateTimeZone (time/default-time-zone)))

(defprotocol ILocalCoerce
  (to-local-date-time [obj] "convert `obj` to a local Joda DateTime instance retaining time fields."))

(defn- as-local-date-time-from-time-zone
  "Coerce to date-time in the default time zone retaining time fields."
  [obj]
  (-> obj coerce/to-date-time (time/from-time-zone (time/default-time-zone))))

(defn- as-local-date-time-to-time-zone
  "Coerce to date-time in the default time zone."
  [obj]
  (-> obj coerce/to-date-time (time/to-time-zone (time/default-time-zone))))

(defn- from-local-string
  "Return local DateTime instance from string using
   formatters in *local-formatters*, returning first
   which parses."
  [s]
  (first
   (for [f (vals *local-formatters*)
         :let [d (try (fmt/parse f s) (catch Exception _ nil))]
         :when d] d)))

(extend-protocol ILocalCoerce
  nil
  (to-local-date-time [_]
    nil)

  java.util.Date
  (to-local-date-time [date]
    (as-local-date-time-to-time-zone date))

  java.sql.Date
  (to-local-date-time [sql-date]
    (as-local-date-time-to-time-zone sql-date))

  DateTime
  (to-local-date-time [date-time]
    (as-local-date-time-from-time-zone date-time))

  Integer
  (to-local-date-time [integer]
    (as-local-date-time-from-time-zone (long integer)))

  Long
  (to-local-date-time [long]
    (as-local-date-time-from-time-zone long))

  String
  (to-local-date-time [string]
    (from-local-string string))

  java.sql.Timestamp
  (to-local-date-time [timestamp]
    (as-local-date-time-to-time-zone timestamp)))

(defn format-local-time
  "Format obj as local time using the local formatter corresponding
   to format-key."
  [obj format-key]
  (when-let [dt (to-local-date-time obj)]
    (when-let [fmt (format-key *local-formatters*)]
      (fmt/unparse fmt dt))))
