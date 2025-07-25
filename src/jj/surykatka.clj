(ns jj.surykatka
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io]
            [clojure.tools.logging :as logger])
  (:import (java.io InputStream)))

(def ^:const ^:private magic-numbers
  (edn/read-string (slurp (io/resource "resources/signatures.edn"))))

(def ^:const ^:private default-config
  {:check-footer true})

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
  (let [header-count (count header-value)
        starting-bits (into [] (take header-count all-bytes))
        ending-bits (into [] (take-last (count footer-value) all-bytes))]

    (and (matches-signature? header-value starting-bits)
         (matches-signature? footer-value ending-bits))))


(defn- detect-byte-array [data-in-bytes config]
  (some (fn [arg]
          (when
            (some (fn [header-value]
                    (cond
                      (not (nil? (:offset arg)))
                      (verify-header header-value data-in-bytes (:offset arg))

                      (and (not (nil? (:footer arg))) (:check-footer config))
                      (some
                        #(verify-header-and-footer header-value % data-in-bytes)
                        (:footer arg))

                      :else
                      (verify-header header-value data-in-bytes)))
                  (:headers arg))
            arg))
        magic-numbers))


(defn- detect-input-stream [input-stream config]
  (let [buf (byte-array 512)]
    (.read ^InputStream input-stream buf)
    (detect-byte-array buf config)))


(defn get-file-type
  "Detects the file type (extension) of a byte array based on magic number signatures.
 Returns the keyword representing the file extension if detected, otherwise nil."
  ([file] (get-file-type file {}))
  ([file config] (cond (bytes? file)
                       (:type (detect-byte-array file (merge default-config config)))
                       (instance? InputStream file)
                       (:type (detect-input-stream file (merge {:check-footer false} config)))
                       :else
                       (logger/error "Not supported type."))))



(defn get-mime
  "Detects the MIME type of byte array based on magic number signatures.
  Returns the MIME type string if detected, otherwise nil."
  ([file] (get-mime file {}))
  ([file config] (cond (bytes? file)
                       (:mime (detect-byte-array file (merge default-config config)))
                       (instance? InputStream file)
                       (:mime (detect-input-stream file (merge {:check-footer false} config)))
                       :else
                       (logger/error "Not supported type."))))
