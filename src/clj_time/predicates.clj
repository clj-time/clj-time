(ns clj-time.predicates
  "Predicate functions to ask basic questions about a date

  Was it Monday?
  (monday? (clj-time.core/date-time 1999 9 9))

  Is it January?
  (january? (clj-time.core/date-time 2011 1 1))"
  (:require [clj-time.core :as time]))

;; days of the week
(defn monday? [date-time]
  (= (time/day-of-week date-time) 1))

(defn tuesday? [date-time]
  (= (time/day-of-week date-time) 2))

(defn wednesday? [date-time]
  (= (time/day-of-week date-time) 3))

(defn thursday? [date-time]
  (= (time/day-of-week date-time) 4))

(defn friday? [date-time]
  (= (time/day-of-week date-time) 5))

(defn saturday? [date-time]
  (= (time/day-of-week date-time) 6))

(defn sunday? [date-time]
  (= (time/day-of-week date-time) 7))


;; months of the year
(defn january? [date-time]
  (= (time/month date-time) 1))

(defn february? [date-time]
  (= (time/month date-time) 2))

(defn march? [date-time]
  (= (time/month date-time) 3))

(defn april? [date-time]
  (= (time/month date-time) 4))

(defn may? [date-time]
  (= (time/month date-time) 5))

(defn june? [date-time]
  (= (time/month date-time) 6))

(defn july? [date-time]
  (= (time/month date-time) 7))

(defn august? [date-time]
  (= (time/month date-time) 8))

(defn september? [date-time]
  (= (time/month date-time) 9))

(defn october? [date-time]
  (= (time/month date-time) 10))

(defn november? [date-time]
  (= (time/month date-time) 11))

(defn december? [date-time]
  (= (time/month date-time) 12))
