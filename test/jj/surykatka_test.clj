(ns jj.surykatka-test
  (:require [clojure.test :refer [are deftest]]
            [jj.surykatka :as surykatka])
  (:import (java.io File FileInputStream)))

(deftest byte-array-tests
  (are [expected-type file-path] (= expected-type (surykatka/detect-file-type (.readAllBytes (FileInputStream. (File. ^String file-path)))))
                                 :7z "test/resources/file.7z"
                                 :bmp "test/resources/file.bmp"
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
                                 :xz "test/resources/file.xz"
                                 :zip "test/resources/file.zip"))

(deftest get-mime-type
  (are [expected-type file-path] (= expected-type (surykatka/get-mime-type (.readAllBytes (FileInputStream. (File. ^String file-path)))))
                                 "application/7z" "test/resources/file.7z"
                                 "image/bmp" "test/resources/file.bmp"
                                 "image/gif" "test/resources/file.gif"
                                 "application/gzip" "test/resources/file.gz"
                                 "image/jpeg" "test/resources/file.exif.jpg"
                                 "image/jpeg" "test/resources/file.jpg"
                                 "application/pdf" "test/resources/file.pdf"
                                 "image/png" "test/resources/file.png"
                                 "application/postscript" "test/resources/file.ps"
                                 "text/x-shellscript" "test/resources/file.sh"
                                 "application/x-tar" "test/resources/file.tar"
                                 "image/webp" "test/resources/file.webp"
                                 "application/x-xz" "test/resources/file.xz"
                                 "application/zip" "test/resources/file.zip"))

(deftest should-fail-because-of-wrong-footer
  (are [file-path] (= nil (surykatka/get-mime-type (.readAllBytes (FileInputStream. (File. ^String file-path)))))
                   "test/resources/file.wrong-footer.jpg"))
