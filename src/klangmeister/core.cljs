(ns klangmeister.core
  (:require
    [klangmeister.processing] ; Import action defs.
    [klangmeister.eval :as eval]
    [klangmeister.views :as view]
    [klangmeister.framework :as framework]
    [reagent.core :as reagent]))

(def initial-text
  "
(defn fuzz! [{:keys [duration pitch]}]
   (inst/>>> (inst/saw pitch 1.5)
     (inst/percuss 0.1 0.5)
     (inst/volume 0.1)
     inst/destination))

(->> (melody/phrase [1 1/2 1/2 1 1 2 2]
                    [0 1 0 2 -3 1 -1])
     (melody/with (melody/phrase [1 1/2 1/2 1 1 1/2 1/2 1/2 1/2 2]
                                 [4 4 5 4 7 6 7 6 5 4]))
     (melody/wherever (constantly true) :instrument (melody/is fuzz!))
     (melody/where :time (melody/bpm 120))
     (melody/where :duration (melody/bpm 120))
     (melody/where :pitch (comp scale/high scale/C scale/sharp scale/major)))")

(defonce state-atom
  (reagent/atom
    {:looping? false
     :error nil
     :text initial-text
     :music (-> initial-text eval/uate :value)}))

(defn reload! []
  (swap! state-atom identity))

(defn mount-root []
  (reagent/render
    [view/render (framework/handler-for state-atom) state-atom]
    js/document.body))

(defn main []
  (mount-root))

(main)
