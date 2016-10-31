(defproject clj-time/clj-time "0.12.1"
  :description "A date and time library for Clojure, wrapping Joda Time."
  :url "https://github.com/clj-time/clj-time"
  :mailing-list {:name "clj-time mailing list"
                 :archive "https://groups.google.com/forum/?fromgroups#!forum/clj-time"
                 :post "clj-time@googlegroups.com"}
  :license {:name "MIT License"
            :url "http://www.opensource.org/licenses/mit-license.php"
            :distribution :repo}
  :dependencies [[joda-time "2.9.4"]
                 [org.clojure/clojure "1.8.0" :scope "provided"]]
  :min-lein-version "2.0.0"
  :global-vars {*warn-on-reflection* true}
  :profiles {:dev {:dependencies [[org.clojure/java.jdbc "0.6.1"]]
                   :plugins [[codox "0.8.10"]]}
             :midje {:dependencies [[midje "1.9.0-alpha5"]]
                     :plugins      [[lein-midje "3.2.1"]
                                    [midje-readme "1.0.9"]]
                     :midje-readme {:require "[clj-time.core :as t] [clj-time.predicates :as pr] [clj-time.format :as f] [clj-time.coerce :as c]"}}
             :1.6    {:dependencies [[org.clojure/clojure "1.6.0"]]}
             :1.7    {:dependencies [[org.clojure/clojure "1.7.0"]]}
             :master {:repositories [["snapshots" "https://oss.sonatype.org/content/repositories/snapshots/"]]
                      :dependencies [[org.clojure/clojure "1.9.0-master-SNAPSHOT"]]}}
  :aliases {"test-all" ["with-profile" "dev,master,default,midje:dev,default,midje:dev,1.6,midje:dev,1.7,midje" "test"]})
