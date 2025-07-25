# Surykatka

[![Clojars Project](https://img.shields.io/clojars/v/org.clojars.jj/surykatka.svg)](https://clojars.org/org.clojars.jj/surykatka)

A Clojure library that identifies file type based on magic numbers.

## Installation

Add surykatka to your dependency list with:

```[org.clojars.jj/surykatka "1.3.1"]```

## Usage

``` clojure
(:require [jj.surykatka :as surykatka])

;; byte array
(= :postscript (surykatka/get-file-type (.readAllBytes (FileInputStream. (File. "test/resources/file.ps"))))) 
(= "image/jpeg" (surykatka/get-mime (.readAllBytes (FileInputStream. (File. "test/resources/file.jpg"))))) 

;; Or InputStream
(= :jpeg (surykatka/get-file-type (FileInputStream. (File. ^String file-path))))


```

If verification of trailing bytes is not wanted:

``` clojure
(= "image/jpeg" (surykatka/get-mime (.readAllBytes (FileInputStream. (File. "test/resources/file.jpg"))) {:check-footer false} )) 
```

| suppported-types         |
|--------------------------|
| 7z                       |
| bmp                      |
| db                       |
| gif                      |
| gzip                     |
| jpeg                     |
| pdf                      |
| png                      |
| postscript               |
| shellscript              |
| tar                      |
| webp                     |
| x509-certificate         |
| x509-certificate-request |
| x509-dsa-private-key     |
| x509-private-key         |
| x509-rsa-private-key     |
| xml                      |
| xz                       |
| zip                      |



# Limitations
* **Footer verification is disabled** when using InputStream inputs. Only header-based file type detection is performed since the library reads only the first 512 bytes for efficiency.
* **Stream consumption**: InputStreams are partially consumed during file type detection. If you need to use the same InputStream after detection, wrap it with a `BufferedInputStream` and call `mark()` before passing it to the detection functions, then `reset()` afterward to rewind the stream.

## License

Copyright © 2025 [ruroru](https://github.com/ruroru)

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
