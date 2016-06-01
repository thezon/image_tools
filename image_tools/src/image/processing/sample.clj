(ns image.processing.sample
  (:require [image.processing.base-image-operations :as bio]
             [image.processing.support :as sup]))

(defn random-pixels [size-map sample-num]
  "returns a vector of random loc-maps of sample-num"
  (into []
        (map (fn [x y] {:x x :y y}) 
             (repeatedly sample-num #(rand-int (:x size-map))) 
             (repeatedly sample-num #(rand-int (:y size-map))))))

(defn count-segment-vec [sub-image-vec ref-value]
  "Counts number of pixels equal to ref-value" 
  (count
    (filter #(= ref-value %) sub-image-vec)))

(defn count-pxlval-image [image ref-value]
  "Counts number of pixels equal to ref-value" 
      (let [size (bio/image-size image true)]
        (apply +
          (flatten
            (map 
            (fn[x] 
              (map 
                (fn[y] 
                  (if 
                    (= ref-value (bio/pixel-rgb image {:x x :y y}))
                    1
                    0))
                (range 0 (:y size))))
            (range 0 (:x size)))))))


(defn image-box-split [image side]
  "splits image into square sub images-- Note truncates modulous at right and bottom"
  (flatten
    (let [size (bio/image-size image true)]
      (map (fn [x] 
             (map (fn [y] 
                    {:x x :y y
                     :subimage
                       (sup/sub-image image {:x x :y y} side side)}) 
                  (butlast (range 0 (:y size) side ))) )
                (butlast (range 0 (:x size) side))))))


(defn segment-threshold [image min-px-perc max-px-perc ref-color]
  "returns true if count of ref-color is in between min-px-perc max-px-perc"
  (let [size (bio/image-size image true)
        num-pxl (* (:x size) (:y size))
        min-px (* min-px-perc  num-pxl)
        max-px (* max-px-perc num-pxl)
        color-count (count-pxlval-image image ref-color)]
                (if (and (>  color-count min-px)
                         (<  color-count max-px))
                true
                false)))

(defn box-count-analysis [image box-size low-perc-thresh high-perc-thresh ref-color]
  "splits image into boxs of size box-size and sets :seg-thresh to true if ref-color count is between low-perc-thresh high-perc-thresh"
  (map 
    #(conj
       %
       {:seg-thresh (segment-threshold (:subimage %) low-perc-thresh high-perc-thresh ref-color)})
    (image-box-split image box-size)))


;--- holding for deletion
(comment
(defn wind-counts-backup [win-result num-pxl min-px-perc max-px-perc]
  
  "takes win-result map and augements :feature-count <True/False>
   if # segemented pixels is within threshold min-px-perc and max-px-perc"
  (let [
        min-px (* min-px-perc  num-pxl)
        max-px (* max-px-perc num-pxl)]
    (map 
      #(conj % 
             {:feature-1 
              (if (and (> (:count %) min-px)
                       (< (:count %) max-px))
                true
                false)}) win-result)))

(defn sliding-box-backup [image side]
  "Not complete should take the static calc as a function"
  (flatten
    (let [size (bio/image-size image true)]
      (map (fn [x] 
             (map (fn [y] 
                    {:x x :y y
                     :count
                     (count-segment 
                       (bio/sq-wind-rgb image {:x x :y y} side)
                       (bio/black-color))}) 
                  (butlast (range 0 (:y size) side ))) )
                (butlast (range 0 (:x size) side))))))

)