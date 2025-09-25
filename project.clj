(defproject org.clojars.jj/surykatka "1.4.1-SNAPSHOT"
  :description "A Clojure library that identifies file type based on magic numbers."

  :url "https://github.com/ruroru/surykatka"
  :license {:name "EPL-2.0"
            :url  "https://www.eclipse.org/legal/epl-2.0/"}

  :dependencies [[org.clojure/clojure "1.12.3"]
                 [org.clojure/tools.logging "1.3.0"]]

  :deploy-repositories [["clojars" {:url      "https://repo.clojars.org"
                                    :username :env/clojars_user
                                    :password :env/clojars_pass}]]


  :resource-paths ["src/resources"]
  :profiles {:test {:global-vars {*warn-on-reflection* true}}}

  :plugins [[org.clojars.jj/bump "1.0.4"]
            [org.clojars.jj/strict-check "1.1.0"]
            [org.clojars.jj/bump-md "1.1.0"]])
