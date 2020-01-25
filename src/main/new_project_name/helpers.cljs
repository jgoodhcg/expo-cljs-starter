(ns new-project-name.helpers)

(def <sub (comp deref re-frame.core/subscribe))

(def >evt re-frame.core/dispatch)
