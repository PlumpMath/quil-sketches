(ns quil-sketches.crossing
  (:use quil.core))

(defn setup []
  (set-state! :bg-images (reverse (map load-image
                                       (map #(str "crossing0" (format "%02d" %) ".jpeg")
                                            (range 6 19)))))
  (smooth)
  (frame-rate 4)
  (color-mode :hsb)
  (no-fill)
  (stroke-weight 8))

(defn draw []
  (let [phase (int (+ 6 (* 6 (sin (* TWO-PI 0.1 (mod (frame-count) 10))))))]
    (with-translation [-160 -80]
      (scale 0.6)
      (image (nth (state :bg-images) phase) 0 0)
      (tint 300 100 100))

    (with-translation [(/ (width) 2)
                       (+ (- 36 (* 3.4 phase)) (/ (height) 2))]
        (rotate phase)
        (scale (* 0.0017 (* phase phase)))
        (stroke 100 100 (+ 80 (* phase phase)))
        (begin-shape)
        (doseq [i (range 350)]
          (curve-vertex (* (sin (/ i 5.0)) (* 2 i))
                        (* (cos (/ i 5.0)) (* 2 i))))
        (end-shape))
    (display-filter :blur 0.8)))

(defsketch crossing
  :title "railway crossing"
  :setup setup
  :draw draw
  :size [500 300]
  :renderer :p2d)

(sketch-stop crossing)
(sketch-start crossing)
