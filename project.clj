(defproject demo-chat "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.clojure/clojurescript "1.10.520"]
                 [org.clojure/core.async "0.4.500"]
                 [reagent "0.8.1"]
                 [re-frame "0.10.8"]
                 [compojure "1.6.1"]
                 [yogthos/config "1.1.2"]
                 [ring "1.7.1"]
                 [http-kit "2.3.0"]
                 [pneumatic-tubes "0.3.0"]
                 [com.taoensso/carmine "2.19.1"]]

  :plugins [[lein-cljsbuild "1.1.7"]
            [jonase/eastwood "0.3.5"]]
  
  :min-lein-version "2.7.1"

  :source-paths ["src/clj" "src/cljc"]

  :test-paths ["test/clj"]

  :clean-targets ^{:protect false} ["resources/public/js/compiled" "target"
                                    "test/js"]

  :figwheel {:css-dirs ["resources/public/css"]
             :ring-handler demo-chat.handler/dev-handler}

  :profiles
  {:dev
   {:plugins      [[lein-figwheel "0.5.18"]
                   [lein-doo "0.1.8"]]}
   :prod {}
   :uberjar {:source-paths ["env/prod/clj"]
             :omit-source  true
             :main         demo-chat.server
             :aot          [demo-chat.server]
             :uberjar-name "demo-chat.jar"
             :prep-tasks   ["compile" ["cljsbuild" "once" "min"]]}
   }

  :cljsbuild
  {:builds
   [{:id           "dev"
     :source-paths ["src/cljs" "src/cljc"]
     :figwheel     {:on-jsload "demo-chat.core/mount-root"}
     :compiler     {:main                 demo-chat.core
                    :output-to            "resources/public/js/compiled/app.js"
                    :output-dir           "resources/public/js/compiled/out"
                    :asset-path           "js/compiled/out"
                    :source-map-timestamp true
                    :preloads             [devtools.preload]
                    :external-config      {:devtools/config {:features-to-install :all}}
                    }}

    {:id           "min"
     :source-paths ["src/cljs" "src/cljc"]
     :jar true
     :compiler     {:main            demo-chat.core
                    :output-to       "resources/public/js/compiled/app.js"
                    :optimizations   :advanced
                    :closure-defines {goog.DEBUG false}
                    :pretty-print    false}}]}
  )
