(ns image.processing.image-operations
  (:require [image.processing.base-image-operations :as bio]
            [image.processing.support :as sup]
            [image.processing.draw2d :as draw]))

"These are abstract operations that process the the full image"

(defn diff-images [^java.awt.image.BufferedImage image-one ^java.awt.image.BufferedImage image-two]
     (let [res-image (sup/image-copy image-one)]
       (dorun
         (map #(. res-image setRGB (:x %) (:y %) (- (. image-one getRGB (:x %) (:y %)) (. image-two getRGB (:x %) (:y %))))
              (for [x (range 0 (. image-one getWidth )) y (range 0 (. image-one getHeight))]
                {:x x :y y})))res-image))




(defn sliding-window [^java.awt.image.BufferedImage image window-func analyze-func side-length]
  "Divides into sub images using window-func in steps side-length returning results from analyze-func
   image:BufferedImage 
   window-func: function that creates image window with signature [image map-loc window-size]
   analyze-func: evaluates a window with sig int[] and returns result value
   side-length: reference size of window taken as input to window-func"
  (let [size (bio/image-size image true)
        x-times (int (Math/floor (/ (:x size) side-length)))
        y-times (int (Math/floor (/ (:y size) side-length)))]
    (loop [x 0 wnds []]
      (if (< x x-times)
        wnds
        (recur (inc x) 
               (conj wnds
                     (loop [y 0 val-y []]
                       (if (< y y-times)
                         vals
                         (recur (inc y) 
                                (conj val-y 
                                      (analyze-func
                                        (window-func image {:x x :y y} side-length ))))))))))))

(defn eval-all-pixels [image eval-func]
  "Modifies image directly using eval-func"
  (let [size (bio/image-size image)]
    (loop [x 0]
      (if (< x (second size))
        (do
          (loop [y 0]
            (if (< y (first size))
              (recur (do (bio/update-pixel-rgb image (eval-func image x y) x y) 
                       (inc y)))))
          (recur (inc x)))))))

(defn neighbors-loc [loc image-size]
  "Immediate neighbors --- not Complete needs boundries and remove focus"
    (filter #(and (< (:x %) (:x image-size)) (< (:y %) (:y image-size))
                 (>= (:x %) 0) (>= (:y %) 0))
    (for [x (range (dec (:x loc)) (+ 2 (:x loc)))
          y (range (dec (:y loc)) (+ 2 (:y loc)))]
      {:x x :y y})))

(defn gen-loc-map [start-pnt-map end-pnt-map]
  "gets all locations points inclusive between start-pnt-map end-pnt-map"
  (doall
    (flatten 
      (map 
        (fn [x] (map (fn [y] {:x x :y y}) 
                     (range (:y start-pnt-map) (:y end-pnt-map)))) 
        (range (:x start-pnt-map) (:x end-pnt-map))))))

(defn highlight-feature [image result size feature-key]
  (dorun
    (map #(if (feature-key %) 
            (draw/highlight-red-box image % size)) result)))
