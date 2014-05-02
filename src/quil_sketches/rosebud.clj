(ns quil-sketches.rosebud
  (:use quil.core
        quil-sketches.util))

(defn setup []
  (set-state! :bg-images (map load-image (map #(str "data/snow" % ".jpg") (range 1 6))))
  (frame-rate 4)
  (stroke-weight 2)
  (smooth))

(defn draw []
  ;; tinted snow
  (push-matrix)
  (scale 0.9)
  (image (nth (state :bg-images) (mod (frame-count) (count (state :bg-images)))) 0 -130)
  (apply tint (hex-to-rgb "#C5E0DC"))
  (pop-matrix)

  ;; rose
  (with-translation [(/ (width) 2) (/ (height) 2)]
    (with-rotation [(* 0.015 (frame-count))]
      (apply stroke (hex-to-rgb "#ECE5CE"))
      (doseq [nest (range 8)]
        (apply fill (concat (hex-to-rgb "#E08E79") [(+ (* 2 nest) 66)]))
        (let [sides  6
              sizes  [145 116 92 72 57 45 37 30]
              radius (nth sizes nest)]
          (begin-shape)
          (doseq [i (range sides)]
            (rotate (+ (mod (+ nest (frame-count)) 45) 90 (- (rand-int 45))))
            (curve-vertex (* radius (cos (* TWO-PI (/ i sides))))
                    (* radius (sin (* TWO-PI (/ i sides))))))
          (end-shape))))) 
  (display-filter :blur 0.9))

(defsketch rosebud
  :title "rosebud"
  :setup setup
  :draw draw
  :size [500 300]
  :renderer :p2d)
