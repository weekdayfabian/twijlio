(ns twijlio.config 
  (:require 
    [clj-http.client :as client]
    [camel-snake-kebab.core   :refer :all]
    [camel-snake-kebab.extras :refer [transform-keys]]))

;----- declarations

(def ^:dynamic auth {:account-sid (System/getenv "TWILIO_ACCOUNT_SID") :auth-token (System/getenv "TWILIO_AUTH_TOKEN")})

(def base-url "https://api.twilio.com/2010-04-01")

(def get-req      client/get)
(def post-req     client/post)

;----- utility functions

(defn construct-url [res] 
  (str (clojure.string/join "/" 
    (remove clojure.string/blank? [base-url "Accounts"
                                   (or (:target auth) (:account-sid auth)) 
                                   (or (:resource res))
                                   (or (:id res))
                                   (or (:subresource res))
                                   (or (:subresource-id res))]))
       ".json"))

(defmacro with-account  [temp & body] `(binding  [auth ~temp]  (do ~@body)))

(defmacro with-target-sid  [target-sid & body] `(binding  [auth (merge auth {:target ~target-sid})]  (do ~@body)))

(def set-account-auth! #(reset! auth %))

(def twilio-keywords #(transform-keys ->PascalCaseKeyword %))

(def base-headers #(assoc {} :accept :application/json :as :json :throw-entire-message? true))

(def get-headers #(merge (base-headers) {:content-type :application/json}))

(def post-headers #(merge (base-headers) {:content-type :x-www-form-urlencoded}))

(def auth-headers #(assoc {} :basic-auth [(:account-sid auth) (:auth-token auth)]))
