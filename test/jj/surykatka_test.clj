(ns jj.surykatka-test
  (:require [clojure.test :refer [are deftest]]
            [jj.surykatka :as surykatka])
  (:import (java.io File FileInputStream)))

(deftest byte-array-tests
  (are [expected-type file-path] (= expected-type (surykatka/detect-file-type (.readAllBytes (FileInputStream. (File. ^String file-path)))))
                                 :7z "test/resources/file.7z"
                                 :bmp "test/resources/file.bmp"
                                 :bz2 "test/resources/file.bz2"
                                 :gif "test/resources/file.gif"
                                 :gz "test/resources/file.gz"
                                 :jpg "test/resources/file.exif.jpg"
                                 :jpg "test/resources/file.jpg"
                                 :pdf "test/resources/file.pdf"
                                 :png "test/resources/file.png"
                                 :ps "test/resources/file.ps"
                                 :shell-script "test/resources/file.sh"
                                 :tar "test/resources/file.tar"
                                 :webp "test/resources/file.webp"
                                 :wim "test/resources/file.wim"
                                 :xz "test/resources/file.xz"
                                 :zip "test/resources/file.zip"))


(deftest input-stream-tests
  (are [expected-type file-path] (= expected-type (surykatka/detect-file-type (FileInputStream. (File. ^String file-path))))
                                 :7z "test/resources/file.7z"
                                 :bmp "test/resources/file.bmp"
                                 :bz2 "test/resources/file.bz2"
                                 :gif "test/resources/file.gif"
                                 :gz "test/resources/file.gz"
                                 :jpg "test/resources/file.exif.jpg"
                                 :jpg "test/resources/file.jpg"
                                 :pdf "test/resources/file.pdf"
                                 :png "test/resources/file.png"
                                 :ps "test/resources/file.ps"
                                 :shell-script "test/resources/file.sh"
                                 :tar "test/resources/file.tar"
                                 :webp "test/resources/file.webp"
                                 :wim "test/resources/file.wim"
                                 :xz "test/resources/file.xz"
                                 :zip "test/resources/file.zip"))