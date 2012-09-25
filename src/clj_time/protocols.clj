(ns clj-time.protocols
  (:import (org.joda.time ReadablePeriod ReadableDateTime ReadableInstant ReadableInterval Interval)))

(defprotocol DateTimeProtocol
  "Interface for various date time functions"
  (year [this] "Return the year component of the given date/time.")
  (month [this]   "Return the month component of the given date/time.")
  (day [this]   "Return the day of month component of the given date/time.")
  (day-of-week [this]   "Return the day of week component of the given date/time. Monday is 1 and Sunday is 7")
  (hour [this]   "Return the hour of day component of the given date/time.
                  A time of 12:01am will have an hour component of 0.")
  (minute [this]   "Return the minute of hour component of the given date/time.")
  (sec [this]   "Return the second of minute component of the given date/time.")
  (milli [this]   "Return the millisecond of second component of the given date/time."))

(defprotocol ChangeProtocol
  (plus- [this #^ReadablePeriod period]   "Returns forwards by the given Period(s).")
  (minus- [this #^ReadablePeriod period]  "Returns backwards by the given Period(s)."))

(defprotocol ComparisonProtocol
  (after? [this that] "Returns true if 'this' is strictly after 'that'.")
  (before? [this that] "Returns true if 'this' is strictly after 'that'."))

(extend-type org.joda.time.ReadablePeriod
  ChangeProtocol
  (plus- [this #^ReadablePeriod period] (.plus this period))
  (minus- [this #^ReadablePeriod period] (.minus this period)))

(extend-type org.joda.time.ReadableInterval
  ComparisonProtocol
  (after? [this that] (.isAfter this that))
  (before? [this that] (.isBefore this that))
  ChangeProtocol
  (plus- [this #^ReadablePeriod period]
    (Interval. (.getStart this)
               (.plus (.toPeriod this) period)))
  (minus- [this #^ReadablePeriod period]
    (Interval. (.getStart this)
               (.minus (.toPeriod this) period))))

(extend-type org.joda.time.ReadableDateTime
  ReadableDateTimeProtocol
  (year [this] (.getYear this))
  (month [this] (.getMonthOfYear this))
  (day [this] (.getDayOfMonth this))
  (day-of-week [this] (.getDayOfWeek this))
  (hour [this] (.getHourOfDay this))
  (minute [this] (.getMinuteOfHour this))
  (sec [this] (.getSecondOfMinute this))
  (milli [this] (.getMillisOfSecond this))
  ComparisonProtocol
  (after? [this that]
    (cond (instance? ReadableInterval that) (.isAfter that this)
          :else (.isAfter this that)))
  (before? [this that]
    (cond (instance? ReadableInterval that) (.isBefore that this)
          :else (.isBefore this that)))
  ChangeProtocol
  (plus- [this #^ReadablePeriod period] (.plus this period))
  (minus- [this #^ReadablePeriod period] (.minus this period)))
