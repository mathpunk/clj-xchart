(ns com.hypirion.clj-xchart.specs.series.xy
  (:require [clojure.spec.alpha :as s]
            [com.hypirion.clj-xchart :as c]
            [com.hypirion.clj-xchart.specs.macros :refer (defkeyset-spec)]
            [com.hypirion.clj-xchart.specs.series :as series]
            [com.hypirion.clj-xchart.specs.styling.xy :as xy]))

(s/def ::render-style ::xy/render-style)
(s/def ::style (s/merge ::series/style-base (s/keys :opt-un [::render-style])))
(s/def ::series-elem (s/and (s/keys :req-un [::series/x ::series/y]
                                       :opt-un [::series/error-bars ::style])
                               series/axis-counts-match?
                               series/data-compatible-with-render-style?))
(s/def ::series (s/map-of ::series/series-name ::series-elem))
