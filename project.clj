(defproject quil-sketches "0.1.0-SNAPSHOT"
  :description "A collection of sketches using the Quil clojure wrapper for Processing."
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/data.json "0.2.3"]
                 ;; [org.clojars.automata/ddf.minim "2.1.0"]
                 [nio "1.0.2"]
                 [gloss "0.2.2"]
                 [clj-time "0.6.0"]
                 [quil "1.6.0"]]
  :jvm-opts ["-server" "-Xmx8g"])
