(ns image.processing.draw2d
 (:import [java.awt Graphics2D Color]))

(defn highlight-rect [image map-loc width height color]
  (let [ic (. image createGraphics)
        _ (. ic setColor color)]
  (. ic drawRect (:x map-loc) (:y map-loc) width height)))

(defn highlight-red-box [image map-loc size]
  (highlight-rect image map-loc size size (Color. 255 0 0)))