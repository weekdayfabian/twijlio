(ns twijlio.config 
  (:require 
    [clj-http.client :as client]))

;----- declarations

(def account-sid  (System/getenv "TWILIO_ACCOUNT_SID"))
(def auth-token  (System/getenv "TWILIO_AUTH_TOKEN"))

(def base-url "https://api.twilio.com/2010-04-01")

(def get-req      client/get)
(def post-req     client/post)

;----- utility functions

(defn construct-url 
  ([resource id] (str base-url "/Accounts/" account-sid 
                      (when resource (str "/" resource)) 
                      (when id ("/" id)) ".json")))

(defn my-capitalize [s] (str (clojure.string/capitalize (first s)) (subs s 1)))

(def capitalize-keys #(zipmap (map (comp keyword my-capitalize name) (keys %)) (vals %)))

(def base-headers #(assoc {} :accept :application/json :as :json :throw-entire-message? true))

(def get-headers #(merge (base-headers) {:content-type :application/json}))

(def post-headers #(merge (base-headers) {:content-type :x-www-form-urlencoded}))

(def auth-headers #(assoc {} :basic-auth [account-sid auth-token]))
