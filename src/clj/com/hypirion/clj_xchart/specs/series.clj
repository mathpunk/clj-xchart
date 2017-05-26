(ns com.hypirion.clj-xchart.specs.series
  (:require [clojure.spec.alpha :as s]
            [com.hypirion.clj-xchart :as c]
            [com.hypirion.clj-xchart.specs.styling :as sty]))

(defn axis-counts-match?
  [{:keys [x y error-bars bubble]}]
  (and (= (count x)
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

(defmethod data-compatible-with-render-style? [:area]
  [series]
  (ordered? (second (:x series))))

(defn finite?
  [x]
  (not (or (Double/isInfinite x) (Double/isNaN x))))

(s/def ::series-name (s/and string? #(not (empty? %))))

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

(s/def ::style-base (s/keys :opt-un [::marker-color ::marker-type
                                     ::line-color ::line-style ::line-width
                                     ::fill-color ::show-in-legend?]))

(s/def ::line-width (s/and ::chartable-number pos?))
(s/def ::marker-color ::sty/color)
(s/def ::line-color ::sty/color)
(s/def ::fill-color ::sty/color)
(s/def ::show-in-legend? any?)   ;; truthish

;; these capture some ad-hoc naming differences between series styling top-level styling
(s/def ::marker-type ::sty/marker)
(s/def ::line-style ::sty/stroke)



