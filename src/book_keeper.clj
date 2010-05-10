(ns #^{:doc "The code for exercise 3 of week 2 of the RubyLearning.org 
            Clojure 101 course."
       :author "Oleg Burykin"}
  book-keeper
  (:use (clojure.contrib.seq-utils/flatten))
  (:import java.text.DateFormat))

(defstruct #^{:doc "Basic structure for book information."}
  book :title :authors)

(def #^{:doc "Place where book shelves live."}
     library (ref (sorted-map)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Shelf API
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn create-shelf
  "Creates new book-shelf and returns it's id."
  []
  (dosync
   (let [new-shelf #{}
	 lib @library
	 last-shelf (last lib)
	 new-id (if last-shelf (inc (key last-shelf)) 0)]
     (alter library assoc new-id new-shelf)
     new-id)))

(def #^{:doc "Default book shelf id"}
     default-shelf (create-shelf))

(defn empty-shelf
  "Empty given shelf."
  [shelf]
  #{})

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Book API
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn create-book
  "Creates a book."
  [title authors] (struct book title authors))

(defn get-book
  "Gets a copy of a book from the person shelf. For now person is the same as shelf id."
  ([book] (get-book default-shelf book))
  ([shelf book]
     ((@library shelf) book)))

(defn add-book
  "Adds book to shelf (default one using if not specified)."
  ([book] (add-book default-shelf book))
  ([shelf book]
     (dosync
      (alter library update-in [shelf] conj book))))

(defn remove-book
  "Removes book from the shelf (removes from default one if no shelf given)"
  ([book] (remove-book default-shelf book))
  ([shelf book]
     (dosync
      (alter library update-in [shelf] disj book))))

(defn- replace-book
  "Replaces book in the given shelf"
  [shelf old-book new-book]
  (-> shelf
      (disj old-book)
      (conj new-book)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Loan API
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn is-loaned?
  "Predicate to test whether book is loaned to somebody."
  ([book]
     (let [book-meta (meta book)]
       (if book-meta
	 (not (nil? (book-meta :loan-data)))
	 false))))

(defn- loan-book
  [lib from-person to-person book opts-map]
  (let [book-loaned-to (with-meta book
			 {:loan-data (conj opts-map {:to to-person})})
	book-loaned-from (with-meta book
			   {:loan-data (conj opts-map {:from from-person})})]
    (-> lib
	(update-in [from-person] replace-book book book-loaned-to)
	(update-in [to-person] conj book-loaned-from))))

(defn- return-book
  [lib to-person from-person book]
  (let [wo-meta (with-meta book nil)]
    (-> lib
	(update-in [to-person] replace-book book wo-meta)
	(update-in [from-person] disj book))))

(defn add-loan!
  "Make this book loaned to friend.
Friend here represented as his book-shelf id.
Potentially could be person record, which has his shelf id assigned to it.
TODO: Add exceptions throwning, in case if no such book on shelf,
       or already loaned, or when already have that book."
  ([to-person book] (add-loan! default-shelf to-person book))
  ([from-person to-person book & options]
     (dosync
      (ensure library)
      (if (not (is-loaned? (get-book from-person book)))
	(alter library loan-book from-person to-person book (apply hash-map options))
	(println "Book is already loaned to someone")))))

(defn accept-return!
  "Accept return of book from person"
  ([from-person book] (accept-return! default-shelf from-person book))
  ([to-person from-person book]
     (dosync
      (ensure library)
      (if (is-loaned? (get-book to-person book))
	(let [wo-meta (with-meta book nil)]
	  (alter library return-book to-person from-person book))
	(println "Book wasn't loaned!")))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Empty functions
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn empty-library
  "Empty whole library."
  []
  (dosync
   (alter library empty)
   (def default-shelf (create-shelf))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Print functions
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn comma-sep
  "Creates a comma-separated string from a sequence of names."
  [names]
  (apply str (interpose ", " names)))

(defn print-book
  "Prints out information about a book."
  [{:keys [title authors]}]
  (println "Title:" title)
  (println "  Authors: " (comma-sep authors)))

(defn loan-string
  [book]
  (let [loan-data (-> book meta :loan-data)
	from (loan-data :from)
	to (loan-data :to)
	ret-by (loan-data :return-by)]
    (str "(" 
	 (if to (str "book is loaned to: " to " ") "")
	 (if from (str "book is taken from: " from " ") "")
	 (if ret-by
	   (str "Should be returned by "
		(.format (DateFormat/getDateInstance DateFormat/SHORT) (.getTime ret-by)))
	   "")
	 ")")))

(defn simple-print-book
  "Prints out information about book in a simplified manner"
  [{:keys [title] :as book}]
   (println title
	    (if (is-loaned? book)
	      (loan-string book)
	      "")))

(defn print-shelf
  "Prints books from a given shelf (prints default shelf books if no shelf given)."
  ([] (print-shelf (@library default-shelf)))
  ([id shelf]
     (if (empty? shelf)
       (println id "shelf: No books in this shelf")
       (do
	 (doseq [book shelf]
	   (print id "shelf: ")
	   (simple-print-book book))
	 (print "=======================\n")
	 (println "Shelf" id "contains" (count shelf) "books.\n")))))

(defn print-lib
  "Prints contents of the whole shelves in Library."
  []
  (dosync
   (doseq [shelf-rec @library] (print-shelf (key shelf-rec)
					    (val shelf-rec)))))
