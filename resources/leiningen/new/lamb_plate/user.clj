(ns user
  (:require [{{name}}.core :as core]
            [{{name}}.handler :as handler]
            [clojure.tools.namespace.repl :refer  (refresh refresh-all)]
            [com.stuartsierra.component :as component])
  (:import (com.amazonaws.services.lambda.runtime Context)))

(def system nil)

(defn- stub-context
  "Creates a mock Context object useful for testing locally."
  []
  (reify Context
    (getLogStreamName [this] "{{name}}-log-stream")
    (getLogGroupName [this] "{{name}}-log-group")))

(defn- event-fixture
  "Creates a java.util.LinkedHashMap to represent an AWS Lambda
  event map."
  []
  (doto (java.util.LinkedHashMap.)
    (.put "name" "Lambda")))

(defn- init
  []
  ; Create your stub event map here
  (let [event (event-fixture)
        ctx (stub-context)]
    (alter-var-root #'system (constantly 
                               (core/system event ctx)))))

(defn- start
  "Starts the system"
  []
  (alter-var-root #'system component/start))

(defn- stop
  "Stops the system"
  []
  (alter-var-root #'system
                  (fn [s] (when s (component/stop s)))))

(defn- go
  []
  (init)
  (start))

(defn reset
  "Stops the system, refreshses all namespaces in dependency order, and
  then starts a fresh system."
  []
  (stop)
  (refresh :after `user/go))

(defn execute
  "Executes the handler function on the current system."
  []
  (handler/execute (:handler system)))
