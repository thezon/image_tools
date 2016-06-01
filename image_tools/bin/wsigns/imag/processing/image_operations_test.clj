(ns wsigns.imag.processing.image-operations-test
  (:require [image.processing.image-operations :as ops]))

(ops/gen-loc-map  {:x 10 :y 10}{:x 15 :y 15})

;boudary case to large
(neighbors-loc {:x 0 :y 0} {:x 11 :y 11} )
;boundary case to small
(neighbors-loc {:x 10 :y 10} {:x 11 :y 11} )