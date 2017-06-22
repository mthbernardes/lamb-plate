(ns {{namespace}}
  (:require [clojure.tools.logging :as log]
            [clojure.walk :as walk]
            [com.stuartsierra.component :as component]
            [environ.core :refer  [env]]
            [{{name}}.aws :as aws]
            [{{name}}.handler :as handler])
  (:import (com.amazonaws.services.lambda.runtime Context))
  (:gen-class
   :name {{namespace}}.LambdaHandler
   :methods [[handle [Object com.amazonaws.services.lambda.runtime.Context] String]]))

(defn linkedhashmap->map
  "Converts an event instance (java.util.LinkedHashMap) into a standard
  Clojure map, converting keys to keywords."
  [event]
  (-> (into {} event)
      walk/keywordize-keys))

(defn system
  [event ctx]
  (let [evt (linkedhashmap->map event)]
    (component/system-map
      :aws (aws/aws-component evt ctx)
      :handler (component/using
                 (handler/handler-component)
                 [:aws]))))

(defn -handle
  "The main entry point to the Lambda function. event is expected to be an instance
  of type java.util.LinkedHashMap, and ctx is of type Context.
  
  For more details, you can find the Labmda handler function documentation for Java at: 
  http://docs.aws.amazon.com/lambda/latest/dg/java-programming-model-handler-types.html"
  [this event ctx]
  (log/info (format "Invoking handle(), LogStreamName: %s, LogGroupName: %s" 
                    (.getLogStreamName ctx) (.getLogGroupName ctx)))
  (-> (system event ctx)
      (component/start)
      :handler
      handler/execute))
