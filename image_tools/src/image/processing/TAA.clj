(ns image.processing.TAA
  (:require [image.processing.base-image-operations :as bio]
            [image.processing.sample :as sample]))

(defn linear-path [image loc-map direction-map threshold thresh-compare max-path]
  
  (let [size-map (bio/image-size image true)]
    (loop [x (:x loc-map) y (:y loc-map) moves 0 path []]
      (if (or (>= moves max-path)
              (< x 0)
              (< y 0)
              (>= x (dec (:x size-map)))
              (>= y (dec (:y size-map)))
              (thresh-compare (bio/pixel-rgb image x y) threshold))
        path
        (recur (+ x (:x direction-map)) (+ y (:y direction-map)) (inc moves) (conj path {:x x :y y}))))))

(defn TAA-threshold-trails [image num-ants max-path]
  (map (fn [loc-map] 
         (let [x (:x loc-map)
               y (:y loc-map)]
           (linear-path image 
                        {:x x :y y} 
                        {:x (rand-nth [1 0 -1]) :y (rand-nth [1 0 -1])} 
                        (bio/pixel-rgb image {:x x  :y y}) > max-path)))
       (sample/random-pixels (bio/image-size image true) num-ants)))

(defn TAA-thresholding  [image num-ants max-path min-path]
  (filter 
    #(not (nil? %))
    (map #(if (and (< min-path (count %))
                   (>  max-path (count %)))
            (last %))
         (TAA-threshold-trails image num-ants max-path))))



;(TAA-thresholding i 4 4 2)