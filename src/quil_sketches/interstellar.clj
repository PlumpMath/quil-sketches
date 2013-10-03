(ns quil-sketches.interstellar
  (:use quil.core
        quil-sketches.util))

(defn setup []
  (smooth)
  (no-fill)
  (stroke-weight 4)
  (color-mode :hsb 360 100 100 100)
  (let [ys (range 0 (/ (height) 2) 35)]
    (set-state! :img (load-image "data/cat-eye-nebula.jpg")
                :ys ys
                :max-y (apply max ys))))

(defn draw []
  (frame-rate 20)
  (with-rotation [(nth [(radians -0.08) (radians 0.08)] (mod (frame-count) 2))]
    (scale 1.2)
    (image (state :img) -48 -50)
    (tint 100 100 130 60))
  (doseq [y (map #(mod (+ % (frame-count)) (state :max-y)) (state :ys))]
    (let [x (- (/ (height) 2) y)]
      (stroke 200 (- (/ (height) 2) y) 130 60)
      (fill 300 (- (/ (height) 2) y) 130 8)
      (with-translation [(/ (width) 2) (/ (height) 2)]
        (ellipse 0 0 (- (width) (* 1.7 x)) (- (height) (* 1.7 y)))))))

(defsketch interstellar
  :title "cat's eye nebula"
  :setup setup
  :draw draw
  :renderer :p2d
  :size [500 500])
