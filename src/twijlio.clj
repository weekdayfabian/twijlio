(ns twijlio 
  (:require 
    [clj-http.client          :as client]
    [twijlio.config           :refer [get-req 
                                      post-req 
                                      get-headers 
                                      post-headers 
                                      auth-headers
                                      construct-url 
                                      with-account 
                                      with-target-sid
                                      twilio-keywords] 
                              :as conf]))

(def route-maps 
  {:accounts                {:resource nil}
   :calls                   {:resource "Calls"} 
   :conferences             {:resource "Conferences"} ;; TODO
   :media                   {:resource "Messages" :subresource "Media"}
   :messages                {:resource "Messages"}
   :incoming_phone_numbers  {:resource "IncomingPhoneNumbers"}
   :queues                  {:resource "Queues"} ;; TODO
   :recordings              {:resource "Recordings"}})

(defn twilio-get [entity extra-params] 
  (let [url     (construct-url (into extra-params (get route-maps entity)))
        payload (merge (get-headers) 
                       {:query-params (twilio-keywords (:query extra-params))} 
                       (auth-headers)) 
        resp (get-req url payload)]
    (:body resp)))


(defn twilio-post [entity extra-params] 
   (let [url      (construct-url (into extra-params (get route-maps entity)))
         payload  (merge (post-headers) 
                        (when (:form extra-params) {:form-params (twilio-keywords (:form extra-params))}) 
                        (when (:query extra-params) {:query-params (twilio-keywords (:query extra-params))}) 
                        (auth-headers)) 
         resp (post-req url payload)] 
     (:body resp)))

;----- calls

(defn get-calls 
  ([] (get-calls {}))
  ([params] (twilio-get :calls {:query params})))

(defn get-call [sid] 
  (twilio-get :calls {:id sid}))

(defn make-call [to from params]
  (twilio-post :calls {:form (merge {:to to :from from} params)}))

(defn modify-call [sid params] 
  (twilio-post :calls {:id sid :form params}))

(defn hangup-call [sid] 
  (modify-call sid {:status "completed"}))

;----- messages

(defn send-message [to from params] 
  (twilio-post :messages {:form (merge {:to to :from from} params)}))

(defn get-messages 
  ([] (get-messages {}))
  ([params] (twilio-get :messages {:query params})))

(defn get-message [sid] 
  (twilio-get :messages {:id sid}))

(defn get-messages-for-number [number] 
  (into [] 
        (concat 
          (:messages (get-messages {:to number}))
          (:messages (get-messages {:from number})))))

;----- numbers

(defn get-incoming-phone-numbers 
  ([] (get-incoming-phone-numbers {})) 
  ([params] (twilio-get :incoming_phone_numbers {:query params})))

(defn get-incoming-phone-number [sid] 
  (twilio-get :incoming_phone_numbers {:id sid}))

(defn modify-incoming-phone-number [sid params] 
  (twilio-post :incoming_phone_numbers {:id sid :form params}))

(defn create-incoming-phone-number [params] 
  (twilio-post :incoming_phone_numbers params))

;----- recordings

(defn get-recordings 
  ([] (get-recordings {}))
  ([params] (twilio-get :recordings {:query params})))

(defn get-recording [sid] 
  (twilio-get :recordings {:id sid}))

;----- accounts

(defn get-accounts 
  ([] (get-accounts {}))
  ([params] (with-target-sid "" (twilio-get :accounts {}))))

(defn get-account [sid] 
  (with-target-sid sid (twilio-get :accounts {})))

(defn modify-account [sid params] 
  (with-target-sid sid (twilio-post :accounts {:form params})))

(defn create-subaccount [params] 
  (twilio-post :accounts {:form params}))
