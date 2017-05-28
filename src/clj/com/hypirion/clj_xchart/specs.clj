(ns com.hypirion.clj-xchart.specs
  (:require [clojure.spec.alpha :as s]
            [com.hypirion.clj-xchart :as c]
            [com.hypirion.clj-xchart.specs.series :as series]
            [com.hypirion.clj-xchart.specs.series.bubble :as bubble]
            [com.hypirion.clj-xchart.specs.series.category :as category]
            [com.hypirion.clj-xchart.specs.series.xy :as xy]
            [com.hypirion.clj-xchart.specs.styling :as sty]))

(defn styling-matches-series?
  "Styling can only talk about as many series as there are series."
  [{:keys [series style]}]
  (<= (count (:series style))
      (count (:x series))))

;; without spec, this fails xchart validation on AWT thread
(defmulti series-compatible-with-render-style?
  "Is series data compatible with chart render style?"
  (fn [{:keys [series style]}] (:render-style style)))
(defmethod series-compatible-with-render-style? :default
  [_]
  true)
(defmethod series-compatible-with-render-style? [:area]
  [{:keys [series]}]
  (every?
   (fn [{:keys [x style]}]
     (or (:render-style style)
         (series/ordered? (second x))))
   (vals series)))

(s/def ::xy-chart-args (s/and (s/cat :series ::xy/series :style (s/? ::sty/xy-styling))
                              styling-matches-series?
                              series-compatible-with-render-style?))
(s/fdef c/xy-chart :args ::xy-chart-args)

(s/def ::bubble-chart*-args (s/and (s/cat :series ::bubble/series :style ::sty/bubble-styling)
                                   styling-matches-series?))

(s/def ::category-chart-args (s/and (s/cat :series ::category/series :style ::sty/category-styling)
                                    styling-matches-series?))
