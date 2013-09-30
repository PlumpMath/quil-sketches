(ns quil-sketches.util
  (:use quil.core))

(def eigengrau 0x181818)

(defn hex-to-rgb [hex]
  (map (comp #(Integer/parseInt % 16) (partial apply str))
       (partition 2 (.replace hex "#" ""))))
