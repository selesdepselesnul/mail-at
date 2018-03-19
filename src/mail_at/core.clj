(ns mail-at.core
  (:gen-class)
  (:require [postal.core :as postal]))

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

(defn -main
  [& args]
  (println "Init !"))


