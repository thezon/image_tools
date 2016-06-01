(ns image.processing.base-image-operations
  (:import [java.awt Color]))

(defn image-size [^java.awt.image.BufferedImage image & map?]
  (if map?
    {:x (. image getWidth) :y (. image getHeight)}
    [(. image getHeight) (. image getWidth)]))

(defn pixel-rgb 
  ([^java.awt.image.BufferedImage image x-loc y-loc]
    (. image getRGB x-loc y-loc))
  ([^java.awt.image.BufferedImage image loc-map]
    (. image getRGB (:x loc-map) (:y loc-map))))

(defn sq-wind-rgb [^java.awt.image.BufferedImage image loc-map side-length]
  "Returns square of size side-length with reference point loc-map"
   (vec (. image getRGB (:x loc-map) (:y loc-map) side-length side-length nil 0 side-length)))

(defn update-pixel-rgb 
  "Image is mutable and changed directly"
  ([^java.awt.image.BufferedImage image rgb x-loc y-loc]
    (. image setRGB x-loc y-loc rgb))
  ([^java.awt.image.BufferedImage image red green blue x-loc y-loc]
    (. image setRGB x-loc y-loc (. (Color. red green blue) getRGB))))

(defn white-color []
  (. (Color. 255 255 255) getRGB))

(defn black-color []
  (. (Color. 0 0 0) getRGB))

(defn red-color []
  (. (Color. 255 0 0) getRGB))

(defn update-pixels-rgb 
  ([image vec-pix-maps]
    "updats several pixels
   vec-pix-maps: [{:x ? :y ? :val ?} ... ]"
    (do
      (dorun
        (map #(update-pixel-rgb image (:val %) (:x %) (:y %)) vec-pix-maps)) nil))
  
  ([image vec-map-loc color]
    (try
      (do
        (dorun
          (map #(update-pixel-rgb image color (:x %) (:y %)) vec-map-loc)) nil)
      (catch Exception e (str "Error in update pixels: " e )))))
