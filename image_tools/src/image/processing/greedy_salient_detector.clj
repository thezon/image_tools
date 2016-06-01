(ns image.processing.greedy-salient-detector
 (:require [image.processing.base-image-operations :as bio]
           [image.processing.image-operations :as ops]))

(defn has-boundry-pxl? [vec-loc-map im-size]
  (not
    (empty?
      (filter 
        #(or (= 0 (:x %))
             (= 0 (:y %))
             (= (dec (:x im-size)) (:x %))
             (= (dec (:y im-size)) (:y %))) vec-loc-map))))

(defn update-sets [eval-point neib-points point-set]
  "updates uneval and eval sets with distinct locations"
  (let [uneval-set (:uneval point-set)
        eval-set   (:eval point-set)]
    {:uneval (clojure.set/difference  (apply conj uneval-set neib-points) eval-set #{eval-point})
     :eval   (conj eval-set eval-point)}))

(defn eval-neigh [image loc-map pxl-val]
  "Gets neibhoors of loc-map that have value pxl-val"
  (into #{} 
        (filter #(not (nil? %))
                (map #(if (= pxl-val (bio/pixel-rgb image %)) %)
                     (ops/neighbors-loc loc-map (bio/image-size image true))))))

(defn greedy-salient-region [cur-pnt-map image & options-map]
  "gets contigous points of same value starting with cur-pnt
   options-map: :max-size max number of get neighbor itertions 
                :ref-color overide cur-pnt color"
  (let [max-size (if (:max-size (first options-map))
                   (:max-size (first options-map))
                   10000000 )
        px-col (if (:ref-color (first options-map))
                 (:ref-color (first options-map))
                 (bio/pixel-rgb image cur-pnt-map) )]
    (loop [point-set {:uneval #{cur-pnt-map} :eval #{}} i 0]
      (if (or (empty? (:uneval point-set)) (> i max-size))
        (:eval point-set)
        (let [curr-pnt (first (:uneval point-set))]
          (recur (update-sets curr-pnt (eval-neigh image curr-pnt px-col) point-set)
                 (inc i)))))))

(defn greedy-salient-regions [image]
  "Get all contigous regions in an image"
  (let [size (bio/image-size image true)
        x (dec (:x size))
        y (dec (:y size))
        num-pxl (* x y)]
    (loop [unproc-ptns (into #{} (ops/gen-loc-map {:x 0 :y 0} {:x x :y y})) salient []]
      (if (empty? unproc-ptns)
        salient
        (let [sr (greedy-salient-region (first unproc-ptns) image {:max-size num-pxl})]
          (recur (clojure.set/difference unproc-ptns sr) (conj salient sr)))))))

(defn grdy-detect-isolated-region [image min-px-size]
  "get all contigous regions in a region that are completely isolateda dn greater than min-px-size"
  (let [size (bio/image-size image true)
        large-regions (filter #(< min-px-size  (count %)) (greedy-salient-regions image))
        isolated (map 
                   (fn [sr] (if (not (has-boundry-pxl? sr size)) sr)) large-regions)] 
    (filter #(not (= 0 (count %))) isolated)))