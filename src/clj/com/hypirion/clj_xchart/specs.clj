(ns com.hypirion.clj-xchart.specs
  (:require [clojure.spec.alpha :as s]
            [com.hypirion.clj-xchart :as c]
            [com.hypirion.clj-xchart.specs.series :as series]
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
     (or (series/ordered? (second x))
         (:render-style style)))
   (vals series)))

(defn bubble-render-style?
  [{:keys [style]}]
  (#{:bubble nil} (-> style :render-style first)))

(defn category-render-style?
  [{:keys [style]}]
  (#{:category nil} (-> style :render-style first)))

(s/def ::xy-chart-args (s/and (s/cat :series ::series/xy-series :style ::sty/xy-styling)
                              styling-matches-series?
                              series-compatible-with-render-style?))
(s/fdef c/xy-chart :args ::xy-chart-args)

(s/def ::bubble-chart*-args (s/and (s/cat :series ::series/bubble-series :style ::sty/styling)
                                   styling-matches-series?
                                   bubble-render-style?))

(s/def ::category-chart-args (s/and (s/cat :series ::series/category-series :style ::sty/styling)
                                    styling-matches-series?
                                    category-render-style?))
