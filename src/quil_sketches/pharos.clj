(ns quil-sketches.pharos
  (:use quil.core
        quil-sketches.util))

(def palette [(hex-to-rgb "#ECE5CE")
              (hex-to-rgb "#774F38")
              (hex-to-rgb "#E08E79")
              (hex-to-rgb "#F1D4AF")
              (hex-to-rgb "#C5E0DC")])

(def counter (atom 0))

(defn setup []
  (set-state! :bg-images (map load-image (map #(str "sky" % ".jpg") (range 1 6))))
  (frame-rate 4)
  (smooth))

(defn draw-triangle [x y w up]
  (if up
    (triangle x y (+ x w) y (+ x (/ w 2)) (+ y (* w 0.8)))
    (triangle (+ (/ w 2) x) (+ y (* w 0.8)) (+ x w) y (+ x (* w 1.5)) (+ y (* w 0.8)))))

(defn draw-vertices [x y w up]
  (if (= 0 (mod (swap! counter inc) 2))
    (apply fill (concat (last (rest palette)) [128]))
    (no-fill))
  (begin-shape)
  (let [z (- (+ 10 (mod @counter 15)))]
    (if up
      (do
        (vertex x y 10)
        (vertex (+ x w) y z)
        (vertex (+ x (/ w 2)) (+ y (* w 0.8)) z)
        (vertex x y 10))
      (do
        (vertex (+ (/ w 2) x) (+ y (* w 0.8)) z)
        (vertex (+ x w) y 10)
        (vertex (+ x (* w 1.5)) (+ y (* w 0.8)) 10)
        (vertex (+ (/ w 2) x) (+ y (* w 0.8)) z))))
  (end-shape))

(defn draw []
  (with-translation [-800 -430 -500]
    (scale 2.5)
    (image (nth (state :bg-images) (mod (frame-count) (count (state :bg-images)))) 0 0)
    (apply tint (hex-to-rgb "#E08E79")))

  (frame-rate 8)
  (with-translation [(- 256 (mod (frame-count) 20)) 90 -50]
    (rotate-y (- (radians (* 20 (frame-count)))))
    (stroke-weight 2)
    (apply stroke (concat (first palette) [168]))
    (let [w 50 h (* w 0.8)]
      (draw-vertices (- (/ w 2)) (* 3 h) w false)
      (doseq [i (range -2 1)]
        (draw-vertices (+ w (* i w)) (* 2 h) w true)
        (draw-vertices (+ (/ w 2) (* i w)) h w false))
      (doseq [i (range -1 1)]
        (draw-vertices (+ (/ w 2) (* i w)) (* 3 h) w true)
        (draw-vertices (* i w) (* 2 h) w false))
      (doseq [i (range -1 2)]
        (draw-vertices (* i w) 0 w true))
      (doseq [i (range -2 2)]
        (draw-vertices (* i w) 0 w false)
        (draw-vertices (+ (/ w 2) (* i w)) h w true))))

  (display-filter :blur 0.8))

(defsketch diamond
  :title "diamonds in the sky"
  :setup setup
  :draw draw
  :size [500 300]
  :renderer :p3d)

(sketch-stop diamond)
(sketch-start diamond)
