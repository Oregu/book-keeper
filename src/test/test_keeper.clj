(ns #^{:doc "Test cases for book-keeper app. TODO: Provide usage of test clojure framework."
       :author "Oleg Burykin"}
  test.test-keeper
(:use book-keeper))


(defn test-keeper []
  (let [friend-shelf (create-shelf)
	other-friend-shelf (create-shelf)
	caroll (create-book "Through the Looking Glass" ["Lewis Carroll"])
	wilde (create-book "The Complete Short Stories" ["Oscar Wilde"])
	hemingway (create-book "A Moveable Feast" ["Ernest Hemingway"])
	stevenson (create-book "Dr Jekkyl..." ["Stevenson"])]

     (println "1) ")
     (print-lib)
     (add-book caroll)
     (add-book wilde)
     (println "2) ")
     (print-lib)
     (add-book friend-shelf hemingway)
     (add-book other-friend-shelf stevenson)
     (remove-book caroll)
     (println "After all: ")
     (print-lib)
     (flush)))
