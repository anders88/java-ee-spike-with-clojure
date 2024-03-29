(ns clojure-person-view.core
(:use [hiccup.core])  
(:use [hiccup.page])  
(:use [hiccup.form])  
(:gen-class
   :methods [
             [generateCreatePage [String String String String] String]
             [generateSearchPage [java.util.List] String]
             [personViewClojure [] no.steria.javaeespike.common.PersonView]
             ])
             
)
   


(defn -generateCreatePage [this first-name last-name birth-date errormessage]
  (html5 [:body 
          (if (not (nil? errormessage)) [:div {:style "color:#FF0000"} errormessage])
          (form-to [:post "createPerson.html"]
               [:p    
               (label "first_name" "First name")
               (text-field "first_name" first-name)]
               [:p
               (label "last_name" "Last name")
               (text-field "last_name" last-name)]
               [:p
               (label "birth_date" "Birth date")
               (text-field "birth_date" birth-date)
               ]
               (submit-button {:name "createPerson"} "Create person"))
          
          ])
          )

(defn -generateSearchPage [this searchResults]
  (html5 [:body 
          (form-to [:get "findPeople.html"]
               (text-field "name_query")
               (submit-button {:name "findPeople"} "Find people"))
          [:ul (for [result searchResults] [:li result])]])
  )

(def date-format (org.joda.time.format.DateTimeFormat/forPattern "dd.MM.yyyy"))

(defn format-birth-date [birth-date]
  (if (nil? birth-date) ""
    (str " (" (.print date-format birth-date) ")"))
  )

(defn person-display-string [person]
  (str (.getFirstName person) " " (.getLastName person) (format-birth-date (.getBirthDate person))) 
  )

(defn -personViewClojure [this]
  (proxy [no.steria.javaeespike.common.PersonView] []
    (displayCreatePage [print-writer first-name last-name birth-date errormessage] 
                       (.append print-writer (-generateCreatePage this first-name last-name birth-date errormessage)))
    (displaySearchPage [print-writer search-results] (.append print-writer (-generateSearchPage this (map person-display-string search-results))))
  )
  )


