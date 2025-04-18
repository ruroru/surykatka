(ns jj.surykatka
  (:import (java.io InputStream)))


(defrecord Signature [header])
(defrecord SignatureWithFooter [header footer])

(def ^:const ^:private hex-template "%02x")
(def ^:const ^:private magic-numbers
  {
   :7z           #{(Signature. '(0x37 0x7A 0xBC 0xAF 0x27 0x1C))}
   :bmp          #{(Signature. '(0x42 0x4d))}
   :bz2          #{(Signature. '(0x42 0x5A 0x68))}
   :gif          #{(Signature. '(0x47 0x49 0x46 0x38 0x37 0x61))
                   (Signature. '(0x47 0x49 0x46 0x38 0x39 0x61))}
   :gz           #{(Signature. '(0x1F 0x8B))}
   :jpg          #{(SignatureWithFooter. '(0xFF 0xD8 0xFF 0xDB) '(0xFF 0xD9))
                   (SignatureWithFooter. '(0xFF 0xD8 0xFF 0xE0 0x00 0x10 0x4A 0x46) '(0xFF 0xD9))
                   (SignatureWithFooter. '(0xFF 0xD8 0xFF 0xE0) '(0xFF 0xD9))
                   (SignatureWithFooter. '(0xFF 0xD8 0xFF 0xE1 :any :any 0x45 0x78 0x69 0x66 0x00 0x00) '(0xFF 0xD9))
                   (SignatureWithFooter. '(0xFF 0xD8 0xFF 0xEE) '(0xFF 0xD9))}
   :pdf          #{(Signature. '(0x25 0x50 0x44 0x46 0x2D))}
   :png          #{(Signature. '(0x89 0x50 0x4E 0x47 0x0D 0x0A 0x1A 0x0A))}
   :ps           #{(Signature. '(0x25 0x21))}
   :shell-script #{(Signature. '(0x23 0x21))}
   :tar          #{(Signature. '(0x75 0x73 0x74 0x61 0x72 0x00))
                   (Signature. '(0x75 0x73 0x74 0x61 0x72 0x20 0x20 0x00))}
   :webp         #{(Signature. '(0x52 0x49 0x46 0x46 :any :any :any :any 0x57 0x45 0x42 0x50))}
   :wim          #{(Signature. '(0x4D 0x53 0x57 0x49 0x4D 0x00 0x00 0x00 0xD0 0x00 0x00 0x00 0x00))}
   :xz           #{(Signature. '(0xFD 0x37 0x7A 0x58 0x5A 0x00))}
   :zip          #{(Signature. '(0x50 0x4B 0x03 0x04))
                   (Signature. '(0x50 0x4B 0x05 0x06))
                   (Signature. '(0x50 0x4B 0x07 0x08))}})


(defn- byte-array->hex-string-vector [^bytes byte-array]
  (mapv (fn [number]
          (if (number? number?)
            (format hex-template (bit-and number 0xFF))
            number))
        byte-array))


(defn- byte-array->unsigned-list [arr]
  (mapv #(bit-and % 0xFF) arr))


(defn- matches-signature? [expected-array array-from-file]
  (cond
    (and (= (count expected-array) 0) (= (count array-from-file) 0)) true
    :else
    (do
      (let [o1 (first expected-array)
            o2 (first array-from-file)]
        (if (or (= o1 o2) (= o1 :any))
          (recur (rest expected-array) (rest array-from-file))
          false)))))


(defn- verify-header
  ([expected-starting-bits all-bytes]
   (let [starting-bits (byte-array->hex-string-vector (take (count (:header expected-starting-bits)) (byte-array->unsigned-list all-bytes)))]
     (matches-signature? (:header expected-starting-bits) starting-bits)))
  ([expected-starting-bits all-bytes skip-bytes]
   (let [starting-bits (byte-array->hex-string-vector (take (count (:header expected-starting-bits)) (byte-array->unsigned-list (drop skip-bytes all-bytes))))]
     (matches-signature? (:header expected-starting-bits) starting-bits))))


(defn- verify-header-and-footer [expected all-bytes]
  (let [ending-bits (byte-array->hex-string-vector (take-last (count (:footer expected)) (byte-array->unsigned-list all-bytes)))
        starting-bits (byte-array->hex-string-vector (take (count (:header expected)) (byte-array->unsigned-list all-bytes)))]
    (and (matches-signature? (:header expected) starting-bits)
         (matches-signature? (:footer expected) ending-bits))))


(defn detect-file-type [^InputStream input-stream]
  (let [all-bytes (.readAllBytes input-stream)]
    (some (fn [arg]
            (let [type (first arg)
                  magic-number-set (second arg)]
              (when
                (some (fn [data]
                        (cond
                          (= type :tar) (verify-header data all-bytes 257)
                          :else
                          (if (instance? SignatureWithFooter data)
                            (verify-header-and-footer data all-bytes)
                            (verify-header data all-bytes))))
                      magic-number-set)
                type)))
          magic-numbers)))

