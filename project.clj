(def java7? (.startsWith (System/getProperty "java.version") "1.7"))

(defproject clj-time/clj-time "0.15.3-SNAPSHOT"
  :description "A date and time library for Clojure, wrapping Joda Time."
  :url "https://github.com/clj-time/clj-time"
  :mailing-list {:name "clj-time mailing list"
                 :archive "https://groups.google.com/forum/?fromgroups#!forum/clj-time"
                 :post "clj-time@googlegroups.com"}
  :license {:name "MIT License"
            :url "http://www.opensource.org/licenses/mit-license.php"
            :distribution :repo}
  :dependencies [[joda-time "2.10"]
                 [org.clojure/clojure "1.10.0" :scope "provided"]]
  :min-lein-version "2.0.0"
  :global-vars {*warn-on-reflection* true}
  :profiles {:dev {:dependencies [[org.clojure/java.jdbc "0.7.9"]
                                  [seancorfield/next.jdbc "1.1.588"]]
                   :plugins [[codox "0.10.6"]]}
             :midje {:dependencies [[midje "1.9.8"]]
                     :plugins      [[lein-midje "3.2.1"]
                                    [midje-readme "1.0.9"]]
                     :midje-readme {:require "[clj-time.core :as t] [clj-time.predicates :as pr] [clj-time.format :as f] [clj-time.coerce :as c]"}}
             :1.7    {:dependencies [[org.clojure/clojure "1.7.0"]]}
             :1.8    {:dependencies [[org.clojure/clojure "1.8.0"]]}
             :1.9    {:dependencies [[org.clojure/clojure "1.9.0"]
                                     [org.clojure/test.check "0.10.0-alpha4"]]
                      :test-paths ["test" "test_spec"]}
             :1.10   {:dependencies [[org.clojure/clojure "1.10.0"]
                                     [org.clojure/test.check "0.10.0-alpha4"]]
                      :test-paths ["test" "test_spec"]}
             :master {:repositories [["snapshots" "https://oss.sonatype.org/content/repositories/snapshots/"]]
                      :dependencies [[org.clojure/clojure "1.11.0-master-SNAPSHOT"]
                                     [org.clojure/test.check "0.10.0-alpha4"]]
                      :test-paths ["test" "test_spec"]}}

  :aliases {"test-all" ["with-profile"
                        ~(str (when-not java7?
                                ;; 1.10+ requires Java 8+
                                (str "dev,master,midje:" ; 1.11 with spec
                                     "dev,1.10,midje:" ; 1.10 with spec
                                     "dev,default,midje:")) ; 1.10 without spec
                              "dev,1.9,midje:" ; 1.9 with spec
                              "dev,1.8,midje:" ; 1.8 is supported too
                              "dev,1.7,midje") ; 1.7 is earliest we support
                        "test"]})
