(ns quil-sketches.triangles
  (:use quil.core
        quil-sketches.util))

(def palette [(hex-to-rgb "#F5F7DD")
              (hex-to-rgb "#454139")
              (hex-to-rgb "#7EBFC3")
              (hex-to-rgb "#F39131")])

(defn draw-triangle [x y up]
  (no-stroke)
  (apply fill (first palette))
  (if up
    (triangle x y (+ x 100) y (+ x 50) (+ y 80))
    (triangle (+ 50 x) (+ 80 y) (+ x 100) y (+ x 150) (+ y 80)))
  (apply fill (concat (rand-nth palette) [(+ (rand-int 66) 178)]))
  (if up
    (triangle x y (+ x 100) y (+ x 50) (+ y 80))
    (triangle (+ 50 x) (+ 80 y) (+ x 100) y (+ x 150) (+ y 80))))

(defn draw []
  (background eigengrau)
  (frame-rate 4)
  (push-matrix)
  (translate 105 130)
  (scale 0.33)
  (doseq [row (range 8 -1 -1)]
    (let [xs (range (* row 50) (+ (* 50 row) (* 100 (- 8 row))) 100)]
      (doseq [x xs]
        (draw-triangle x (* row 80) true))
      (doseq [x (butlast xs)]
        (draw-triangle x (* row 80) false))))
  (display-filter :blur 0.65)
  (display-filter :dilate)
  (display-filter :erode)
  (pop-matrix))

(defn setup []
  (frame-rate 4)
  (smooth)
  (no-stroke))

(defsketch triangular
  :title "Some fun with triangles."
  :setup setup
  :draw draw
  :size [500 500])
