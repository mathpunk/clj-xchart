(ns com.hypirion.clj-xchart.specs.series
  (:require [clojure.spec.alpha :as s]
            [com.hypirion.clj-xchart :as c]
            [com.hypirion.clj-xchart.specs.styling :as sty]))

(defn axis-counts-match?
  [{:keys [x y error-bars bubble]}]
  (and (= (count (second x)) ;; tricky: x is nested by conformance
          (count y)
          (count (or error-bars x))
          (count (or bubble x)))))

(defn ordered?
  [nums]
  (or (nil? (seq nums))
      (boolean (reduce
                (fn [x y] (if (pos? (compare x y))
                            (reduced false)
                            y))
                (first nums)
                nums))))

;; dev trick
#_(defn data-compatible-with-render-style? [])

(defmulti data-compatible-with-render-style? (fn [{:keys [x style]}]
                                              (:render-style style)))
(defmethod data-compatible-with-render-style? :default [series] true)

(defmethod data-compatible-with-render-style? [:xy :area]
  [series]
  (ordered? (second (:x series))))

(defn finite?
  [x]
  (not (or (Double/isInfinite x) (Double/isNaN x))))

(s/def ::series-name (s/and string? #(not (empty? %))))
(s/def ::xy-series-elem (s/and (s/keys :req-un [::x ::y]
                                       :opt-un [::error-bars ::style])
                               axis-counts-match?
                               data-compatible-with-render-style?))
(s/def ::xy-series (s/map-of ::series-name ::xy-series-elem))

(s/def ::bubble-series-elem (s/and (s/keys :req-un [::x ::y ::bubble]
                                       :opt-un [::style])
                               axis-counts-match?))
(s/def ::bubble-series (s/map-of ::series-name ::bubble-series-elem))
(s/def ::category-series-elem (s/and (s/keys :req-un [::x ::y]
                                             :opt-un [::style])
                                     axis-counts-match?))
(s/def ::category-series (s/map-of ::series-name ::category-series-elem))


(s/def ::chartable-number (s/and number? finite?))
;; TODO: turned off for now -- different axes cannot be mixed on same chart
(s/def ::x (s/or :numbers (s/every ::chartable-number :min-count 1)
                 #_:dates #_(s/every inst? :min-count 1)))
(s/def :com.hypirion.clj-xchart.specs.series.category/x
  (s/or :numbers (s/every ::chartable-number :min-count 1)
        #_:dates #_(s/every inst? :min-count 1)
        #_:strings #_(s/every string? :min-count 1)))
(s/def ::y (s/every ::chartable-number :min-count 1))
(s/def ::error-bars (s/every ::chartable-number :min-count 1))
(s/def ::bubble (s/every ::chartable-number :min-count 1))

(s/def ::style (s/keys :opt-un [::marker-color ::marker-type
                                ::line-color ::line-style ::line-width
                                ::fill-color ::show-in-legend? ::render-style]))

(s/def ::line-width (s/and ::chartable-number pos?))
(s/def ::marker-color ::sty/color)
(s/def ::line-color ::sty/color)
(s/def ::fill-color ::sty/color)
(s/def ::show-in-legend? any?)   ;; truthish
(s/def ::render-style ::sty/render-style)

;; these capture some ad-hoc naming differences between series styling top-level styling
(s/def ::marker-type ::sty/marker)
(s/def ::line-style ::sty/stroke)



