(ns wsigns.core
    (:require [image.processing.thresholding :as th]
              [image.processing.support :as sup]
              [image.processing.base-image-operations :as bio]
              [image.processing.image-operations :as ops]
              [image.processing.draw2d :as draw]
              [image.processing.sample :as sample]
              [mikera.image.colours :as color]
              [mikera.image.core :as mik]
              [mikera.image.filters :as filter]
              [mikera.image.spectrum :as spec]
              [image.processing.TAA :as TAA]))

(def i (sup/slurp-image))
(def i-g ((filter/grayscale) i))
(def i-s (sup/image-copy i-g))
(def i-b (sup/image-copy i))

(ops/eval-all-pixels i-s (th/threshold-pxl 
                           (/ (bio/black-color) 3) (/ (bio/black-color) 2) 
                           (bio/black-color) 
                           (bio/white-color)))

(def result (sample/box-count-analysis  i-s 60 0.1 0.99 (bio/black-color)))
(ops/highlight-feature i-b result 60 :seg-thresh)

(sup/show-image i)
(sup/show-image i-s)
(sup/show-image i-b)
(sup/show-image (ops/diff-images ((filter/box-blur 10 10) i-s) ((filter/box-blur 7 7) i-s)))


(let [size {:x 300 :y 300}
         rs-i ((filter/grayscale) (sup/sub-image i {:x 400 :y 300} 300 300))
         r-i (sup/create-rgb-image 300 300)
         _ (bio/update-pixels-rgb r-i (TAA/TAA-thresholding rs-i  100000 50 5)  (bio/red-color))
         _ (sup/show-image r-i)
         _ (sup/show-image (ops/diff-images   rs-i ((filter/box-blur 5 5) rs-i)))])