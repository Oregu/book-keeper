(ns #^{:doc "Test cases for book-keeper app. TODO: Provide usage of test clojure framework."
       :author "Oleg Burykin"}
  test.test-keeper
(:use book-keeper query)
(:import java.util.GregorianCalendar))


(defn test-keeper []
  (let [friend-shelf (create-shelf)
	other-friend-shelf (create-shelf)
	caroll (create-book "Through the Looking Glass" ["Lewis Carroll"])
	wilde (create-book "The Complete Short Stories" ["Oscar Wilde"])
	hemingway (create-book "A Moveable Feast" ["Ernest Hemingway"])
	stevenson (create-book "Dr Jekyll and Mr Hyde" ["Stevenson"])
	jerome (create-book "Three Men in a Boat" ["Jerome K. Jerome"])
	harris (create-book "Jigs & Reels" ["Joanne Harris"])]

     (println "1) ")
     (print-lib)

     (add-book caroll)
     (add-book wilde)
     (add-book jerome)
     (add-book harris)
     (add-book friend-shelf hemingway)
     (add-book other-friend-shelf stevenson)

     (add-loan! default-shelf other-friend-shelf jerome
		:return-by (GregorianCalendar. 2010 7 1))
     (add-loan! default-shelf other-friend-shelf caroll)
     (add-loan! default-shelf friend-shelf harris)

     #_(println "2) ")
     #_(print-lib)
     #_(remove-book caroll)
     #_(accept-return! other-friend-shelf jerome)

     (println "After all: ")
     (print-lib)
     (flush)))

(defn test-books-written-by-karl []
  (get-by-author best-sellers "Karl Rove"))

(defn test-book-by-many-authors []
  (get-by-author-count best-sellers))

(defn test-loaners
  ([] (test-loaners default-shelf))
  ([shelf]
     (get-loaners (@library shelf))))

(defstruct #^{:doc "Basic structure for book information."}
  book-amazon :title :authors :price)

(def #^{:doc "The top ten Amazon best sellers on 16 Mar 2010."}
  best-sellers
  [(struct book-amazon
           "The Big Short"
           ["Michael Lewis"]
           15.09)
   (struct book-amazon
           "The Help"
           ["Kathryn Stockett"]
           9.50)
   (struct book-amazon
           "Change Your Prain, Change Your Body"
           ["Daniel G. Amen M.D."]
	   14.29)
   (struct book-amazon
           "Food Rules"
           ["Michael Pollan"]
           5.00)
   (struct book-amazon
           "Courage and Consequence"
           ["Karl Rove"]
           16.50)
   (struct book-amazon
           "A Patriot's History of the United States"
           ["Larry Schweikart","Michael Allen"]
           12.00)
   (struct book-amazon
           "The 48 Laws of Power"
           ["Robert Greene"]
           11.00)
   (struct book-amazon
           "The Five Thousand Year Leap"
           ["W. Cleon Skousen","James Michael Pratt","Carlos L Packard","Evan Frederickson"]
           10.97)
   (struct book-amazon
           "Chelsea Chelsea Bang Bang"
           ["Chelsea Handler"]
           14.03)
   (struct book-amazon
           "The Kind Diet"
           ["Alicia Silverstone","Neal D. Barnard M.D."]
           16.00)])
