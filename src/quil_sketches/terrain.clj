;; quil port of http://pastebin.com/MzefraMq
(ns quil-sketches.terrain
  (:import [processing.core PVector])
  (:use quil.core
        quil-sketches.util))

(defn new-terrain [detail]
  (let [t-size (int (pow 2 detail))
        t-max  (- t-size 1)]
    (-> (into [] (repeatedly t-size #(vec (repeat t-size 0.0))))
        (assoc-in [0 0] t-max)
        (assoc-in [t-max t-max] 0)
        (assoc-in [t-max 0] (float (/ t-max 2)))
        (assoc-in [0 t-max] (float (/ t-max 2))))))

(defn filtered-average [nums]
  (let [valid     (remove (partial = -1) nums)
        num-valid (count valid)]
    (if (= num-valid 0) 0 (/ (reduce + valid) num-valid))))

(defn square [terrain x y sz offset]
  (assoc-in terrain [x y]
            (+ offset (filtered-average [(get-in terrain [(- x sz) (- y sz)] -1)
                                         (get-in terrain [(- x sz) (+ y sz)] -1)
                                         (get-in terrain [(+ x sz) (- y sz)] -1)
                                         (get-in terrain [(+ x sz) (+ y sz)] -1)]))))

(defn diamond [terrain x y sz offset]
  (assoc-in terrain [x y]
            (+ offset (filtered-average [(get-in terrain [x (- y sz)] -1)
                                         (get-in terrain [x (+ y sz)] -1)
                                         (get-in terrain [(- x sz) y] -1)
                                         (get-in terrain [(+ x sz) y] -1)]))))

(defn generate
  ([terrain roughness] (generate terrain roughness (count terrain)))
  ([terrain roughness sz]
     (let [half (/ sz 2)]
       (if (> 1 half)
         terrain
         (let [t-max (count terrain)
               scale (* roughness sz)]
           (generate (reduce (fn [t [y x]] (diamond t x y half (random (- scale) scale)))
                             (reduce (fn [t [x y]] (square t x y half (random (- scale) scale)))
                                     terrain
                                     (for [x (range half t-max sz) y (range half t-max sz)] [x y]))
                             (for [y (range 0 t-max half) x (range (mod (+ y half) sz) t-max sz)] [y x]))
                     roughness (/ sz 2)))))))

(defn color-rect [top bottom c]
  (when (>= (.y bottom) (.y top))
    (no-stroke)
    (fill c)
    (rect (.x top) (.y top) (- (.x bottom) (.x top)) (- (.y bottom) (.y top)))))

(defn project [terrain x y z]
  (let [t-size (count terrain)
        point (PVector. (* 0.5 (- (+ t-size x) y)) (* 0.5 (+ x y)))
        z     (+ (- (* t-size 0.5) z) (* (.y point) 0.75))
        x     (* (- (.x point) (* t-size 0.5)) 6)
        y     (+ (* (- t-size (.y point)) 0.005) 1)]
    (PVector. (+ (* 0.5 (width)) (/ x y)) (+ (* 0.2 (height)) (/ z y)))))

(defn draw-terrain [terrain]
  (let [t-size (count terrain)
        t-max  (- t-size 1)]
    (doseq [y (range t-size)]
      (doseq [x (range t-size)]
        (let [val    (get-in terrain [x y] -1)
              top    (project terrain x y val)
              bottom (project terrain (+ 1 x) y 0)
              water  (project terrain x y (* t-size 0.3))]
          (color-rect top bottom
                      (if (or (= x t-max) (= y t-max)) 0
                          (+ 128 (* 50 (- (get-in terrain [(+ 1 x) y] -1) val)))))
          (color-rect water bottom (color 50 150 200 (* 256 0.15))))))))

(defn setup []
  (frame-rate 10)
  (set-state! :terra (atom (generate (new-terrain 8) 0.7))))

(defn draw []
  (frame-rate 2)
  (apply background (hex-to-rgb "#C1EDF2"))
  (draw-terrain @(state :terra)))

(defsketch terrain
  :title "terrain"
  :setup setup
  :draw draw
  :mouse-clicked #(reset! (state :terra) (generate (new-terrain 8) 0.7))
  :size [900 500])
