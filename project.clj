(defproject clj-time/clj-time "0.4.6-SNAPSHOT"
  :description "A date and time library for Clojure, wrapping Joda Time."
  :url "https://github.com/seancorfield/clj-time"
  :mailing-list {:name "clj-time mailing list"
                 :archive "https://groups.google.com/forum/?fromgroups#!forum/clj-time"
                 :post "clj-time@googlegroups.com"}
  :license {:name "MIT License"
            :url "http://www.opensource.org/licenses/mit-license.php"
            :distribution :repo}
  :dependencies [[joda-time "2.1"] [org.clojure/clojure "1.4.0"]]
  :min-lein-version "2.0.0"
  :profiles {:dev {:dependencies [[utilize "0.1.2"]]
                   :plugins [[codox "0.6.1"]]}
             :1.2 {:dependencies [[org.clojure/clojure "1.2.1"]]}
             :1.3 {:dependencies [[org.clojure/clojure "1.3.0"]]}
             :1.5 {:dependencies [[org.clojure/clojure "1.5.1"]]}}
  :aliases {"test-all" ["with-profile" "dev,default:dev,1.2,default:dev,1.3,default:dev,1.5,default" "test"]})
