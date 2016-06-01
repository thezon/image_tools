(ns image.processing.exp.image-operations
  (:require [image.processing.thresholding :as th]
              [image.processing.support :as sup]
              [image.processing.base-image-operations :as bio]
              [image.processing.image-operations :as ops]
              [image.processing.draw2d :as draw]
              [image.processing.sample :as sample]
              [mikera.image.colours :as color]
              [mikera.image.core :as mik]
              [mikera.image.filters :as filter]
              [mikera.image.spectrum :as spec]))


(defn diff-example []
  "example of difference"
  (let [image (sup/slurp-image)]
    (sup/show-image 
      (ops/diff-images ((filter/box-blur 3 3) image)((filter/box-blur 10 10) image)))))