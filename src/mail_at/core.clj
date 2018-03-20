(ns mail-at.core
  (:gen-class)
  (:require [postal.core :as postal]
            [hara.io.scheduler :as scheduler]
            [clojure.string :as str]))

(defn send-email [prop pass]
  (let [{:keys [host user email subject body receivers]} prop]
    (doseq [receiver receivers]
      (postal/send-message
       {:host host
        :user user
        :pass pass
        :ssl true}
       {:from email
        :to receiver
        :subject subject
        :body body}))))

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

(defn -main
  [& args]
  (println "Init !"))


