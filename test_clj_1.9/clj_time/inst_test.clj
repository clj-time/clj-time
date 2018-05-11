(ns clj-time.inst-test
  (:refer-clojure :exclude [extend second])
  (:require [clojure.test :refer :all]
            [clj-time.core :refer :all])
  (:import org.joda.time.DateTime))

(deftest test-inst
  (let [^DateTime n (now)]
    (is (inst? n))
    (is (= (inst-ms n) (.getMillis n)))))
