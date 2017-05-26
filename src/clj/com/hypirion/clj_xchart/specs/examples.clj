(ns com.hypirion.clj-xchart.specs.examples
  (:require [clojure.spec.alpha :as s]
            [clojure.spec.gen.alpha :as gen]
            [com.hypirion.clj-xchart :as c]
            [com.hypirion.clj-xchart.specs :as specs]
            [com.hypirion.clj-xchart.specs.series :as series]
            [com.hypirion.clj-xchart.specs.styling :as sty])
  (:import [java.awt Color Font]
           [org.knowm.xchart.style Theme]
           [org.knowm.xchart.style.markers Marker]))

(def example-font-set (into #{} (for [style [Font/PLAIN Font/BOLD Font/ITALIC]
                          size [10 12 14 16]
                          lfont ["Serif" "SansSerif" "Monospaced"]]
                                  (Font. lfont style size))))

(defn force-axis-counts
  [{:keys [x y error-bars bubble] :as series}]
  (let [ct (min (count x) (count y)
                (count (or error-bars x))
                (count (or bubble x)))]
    (-> series
        (update :x subvec 0 ct)
        (update :y subvec 0 ct)
        (update :error-bars (fn [x] (when x (subvec x 0 ct))))
        (update :bubble (fn [x] (when x (subvec x 0 ct)))))))

(defn force-style-counts
  [[series styling]]
  (if (< (count (:x series))
         (count (:series styling)))
    [series (update styling :series #(subvec % 0 (count (:x series))))]
    [series styling]))

(def example-series-names
  #{"Grommets" "Hit Points" "Expected" "Actual" "Emacs Users" "Vim Users" "Pirates" "Global Warming"})

(s/def ::xy-series-elem* (s/keys :req-un [::series/x ::series/y] :opt-un [::series/error-bars ::series/style]))
(s/def ::bubble-series-elem* (s/keys :req-un [::series/x ::series/y ::series/bubble] :opt-un [::series/style]))
(s/def ::category-series-elem* (s/keys :req-un [::series/x ::series/y] :opt-un [::series/style]))
;; generators need their subgenerators
(declare generators)

(def generators
  {:com.hypirion.clj-xchart.specs.chart/title
   #(s/gen (into #{} (map (fn [n] (str "Example Title " n))) (range 1 5)))
   ::series/chartable-number #(s/gen double?)
   ::series/series-name #(s/gen example-series-names)
   ::series/xy-series-elem #(->> (s/gen ::xy-series-elem* generators)
                                 (gen/fmap force-axis-counts))
   ::series/bubble-series-elem #(->> (s/gen ::bubble-series-elem* generators)
                                     (gen/fmap force-axis-counts))
   ::series/category-series-elem #(->> (s/gen ::category-series-elem* generators)
                                       (gen/fmap force-axis-counts))
   ::series/show-in-legend? #(s/gen boolean?)
   ::sty/width #(s/gen #{200 300 400 500})
   ::sty/height #(s/gen #{100 200 300})
   ::sty/color #(s/gen ::sty/builtin-color)
   ::sty/marker #(s/gen ::sty/builtin-marker)
   ::sty/stroke #(s/gen ::sty/builtin-stroke)
   ::sty/font #(s/gen example-font-set)
   ::sty/theme #(s/gen ::sty/builtin-theme)})
