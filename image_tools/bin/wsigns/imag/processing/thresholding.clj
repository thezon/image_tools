(ns wsigns.imag.processing.thresholding
  (:require [image.processing.thresholding :as th]
            [image.processing.support :as sup]
            [image.processing.base-image-operations :as bio]
            [image.processing.image-operations :as ops]))

(def im (sup/slurp-image))

(ops/eval-all-pixels im (th/simple-threshold (/ (bio/black-color) 2)))

(sup/show-image im)