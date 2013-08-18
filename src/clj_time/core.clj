(ns clj-time.core
  "The core namespace for date-time operations in the clj-time library.

   Create a DateTime instance with date-time (or a LocalDateTime instance with local-date-time),
   specifying the year, month, day, hour, minute, second, and millisecond:

     => (date-time 1986 10 14 4 3 27 456)
     #<DateTime 1986-10-14T04:03:27.456Z>

     => (local-date-time 1986 10 14 4 3 27 456)
     #<LocalDateTime 1986-10-14T04:03:27.456>

   Less-significant fields can be omitted:

     => (date-time 1986 10 14)
     #<DateTime 1986-10-14T00:00:00.000Z>

     => (local-date-time 1986 10 14)
     #<LocalDateTime 1986-10-14T00:00:00.000>

   Get the current time with (now) and the start of the Unix epoch with (epoch).

   Once you have a date-time, use accessors like hour and second to access the
   corresponding fields:

     => (hour (date-time 1986 10 14 22))
     22

     => (hour (local-date-time 1986 10 14 22))
     22

   The date-time constructor always returns times in the UTC time zone. If you
   want a time with the specified fields in a different time zone, use
   from-time-zone:

     => (from-time-zone (date-time 1986 10 22) (time-zone-for-offset -2))
     #<DateTime 1986-10-22T00:00:00.000-02:00>

   If on the other hand you want a given absolute instant in time in a
   different time zone, use to-time-zone:

     => (to-time-zone (date-time 1986 10 22) (time-zone-for-offset -2))
     #<DateTime 1986-10-21T22:00:00.000-02:00>

   In addition to time-zone-for-offset, you can use the time-zone-for-id and
   default-time-zone functions and the utc Var to constgruct or get DateTimeZone
   instances.

   The functions after? and before? determine the relative position of two
   DateTime instances:

     => (after? (date-time 1986 10) (date-time 1986 9))
     true

     => (after? (local-date-time 1986 10) (local-date-time 1986 9))
     true

   Often you will want to find a date some amount of time from a given date. For
   example, to find the time 1 month and 3 weeks from a given date-time:

     => (plus (date-time 1986 10 14) (months 1) (weeks 3))
     #<DateTime 1986-12-05T00:00:00.000Z>

     => (plus (local-date-time 1986 10 14) (months 1) (weeks 3))
     #<LocalDateTime 1986-12-05T00:00:00.000Z>

   An Interval is used to represent the span of time between two DateTime
   instances. Construct one using interval, then query them using within?,
   overlaps?, and abuts?

     => (within? (interval (date-time 1986) (date-time 1990))
                 (date-time 1987))
     true

   To find the amount of time encompased by an interval, use in-seconds and
   in-minutes:

     => (in-minutes (interval (date-time 1986 10 2) (date-time 1986 10 14)))
     17280

   Note that all functions in this namespace work with Joda objects or ints. If
   you need to print or parse date-times, see clj-time.format. If you need to
   ceorce date-times to or from other types, see clj-time.coerce."
  (:refer-clojure :exclude [extend second])
  (:import (org.joda.time ReadablePartial ReadableDateTime ReadableInstant ReadablePeriod DateTime
                          DateMidnight YearMonth LocalDate DateTimeZone Period PeriodType Interval
                          Years Months Weeks Days Hours Minutes Seconds LocalDateTime MutableDateTime
                          DateTimeUtils)
           (org.joda.time.base BaseDateTime)))

(defn deprecated [message]
  (println "DEPRECATION WARNING: " message))

