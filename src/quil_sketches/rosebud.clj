(ns quil-sketches.rosebud
  (:use quil.core
        quil-sketches.util))

(def counter (atom 0))

(defn setup []
  (set-state! :bg-images (map load-image (map #(str "data/snow" % ".jpg") (range 1 6))))
  (frame-rate 4)
  (smooth))

(defn fill-or-not []
  (if (rand-nth [false true])
    (apply fill (concat (hex-to-rgb "#E08E79") [96]))
    (no-fill)))

(defn draw-triangle [x y w up]
  (fill-or-not)
  (if up
    (triangle x y (+ x w) y (+ x (/ w 2)) (+ y (* w 0.8)))
    (triangle (+ (/ w 2) x) (+ y (* w 0.8)) (+ x w) y (+ x (* w 1.5)) (+ y (* w 0.8)))))

(defn draw []
  ;; tinted snow
  (push-matrix)
  (scale 0.9)
  (image (nth (state :bg-images) (mod (frame-count) (count (state :bg-images)))) 0 -130)
  (apply tint (hex-to-rgb "#C5E0DC"))
  (pop-matrix)

  ;; rose
  (smooth)
  (no-fill)
  (stroke-weight 2)
  (apply stroke (hex-to-rgb "#ECE5CE"))

  (translate (/ (width) 2) (/ (height) 2))
  (with-rotation [(* 0.015 @counter)]
    (doseq [nest (range 8)]
      (swap! counter inc)
      (apply fill (concat (hex-to-rgb "#E08E79") [(+ (* 2 nest) 66)]))
      (let [sides  6
            sizes  [145 116 92 72 57 45 37 30]
            radius (nth sizes nest)]                  ;(* (inc nest) 29)
        (begin-shape)
        (doseq [i (range sides) ] ; [0](concat )
          (rotate  (+ (mod @counter 45) 90 (- (rand-int 45))))
          (curve-vertex (* radius (cos (* TWO-PI (/ i sides))))
                        (* radius (sin (* TWO-PI (/ i sides))))))
        (end-shape))))
  (display-filter :blur 0.9))

(defsketch rosebud
  :title "rosebud"
  :setup setup
  :draw draw
  :size [500 300]
  :renderer :p2d)

(sketch-stop rosebud)
(sketch-start rosebud)
