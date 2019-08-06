(ns demo-chat.views
  (:require
   [reagent.core  :as ra]
   [re-frame.core :as rf]
   
   [demo-chat.events :as events]
   [demo-chat.subs :as subs]
   [demo-chat.db :as db]))

(defn user-label [user]
  [:div.user-label 
   [:span.user-label__text (::db/name user)]])

(defn active-user []
  (let [user (rf/subscribe [::subs/user])]
    [:div.active-user 
     [:span.active-user__label "Name: "] 
     [user-label @user]]))

(defn history-item [msg]
  [:div.history-item
   {:class (when (::subs/own msg) "history-item_own")}
   [:span (::db/text msg)]
   [:span.history-item__separator ":"]
   [user-label (::db/from msg)]])

(defn history [history]
  [:div.history
   (map-indexed
    (fn [idx msg] ^{:key idx} [history-item msg])
    history)])

(defn message-input []
  (let [text (ra/atom "")
        on-send (fn []
                  (rf/dispatch [::events/send-message @text])
                  (reset! text ""))]
    (fn [] [:div.message-input
            [:input.message-input__input
             {:type "Text"
              :value @text
              :tab-index 0
              :on-change (fn [e] (reset! text (-> e .-target .-value)))
              :on-key-down (fn [e] (when (= (.-key e) "Enter") (on-send)))}]
            [:div.message-input__send
             {:on-click (fn [e] (on-send))}
             [:span "Send"]]])))

(defn chat []
  (let [hs (rf/subscribe [::subs/history])]
    [:div.chat 
     [:div.chat__header [active-user]]
     [:div.chat__body
      [history @hs] [message-input]]]))

(defn login []
  (let [name (ra/atom "")]
    (fn []
      [:div.login
       [:input.login__name-input
        {:type "Text"
         :value @name
         :tab-index 0
         :on-change (fn [e] (reset! name (-> e .-target .-value)))}]
       [:div.login__login-button
        {:on-click (fn [e]
                     (rf/dispatch [::events/logged-in {::db/name @name}]))}
        [:span "Login"]]])))

(defn app []
  (let [user (rf/subscribe [::subs/user])]
    (if @user
      [chat]
      [login])))