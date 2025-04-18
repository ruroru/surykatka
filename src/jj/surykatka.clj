(ns jj.surykatka
  (:require [clojure.data.json :as json]
            [clojure.java.io :as io]
            [clojure.tools.logging :as logger]))

(def ^:const ^:private magic-numbers
  (json/read-str (slurp (io/resource "resources/signatures.json"))
                 {:key-fn   keyword
                  :value-fn (fn [k v]
                              (cond (= k :extension) (keyword v)
                                    (= k :headers) (map (fn [signature]
                                                          (map (fn [byte-value]
                                                                 (if (number? byte-value)
                                                                   byte-value
                                                                   (keyword byte-value)))
                                                               signature))
                                                        v)
                                    :else v))}))

(defn- matches-signature? [expected array]
  (cond
    (and (empty? expected) (empty? array)) true
    :else (let [[e & es] expected
                [a & as] array]
            (if (or (= e a) (= e :any))
              (recur es as)
              false))))

(defn- verify-header
  ([expected-starting-bits all-bytes]
   (let [starting-bits (take (count expected-starting-bits) all-bytes)]
     (matches-signature? expected-starting-bits starting-bits)))
  ([expected-starting-bits all-bytes skip-bytes]
   (let [starting-bits (take (count expected-starting-bits) (drop skip-bytes all-bytes))]
     (matches-signature? expected-starting-bits starting-bits))))

(defn- verify-header-and-footer [header-value footer-value all-bytes]
  (let [
        header-count (count header-value)
        starting-bits (into [] (take header-count all-bytes))
        ending-bits (into [] (take-last (count footer-value) all-bytes))]
    (and (matches-signature? header-value starting-bits)
         (matches-signature? footer-value ending-bits))))

(defn- detect-type [data-in-bytes]
  (some (fn [arg]
          (when
            (some (fn [header-value]
                    (cond
                      (:offset arg) (verify-header header-value data-in-bytes (:offset arg))
                      (:footer arg) (verify-header-and-footer header-value (:footer arg) data-in-bytes)
                      :else
                      (verify-header header-value data-in-bytes)))
                  (:headers arg))
            arg))
        magic-numbers))

(defn get-file-type [file]
  "Detects the file type (extension) of a byte array based on magic number signatures.
 Returns the keyword representing the file extension if detected, otherwise nil."
  (if (bytes? file)
    (:extension (detect-type file))
    (logger/error "Not a byte array.")))

(defn get-mime [file]
  "Detects the MIME type of a byte array based on magic number signatures.
  Returns the MIME type string if detected, otherwise nil."
  (if (bytes? file)
    (:mime (detect-type file))
    (logger/error "Not a byte array.")))
