(ns twijlio.config 
  (:require 
    [clj-http.client :as client]))

;----- declarations

(def ^:dynamic auth {:account-sid (System/getenv "TWILIO_ACCOUNT_SID") :auth-token (System/getenv "TWILIO_AUTH_TOKEN")})

(def base-url "https://api.twilio.com/2010-04-01")

(def get-req      client/get)
(def post-req     client/post)

;----- utility functions

(defn construct-url 
  ([resource id] (str base-url "/Accounts"
                     (if (:target auth) 
                       (when-not (empty? (:target auth)) (str "/" (:target auth)))
                       (when (:account-sid auth) (str "/" (:account-sid auth)))) 
                     (when resource (str "/" resource)) 
                     (when id (str "/" id)) ".json")))

(defmacro with-account  [temp & body] `(binding  [auth ~temp]  (do ~@body)))

(defmacro with-target-sid  [target-sid & body] `(binding  [auth (merge auth {:target ~target-sid})]  (do ~@body)))

(defn my-capitalize [s] (str (clojure.string/capitalize (first s)) (subs s 1)))

(def capitalize-keys #(zipmap (map (comp keyword my-capitalize name) (keys %)) (vals %)))

(def base-headers #(assoc {} :accept :application/json :as :json :throw-entire-message? true))

(def get-headers #(merge (base-headers) {:content-type :application/json}))

(def post-headers #(merge (base-headers) {:content-type :x-www-form-urlencoded}))

(def auth-headers #(assoc {} :basic-auth [(:account-sid auth) (:auth-token auth)]))
