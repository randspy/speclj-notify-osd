(ns speclj.speclj.report.notify-osd
  (:require [speclj.components :refer [new-characteristic new-description]]
            [speclj.reporting :refer [report-runs report-error]]
            [speclj.results :refer [pass-result fail-result pending-result error-result]]
            [speclj.core :refer :all]
            [speclj.report.notify-osd :as osd]))

(describe "notify osd reporter"
  (with reporter (osd/new-notify-osd-reporter))
  (with result (atom nil))
  (with message (atom nil))

  (around [it]
    (with-redefs [osd/notify-osd (fn [_result _message]
                            (reset! @result _result)
                            (reset! @message _message))]
      (it)))

  (describe "report-runs"

    (it "is a summary information for no test runs"
      (report-runs @reporter [])
      (let [output (with-out-str (report-runs @reporter []))]
        (should= :pass @@result)
        (should= "0 examples, 0 failures\nTook 0.00000 seconds" @@message)))

    (it "is a successful run"
      (let [results [(pass-result nil 0.1) (pass-result nil 0.02)]
            output (with-out-str (report-runs @reporter results))]
        (should= :pass @@result)
        (should= "2 examples, 0 failures\nTook 0.12000 seconds" @@message)))

    (it "is an unsuccessful run"
      (let [results [(pass-result nil 0.1)
                     (fail-result nil 2 1)]
            output (with-out-str (report-runs @reporter results))]
        (should= :fail @@result)
        (should= "2 examples, 1 failures\nTook 2.10000 seconds" @@message)))
        (it "growls a run with pending"
      (let [results [(pass-result nil 0.1)
                     (pending-result nil 1 (speclj.SpecPending. "blah"))]
            output (with-out-str (report-runs @reporter results))]
        (should= :pass @@result)
        (should= "2 examples, 0 failures, 1 pending\nTook 1.10000 seconds"
                 @@message)))

    (it "is a run with failing and pending tests"
      (let [results [(pass-result nil 0.1)
                     (fail-result nil 1.1 0)
                     (pending-result nil 1 0)]
            output (with-out-str (report-runs @reporter results))]
        (should= :fail @@result)
        (should= "3 examples, 1 failures, 1 pending\nTook 2.20000 seconds"
                 @@message)))

    (it "is a run with pass and error tests"
      (let [results [(pass-result nil 0.1)
                     (error-result (#(java.lang.Exception. "Failed to compile")))]
            output (with-out-str (report-runs @reporter results))]
        (should= :error @@result)
        (should= "2 examples, 0 failures, 1 errors\nTook 0.10000 seconds"
                 @@message)))

    (it "is a run with a compilation errors"
      (let [error (error-result (#(java.lang.Exception. "Failed to compile")))
            output (with-out-str (report-error @reporter error))]
        (should= :compilation-error @@result)
        (should= "Exception: Failed to compile" @@message)))))
