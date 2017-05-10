(ns {{namespace}}-test
  (:require [clojure.test :refer :all]
            [{{namespace}} :refer :all]))

(deftest linkedhashmap->map-test
  (testing "LinkedHashMap to Clojure map conversion"
    (let [lhm (doto (java.util.LinkedHashMap.)
                (.put "a" 1)
                (.put "b" 2)
                (.put "c" 3))
          expected-map {:a 1, :b 2, :c 3}]
      (is (= expected-map (linkedhashmap->map lhm))))))
