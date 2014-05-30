(ns speclj.report.notify-osd
  (:require [speclj.results :refer [categorize]]
            [speclj.platform :refer [format-seconds]]
            [speclj.reporting :refer [tally-time]]
            [speclj.report.progress :refer [describe-counts-for]]
            [clojure.java.io :refer [input-stream resource as-url]]
            [clojure.java.shell :refer [sh]])
  (:import [speclj.reporting Reporter]))

;;(with-out-str (sh "notify-send" "-i" "dialog-close" "Bo" "ra"))

(defn notify-osd [result message]
  "test")

(defn notify-osd-message [results]
  (let [result-map (categorize results)
        result     (if (= 0 (count (:fail result-map))) :pass :fail)
        seconds    (format-seconds (tally-time results))
        outcome    (describe-counts-for result-map)
        message    (format "%s\nTook %s seconds" outcome seconds)]
    (notify-osd result message)))

(deftype NotifyOsdReporter []
    Reporter
    (report-message [this message])
    (report-description [this description])
    (report-pass [this result])
    (report-pending [this result])
    (report-fail [this result])
    (report-runs [this results] (notify-osd-message results))
    (report-error [this error]
      (let [exception (.-exception error)]
        (notify-osd :error (format "%s: %s"
                              (.getSimpleName (class exception))
                              (.getMessage exception))))))

(defn new-notify-osd-reporter []
    (NotifyOsdReporter.))
