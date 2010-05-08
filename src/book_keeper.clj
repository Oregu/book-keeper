(ns #^{:doc "The code for exercise 3 of week 2 of the RubyLearning.org 
            Clojure 101 course."
       :author "Oleg Burykin"}
  book-keeper)

(defstruct #^{:doc "Basic structure for book information."}
  book :title :authors)

(def #^{:doc "Place where book shelves live."}
     library (ref (sorted-map)))

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

(defn empty-library
  "Empty whole library."
  []
  (dosync
;; (doseq [shelf @library] (empty-shelf (val shelf)))
   (alter library empty)
   (def default-shelf (create-shelf))))

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

(defn create-book
  "Creates a book."
  [title authors] (struct book title authors))

(defn comma-sep
  "Creates a comma-separated string from a sequence of names."
  [names]
  (apply str (interpose ", " names)))

(defn print-book
  "Prints out information about a book."
  [{:keys [title authors]}]
  (println "Title:" title)
  (println "  Authors: " (comma-sep authors)))

(defn simple-print-book
  "Prints out information about book in a simplified manner"
  [{:keys [title]}]
   (println "Title:" title))

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
