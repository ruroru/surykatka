(defproject org.clojars.jj/surykatka "1.0.0-SNAPSHOT"
  :description "Surykatka is a clojure library, that detect file type based on it's signature. "
  :url "https://github.com/ruroru/surykatka"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url  "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [org.clojure/tools.logging "1.3.0"]]

  :deploy-repositories [["clojars" {:url      "https://repo.clojars.org"
                                    :username :env/clojars_user
                                    :password :env/clojars_pass}]]


  :profiles {:test {:global-vars {*warn-on-reflection* true}}}

  :plugins [[org.clojars.jj/bump "1.0.4"]
            [lein-ancient "0.7.0"]])
