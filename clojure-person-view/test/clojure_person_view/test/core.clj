(ns clojure-person-view.test.core
  (:use [clojure-person-view.core])
  (:use [clojure.test])
  )

(deftest generate-create-page-test
  (let [actual (-generateCreatePage nil "" "" "" nil)]
  (is (.contains actual "<form") actual)
  (is (.contains actual "action=\"createPerson.html\"") actual)
  (is (.contains actual "name=\"first_name\"") actual)
  (is (.contains actual "name=\"last_name\"") actual)
  (is (.contains actual "name=\"birth_date\"") actual)
  )
  )

(deftest generate-create-page-with-values
  (let [actual (-generateCreatePage nil "Luke" "Skywalker" "25.05.1977" nil)]
  (is (.contains actual "value=\"Luke\"") actual)
  (is (.contains actual "value=\"Skywalker\"") actual)
  (is (.contains actual "value=\"25.05.1977\"") actual)
  (is (.contains actual "name=\"createPerson\"") actual)
  )
  )

(deftest should-encode-html
  (let [actual (-generateCreatePage nil "L<&>uke" "Skywalker" "25.05.1977" nil)]
  (is (.contains actual "value=\"L&lt;&amp;&gt;uke\"") actual)
  )
  )

(deftest should-not-show-error-when-no-error
  (let [actual (-generateCreatePage nil "Luke" "Skywalker" "25.05.1977" nil)]
  (is (not (.contains actual "<div")) actual)
  )
  )

(deftest should-contain-errormessage
  (let [actual (-generateCreatePage nil "L<&>uke" "Skywalker" "25.05.1977" "Error something")]
  (is (.contains actual "Error something") actual)
  )
  )

(deftest searchpage-contain-search-and-result
  (let [actual (-generateSearchPage nil ["Anakin" "Luke"])]
  (is (.contains actual "<li>Anakin</li>") actual)
  (is (.contains actual "<form action=\"findPeople.html") actual)
  )
  )

(deftest should-generate-display-string-from-person  
  (is (= "Luke Skywalker" (person-display-string (no.steria.javaeespike.common.Person/create "Luke" "Skywalker" nil))))
  (is (= "Luke Skywalker (25.05.1977)" (person-display-string (no.steria.javaeespike.common.Person/create "Luke" "Skywalker" (org.joda.time.LocalDate. 1977 5 25)))))
 )