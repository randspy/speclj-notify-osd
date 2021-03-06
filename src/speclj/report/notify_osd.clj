(ns speclj.report.notify-osd
  (:require [speclj.results :refer [categorize]]
            [speclj.platform :refer [format-seconds]]
            [speclj.reporting :refer [tally-time]]
            [speclj.report.progress :refer [describe-counts-for]]
            [clojure.java.io :refer [input-stream resource as-url]]
            [clojure.java.shell :refer [sh]])
  (:import [speclj.reporting Reporter]))

(defn- settings [type]
  (case type
    :pass {:name "Success" :icon "dialog-ok"}
    :fail {:name "Failure" :icon "dialog-close"}
    :error {:name "Error" :icon "dialog-close" }
    :compilation-error {:name "Error" :icon "dialog-close"}))

(defn notify-osd [result message]
  (when (not= result :error)
    (let [params (settings result)]
      (try
        (sh "notify-send" "-i" (:icon params) (:name params) message)
        (catch Exception e (println "Caught exception: " (.getMessage e)))))))

(defn get-status [result]
  (cond
   (not= 0 (count (:error result))) :error
   (not= 0 (count (:fail result))) :fail
   :else :pass))

(defn notify-osd-message [results]
  (let [result-map (categorize results)
        result     (get-status result-map)
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
        (println "error")
        (notify-osd :compilation-error (format "%s: %s"
                              (.getSimpleName (class exception))
                              (.getMessage exception))))))

(defn new-notify-osd-reporter []
    (NotifyOsdReporter.))
