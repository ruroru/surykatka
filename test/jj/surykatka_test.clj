(ns jj.surykatka-test
  (:require [clojure.test :refer [are deftest]]
            [jj.surykatka :as surykatka])
  (:import (java.io BufferedInputStream File FileInputStream)))

(def test-files
  [
   {:file-type :7z :mime-type "application/7z" :path "test/resources/file.7z"}
   {:file-type :bmp :mime-type "image/bmp" :path "test/resources/file.bmp"}
   {:file-type :db :mime-type "application/vnd.sqlite3" :path "test/resources/file.db"}
   {:file-type :elf :mime-type "application/octet-stream" :path "test/resources/file.elf"}
   {:file-type :exe :mime-type "application/octet-stream" :path "test/resources/file.exe"}
   {:file-type :gif :mime-type "image/gif" :path "test/resources/file.gif"}
   {:file-type :gzip :mime-type "application/gzip" :path "test/resources/file.gz"}
   {:file-type :jpeg :mime-type "image/jpeg" :path "test/resources/file.exif.jpg"}
   {:file-type :jpeg :mime-type "image/jpeg" :path "test/resources/file.jpg"}
   {:file-type :pdf :mime-type "application/pdf" :path "test/resources/file.pdf"}
   {:file-type :png :mime-type "image/png" :path "test/resources/file.png"}
   {:file-type :postscript :mime-type "application/postscript" :path "test/resources/file.ps"}
   {:file-type :rar :mime-type "application/vnd.rar" :path "test/resources/file.rar"}
   {:file-type :shellscript :mime-type "text/x-shellscript" :path "test/resources/file.sh"}
   {:file-type :tar :mime-type "application/x-tar" :path "test/resources/file.tar"}
   {:file-type :webp :mime-type "image/webp" :path "test/resources/file.webp"}
   {:file-type :x509-certificate-request :mime-type "application/x-pem-file" :path "test/resources/certificate-request-file-with-trailing-new-line.csr"}
   {:file-type :x509-certificate-request :mime-type "application/x-pem-file" :path "test/resources/certificate-request-file.csr"}
   {:file-type :x509-dsa-private-key :mime-type "application/x-pem-file" :path "test/resources/private-dsa-key-file-with-trailing-new-line.pem"}
   {:file-type :x509-dsa-private-key :mime-type "application/x-pem-file" :path "test/resources/private-dsa-key-file.pem"}
   {:file-type :x509-private-key :mime-type "application/x-pem-file" :path "test/resources/private-key-file-with-trailing-new-line.pem"}
   {:file-type :x509-private-key :mime-type "application/x-pem-file" :path "test/resources/private-key-file.pem"}
   {:file-type :x509-rsa-private-key :mime-type "application/x-pem-file" :path "test/resources/private-rsa-key-file-with-trailing-new-line.pem"}
   {:file-type :x509-rsa-private-key :mime-type "application/x-pem-file" :path "test/resources/private-rsa-key-file.pem"}
   {:file-type :xml :mime-type "application/xml" :path "test/resources/file.xml"}
   {:file-type :xz :mime-type "application/x-xz" :path "test/resources/file.xz"}
   {:file-type :zip :mime-type "application/zip" :path "test/resources/file.zip"}
   ])

;; Helper functions
(defn- file->bytes [file-path]
  (.readAllBytes (FileInputStream. (File. ^String file-path))))

(defn- file->input-stream [file-path]
  (FileInputStream. (File. ^String file-path)))

(defn- file->buffered-stream [file-path]
  (BufferedInputStream. (file->input-stream file-path)))

;; Generic test runner
(defmacro run-test-cases [test-fn expected-key]
  `(doseq [{path# :path expected# ~expected-key} test-files]
     (clojure.test/is (= expected# (~test-fn path#))
                      (str "Failed for file: " path#))))

(deftest byte-array-tests
  (run-test-cases #(surykatka/get-file-type (file->bytes %)) :file-type))

(deftest input-stream-tests
  (run-test-cases #(surykatka/get-file-type (file->buffered-stream %)) :file-type))

(deftest get-mime
  (run-test-cases #(surykatka/get-mime (file->bytes %)) :mime-type))

(deftest get-mime-input-stream
  (run-test-cases #(surykatka/get-mime (file->input-stream %)) :mime-type))

(deftest should-fail-because-of-wrong-footer
  (are [file-path] (= nil (surykatka/get-mime (file->bytes file-path) {:check-footer true}))
                   "test/resources/file.wrong-footer.jpg"
                   "test/resources/file.empty"))

(deftest should-return-correct-type-without-verifying-footer
  (are [file-path] (= :jpeg (surykatka/get-file-type (file->bytes file-path) {:check-footer false}))
                   "test/resources/file.wrong-footer.jpg"))

(deftest input-stream-does-not-verify-footer
  (are [file-path] (= :jpeg (surykatka/get-file-type (file->input-stream file-path)))
                   "test/resources/file.wrong-footer.jpg"))