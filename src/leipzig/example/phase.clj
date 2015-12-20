(ns leipzig.example.phase
  (:require [overtone.core :as o]
            [leipzig.melody :refer [bpm all phrase then times where with tempo]]
            [leipzig.scale :as scale]
            [leipzig.canon :as canon]
            [leipzig.chord :as chord]
            [leipzig.live :as live]))

(o/definst foo
  [freq 330 vol 0.6 gate 1 dur 10000]
  (let [env (o/env-gen (o/adsr 0.05 0.2 0.3 0.1)
                       (* gate (o/line:kr 1 0 dur))
                       1 0 1 o/FREE)]
    (o/lpf (* vol env (o/saw freq)) 2200)))

(defmethod live/play-note :foo
  [{:keys [pitch duration]}]
  (when pitch
    (foo (o/midi->hz pitch) 0.3 1 duration)))

(def melody
  (->> (phrase [1 1 1   1 1 1  1 1 1   1  1  1 1   1 1  1  1 1 2]
               [0 7 nil 0 2 0 -2 0 nil 0 nil 0 nil 0 2 -5 -2 0 nil])
       (all :part :foo)
       (tempo (bpm 300))
       (where :pitch scale/C)))

(def melody'
  (->> (phrase [1 1 1   1 1 1  1 1 1   1  1  1 1   1 1  1  1 1 2]
               [0 7 nil 0 2 0 -2 0 nil 0 nil 0 nil 0 2 -5 -2 0 nil])
       (then (phrase [1 1 1   1 1 1  1 1 1   1  1  1 1   1 1  1  1 1 2]
                     [0 7 nil 0 2 0 -2 0 nil 0 nil 0 nil 0 2 -5 -2 0 nil]))
       (then (phrase [1 1 1   1 1 1  1 1 1   1  1  1 1   1 1  1  1 1 4]
                     [0 7 nil 0 2 0 -2 0 nil 0 nil 0 nil 0 2 -5 -2 0 nil]))
       (all :part :foo)
       (tempo (bpm 300))
       (where :pitch scale/C)))

(comment
  (live/jam [(var melody)
             (var melody')])
  (live/stop))
