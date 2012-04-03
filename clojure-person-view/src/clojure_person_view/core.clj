(ns clojure-person-view.core
(:gen-class
   :methods [[hello [String] String]]))
   
 
(defn -hello
  [this s]
  (str "Hello, " s))

