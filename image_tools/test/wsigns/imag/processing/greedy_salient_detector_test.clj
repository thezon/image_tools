(ns wsigns.imag.processing.greedy-salient-detector-test
  (:require [image.processing.greedy-salient-detector :as gsd]))

;need to create image then run with expected results

(gsd/update-sets {:x 10 :y 10} (gsd/eval-neigh i-s {:x 100 :y 100} (bio/black-color)) {:uneval #{} :eval #{}})
(count (greedy-salient-region {:x 200 :y 300} i-s {:max-size 200}))
(bio/update-pixels-rgb i-s (into [] (greedy-salient-region {:x 765 :y 520} i-s {:max-size 2000})) (bio/red-color))