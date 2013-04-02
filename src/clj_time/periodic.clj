(ns clj-time.periodic
  (:require [clj-time.internal.fn :as ifns]
            [clj-time.core :as ct])
  (:import [org.joda.time DateTime Period]))

(defn periodic-seq
  "Returns an infinite sequence of date-time values growing over specific period"
  [^DateTime start ^Period period]
  (iterate (ifns/fpartial ct/plus period) start))
