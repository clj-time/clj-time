;; common dependencies:
(def deps '[[joda-time "2.1"]])

;; project definition with additional dependencies:
(defproject clj-time "0.4.2-SNAPSHOT"
  :description "A date and time library for Clojure, wrapping Joda Time."
  :dev-dependencies [[utilize "0.1.2"]
                     [lein-multi "1.1.0"]]
  :repositories [["sonatype-snapshots" "https://oss.sonatype.org/content/repositories/snapshots/"]]
  :dependencies ~(conj deps '[org.clojure/clojure "1.3.0"])
  :multi-deps {"1.2"  ~(conj deps '[org.clojure/clojure "1.2.1"])
               "1.4"  ~(conj deps '[org.clojure/clojure "1.4.0"])
               "1.5S" ~(conj deps '[org.clojure/clojure "1.5.0-master-SNAPSHOT"])})
