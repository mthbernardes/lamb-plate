(defproject {{raw-name}} "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
				 [log4j/log4j "1.2.17" :exclusions [javax.mail/mail
													javax.jms/jms
													com.sun.jdmk/jmxtools
													com.sun.jmx/jmxri]]
				 [org.clojure/tools.logging "0.3.1"]
				 [com.amazonaws/aws-lambda-java-core "1.1.0"]
                 [com.amazonaws/aws-lambda-java-log4j "1.0.0"]
                 [com.stuartsierra/component "0.3.2"]
                 [environ "1.1.0"]]
  :aot  [clojure.tools.logging.impl]
  :target-path "target/%s"
  :profiles {:dev {:source-paths ["src" "dev"]
                   :dependencies [[org.clojure/tools.namespace "0.2.11"]]}
             :uberjar {:aot :all}}
  :repl-options {:init-ns user})
