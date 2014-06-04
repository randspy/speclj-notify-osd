(defproject speclj-notify-osd "0.0.2"
  :description "Notify-osd reporter for the speclj testing framework"
  :url "https://github.com/randspy/speclj-notify-osd"
  :license {:name "MIT License"
            :url "http://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.5.1"]]
  :profiles {
             :dev {
                   :plugins [[speclj "3.0.2"]]
                   :test-paths ["spec"]
                   :dependencies [[speclj "3.0.2"]]}})
