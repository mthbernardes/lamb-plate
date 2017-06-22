(ns {{name}}.handler
  (:require [clojure.tools.logging :as log]
            [com.stuartsierra.component :as component]
            [clojure.walk :as walk]))

(defrecord HandlerComponent [aws]
  component/Lifecycle

  (start [this]
    (log/info "Starting the Handler component")
    this)

  (stop [this]
    (log/info "Stopping the Handler component")
    this))

(defn execute
  "Executes the handler."
  [{:keys [aws] :as this}]
  (let [n (get-in aws [:event :name] "Nobody")]
    (format "Hello, %s!" n)))

(defn handler-component
  []
  (map->HandlerComponent {}))
