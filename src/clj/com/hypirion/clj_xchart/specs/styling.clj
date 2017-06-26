(ns com.hypirion.clj-xchart.specs.styling
  (:require [clojure.spec.alpha :as s]
            [com.hypirion.clj-xchart :as c]
            [com.hypirion.clj-xchart.specs.macros :refer (defkeyset-spec)]
            [com.hypirion.clj-xchart.specs.styling.bubble :as bubble]
            [com.hypirion.clj-xchart.specs.styling.category :as category]
            [com.hypirion.clj-xchart.specs.styling.xy :as xy])
  (:import [java.awt Color Font Stroke]
           [org.knowm.xchart.style Theme]
           [org.knowm.xchart.style.markers Marker]))

(defn theme?
  [x]
  (instance? Theme x))

(defn font?
  [x]
  (instance? Font x))

(defn color?
  [x]
  (instance? Color x))

(defn marker?
  [x]
  (instance? Marker x))

(defn stroke?
  [x]
  (instance? Stroke x))

(s/def ::styling-base (s/keys :opt-un [::width
                                       ::height
                                       :com.hypirion.clj-xchart.specs.chart/title
                                       ::theme
                                       ::chart
                                       ::legend
                                       ::plot
                                       ::series]))
(s/def ::xy-styling (s/merge ::styling-base (s/keys :opt-un [::xy/render-style])))
(s/def ::category-styling (s/merge ::styling-base (s/keys :opt-un [::category/render-style])))
(s/def ::bubble-styling (s/merge ::styling-base (s/keys :opt-un [::bubble/render-style])))
(s/def ::chart (s/keys :opt-un [::background-color
                                ::font-color
                                ::padding
                                ::title]))
(s/def ::title (s/keys :opt-un [::box
                                ::font
                                ::padding
                                ::visible?]))
(s/def ::box (s/keys :opt-un [::background-color
                              ::border-color
                              ::visible?]))
(s/def ::legend (s/keys :opt-un [::background-color
                                 ::border-color
                                 ::font
                                 ::padding
                                 ::position
                                 ::series-line-length
                                 ::visible?]))
(s/def ::plot (s/keys :opt-un [::background-color
                               ::border-color
                               ::border-visible?
                               ::content-size]))
(s/def ::series (s/coll-of (s/keys :opt-un [::color ::stroke ::marker])))
(s/def ::nat-integer (s/and nat-int? #(<= % Integer/MAX_VALUE)))
(s/def ::pos-integer (s/and pos-int? #(<= % Integer/MAX_VALUE)))
(s/def ::width ::pos-integer)
(s/def ::height ::pos-integer)
(s/def :com.hypirion.clj-xchart.specs.chart/title string?)

(defkeyset-spec ::builtin-theme c/themes)
(s/def ::theme (s/or :builtin ::builtin-theme
                     :custom theme?))

(defkeyset-spec ::builtin-color c/colors)
(s/def ::color (s/or :builtin ::builtin-color
                     :custom color?))
(s/def ::background-color ::color)
(s/def ::font-color ::color)
(s/def ::padding ::nat-integer)
(s/def ::border-color ::color)
(s/def ::border-visible? boolean?)
(s/def ::content-size (s/double-in :min 0.0 :max 1.0))
(s/def ::visible? boolean?)
(s/def ::legend-padding ::padding)
(defkeyset-spec ::builtin-stroke c/strokes)
(s/def ::stroke (s/or :builtin ::builtin-stroke
                      :custom stroke?))
(defkeyset-spec ::builtin-marker c/markers)
(defkeyset-spec ::position c/legend-positions)
(s/def ::series-line-length pos-int?)

(s/def ::marker (s/or :builtin ::builtin-marker
                      :custom marker?))
(s/def ::font font?)


