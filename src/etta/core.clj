(ns etta.core
  (:require clojure.string)
  (:require [net.cgrand.enlive-html :as html]))

(defn fetch-url [url]
  (html/html-resource (java.net.URL. url)))

(defn links [doc]
  (map (comp :href :attrs) (html/select doc [:a])))

(defn base-url [doc]
  (first (map (comp :href :attrs) (html/select doc [:base]))))

(def domain-re #"^\w+\://(\w|\.)+/?")

(defn has-domain? [url]
  (boolean (seq (re-seq domain-re url))))

(defn get-domain [url]
  (first (first (re-seq domain-re url))))

(defn is-rel? [url]
  (not (seq (re-seq #"^/" url))))

(defn force-directory [path]
  (clojure.string/replace (str "/" path "/") #"/+" "/"))

(defn normalize-link [domain base link]
  (cond
   (has-domain? link) link
   (is-rel? link) (str domain (force-directory base) link)
   :else (str domain link)))

(defn links-for-url [url doc]
  (let [domain (get-domain url)
        doc-base (base-url doc)
        base (if doc-base doc-base "/")]
    (map (partial normalize-link domain base) (links doc))))

