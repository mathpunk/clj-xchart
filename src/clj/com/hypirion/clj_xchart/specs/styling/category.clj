(ns com.hypirion.clj-xchart.specs.styling.category
  (:require [clojure.spec.alpha :as s]
            [com.hypirion.clj-xchart :as c]
            [com.hypirion.clj-xchart.specs.macros :refer (defkeyset-spec)]))

(defkeyset-spec ::render-style c/category-render-styles)
