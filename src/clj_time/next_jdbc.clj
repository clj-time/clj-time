(ns clj-time.next-jdbc
  "next.jdbc protocol extensions supporting DateTime coercion.

  To use in your project, just require the namespace:

    => (require 'clj-time.next-jdbc)
    nil

  Doing so will extend the protocols defined by next.jdbc, which will
  cause java.sql.Timestamp objects in JDBC result sets to be coerced to
  org.joda.time.DateTime objects, and vice versa where java.sql.Timestamp
  objects would be required by JDBC."
  (:require [clj-time.coerce :as tc]))

(extend-protocol next.jdbc.prepare/SettableParameter
  org.joda.time.DateTime
  (set-parameter [v ^PreparedStatement s i]
    (.setObject s i (tc/to-sql-time v))))

(extend-protocol next.jdbc.result-set/ReadableColumn
  java.sql.Timestamp
  (read-column-by-index [^org.postgresql.util.PGobject v _2 _3]
    (tc/from-sql-time v))
  java.sql.Date
  (read-column-by-index [^org.postgresql.util.PGobject v _2 _3]
    (tc/from-sql-date v))
  java.sql.Time
  (read-column-by-index [^org.postgresql.util.PGobject v _2 _3]
    (org.joda.time.DateTime. v)))
