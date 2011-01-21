;; The key here is :exposes-methods. It creates a method for you called supe;;rOnCreate, which is the Activity class's onCreate. Equivalent to calling;; super.onCreate() in Java.

(ns com.hsaliak.HelloFlashlight
  (:gen-class
   :extends android.app.Activity
   :exposes-methods {onCreate superOnCreate})
   (:import [android.widget Button TableLayout]
	  [android.app Activity]
	  [android.graphics Color]
	  [android.os Bundle]
	  [android.view View]))

;; View$OnClickListener is a public static interface within the View Class
;; It is meant to be instantiated as an anonymous class, The right way to
;; do this in clojure is to reify the interface and provide the onClick
;; as below. I suppose proxies can also be used but is not recommended.
;; Note that the method has a #^void type hint for the return value. Without
;; it you will get a signature mismathc where an object is returned when
;; void is expected. I have put a do block with returning nil just to be safe.
(defn on-click [c table]
  (let [oc (reify android.view.View$OnClickListener
	    (#^void onClick [this #^View v]
		     (do
		      (.setBackgroundColor table c)
		      nil)))]oc))
    
    
(defn -onCreate [this #^android.os.Bundle bundle ] 
;; The this object reference is passed as the first argument. 
;; This is different from java where its availability is implicit.
	(.superOnCreate this bundle)
	(.setContentView this com.hsaliak.R$layout/main)
;; Clojure really shines here as we have saved a lot of verbosity.
	(let [[table red green blue black white]
	      (map #(.findViewById this %)
		   [com.hsaliak.R$id/Table
		    com.hsaliak.R$id/ButtonRed
		    com.hsaliak.R$id/ButtonGreen
		    com.hsaliak.R$id/ButtonBlue
		    com.hsaliak.R$id/ButtonBlack
		    com.hsaliak.R$id/ButtonWhite])]
	  (.setBackgroundColor table Color/WHITE)
	  ;; watch out for laziness.. 
	  (doall (map #(.setOnClickListener  %1 (on-click %2 table))
	       [red green blue black white]
	       [Color/RED Color/GREEN Color/BLUE Color/BLACK Color/WHITE]))))
	  

