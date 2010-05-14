(ns 
    query
  (:use book-keeper))

(defn get-by-author [shelf author]
  (filter #(->> % :authors (some #{author})) shelf))

(defn get-by-author-reduce [shelf author]
  (reduce #(if (->> %2 :authors (some #{author}))
	     (conj %1 %2)
	     %1)
	  [] shelf))

(defn get-by-author-count [shelf]
  (filter #(> (-> % :authors count) 1) shelf))

(defn- borrowers-list
  "Returns list of people who borrowed book.
   If somebody borrowed it three times, then he appears three times in list)"
  [shelf]
  (filter #(not (nil? %))
	  (map #(-> % meta :loan-data :to) shelf)))

(defn book-borrowers
  "Returns map of borrowers with count of books they borrowed."
  [shelf]
  (reduce #(assoc %1 %2 (inc (get %1 %2 0))) {} (borrowers-list shelf)))

(defn get-awful-borrowers
  "Returns list of people who borrowed more than one book."
  [shelf]
  (map key (filter #(> (val %) 1) (book-borrowers shelf))))
