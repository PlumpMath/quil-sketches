(ns quil-sketches.fungus
  (:use quil.core))

(def WIDTH  500)
(def HEIGHT 335)
(def CHOMP  40) ;; smaller chomps for lighter photos
(def BURN   40)

(def cells  (atom {}))
(def food   (int-array (* WIDTH HEIGHT)))

(defn seed-spores [how-many]
  (reset! cells
          (reduce (fn [m [x y c]] (assoc m [x y] c)) {}
                  (repeatedly how-many #(list (+ 5 (rand-int (- WIDTH 10)))
                                              (+ 5 (rand-int (- HEIGHT 10)))
                                              (* 2 BURN))))))

(defn setup-fungus []
  (smooth)
  (background 230)
  (let [pix (.pixels (load-image "data/landscape.jpg"))]
    (set-state! :pix pix)
    (dotimes [i WIDTH]
      (dotimes [j HEIGHT]
        (aset food
              (+ i (* j WIDTH))
              (max (* 2 CHOMP) (- 255 (bit-and (bit-shift-right (aget pix (+ i (* WIDTH j))) 8) 0xFF)))))))
  (seed-spores 30))

(defn feed [x y]
  (let [morsel (aget food (int (+ x (* y WIDTH))))]
    (if (>= morsel CHOMP)
      (do
        (aset food (+ x (* WIDTH y)) (- morsel CHOMP))
        CHOMP)
      0)))

(defn best-step [x y cells]
  (let [x     (max (min x (- WIDTH 3)) 3)
        y     (max (min y (- HEIGHT 3)) 3)
        top-n (->> (for [i (range (- x 1) (+ x 2))]
                     (for [j (range (- y 1) (+ y 2))]
                       (let [loc [i j]]
                         (when-not (get cells loc)
                           {:loc loc :food (aget food (int (+ i (* j WIDTH))))}))))
                   (flatten)
                   (remove nil?)
                   (sort-by :food))]
    ;; in the case of a tie, choose at random
    (when-let [best (last top-n)]
      (rand-nth (remove #(< (% :food) (best :food)) top-n)))))

(defn update-cells [old-cells new-cells]
  (if-let [[[x y] c] (first old-cells)]
    (let [energy (max (- (+ c (feed x y)) BURN) 0)]
      (if (<= energy 0)
        (recur (rest old-cells) (dissoc new-cells [x y])) ; DEATH
        (if-let [best (best-step x y new-cells)]
          (if (and (> c 80) (< (count (keys new-cells)) 60))
            (recur (rest old-cells)
                   (assoc new-cells [x y] (/ energy 2) (best :loc) (/ energy 2))) ; DIVISION
            (recur (rest old-cells)
                   (assoc (dissoc new-cells [x y]) (best :loc) energy))) ; MOVEMENT
          (recur (rest old-cells) (assoc new-cells [x y] (- energy (* 2 BURN))))))) ; stuck cell
    new-cells))

;; ;; somewhat resembles colored pencil
;; (fill (aget (state :pix) (+ x (* WIDTH y))) (min 86 (aget food (+ x (* y WIDTH)))))
;; (let [sz 4
;;       x1 x
;;       y1 (- y 2 (rand-int sz))
;;       x2 (- x 2 (rand-int sz))
;;       y2 (+ y 2 (rand-int sz))
;;       x3 (+ x 2 (rand-int sz))
;;       y3 (+ y 2 (rand-int sz))]
;;   (begin-shape)
;;   (curve-vertex x1 y1)
;;   (curve-vertex x2 y2)
;;   (curve-vertex x3 y3)
;;   (curve-vertex x1 y1)
;;   (end-shape))

(defn draw-fungus []
  (frame-rate 40)
  (no-stroke)
  (reset! cells (update-cells @cells @cells))
  (doseq [[[x y] c] @cells]
    (when (and (> c 0) (> x 0) (< x (width)) (> y 0) (< y (height)))
      ;; inner/outer ellipses of differing opacity resemble paint, in
      ;; each case we scale the alpha transparency to the remaining
      ;; "food" from the original image.
      (fill (aget (state :pix) (+ x (* WIDTH y)))
            (min 4 (aget food (+ x (* y WIDTH)))))
      (ellipse x y (random 9 18) (random 9 18))
      (fill (aget (state :pix) (+ x (* WIDTH y)))
            (min 32 (aget food (+ x (* y WIDTH)))))
      (ellipse x y (random 4 7) (random 4 7)))))

(defn add-spores-and-food []
  (let [x (mouse-x)
        y (mouse-y)]
    (aset food (+ x (* WIDTH y)) (* 2 CHOMP))
    (reset! cells (assoc @cells [x y] (* 4 BURN)))))

(defsketch fungus-sketch
  :title "image fungus"
  :setup setup-fungus
  :draw draw-fungus
  :size [WIDTH HEIGHT]
  :mouse-released add-spores-and-food ; handy if the colony dies
  :renderer :p2d)

(sketch-stop fungus-sketch)
(sketch-start fungus-sketch)
