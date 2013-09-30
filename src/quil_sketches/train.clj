(ns quil-sketches.train
  (:use quil.core
        quil-sketches.util))

(def counter (atom 0))

(defn setup []
  (set-state! :bg-images (map load-image (map #(str "train" % ".jpg") (range 1 6))))
  (frame-rate 4)
  (smooth))

(defn randonee []
  (rand-nth [false true]))

(defn fill-or-not []
  (if (randonee)
    (apply fill (concat (hex-to-rgb "#C5E0DC") [128]))
    (no-fill)))

(defn draw-triangle [x y w up]
  (fill-or-not)
  (if up
    (triangle x y (+ x w) y (+ x (/ w 2)) (+ y (* w 0.8)))
    (triangle (+ (/ w 2) x) (+ y (* w 0.8)) (+ x w) y (+ x (* w 1.5)) (+ y (* w 0.8)))))

(defn draw []
  ;; tinted tracks
  (with-translation [0 -45]
    (scale 0.6)
    (image (nth (state :bg-images) (mod (frame-count) (count (state :bg-images)))) -150 -60))
  (apply tint (hex-to-rgb "#774F38"))

  (smooth)
  (no-fill)
  (stroke-weight 2)
  (apply stroke (concat (hex-to-rgb "#ECE5CE") [128]))

  (with-translation [205 80]
    (let [w 60 h (* w 0.8)]
      (draw-triangle 0 0 w (randonee))
      (draw-triangle (- (/ w 2)) h w (randonee))
      (draw-triangle (/ w 2) h w (randonee))
      (draw-triangle (- w) h w (randonee))
      (draw-triangle w h w (randonee))
      (scale 1 -1)
      (translate 0 (- (* 2 h)))
      (draw-triangle 0 0 w (randonee))
      (draw-triangle (- (/ w 2)) h w (randonee))
      (draw-triangle (/ w 2) h w (randonee))
      (draw-triangle (- w) h w (randonee))
      (draw-triangle w h w (randonee))))

  (display-filter :blur 0.9))

(defsketch train
  :title "on the tracks"
  :setup setup
  :draw draw
  :size [500 300]
  :renderer :p2d)

(sketch-stop train)
(sketch-start train)
