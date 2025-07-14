(defproject org.clojars.jj/surykatka "1.3.1-SNAPSHOT"
  :description "A Clojure library that identifies file type based on magic numbers."

  :url "https://github.com/ruroru/surykatka"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url  "https://www.eclipse.org/legal/epl-2.0/"}

  :dependencies [[org.clojure/clojure "1.12.1"]
                 [org.clojure/tools.logging "1.3.0"]]

  :deploy-repositories [["clojars" {:url      "https://repo.clojars.org"
                                    :username :env/clojars_user
                                    :password :env/clojars_pass}]]


  :resource-paths ["src/resources"]
  :profiles {:test {:global-vars {*warn-on-reflection* true}}}

  :cljfmt {
           :indentation?                          false
           :remove-multiple-non-indenting-spaces? true
           :split-keypairs-over-multiple-lines?   true
           :sort-ns-references?                   true
           :function-arguments-indentation        :cursive
           }

  :aot [jj.surykatka]
  :plugins [[org.clojars.jj/bump "1.0.4"]
            [org.clojars.jj/strict-check "1.0.2"]
            [org.clojars.jj/bump-md "1.0.0"]
            [dev.weavejester/lein-cljfmt "0.13.1"]
            [lein-ancient "0.7.0"]])
