(ns twijlio 
  (:require 
    [clj-http.client :as client] 
    [twijlio.config :refer [get-req 
                           post-req 
                           get-headers 
                           post-headers 
                           auth-headers
                           construct-url] 
                   :as conf]))

(def route-maps 
  {:accounts    {:route nil}
   :messages    {:route "Messages"}
   :calls       {:route "Calls"} 
   :recordings  {:route "Recordings"}})

(defn twilio-get 
  [entity extra-params] 
  (let [target (:route (get route-maps entity))
        url (construct-url target (:id extra-params))
        payload (merge (get-headers) 
                       {:query-params (:query extra-params)} 
                       (auth-headers)) 
        resp (get-req url payload)] 
    (:body resp)))


(defn twilio-post 
  [entity extra-params] 
   (let [target (:route (get route-maps entity)) 
         url (construct-url target (:id extra-params))
         payload (merge (post-headers) 
                        (when (:form extra-params) {:form-params (conf/capitalize-keys (:form extra-params))}) 
                        (when (:query extra-params) {:query-params (:query extra-params)}) 
                        (auth-headers)) 
         resp (post-req url payload)] 
     (:body resp)))

;----- calls

(defn get-calls 
  ([] (get-calls {}))
  ([params] (twilio-get :calls {:query params})))

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

;----- recordings

(defn get-recordings 
  ([] (get-recordings {}))
  ([params] (twilio-get :recordings {:query params})))

;----- accounts

(defn get-account-info [] 
  (twilio-get :accounts {}))
