(ns quil-sketches.lunar
  (:use quil.core))

(defn setup-lunar []
  (smooth)
  (no-fill)
  (frame-rate 1))

(defn draw-lunar []
  (background 0x181818)
  (stroke 205 225 255 28)
  (translate (/ (width) 2) (/ (height) 2))
  (let [diam 135]
    (dotimes [_ 9999]
      (stroke-weight (random 0.25 0.7))
      (let [angle1 (random TWO-PI)
            angle2 (random TWO-PI)]
        (line (* diam (cos angle1)) (* diam (sin angle1))
              (* diam (cos angle2)) (* diam (sin angle2)))))))

(defsketch lunar
  :title "lunar generations"
  :setup setup-lunar
  :draw draw-lunar
  :size [500 500])
