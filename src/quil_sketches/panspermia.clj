(ns quil-sketches.panspermia
  (:use quil.core
        quil-sketches.util))

(def palette [(hex-to-rgb "#7C7052")
              (hex-to-rgb "#494D4B")
              (hex-to-rgb "#B153C5")
              (hex-to-rgb "#375374")])

(defn setup []
  (smooth)
  (color-mode :rgb)
  (frame-rate 10)
  (no-fill))

(defn draw-spiral []
  (begin-shape :quad-strip)
  (doseq [i (range 100)]
    (apply stroke (concat (rand-nth palette) [120]))
    (stroke-weight (+ 2 (rand-int 8)))
    (doseq [a (range 36)]
      (let [r (- 10 (map-range i 0 100 0 10))
            j (+ 1 i)]
        (vertex (+ (* r (sin (radians (* a 1)))) (* 10 (sin (radians (* i 10)))))
                (- i)
                (+ (* r (cos (radians (* a 1)))) (* 10 (cos (radians (* i 10))))))
        (vertex (+ (* r (sin (radians (* a 1)))) (* 10 (sin (radians (* j 10)))))
                (- j)
                (+ (* r (cos (radians (* a 1)))) (* 10 (cos (radians (* j 10)))))))))
  (end-shape))

(defn draw []
  (background 0)

  (with-translation [-250 -54 0]
    (scale 1)
    (image (load-image "data/hale-bopp.jpg") (mod (frame-count) 2) 0)
    (tint 70))

  (lights)
  (stroke-cap :round)
  (with-translation [305 130 130]
    (rotate-y (- (radians 90)))
    (rotate-x (- (radians 110)))
    (let [roti (- (* TWO-PI (sin (frame-count))))]
      (rotate-y roti))
    (draw-spiral)
    (rotate-y (- (radians 180)))
    (draw-spiral))
  (display-filter :blur 0.8))

(defsketch panspermia
  :title "seeds in the wind"
  :setup setup
  :draw draw
  :size [500 300]
  :renderer :p3d)

(sketch-stop panspermia)
(sketch-start panspermia)
