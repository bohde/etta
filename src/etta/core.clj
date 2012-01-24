(ns etta.core
  (:require [net.cgrand.enlive-html :as html]))

(defn fetch-url [url]
  (html/html-resource (java.net.URL. url)))

(defn links [doc]
  (map (comp :href :attrs) (html/select doc [:a])))

;(links (fetch-url "http://google.com/"))


