(ns {{name}}.aws
  (:require [com.stuartsierra.component :as component]
			[clojure.tools.logging :as log]))

(defrecord AWS [event ctx]
  component/Lifecycle

  (start [{:keys [event ctx] :as this}]
	(log/info (format "Starting the AWS component\nEvent: %s\nContext: %s" event ctx))
	this)

  (stop [this]
	(log/info "Stopping the AWS component")
	this))

(defn aws-component
  [evt ctx]
  (map->AWS {:event evt
			 :ctx ctx}))
