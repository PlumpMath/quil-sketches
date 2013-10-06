(ns quil-sketches.l-system
  (:use quil.core))

(defn setup []
  (smooth))

;; turtle-style functions to support l-system graphing
(defn forward-3d [distance]
  (line 0 0 0 distance)
  (translate 0 distance 0))

(defn roll [angle]
  (rotate-x (radians angle)))

(defn pitch [angle]
  (rotate-y (radians angle)))

(defn yaw [angle]
  (rotate-z (radians angle)))

;; an easing function for trunk thickness falloff
(defn ease-out [t b d]
  (let [t (/ t d)]
    (+ (* t (* t 0.2)) b)))

(defn l-system [rules curr-state depth]
  (if (zero? depth)
    curr-state 
    (mapcat #(l-system rules (rules % [%]) (dec depth)) curr-state)))

(defn fractal-plant-3d []
  (let [angle        40
        up           []
        operations   {\F #(forward-3d 16)
                      \- #(roll (- (+ (random -0.5 0.3) angle)))
                      \+ #(roll (+ (random -0.3 0.5) angle))
                      \& #(pitch (- (random -0.3 0.5) angle))
                      \% #(pitch (+ (random -0.3 0.5) angle))
                      \> #(yaw (- (random -0.3 0.5) angle))
                      \[ #(do (push-matrix)
                              (stroke (random 80 110) 100 100))
                      \] #(pop-matrix)}
        op-list      (l-system {\F "F[&&+F]F[->F][->%F][&F][&+F]"} "F" 4)]
    (loop [ops       op-list
           op-count  (count ops)
           i         (count ops)]
      (when-let [op (first ops)]
        (stroke-weight (max 2 (* 32 (ease-out i 0 op-count))))
        ((operations op))
        (recur (rest ops) op-count (dec i))))))

(defn draw []
  (no-fill)
  (color-mode :hsb)
  (frame-rate 5)
  (background 200 255 28)
  (stroke 100 100 100)
  (lights)
  (camera)
  (stroke-weight 5)
  (with-translation [(* (width) 1/2) (* 0.99 (height)) 0]
    (scale 0.96)
    (rotate-x (radians 180))
    (rotate-y (radians (* 36 (mod (frame-count) 10))))
    (fractal-plant-3d)))

(defsketch l-systems
  :title "l-systems"
  :setup setup
  :draw draw
  :renderer :p3d
  :size [500 500])

(sketch-stop l-systems)
(sketch-start l-systems)
