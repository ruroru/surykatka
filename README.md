# Surykatka
A Clojure library that identifies file types by reading their magic numbers.

## Installation
```[org.clojars.jj/surykatka "1.0.0"]```

## Usage

``` clojure
(:require [jj.surykatka :as surykatka])

(= :postscript (surykatka/detect-file-type (.readAllBytes (FileInputStream. (File. "test/resources/file.ps"))))) 
(= "image/jpeg" (surykatka/get-mime-type (.readAllBytes (FileInputStream. (File. "test/resources/file.jpg"))))) 
```

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
