(ns com.hypirion.clj-xchart.specs.macros
  (:require [clojure.spec.alpha :as s]))

(defmacro defkeyset-spec
  "Make a spec named k that allows the keys of m, a map resolvable
at compile time."
  [k m]
  (let [s (into #{} (keys @(resolve m)))]
    `(s/def ~k ~s)))
