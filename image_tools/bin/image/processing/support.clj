(ns image.processing.support
 (:refer-clojure :exclude [read])
  (:require [clojure.java.io :as io])
  (:import [java.awt Image]
           [java.awt.image RenderedImage BufferedImageOp BufferedImage ColorModel WritableRaster]
           [javax.imageio ImageIO ImageWriter ImageWriteParam IIOImage]
           [javax.imageio.stream FileImageOutputStream]
           [javax.swing JFrame JFileChooser JLabel ImageIcon]))


(defn slurp-image
  "Reads a BufferedImage from source"
  ([]
    (let [filechooser (JFileChooser. "C:/") 
         retval (.showOpenDialog filechooser nil) ]
    (if (= retval JFileChooser/APPROVE_OPTION)
      (slurp-image (.getSelectedFile filechooser)) "")))
  ([source]
  (ImageIO/read (io/file source))))

(defn spit-image [name ext image & opt-dir]
  "writes imaget to a file"
  (ImageIO/write image ext 
                 (java.io.File.
                   (if (empty? opt-dir)
                     name
                     (str (first opt-dir) name)))))
 
(defn show-image [image & opt]
  "displays images --opt not complete"
  (let [jf (new JFrame) 
        jl (JLabel. (new ImageIcon image))
        _ (. jf add jl)
        _ (. jf setSize 400 400)
        _ (. jf setVisible true)])nil)

                    
(defn create-rgb-image [width height]
  (BufferedImage. width height 
                  (. BufferedImage  TYPE_INT_RGB)))

(defn image-copy [^java.awt.image.BufferedImage image]
  (let [cm (. image getColorModel)
        ;is-alpha (. cm getColorModel)
        raster (. image copyData nil)]
    (BufferedImage. cm raster true nil)))

(defn sub-image [^java.awt.image.BufferedImage image map-loc width height]
  "creates a subimage of image at map-loc width height"
  (. image getSubimage (:x map-loc) (:y map-loc) width height))