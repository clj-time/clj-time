# `clj-time`

A date and time library for Clojure, wrapping the [Joda Time](http://joda-time.sourceforge.net/) library.

## Usage

The main namespace for date-time operations in the `clj-time` library is `clj-time.core`.

    => (use 'clj-time.core)

Create a DateTime instance with date-time, specifying the year, month, day, hour, minute, second, and millisecond:

    => (date-time 1986 10 14 4 3 27 456)
    #<DateTime 1986-10-14T04:03:27.456Z>

Less-significant fields can be omitted:

    => (date-time 1986 10 14)
    #<DateTime 1986-10-14T00:00:00.000Z>

Get the current time with `(now)` and the start of the Unix epoch with `(epoch)`.

Once you have a date-time, use accessors like `hour` and `sec` to access the corresponding fields:

    => (hour (date-time 1986 10 14 22))
    22

The date-time constructor always returns times in the UTC time zone. If you want a time with the specified fields in a different time zone, use `from-time-zone`:

    => (from-time-zone (date-time 1986 10 22) (time-zone-for-offset -2))
    #<DateTime 1986-10-22T00:00:00.000-02:00>

If on the other hand you want a given absolute instant in time in a different time zone, use `to-time-zone`:

    => (to-time-zone (date-time 1986 10 22) (time-zone-for-offset -2))
    #<DateTime 1986-10-21T22:00:00.000-02:00>

In addition to `time-zone-for-offset`, you can use the `time-zone-for-id` and `default-time-zone` functions and the `utc` Var to construct or get `DateTimeZone` instances.

The functions `after?` and `before?` determine the relative position of two
DateTime instances:

    => (after? (date-time 1986 10) (date-time 1986 9))
    true

Often you will want to find a date some amount of time from a given date. For
example, to find the time 1 month and 3 weeks from a given date-time:

    => (plus (date-time 1986 10 14) (months 1) (weeks 3))
    #<DateTime 1986-12-05T00:00:00.000Z>

An `Interval` is used to represent the span of time between two `DateTime`
instances. Construct one using `interval`, then query them using `within?`,
`overlaps?`, and `abuts?`

    => (within? (interval (date-time 1986) (date-time 1990))
                (date-time 1987))
    true

The `in-secs` and `in-minutes` functions can be used to describe intervals in the corresponding temporal units:

    => (in-minutes (interval (date-time 1986 10 2) (date-time 1986 10 14)))
    17280

If you need to parse or print date-times, use `clj-time.format:

    => (use 'clj-time.format)

Printing and printing are controlled by formatters. You can either use one of the built in ISO8601 formatters or define your own, e.g.:

    (def built-in-formatter (formatters :basic-date-time))
    (def custom-formatter (formatter \"yyyyMMdd\"))

To see a list of available built-in formatters and an example of a date-time printed in their format:

    => (show-formatters)

Once you have a formatter, parsing and printing are strait-forward:

    => (parse custom-formatter \"20100311\")
    #<DateTime 2010-03-11T00:00:00.000Z>
    
    => (unparse custom-formatter (date-time 2010 10 3))
    \"20101003\"

To parse dates in multiple formats and format dates in just one format, you can do this:

    => (def multi-parser (formatter (default-time-zone) \"YYYY-MM-dd\" \"YYYY/MM/dd\"))

    => (unparse multi-parser (parse multi-parser "\2012-02-01\"))
    \"2012-02-01\"

    => (unparse multi-parser (parse multi-parser \"2012/02/01\"))
    \"2012-02-01\"

The namespace `clj-time.coerce` contains utility functions for coercing Joda `DateTime` instances to and from various other types:

    => (use 'clj-time.coerce)

For example, to convert a Joda `DateTime` to and from a Java `long`:

    => (to-long (date-time 1998 4 25))
    893462400000
  
    => (from-long 893462400000)
    #<DateTime 1998-04-25T00:00:00.000Z>"

## Installation

`clj-time` is available as a Maven artifact via [Clojars](http://clojars.org/clj-time).

## Development

Running the tests:

    $ lein deps
    $ lein test

## License

Released under the MIT License: <http://www.opensource.org/licenses/mit-license.php>
