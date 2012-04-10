(ns clojure-person-view.core
(:use [hiccup.core])  
(:use [hiccup.page])  
(:use [hiccup.form])  
(:gen-class
   :methods [[hello [String] String]])
)
   
 
(defn -hello
  [this s]
  (str "Hello, " s))

(defn generate-create-page []
  (html5 [:body 
          (form-to [:post "createPerson.html"]
               (text-field "first_name" "")
               (text-field "last_name" "")
               (text-field "last_name" "")
               (text-field "birth_date" "")
               (submit-button "Create person"))
          
          ])
          )



