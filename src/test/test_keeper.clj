(ns #^{:doc "Test cases for book-keeper app. TODO: Provide usage of test clojure framework."
       :author "Oleg Burykin"}
  test.test-keeper
(:use book-keeper)
(:import java.util.GregorianCalendar))


(defn test-keeper []
  (let [friend-shelf (create-shelf)
	other-friend-shelf (create-shelf)
	caroll (create-book "Through the Looking Glass" ["Lewis Carroll"])
	wilde (create-book "The Complete Short Stories" ["Oscar Wilde"])
	hemingway (create-book "A Moveable Feast" ["Ernest Hemingway"])
	stevenson (create-book "Dr Jekyll and Mr Hyde" ["Stevenson"])
	jerome (create-book "Three Men in a Boat" ["Jerome K. Jerome"])]

     (println "1) ")
     (print-lib)
     (add-book caroll)
     (add-book wilde)
     (add-book jerome)
     (add-book friend-shelf hemingway)
     (add-book other-friend-shelf stevenson)
     (add-loan! default-shelf other-friend-shelf jerome
		:return-by (GregorianCalendar. 2010 7 1))
     (println "2) ")
     (print-lib)
     (remove-book caroll)
     (accept-return! other-friend-shelf jerome)
     (println "After all: ")
     (print-lib)
     (flush)))
