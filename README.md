# `clj-time` <a href="http://travis-ci.org/#!/seancorfield/clj-time/builds"><img src="https://secure.travis-ci.org/seancorfield/clj-time.png" /></a>

A date and time library for Clojure, wrapping the [Joda Time](http://joda-time.sourceforge.net/) library.

## Artifacts

`clj-time` artifacts are [released to Clojars](https://clojars.org/clj-time/clj-time).

If you are using Maven, add the following repository definition to your `pom.xml`:

``` xml
<repository>
  <id>clojars.org</id>
  <url>http://clojars.org/repo</url>
</repository>
```

### The Most Recent Release

With Leiningen:

``` clj
[clj-time "0.6.0"]
```

With Maven:

``` xml
<dependency>
  <groupId>clj-time</groupId>
  <artifactId>clj-time</artifactId>
  <version>0.6.0</version>
</dependency>
```

## Bugs and Enhancements

Please open issues against the [official clj-time repo on Github](https://github.com/clj-time/clj-time/issues).

**Note: version 0.6.0 introduces a number of API changes to improve consistency.**
The API now uses `second`, `seconds` and `millis` where it previously had `sec`, `secs` and `msecs`. The older API is still present but marked as deprecated (and calling deprecated functions will print a message to the console as well returning the result). The older API will be removed in version 0.7.0 so be prepared to update your code.

## Usage

### clj-time.core

The main namespace for date-time operations in the `clj-time` library is `clj-time.core`.

``` clj
=> (use 'clj-time.core)
``` 

Create a DateTime instance with date-time, specifying the year, month, day, hour, minute, second, and millisecond:

``` clj
=> (date-time 1986 10 14 4 3 27 456)
#<DateTime 1986-10-14T04:03:27.456Z>
```

Less-significant fields can be omitted:

``` clj
=> (date-time 1986 10 14)
#<DateTime 1986-10-14T00:00:00.000Z>
```

Get the current time with `(now)` and the start of the Unix epoch with `(epoch)`.

Once you have a date-time, use accessors like `hour` and `second` to access the corresponding fields:

``` clj
=> (hour (date-time 1986 10 14 22))
22
```

The date-time constructor always returns times in the UTC time zone. If you want a time with the specified fields in a different time zone, use `from-time-zone`:

``` clj
=> (from-time-zone (date-time 1986 10 22) (time-zone-for-offset -2))
#<DateTime 1986-10-22T00:00:00.000-02:00>
```

If on the other hand you want a given absolute instant in time in a different time zone, use `to-time-zone`:

``` clj
=> (to-time-zone (date-time 1986 10 22) (time-zone-for-offset -2))
#<DateTime 1986-10-21T22:00:00.000-02:00>
```

In addition to `time-zone-for-offset`, you can use the `time-zone-for-id` and `default-time-zone` functions and the `utc` Var to construct or get `DateTimeZone` instances.

If you only want a date with no time component, consider using the `local-date` and `today` functions.
These return `LocalDate` instances that do not have time components (and thus don't suffer from timezone-related shifting).

``` clj
=> (local-date 2013 3 20)
#<LocalDate 2013-03-20>
```

The functions `after?` and `before?` determine the relative position of two
DateTime instances:

``` clj
=> (after? (date-time 1986 10) (date-time 1986 9))
true
```

Often you will want to find a date some amount of time from a given date. For
example, to find the time 1 month and 3 weeks from a given date-time:

``` clj
=> (plus (date-time 1986 10 14) (months 1) (weeks 3))
#<DateTime 1986-12-05T00:00:00.000Z>
```

An `Interval` is used to represent the span of time between two `DateTime`
instances. Construct one using `interval`, then query them using `within?`,
`overlaps?`, and `abuts?`

``` clj
=> (within? (interval (date-time 1986) (date-time 1990))
            (date-time 1987))
true
```

The `in-seconds` and `in-minutes` functions can be used to describe intervals in the corresponding temporal units:

``` clj
=> (in-minutes (interval (date-time 1986 10 2) (date-time 1986 10 14)))
17280
```

### clj-time.format

If you need to parse or print date-times, use `clj-time.format`:

``` clj
=> (use 'clj-time.format)
```

Printing and printing are controlled by formatters. You can either use one of the built in ISO8601 formatters or define your own, e.g.:

``` clj
(def built-in-formatter (formatters :basic-date-time))
(def custom-formatter (formatter "yyyyMMdd"))
```

To see a list of available built-in formatters and an example of a date-time printed in their format:

``` clj
=> (show-formatters)
```

Remember that `mm` is minutes, `MM` is months, `ss` is seconds and `SS` is milliseconds.

Once you have a formatter, parsing and printing are strait-forward:

``` clj
=> (parse custom-formatter "20100311")
#<DateTime 2010-03-11T00:00:00.000Z>

=> (unparse custom-formatter (date-time 2010 10 3))
"20101003"
```

To parse dates in multiple formats and format dates in just one format, you can do this:

``` clj
=> (def multi-parser (formatter (default-time-zone) "YYYY-MM-dd" "YYYY/MM/dd"))

=> (unparse multi-parser (parse multi-parser "2012-02-01"))
"2012-02-01"

=> (unparse multi-parser (parse multi-parser "2012/02/01"))
"2012-02-01"
```

`clj-time.core/today-at` returns a moment in time at the given hour, minute and second
on the current date:

``` clojure
=> (today-at 12 00)
#<DateTime 2013-03-29T12:00:00.000Z>
=> (today-at 12 00 05)
#<DateTime 2013-03-29T12:00:05.000Z>
```


### clj-time.coerce

The namespace `clj-time.coerce` contains utility functions for coercing Joda `DateTime` instances to and from various other types:

``` clj
=> (use 'clj-time.coerce)
```

For example, to convert a Joda `DateTime` to and from a Java `long`:

``` clj
=> (to-long (date-time 1998 4 25))
893462400000

=> (from-long 893462400000)
#<DateTime 1998-04-25T00:00:00.000Z>
```

And by the magic of protocols you can pass in an isoformat string and get the unix epoch milliseconds:

``` clj
=> (to-long "2013-08-01")
1375315200000
```

There are also conversions to and from `java.util.Date` (`to-date` and
`from-date`), `java.sql.Date` (`to-sql-date` and `from-sql-date`),
`java.sql.Timestamp` (`to-sql-time` and `from-sql-time`) and several other types.

### clj-time.local

The namespace `clj-time.local` contains functions for working with local time without having to shift to/from utc,
the preferred time zone of clj-time.core.

Get the current local time with

``` clj
=> (local-now)
```

Get a local date-time instance retaining the time fields with

``` clj
=> (to-local-date-time obj)
```

The following all return 1986-10-14 04:03:27.246 with the local time zone.

``` clj
(to-local-date-time (clj-time.core/date-time 1986 10 14 4 3 27 246))
(to-local-date-time "1986-10-14T04:03:27.246")
(to-local-date-time "1986-10-14T04:03:27.246Z")
```

The dynamic var \*local-formatters\* contains a map of local formatters for parsing and printing. It is initialized
with all the formatters in clj-time.format localized.

to-local-date-time for strings uses \*local-formatters\* to parse.

Format an obj using a formatter in \*local-formatters\* corresponding to the format-key passed in with

``` clj
=> (format-local-time (local-now) :basic-date-time)
```


### clj-time.periodic

`clj-time.periodic/periodic-seq` returns an infinite sequence of instants
separated by a time period starting with the given point in time:

``` clojure
(use 'clj-time.periodic)
(use 'clj.time.core)

;; returns 10 instants starting with current time separated
;; by 12 hours
(take 10 (periodic-seq (now) (hours 12)))
```


## Development

Running the tests:

    $ lein test-all

(assumes Leiningen 2.x)

## Documentation

The complete [API documentation](http://clj-time.github.com/clj-time/doc/index.html) is also available (codox generated).

## License

Released under the MIT License: <https://github.com/clj-time/clj-time/blob/master/MIT-LICENSE.txt>

