(ns image.processing.thresholding
  (:require [image.processing.base-image-operations :as bio]
            [image.processing.image-operations :as ops]))

; threshold is defined as binary diachotomy of image based on a value

(defn simple-threshold [threshold]
  "creates a function used for thresholding image"
  (fn [image x y] 
    (if (< threshold (bio/pixel-rgb image x y))
      (bio/white-color)
      (bio/black-color))))

(defn threshold-pxl 
  ([threshold greater-color less-color] 
    "creates a function used for a binary thresholding of image
     threshold: boundary value to be diachotomized
     greater-color: greater than threshold color
     less-color: less than threshold color"
    (threshold-pxl (bio/white-color) threshold less-color greater-color))
  
  ([thresh-low thresh-high color-out color-in] 
    "creates a function used for bandpass thresholding of image
     threshold-low: lower boundary of threshold region
     threshold-high: higher boundary of threshold region
     color-out: color outside of threshold region
     color-in: color inside of threshold region"
    (fn [image x y] 
      (let [pv (bio/pixel-rgb image x y)]
        (if (and (< thresh-high pv)
                 (> thresh-low pv))
          color-in
          color-out)))))

(defn simple-thresold-image [image]
  "thresholds image by half black value"
  (ops/eval-all-pixels image (threshold-pxl (/ (bio/black-color) 2) (bio/black-color)(bio/white-color))))
                       
                       ;(simple-threshold (/ (bio/black-color) 2))))