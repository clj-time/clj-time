[![Build Status](https://travis-ci.org/clj-time/clj-time.svg?branch=master)](https://travis-ci.org/clj-time/clj-time)
[![Dependencies Status](https://versions.deps.co/clj-time/clj-time/status.svg)](https://versions.deps.co/clj-time/clj-time)
[![Downloads](https://versions.deps.co/clj-time/clj-time/downloads.svg)](https://versions.deps.co/clj-time/clj-time)
[![Join the chat at https://gitter.im/clj-time/clj-time](https://badges.gitter.im/clj-time/clj-time.svg)](https://gitter.im/clj-time/clj-time?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

# clj-time

A date and time library for Clojure, wrapping the [Joda Time](http://www.joda.org/joda-time/) library. **The Joda Time website says:**

> Note that from Java SE 8 onwards, users are asked to migrate to java.time (JSR-310) - a core part of the JDK which replaces this project.

If you are using Java 8 or later, consider using the built-in Java Time instead of Joda Time -- and look at [clojure.java-time](https://github.com/dm3/clojure.java-time) if you want a Clojure wrapper for that. See [Converting from Joda Time to java.time](http://blog.joda.org/2014/11/converting-from-joda-time-to-javatime.html) for more details about the similarities and differences between the two libraries.

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

```
[clj-time "0.14.4"]
```

With Maven:

``` xml
<dependency>
  <groupId>clj-time</groupId>
  <artifactId>clj-time</artifactId>
  <version>0.14.4</version>
</dependency>
```

## Bugs and Enhancements

Please open issues against the [official clj-time repo on Github](https://github.com/clj-time/clj-time/issues). `clj-time` is a very thin wrapper around Joda Time. That means that if Joda Time has a "peculiar behavior", it's likely to be surfaced directly in `clj-time` as well. A good example of this is `clj-time.format/unparse` which simply calls Joda Time's `.print` method -- and if the date passed in happens to be `nil`, you silently get back the _current date/time_ (many people would expect an exception!).

## Mailing List

Please ask questions on the [clj-time mailing list](http://groups.google.com/forum/#!forum/clj-time).


## Usage

### clj-time.core

The main namespace for date-time operations in the `clj-time` library is `clj-time.core`.

``` clj
(require '[clj-time.core :as t])
```

Create a DateTime instance with date-time, specifying the year, month,
day, hour, minute, second, and millisecond:


``` clj
(t/date-time 1986 10 14 4 3 27 456)
=> #<DateTime 1986-10-14T04:03:27.456Z>
```

Less-significant fields can be omitted:

``` clj
(t/date-time 1986 10 14)
=> #<DateTime 1986-10-14T00:00:00.000Z>
```

Get the current time with `now` and the start of the Unix epoch with
`epoch`.

Once you have a date-time, use accessors like `hour` and `second` to
access the corresponding fields:


```clojure
(t/hour (t/date-time 1986 10 14 22))
=> 22
```

The date-time constructor always returns times in the UTC time
zone. If you want a time with the specified fields in a different time
zone, use `from-time-zone`:


``` clj
(t/from-time-zone (t/date-time 1986 10 22) (t/time-zone-for-offset -2))
=> #<DateTime 1986-10-22T00:00:00.000-02:00>
```

If on the other hand you want a given absolute instant in time in a
different time zone, use `to-time-zone`:


``` clj
(t/to-time-zone (t/date-time 1986 10 22) (t/time-zone-for-offset -2))
=> #<DateTime 1986-10-21T22:00:00.000-02:00>
```

In addition to `time-zone-for-offset`, you can use the
`time-zone-for-id` and `default-time-zone` functions and the `utc` Var
to construct or get `DateTimeZone` instances.


If you only want a date with no time component, consider using the
`local-date` and `today` functions. These return `LocalDate` instances
that do not have time components (and thus don't suffer from
timezone-related shifting).


``` clj
(t/local-date 2013 3 20)
=> #<LocalDate 2013-03-20>
```

The functions `equal?`, `after?`, and `before?` determine the relative position
of two DateTime instances:

```clojure
(t/equal? (t/date-time 1986 10) (t/date-time 1986 10))
=> true
(t/after? (t/date-time 1986 10) (t/date-time 1986 9))
=> true
(t/before? (t/date-time 1986 9) (t/date-time 1986 10))
=> true
```

Often you will want to find a date some amount of time from a given
date. For example, to find the time 1 month and 3 weeks from a given
date-time:


``` clj
(t/plus (t/date-time 1986 10 14) (t/months 1) (t/weeks 3))
=> #<DateTime 1986-12-05T00:00:00.000Z>
```

An `Interval` is used to represent the span of time between two
`DateTime` instances. Construct one using `interval`, then query them
using `within?`, `overlaps?`, and `abuts?`

```clojure
(t/within? (t/interval (t/date-time 1986) (t/date-time 1990))
              (t/date-time 1987))
=> true
```

The `in-seconds` and `in-minutes` functions can be used to describe
intervals in the corresponding temporal units:

```clojure
(t/in-minutes (t/interval (t/date-time 1986 10 2) (t/date-time 1986 10 14)))
=> 17280
```

The `overlap` function can be used to get an `Interval` representing the
overlap between two intervals:

``` clj
(t/overlap (t/interval (t/date-time 1986) (t/date-time 1990))
         (t/interval (t/date-time 1987) (t/date-time 1991)))
=> #<Interval 1987-01-01T00:00:00.000Z/1990-01-01T00:00:00.000Z>
```

`today-at` returns a moment in time at the given hour,
minute and second on the current date UTC; not the current system date:

``` clj
(t/today-at 12 00)
=> #<DateTime 2013-03-29T12:00:00.000Z>
(t/today-at 12 00 05)
=> #<DateTime 2013-03-29T12:00:05.000Z>

;; System clock says 11PM on 12/20/2016 UTC-5
(t/today-at 7 00 00)
=> #<DateTime 2016-12-21T7:00:00.000Z>
```

### clj-time.format

If you need to parse or print date-times, use `clj-time.format`:

``` clj
(require '[clj-time.format :as f])
```

Parsing and printing are controlled by formatters. You can either use
one of the built in ISO8601 formatters or define your own, e.g.:

```clojure
(def built-in-formatter (f/formatters :basic-date-time))
(def custom-formatter (f/formatter "yyyyMMdd"))
```

To see a list of available built-in formatters and an example of a
date-time printed in their format:


``` clj
(f/show-formatters)
```

Remember that `mm` is minutes, `MM` is months, `ss` is seconds and
`SS` is milliseconds. You can find a [complete list of patterns](http://www.joda.org/joda-time/key_format.html)
on the Joda Time website.

Once you have a formatter, parsing and printing are straightforward:

``` clj
(f/parse custom-formatter "20100311")
=> #<DateTime 2010-03-11T00:00:00.000Z>
```
```clojure
(f/unparse custom-formatter (t/date-time 2010 10 3))
=> "20101003"
```

To parse dates in multiple formats and format dates in just one
format, you can do this:


```clojure
(def multi-parser (f/formatter (t/default-time-zone) "YYYY-MM-dd" "YYYY/MM/dd"))

(f/unparse multi-parser (f/parse multi-parser "2012-02-01"))
=> "2012-02-01"

(f/unparse multi-parser (f/parse multi-parser "2012/02/01"))
=> "2012-02-01"
```

Note: Joda Time's `.print` method accepts a null date/time object and substitutes the current date/time, so `(f/unparse my-fmt nil)` will not throw an exception -- it will just silently return the current date/time!

### clj-time.coerce

The namespace `clj-time.coerce` contains utility functions for
coercing Joda `DateTime` instances to and from various other types:


``` clj
(require '[clj-time.coerce :as c])
```

For example, to convert a Joda `DateTime` to and from a Java `long`:

```clojure
(c/to-long (t/date-time 1998 4 25))
=> 893462400000
```
``` clj
(c/from-long 893462400000)
=> #<DateTime 1998-04-25T00:00:00.000Z>
```

And by the magic of protocols you can pass in an isoformat string and
get the unix epoch milliseconds:

```clojure
(c/to-long "2013-08-01")
=> 1375315200000
```

There are also conversions to and from `java.util.Date` (`to-date` and
`from-date`), `java.sql.Date` (`to-sql-date` and `from-sql-date`),
`java.sql.Timestamp` (`to-sql-time` and `from-sql-time`) and several
other types.

### clj-time.local

The namespace `clj-time.local` contains functions for working with
local time without having to shift to/from utc, the preferred time
zone of clj-time.core.

``` clj
(require '[clj-time.local :as l])
```

Get the current local time with

``` clj
(l/local-now)
```

Get a local date-time instance retaining the time fields with

``` clj
(l/to-local-date-time obj)
```

The following all return 1986-10-14 04:03:27.246 with the local time
zone.

``` clj
(l/to-local-date-time (clj-time.core/date-time 1986 10 14 4 3 27 246))
(l/to-local-date-time "1986-10-14T04:03:27.246")
(l/to-local-date-time "1986-10-14T04:03:27.246Z")
```

The dynamic var `*local-formatters*` contains a map of local
formatters for parsing and printing. It is initialized with all the
formatters in clj-time.format localized.

to-local-date-time for strings uses `*local-formatters*` to parse.

Format an obj using a formatter in `*local-formatters*` corresponding
to the format-key passed in with

``` clj
(l/format-local-time (l/local-now) :basic-date-time)
```


### clj-time.periodic

`clj-time.periodic/periodic-seq` returns an infinite sequence of instants
separated by a time period starting with the given point in time:

``` clj
(require '[clj-time.periodic :as p])
(require '[clj-time.core :as t])

;; returns 10 instants starting with current time separated
;; by 12 hours
(take 10 (p/periodic-seq (t/now) (t/hours 12)))
```

In particular, if you ask for a sequence of instants separated by a month, you will get dates where the month increases each time (rather than being, say, 30 days apart).

### clj-time.predicates

`clj-time.predicates` comes with a set of handy predicates to
check for common conditions. For instance:

``` clj
(require '[clj-time.core :as t])
(require '[clj-time.predicates :as pr])
```
``` clojure
(pr/monday? (t/date-time 1999 9 9))
=> false

(pr/january? (t/date-time 2011 1 1))
=> true

(pr/weekend? (t/date-time 2014 1 26))
=> true

(pr/weekday? (t/date-time 2014 1 26))
=> false

(pr/last-day-of-month? (t/date-time 2014 1 26))
=> false

(pr/first-day-of-month? (t/date-time 2014 1 26))
=> false
```

### clj-time.jdbc

`clj-time.jdbc` registers protocol extensions so you don’t have to use
`clj-time.coerce` yourself to coerce to and from SQL timestamps.

From the REPL:

``` clj
(require 'clj-time.jdbc)
```

In your project:

``` clj
(ns my.neat.project
  (:require [clj-time.jdbc]))

; They're registered and ready to use.
```

Now you can use `org.joda.time.DateTime` objects when "writing" to the database
in place of `java.sql.Timestamp` objects, and expect `org.joda.time.DateTime`
objects when "reading" where you would have previously expected
`java.sql.Timestamp` objects.

## Development

Running the tests:

    $ rm -f test/readme.clj && lein test-all && lein test-readme

(assumes Leiningen 2.x)

## Documentation

The complete [API documentation](http://clj-time.github.com/clj-time/doc/index.html) is also available (codox generated).

## License

Released under the MIT License: <https://github.com/clj-time/clj-time/blob/master/MIT-LICENSE.txt>
