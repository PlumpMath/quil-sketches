(ns quil-sketches.dotty
  (:use quil.core
        quil-sketches.util))

(def palette [(hex-to-rgb "##1E172D")
              (hex-to-rgb "#F83163")
              (hex-to-rgb "#01EADE")])

(defn setup []
  (color-mode :rgb)
  (apply background (first palette))
  (no-stroke)
  (smooth))

(def WIDTH 500)
(def HEIGHT 500)
(def dot-per-row 10)
(def increment (/ WIDTH dot-per-row))
(def dots (map (fn [y odd]
                 (let [start (if odd (- (/ increment 2)) increment)]
                   (for [x (range (int start) WIDTH increment)]
                      [x y (if odd (second palette) (nth palette 2)) odd])))
               (range (- (/  increment 2)) (* HEIGHT 1.3) (* 0.9 increment))
               (cycle [true false])))

(defn draw []
  (frame-rate 10)
  (apply background (first palette))
  (doseq [row (drop-last 3 dots)]
    (doseq [[x y c odd] row]
      (apply fill (concat c [200]))
      (let [max-r 35
            odd-r (* max-r (abs (sin (radians (* 18 (mod (frame-count) 20))))))
            even-r (- max-r odd-r)]
        (if odd
          (ellipse x y odd-r odd-r)
          (ellipse x y even-r even-r)))))
;  (save-frame)
  )

(defsketch dotty
  :title "dotty"
  :setup setup
  :draw draw
  :renderer :p2d
  :size [WIDTH HEIGHT])

