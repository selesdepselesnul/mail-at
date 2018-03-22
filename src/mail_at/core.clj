(ns mail-at.core
  (:gen-class)
  (:require [postal.core :as postal]
            [hara.io.scheduler :as scheduler]
            [clojure.string :as str]))

(defn send-email [prop]
  (let [{:keys [host pass email subject body receivers]} prop]
    (doseq [receiver receivers]
      (postal/send-message
       {:host host
        :user email
        :pass pass
        :ssl true}
       {:from email
        :to receiver
        :subject subject
        :body body}))))

(defn send-html-email [prop pass]
  (let [body [{:type "text/html"
               :content (slurp (:html-path prop))}]]
    (send-email (assoc prop :body body :pass pass))))

(defn run-at [time handler]
  (let [time-splitted (str/split time #":")]
    (scheduler/start!
     (scheduler/scheduler
        {:print-task
         {:handler handler
          :schedule
          (format "01 %s %s * * * *"
                  (nth time-splitted 1)
                  (nth time-splitted 0))}}))))

(defn read-config [path]
  (read-string (slurp path)))

(defn send-email-by-schedule [prop pass]
  (run-at (:run-at prop)
          #(send-html-email prop pass)))

(defn -main
  [& args]
  (println "Init !"))


