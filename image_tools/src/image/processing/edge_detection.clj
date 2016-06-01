(ns image.processing.edge-detection)

(defn find-edge-point [segmented-image start-loc-map]
  "In a binary image find the first point differnt for the start point"
  (let [current-value (bio/pixel-rgb segmented-image (:x start-loc-map) (:y start-loc-map))
        size-map (bio/image-size segmented-image true)]
    (loop [x  (:x start-loc-map)]
      (if (or (not= current-value (bio/pixel-rgb segmented-image x (:y start-loc-map)))
              (<= x 0)
              (>= x (dec (:x size-map))))
        {:x x :y (:y start-loc-map)}
        (recur (inc x))))))

(defn next-edge-pnt [cur-pnt-map image]
  (let [nb (ops/neighbors-loc cur-pnt-map (bio/image-size image))
        possNb (filter #(not= (:val %) (bio/red-color)) (map (fn [x] {:val (bio/pixel-rgb image x) :loc x}) nb))]possNb))