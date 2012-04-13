(ns clojure-person-view.core
(:use [hiccup.core])  
(:use [hiccup.page])  
(:use [hiccup.form])  
(:gen-class
   :methods [
             [hello [String] String]
             [generateCreatePage [String String String String] String]
             ])
             
)
   
 
(defn -hello
  [this s]
  (str "Hello, " s))

(defn -generateCreatePage [this first-name last-name birth-date errormessage]
  (html5 [:body 
          (if (not (nil? errormessage)) [:div {:style "color:#FF0000"} errormessage])
          (form-to [:post "createPerson.html"]
               (text-field "first_name" first-name)
               (text-field "last_name" last-name)
               (text-field "birth_date" birth-date)
               (submit-button "Create person"))
          
          ])
          )



