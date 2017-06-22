(ns leiningen.new.lamb-plate
  "Generate an AWS Lambda Clojure project."
  (:require [leiningen.new.templates :refer [renderer year date project-name
                                             ->files sanitize-ns name-to-path
                                             multi-segment]]
            [leiningen.core.main :as main]))

(defn lamb-plate
  "A general project for creating AWS Lambda functions with Clojure.
  Accepts a group id in the project name: `lein new lamb-plate foo.bar/baz`"
  [name]
  (let [render (renderer "lamb-plate")
        main-ns (multi-segment (sanitize-ns name))
        data {:raw-name name
              :name (project-name name)
              :namespace main-ns
              :nested-dirs (name-to-path name)
              :year (year)
              :date (date)}]
    (main/info "Generating a project called" name "based on the 'lamb-plate' template.")
    (main/info "This template is intended for AWS Lambda projects.")
    (->files data
             ["project.clj" (render "project.clj" data)]
             ["README.md" (render "README.md" data)]
             ["doc/intro.md" (render "intro.md" data)]
             [".gitignore" (render "gitignore" data)]
             [".hgignore" (render "hgignore" data)]
             ["src/{{nested-dirs}}/core.clj" (render "core.clj" data)]
             ["src/{{nested-dirs}}/aws.clj" (render "aws.clj" data)]
             ["src/{{nested-dirs}}/handler.clj" (render "handler.clj" data)]
             ["test/{{nested-dirs}}/core_test.clj" (render "test.clj" data)]
             ["dev/user.clj" (render "user.clj" data)]
             ["LICENSE" (render "LICENSE" data)]
             ["CHANGELOG.md" (render "CHANGELOG.md" data)]
             ["resources/log4j.properties" (render "log4j.properties" data)])))
