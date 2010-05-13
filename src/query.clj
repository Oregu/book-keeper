(ns 
    query
  (:use book-keeper))

(defn get-by-author [shelf author]
  (filter #(->> % :authors (some #{author})) shelf))

(defn get-by-author-count [shelf]
  (filter #(> (-> % :authors count) 1) shelf))

(defn loaners-list [shelf]
  (filter #(not (nil? %))
	  (map #(-> % meta :loan-data :to) shelf)))

(defn get-loaners [shelf]
  (let [result {}]
    (doseq [loaner (loaners-list shelf)]
      (assoc result loaner (if (contains? result loaner) (inc (result loaner)) 1))
      result)))
