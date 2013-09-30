(ns quil-sketches.ottoman
  (:use quil.core
        quil-sketches.util))

(def palette [(hex-to-rgb "#e0e0e0")
              (hex-to-rgb "#B6B081")
              (hex-to-rgb "#CAA13C")
              (hex-to-rgb "#AB5008")
              (hex-to-rgb "#2E524E")])

(defn setup []
  (set-state! :img (load-image "data/paper.jpg"))
  (frame-rate 4)
  (no-fill)
  (smooth))

(def rotor (atom 0))
(def zoomer (atom 40))
(def zoomer-flag (atom 1))

(defn draw []
  (apply background (first palette))
  (image (state :img) 0 0)
  (frame-rate 10)
  (stroke-weight 7)
  (translate 250 160)

  (if (>= @zoomer 40)
    (reset! zoomer-flag 2))  
  (if (<= @zoomer -40)
    (reset! zoomer-flag 1))
  (if (= 1 @zoomer-flag)
    (do (swap! zoomer inc)
        (swap! rotor inc))
    (do (swap! zoomer dec)
        (swap! rotor dec)))

  (rotate (radians @rotor))

  (doseq [i (range 0 360 1.5)] 
    (push-matrix)
    (rotate (radians i))
    (translate 0 @zoomer)
    (rotate (radians i))
    (scale 1.45)
    (scale (map-range (sin (radians (* i 7))) -1 1 0.35 1)
           (map-range (sin (radians (* i 3))) -1 1 0.7 1))
    (apply stroke (concat (rand-nth palette) [164]))
    (ellipse 0 0 120 80)
    (pop-matrix)))

(defsketch ottoman
  :title "autumnal"
  :setup setup
  :draw draw
  :size [500 330])
