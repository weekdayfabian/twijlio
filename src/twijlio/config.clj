(ns twijlio.config 
  (:require 
    [clj-http.client :as client]))

;----- declarations

(def ^:dynamic auth {:account-sid (System/getenv "TWILIO_ACCOUNT_SID") :auth-token (System/getenv "TWILIO_AUTH_TOKEN")})

(def base-url "https://api.twilio.com/2010-04-01")

(def get-req      client/get)
(def post-req     client/post)

;----- utility functions

(defn construct-url [res] 
  (clojure.string/join "/" (remove clojure.string/blank? [base-url "Accounts"
                                                          (or (:target auth) (:account-sid auth)) 
                                                          (or (:resource res))
                                                          (or (:id res))
                                                          (or (:subresource res))
                                                          (or (:subresource-id res))
                                                          ".json"])))

(defmacro with-account  [temp & body] `(binding  [auth ~temp]  (do ~@body)))

(defmacro with-target-sid  [target-sid & body] `(binding  [auth (merge auth {:target ~target-sid})]  (do ~@body)))

(def set-account-auth! [sid token] (reset! auth {:account-sid sid :auth-token token}))

(def my-capitalize #(str (clojure.string/capitalize (first %)) (subs % 1)))

(def capitalize-keys #(zipmap (map (comp keyword my-capitalize name) (keys %)) (vals %)))

(def base-headers #(assoc {} :accept :application/json :as :json :throw-entire-message? true))

(def get-headers #(merge (base-headers) {:content-type :application/json}))

(def post-headers #(merge (base-headers) {:content-type :x-www-form-urlencoded}))

(def auth-headers #(assoc {} :basic-auth [(:account-sid auth) (:auth-token auth)]))
