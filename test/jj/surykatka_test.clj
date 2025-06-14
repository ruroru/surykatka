(ns jj.surykatka-test
  (:require [clojure.test :refer [are deftest]]
            [jj.surykatka :as surykatka])
  (:import (java.io File FileInputStream)))

(deftest byte-array-tests
  (are [expected-type file-path] (= expected-type (surykatka/get-file-type (.readAllBytes (FileInputStream. (File. ^String file-path)))))
                                 :7z "test/resources/file.7z"
                                 :bmp "test/resources/file.bmp"
                                 :db "test/resources/file.db"
                                 :gif "test/resources/file.gif"
                                 :gzip "test/resources/file.gz"
                                 :jpeg "test/resources/file.exif.jpg"
                                 :jpeg "test/resources/file.jpg"
                                 :pdf "test/resources/file.pdf"
                                 :png "test/resources/file.png"
                                 :postscript "test/resources/file.ps"
                                 :shellscript "test/resources/file.sh"
                                 :tar "test/resources/file.tar"
                                 :webp "test/resources/file.webp"
                                 :x509-certificate-request "test/resources/certificate-request-file-with-trailing-new-line.csr"
                                 :x509-certificate-request "test/resources/certificate-request-file.csr"
                                 :x509-dsa-private-key "test/resources/private-dsa-key-file-with-trailing-new-line.pem"
                                 :x509-dsa-private-key "test/resources/private-dsa-key-file.pem"
                                 :x509-private-key "test/resources/private-key-file-with-trailing-new-line.pem"
                                 :x509-private-key "test/resources/private-key-file.pem"
                                 :x509-rsa-private-key "test/resources/private-rsa-key-file-with-trailing-new-line.pem"
                                 :x509-rsa-private-key "test/resources/private-rsa-key-file.pem"
                                 :xml "test/resources/file.xml"
                                 :xz "test/resources/file.xz"
                                 :zip "test/resources/file.zip"
                                 ))

(deftest get-mime
  (are [expected-type file-path] (= expected-type (surykatka/get-mime (.readAllBytes (FileInputStream. (File. ^String file-path)))))
                                 "application/7z" "test/resources/file.7z"
                                 "application/gzip" "test/resources/file.gz"
                                 "application/pdf" "test/resources/file.pdf"
                                 "application/postscript" "test/resources/file.ps"
                                 "application/vnd.sqlite3" "test/resources/file.db"
                                 "application/x-pem-file" "test/resources/certificate-request-file-with-trailing-new-line.csr"
                                 "application/x-pem-file" "test/resources/certificate-request-file-with-trailing-new-line.csr"
                                 "application/x-pem-file" "test/resources/certificate-request-file.csr"
                                 "application/x-pem-file" "test/resources/certificate-request-file.csr"
                                 "application/x-pem-file" "test/resources/private-dsa-key-file-with-trailing-new-line.pem"
                                 "application/x-pem-file" "test/resources/private-dsa-key-file.pem"
                                 "application/x-pem-file" "test/resources/private-key-file-with-trailing-new-line.pem"
                                 "application/x-pem-file" "test/resources/private-key-file.pem"
                                 "application/x-pem-file" "test/resources/private-rsa-key-file-with-trailing-new-line.pem"
                                 "application/x-pem-file" "test/resources/private-rsa-key-file.pem"
                                 "application/x-tar" "test/resources/file.tar"
                                 "application/x-xz" "test/resources/file.xz"
                                 "application/xml" "test/resources/file.xml"
                                 "application/zip" "test/resources/file.zip"
                                 "image/bmp" "test/resources/file.bmp"
                                 "image/gif" "test/resources/file.gif"
                                 "image/jpeg" "test/resources/file.exif.jpg"
                                 "image/jpeg" "test/resources/file.jpg"
                                 "image/png" "test/resources/file.png"
                                 "image/webp" "test/resources/file.webp"
                                 "text/x-shellscript" "test/resources/file.sh"
                                 ))

(deftest should-fail-because-of-wrong-footer
  (are [file-path] (= nil (surykatka/get-mime (.readAllBytes (FileInputStream. (File. ^String file-path)))
                                              {:check-footer true}))
                   "test/resources/file.wrong-footer.jpg"))

(deftest should-return-correct-type-without-verifying-footer
  (are [file-path] (= :jpeg (surykatka/get-file-type (.readAllBytes (FileInputStream. (File. ^String file-path))) {:check-footer false}))
                   "test/resources/file.wrong-footer.jpg"))