(defprotocol DateTimeProtocol
  "Interface for various date time functions"
  (year [this] "Return the year component of the given date/time.")
  (month [this]   "Return the month component of the given date/time.")
  (day [this]   "Return the day of month component of the given date/time.")
  (day-of-week [this]   "Return the day of week component of the given date/time. Monday is 1 and Sunday is 7")
  (hour [this]   "Return the hour of day component of the given date/time. A time of 12:01am will have an hour component of 0.")
  (minute [this]   "Return the minute of hour component of the given date/time.")
  (sec [this]   "Return the second of minute component of the given date/time.")
  (second [this]   "Return the second of minute component of the given date/time.")
  (milli [this]   "Return the millisecond of second component of the given date/time.")
  (after? [this that] "Returns true if ReadableDateTime 'this' is strictly after date/time 'that'.")
  (before? [this that] "Returns true if ReadableDateTime 'this' is strictly before date/time 'that'.")
  (plus- [this #^ReadablePeriod period]
    "Returns a new date/time corresponding to the given date/time moved forwards by the given Period(s).")
  (minus- [this #^ReadablePeriod period]
    "Returns a new date/time corresponding to the given date/time moved backwards by the given Period(s)."))

(extend-protocol DateTimeProtocol
  org.joda.time.DateTime
  (year [this] (.getYear this))
  (month [this] (.getMonthOfYear this))
  (day [this] (.getDayOfMonth this))
  (day-of-week [this] (.getDayOfWeek this))
  (hour [this] (.getHourOfDay this))
  (minute [this] (.getMinuteOfHour this))
  (sec [this]
    (deprecated "sec is being deprecated in favor of second")
    (.getSecondOfMinute this))
  (second [this] (.getSecondOfMinute this))
  (milli [this] (.getMillisOfSecond this))
  (after? [this #^ReadableInstant that] (.isAfter this that))
  (before? [this #^ReadableInstant that] (.isBefore this that))
  (plus- [this #^ReadablePeriod period] (.plus this period))
  (minus- [this #^ReadablePeriod period] (.minus this period))

  org.joda.time.DateMidnight
  (year [this] (.getYear this))
  (month [this] (.getMonthOfYear this))
  (day [this] (.getDayOfMonth this))
  (day-of-week [this] (.getDayOfWeek this))
  (hour [this] (.getHourOfDay this))
  (minute [this] (.getMinuteOfHour this))
  (sec [this]
    (deprecated "sec is being deprecated in favor of second")
    (.getSecondOfMinute this))
  (second [this] (.getSecondOfMinute this))
  (milli [this] (.getMillisOfSecond this))
  (after? [this #^ReadableInstant that] (.isAfter this that))
  (before? [this #^ReadableInstant that] (.isBefore this that))
  (plus- [this #^ReadablePeriod period] (.plus this period))
  (minus- [this #^ReadablePeriod period] (.minus this period))

  org.joda.time.LocalDateTime
  (year [this] (.getYear this))
  (month [this] (.getMonthOfYear this))
  (day [this] (.getDayOfMonth this))
  (day-of-week [this] (.getDayOfWeek this))
  (hour [this] (.getHourOfDay this))
  (minute [this] (.getMinuteOfHour this))
  (sec [this]
    (deprecated "sec is being deprecated in favor of second")
    (.getSecondOfMinute this))
  (second [this] (.getSecondOfMinute this))
  (milli [this] (.getMillisOfSecond this))
  (after? [this #^ReadablePartial that] (.isAfter this that))
  (before? [this #^ReadablePartial that] (.isBefore this that))
  (plus- [this #^ReadablePeriod period] (.plus this period))
  (minus- [this #^ReadablePeriod period] (.minus this period))

  org.joda.time.YearMonth
  (year [this] (.getYear this))
  (month [this] (.getMonthOfYear this))
  (after? [this #^ReadablePartial that] (.isAfter this that))
  (before? [this #^ReadablePartial that] (.isBefore this that))
  (plus- [this #^ReadablePeriod period] (.plus this period))
  (minus- [this #^ReadablePeriod period] (.minus this period))

  org.joda.time.LocalDate
  (year [this] (.getYear this))
  (month [this] (.getMonthOfYear this))
  (day [this] (.getDayOfMonth this))
  (day-of-week [this] (.getDayOfWeek this))
  (after? [this #^ReadablePartial that] (.isAfter this that))
  (before? [this #^ReadablePartial that] (.isBefore this that))
  (plus- [this #^ReadablePeriod period] (.plus this period))
  (minus- [this #^ReadablePeriod period] (.minus this period)))

(def ^{:doc "DateTimeZone for UTC."}
      utc
  (DateTimeZone/UTC))

(defn now
  "Returns a DateTime for the current instant in the UTC time zone."
  []
  (DateTime. #^DateTimeZone utc))

(defn today-at-midnight
  "Returns a DateMidnight for today at midnight in the UTC time zone."
  []
  (DateMidnight. #^DateTimeZone utc))

(defn epoch
  "Returns a DateTime for the begining of the Unix epoch in the UTC time zone."
  []
  (DateTime. (long 0) #^DateTimeZone utc))

(defn date-midnight
  "Constructs and returns a new DateMidnight in UTC.
   Specify the year, month of year, day of month. Note that month and day are
   1-indexed. Any number of least-significant components can be ommited, in which case
   they will default to 1."
  ([year]
    (date-midnight year 1 1))
  ([^long year ^long month]
    (date-midnight year month 1))
  ([^Long year ^Long month ^Long day]
    (DateMidnight. year month day #^DateTimeZone utc)))

(defn ^DateTime date-time
  "Constructs and returns a new DateTime in UTC.
   Specify the year, month of year, day of month, hour of day, minute if hour,
   second of minute, and millisecond of second. Note that month and day are
   1-indexed while hour, second, minute, and millis are 0-indexed.
   Any number of least-significant components can be ommited, in which case
   they will default to 1 or 0 as appropriate."
  ([year]
   (date-time year 1 1 0 0 0 0))
  ([year month]
   (date-time year month 1 0 0 0 0))
  ([year month day]
   (date-time year month day 0 0 0 0))
  ([year month day hour]
   (date-time year month day hour 0 0 0))
  ([year month day hour minute]
   (date-time year month day hour minute 0 0))
  ([year month day hour minute second]
   (date-time year month day hour minute second 0))
  ([#^Integer year #^Integer month #^Integer day #^Integer hour
    #^Integer minute #^Integer second #^Integer millis]
   (DateTime. year month day hour minute second millis #^DateTimeZone utc)))

(defn #^org.joda.time.LocalDateTime local-date-time
  "Constructs and returns a new LocalDateTime.
   Specify the year, month of year, day of month, hour of day, minute if hour,
   second of minute, and millisecond of second. Note that month and day are
   1-indexed while hour, second, minute, and millis are 0-indexed.
   Any number of least-significant components can be ommited, in which case
   they will default to 1 or 0 as appropriate."
  ([year]
   (local-date-time year 1 1 0 0 0 0))
  ([year month]
   (local-date-time year month 1 0 0 0 0))
  ([year month day]
   (local-date-time year month day 0 0 0 0))
  ([year month day hour]
   (local-date-time year month day hour 0 0 0))
  ([year month day hour minute]
   (local-date-time year month day hour minute 0 0))
  ([year month day hour minute second]
   (local-date-time year month day hour minute second 0))
  ([#^Integer year #^Integer month #^Integer day #^Integer hour
    #^Integer minute #^Integer second #^Integer millis]
   (LocalDateTime. year month day hour minute second millis)))

(defn #^org.joda.time.YearMonth year-month
  "Constructs and returns a new YearMonth.
   Specify the year and month of year. Month is 1-indexed and defaults
   to January (1)."
  ([year]
     (year-month year 1))
  ([#^Integer year #^Integer month]
     (YearMonth. year month)))

(defn #^org.joda.time.LocalDate local-date
  "Constructs and returns a new LocalDate.
   Specify the year, month, and day. Does not deal with timezones."
  [#^Integer year #^Integer month #^Integer day]
  (LocalDate. year month day))

(defn #^org.joda.time.LocalDate today
  "Constructs and returns a new LocalDate representing today's date.
   LocalDate objects do not deal with timezones at all."
  []
  (LocalDate.))

(defn time-zone-for-offset
  "Returns a DateTimeZone for the given offset, specified either in hours or
   hours and minutes."
  ([hours]
   (DateTimeZone/forOffsetHours hours))
  ([hours minutes]
   (DateTimeZone/forOffsetHoursMinutes hours minutes)))

(defn time-zone-for-id
  "Returns a DateTimeZone for the given ID, which must be in long form, e.g.
   'America/Matamoros'."
  [#^String id]
  (DateTimeZone/forID id))

(defn default-time-zone
  "Returns the default DateTimeZone for the current environment."
  []
  (DateTimeZone/getDefault))

(defn #^org.joda.time.DateTime
  to-time-zone
  "Returns a new ReadableDateTime corresponding to the same absolute instant in time as
   the given ReadableDateTime, but with calendar fields corresponding to the given
   TimeZone."
  [#^DateTime dt #^DateTimeZone tz]
  (.withZone dt tz))

(defn #^org.joda.time.DateTime
  from-time-zone
  "Returns a new ReadableDateTime corresponding to the same point in calendar time as
   the given ReadableDateTime, but for a correspondingly different absolute instant in
   time."
  [#^DateTime dt #^DateTimeZone tz]
  (.withZoneRetainFields dt tz))

(defn years
  "Given a number, returns a Period representing that many years.
   Without an argument, returns a PeriodType representing only years."
  ([]
     (PeriodType/years))
  ([#^Integer n]
     (Years/years n)))

(defn months
  "Given a number, returns a Period representing that many months.
   Without an argument, returns a PeriodType representing only months."
  ([]
     (PeriodType/months))
  ([#^Integer n]
     (Months/months n)))

(defn weeks
  "Given a number, returns a Period representing that many weeks.
   Without an argument, returns a PeriodType representing only weeks."
  ([]
     (PeriodType/weeks))
  ([#^Integer n]
     (Weeks/weeks n)))

(defn days
  "Given a number, returns a Period representing that many days.
   Without an argument, returns a PeriodType representing only days."
  ([]
     (PeriodType/days))
  ([#^Integer n]
     (Days/days n)))

(defn hours
  "Given a number, returns a Period representing that many hours.
   Without an argument, returns a PeriodType representing only hours."
  ([]
     (PeriodType/hours))
  ([#^Integer n]
     (Hours/hours n)))

(defn minutes
  "Given a number, returns a Period representing that many minutes.
   Without an argument, returns a PeriodType representing only minutes."
  ([]
     (PeriodType/minutes))
  ([#^Integer n]
     (Minutes/minutes n)))

(defn seconds
  "Given a number, returns a Period representing that many seconds.
   Without an argument, returns a PeriodType representing only seconds."
  ([]
     (PeriodType/seconds))
  ([#^Integer n]
     (Seconds/seconds n)))

(defn secs
  "DEPRECATED"
  ([]
     (seconds))
  ([#^Integer n]
     (seconds n)))

(defn millis
  "Given a number, returns a Period representing that many milliseconds.
   Without an argument, returns a PeriodType representing only milliseconds."
  ([]
     (PeriodType/millis))
  ([#^Integer n]
     (Period/millis n)))

(defn plus
  "Returns a new date/time corresponding to the given date/time moved forwards by
   the given Period(s)."
  ([dt #^ReadablePeriod p]
     (plus- dt p))
  ([dt p & ps]
     (reduce plus- (plus- dt p) ps)))

(defn minus
  "Returns a new date/time object corresponding to the given date/time moved backwards by
   the given Period(s)."
  ([dt #^ReadablePeriod p]
   (minus- dt p))
  ([dt p & ps]
     (reduce minus- (minus- dt p) ps)))

(defn ago
  "Returns a DateTime a supplied period before the present.
  e.g. (-> 5 years ago)"
  [#^Period period]
  (minus (now) period))

(defn from-now
  "Returns a DateTime a supplied period after the present.
  e.g. (-> 30 minutes from-now)"
  [#^Period period]
  (plus (now) period))

(defn interval
  "Returns an interval representing the span between the two given ReadableDateTimes.
   Note that intervals are closed on the left and open on the right."
  [#^ReadableDateTime dt-a #^ReadableDateTime dt-b]
  (Interval. dt-a dt-b))

(defn start
  "Returns the start DateTime of an Interval."
  [#^Interval in]
  (.getStart in))

(defn end
  "Returns the end DateTime of an Interval."
  [#^Interval in]
  (.getEnd in))

(defn extend
  "Returns an Interval with an end ReadableDateTime the specified Period after the end
   of the given Interval"
  [#^Interval in & by]
  (.withEnd in (apply plus (end in) by)))

(defn in-millis
  "Returns the number of milliseconds in the given Interval."
  [#^Interval in]
  (.toDurationMillis in))

(defn in-msecs
  "DEPRECATED: Returns the number of milliseconds in the given Interval."
  [#^Interval in]
  (deprecated "DEPRECATED: use in-millis")
  (in-millis in))

(defn in-seconds
  "Returns the number of standard seconds in the given Interval."
  [#^Interval in]
  (.getSeconds (.toPeriod in (seconds))))

(defn in-secs
  "DEPRECATED: Returns the number of standard seconds in the given Interval."
  [#^Interval in]
  (deprecated "Please use in-seconds instead.")
  (in-seconds in))

(defn in-minutes
  "Returns the number of standard minutes in the given Interval."
  [#^Interval in]
  (.getMinutes (.toPeriod in (minutes))))

(defn in-hours
  "Returns the number of standard hours in the given Interval."
  [#^Interval in]
  (.getHours (.toPeriod in (hours))))

(defn in-days
  "Returns the number of standard days in the given Interval."
  [#^Interval in]
  (.getDays (.toPeriod in (days))))

(defn in-weeks
  "Returns the number of standard weeks in the given Interval."
  [#^Interval in]
  (.getWeeks (.toPeriod in (weeks))))

(defn in-months
  "Returns the number of standard months in the given Interval."
  [#^Interval in]
  (.getMonths (.toPeriod in (months))))

(defn in-years
  "Returns the number of standard years in the given Interval."
  [#^Interval in]
  (.getYears (.toPeriod in (years))))

(defn within?
  "With 2 arguments: Returns true if the given Interval contains the given
   ReadableDateTime. Note that if the ReadableDateTime is exactly equal to the
   end of the interval, this function returns false.
   With 3 arguments: Returns true if the start ReadablePartial is
   equal to or before and the end ReadablePartial is equal to or after the test
   ReadablePartial."
  ([#^Interval i #^ReadableDateTime dt]
     (.contains i dt))
  ([#^ReadablePartial start #^ReadablePartial end #^ReadablePartial test]
     (or (= start test)
         (= end test)
         (and (before? start test) (after? end test)))))

(defn overlaps?
  "With 2 arguments: Returns true of the two given Intervals overlap.
   Note that intervals that satisfy abuts? do not satisfy overlaps?
   With 4 arguments: Returns true if the range specified by start-a and end-a
   overlaps with the range specified by start-b and end-b."
  ([#^Interval i-a #^Interval i-b]
     (.overlaps i-a i-b))
  ([#^ReadablePartial start-a #^ReadablePartial end-a
    #^ReadablePartial start-b #^ReadablePartial end-b]
     (or (and (before? start-b end-a) (after? end-b start-a))
         (and (after? end-b start-a) (before? start-b end-a))
         (or (= start-a end-b) (= start-b end-a)))))

(defn abuts?
  "Returns true if Interval i-a abuts i-b, i.e. then end of i-a is exactly the
   beginning of i-b."
  [#^Interval i-a #^Interval i-b]
  (.abuts i-a i-b))

(defn years?
  "Returns true if the given value is an instance of Years"
  [val]
  (instance? Years val))

(defn months?
  "Returns true if the given value is an instance of Months"
  [val]
  (instance? Months val))

(defn weeks?
  "Returns true if the given value is an instance of Weeks"
  [val]
  (instance? Weeks val))

(defn days?
  "Returns true if the given value is an instance of Days"
  [val]
  (instance? Days val))

(defn hours?
  "Returns true if the given value is an instance of Hours"
  [val]
  (instance? Hours val))

(defn minutes?
  "Returns true if the given value is an instance of Minutes"
  [val]
  (instance? Minutes val))

(defn seconds?
  "Returns true if the given value is an instance of Seconds"
  [val]
  (instance? Seconds val))

(defn secs?
  "DEPRECATED"
  [val]
  (deprecated "Please use seconds? instead.")
  (seconds? val))

(defn mins-ago
  [d]
  (in-minutes (interval d (now))))


(defn ^DateTime last-day-of-the-month
  ([^DateTime dt]
     (last-day-of-the-month (.getYear dt) (.getMonthOfYear dt)))
  ([^long year ^long month]
     (-> ^DateTime (date-time year month) .dayOfMonth .withMaximumValue)))

(defn number-of-days-in-the-month
  (^long [^DateTime dt]
         (number-of-days-in-the-month (.getYear dt) (.getMonthOfYear dt)))
  (^long [^long year ^long month]
         (.getDayOfMonth ^DateTime (last-day-of-the-month year month))))

(defn ^DateTime first-day-of-the-month
  ([^DateTime dt]
     (first-day-of-the-month (.getYear dt) (.getMonthOfYear dt)))
  ([^long year ^long month]
     (-> ^DateTime (date-time year month) .dayOfMonth .withMinimumValue)))


(defn ^DateTime today-at
  ([^long hours ^long minutes ^long seconds ^long millis]
     (let [^MutableDateTime mdt (.toMutableDateTime ^DateTime (now))]
       (.toDateTime (doto mdt
                      (.setHourOfDay      hours)
                      (.setMinuteOfHour   minutes)
                      (.setSecondOfMinute seconds)
                      (.setMillisOfSecond millis)))))
  ([^long hours ^long minutes ^long seconds]
     (today-at hours minutes seconds 0))
  ([^long hours ^long minutes]
     (today-at hours minutes 0)))

(defn do-at* [^BaseDateTime base-date-time body-fn]
  (DateTimeUtils/setCurrentMillisFixed (.getMillis base-date-time))
  (try
    (body-fn)
    (finally
      (DateTimeUtils/setCurrentMillisSystem))))

(defmacro do-at
  "Like clojure.core/do except evalautes the expression at the given date-time"
  [^BaseDateTime base-date-time & body]
  `(do-at* ~base-date-time
    (fn [] ~@body)))
