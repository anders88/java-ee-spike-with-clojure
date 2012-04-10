(ns clojure-person-view.test.core
  (:use [clojure-person-view.core])
  (:use [clojure.test])
  )

(deftest generate-create-page-test
  (let [actual (generate-create-page)]
  (is (.contains actual "<form") actual)
  (is (.contains actual "action=\"createPerson.html\"") actual)
  (is (.contains actual "name=\"first_name\"") actual)
  (is (.contains actual "name=\"last_name\"") actual)
  (is (.contains actual "name=\"birth_date\"") actual)
  )
  )

