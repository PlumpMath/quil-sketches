(ns quil-sketches.galactica
  (:use quil.core
        quil-sketches.util))

;; re-eval these palette defs while it's running
(def palette [(hex-to-rgb "#F5F7DD")
              (hex-to-rgb "#454139")
              (hex-to-rgb "#7EBFC3")
              (hex-to-rgb "#F39131")])

(def palette [(hex-to-rgb "#773198")
              (hex-to-rgb "#B153C5")
              (hex-to-rgb "#375374")])

(def palette [(hex-to-rgb "#312C20")
              (hex-to-rgb "#494D4B")
              (hex-to-rgb "#7C7052")
              (hex-to-rgb "#B3A176")])

(def wobbler (atom 0))

(defn setup []
  (reset! wobbler 0)
  (background 24 24 24)
  (frame-rate 20)
  (smooth))

(defn draw []
  (swap! wobbler #(+ % (rand-int 64)))
  (no-fill)
  (apply stroke (concat (rand-nth palette) [32]))
    (begin-shape)
  (doseq [j (range 0 169)]
    (let [a (/ (* j PI) 48)
          r (* (noise (* (mod j 96) 0.1) (* @wobbler 0.003))
               (- 9 (mod @wobbler 9))
               (+ (sin (* @wobbler 0.001)) 1)
               20)]
      (curve-vertex (+ 300 (* r (sin a))) (+ 300 (* r (cos a))))))
  (end-shape))

(defsketch galactica
  :title "galactica"
  :setup setup
  :draw draw
  :size [600 600])

(sketch-stop galactica)
(sketch-start galactica)
