(defproject landing-page "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.11.1"]
                 [ring-server "0.5.0"]
                 [reagent "1.2.0"]
                 [reagent-utils "0.3.4"]
                 [ring "1.9.5"]
                 [ring/ring-defaults "0.3.3"]
                 [hiccup "1.0.5"]
                 [yogthos/config "1.2.0"]
                 [org.clojure/clojurescript "1.11.54"
                  :scope "provided"]
                 [metosin/reitit "0.7.0-alpha7"]
                 [pez/clerk "1.0.0"]
                 [venantius/accountant "0.2.5"
                  :exclusions [org.clojure/tools.reader]]
                 [arttuka/reagent-material-ui "5.11.12-0"]
                 [re-frame "1.4.0"]]

  :jvm-opts ["-Xmx1G"]
  
  :plugins [[lein-environ "1.1.0"]
            [lein-cljsbuild "1.1.7"]
            [lein-asset-minifier "0.4.6"
             :exclusions [org.clojure/clojure]]]

  :ring {:handler landing-page.handler/app
         :uberwar-name "landing-page.war"}

  :min-lein-version "2.5.0"
  :uberjar-name "landing-page.jar"
  :main landing-page.server
  :clean-targets ^{:protect false}
  [:target-path
   [:cljsbuild :builds :app :compiler :output-dir]
   [:cljsbuild :builds :app :compiler :output-to]]

  :source-paths ["src/clj" "src/cljc" "src/cljs"]
  :resource-paths ["resources" "target/cljsbuild"]

  :minify-assets
  [[:css {:source "resources/public/css/site.css"
          :target "resources/public/css/site.min.css"}]]

  :cljsbuild
  {:builds {:min
            {:source-paths ["src/cljs" "src/cljc" "env/prod/cljs"]
             :compiler
             {:output-to        "target/cljsbuild/public/js/app.js"
              :output-dir       "target/cljsbuild/public/js"
              :source-map       "target/cljsbuild/public/js/app.js.map"
              :optimizations :advanced
              :infer-externs true
              :pretty-print  false}}
            :app
            {:source-paths ["src/cljs" "src/cljc" "env/dev/cljs"]
             :figwheel {:on-jsload "landing-page.core/mount-root"}
             :compiler
             {:main "landing-page.dev"
              :asset-path "/js/out"
              :output-to "target/cljsbuild/public/js/app.js"
              :output-dir "target/cljsbuild/public/js/out"
              :source-map true
              :optimizations :none
              :pretty-print  true}}



            }
   }

  :figwheel
  {:http-server-root "public"
   :server-port 3449
   :nrepl-port 7002
   :nrepl-middleware [cider.piggieback/wrap-cljs-repl
                      ]
   :css-dirs ["resources/public/css"]
   :ring-handler landing-page.handler/app}



  :profiles {:dev {:repl-options {:init-ns landing-page.repl}
                   :dependencies [[cider/piggieback "0.5.3"]
                                  [binaryage/devtools "1.0.6"]
                                  [ring/ring-mock "0.4.0"]
                                  [ring/ring-devel "1.9.5"]
                                  [prone "2021-04-23"]
                                  [figwheel-sidecar "0.5.20"]
                                  [nrepl "0.9.0"]
                                  [thheller/shadow-cljs "2.16.7"]
                                  [pjstadig/humane-test-output "0.11.0"]
                                  
 ]

                   :source-paths ["env/dev/clj"]
                   :plugins [[lein-figwheel "0.5.20"]
]

                   :injections [(require 'pjstadig.humane-test-output)
                                (pjstadig.humane-test-output/activate!)]

                   :env {:dev true}}

             :shadow-cljs {:dependencies [[com.google.javascript/closure-compiler-unshaded "v20211201"]]}

             :uberjar {:hooks [minify-assets.plugin/hooks]
                       :source-paths ["env/prod/clj"]
                       :prep-tasks ["compile" ["cljsbuild" "once" "min"]]
                       :env {:production true}
                       :aot :all
                       :omit-source true}})
