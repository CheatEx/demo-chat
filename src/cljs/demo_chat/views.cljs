(ns demo-chat.views
  (:require
   [clojure.string :as str]
   [reagent.core  :as ra]
   [re-frame.core :as rf]
   
   [demo-chat.events :as events]
   [demo-chat.subs :as subs]
   [demo-chat.db :as db]))

(defn user-label [user me?]
  [:div.user-label 
   {:class (when me? "user-label_own")}
   [:span.user-label__text (::db/name user)]])

(defn active-user []
  (let [user (rf/subscribe [::subs/user])]
    [:div.active-user 
     [:span.active-user__label "Name: "] 
     [user-label @user true]]))

(defn history-item [msg]
  [:div.history-item
   {:class (when (::subs/own msg) "history-item_own")}
   [user-label (::db/from msg) (::subs/own msg)]
   [:span.history-item__separator ":"]
   [:span (::db/text msg)]])

(defn history [history]
  (let [!ref (atom nil)]
    (ra/create-class
     {:display-name "history"
      
      :component-did-update
      (fn []
        (when @!ref
          (set! (.-scrollTop @!ref) (.-scrollHeight @!ref))))
      
      :reagent-render
      (fn [history]
        [:div.history {:ref (fn [com] (reset! !ref com))}
         (map-indexed
          (fn [idx msg] ^{:key idx} [history-item msg])
          history)])
      })))

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

(defn status-icon []
  (let [connected (rf/subscribe [::subs/connected])]
    [:div.status-icon {:class (when @connected "status-icon_connected")}
     [:div.status-icon__light {:title (if @connected "Connected" "Disconnected")}]]))

(defn chat-header []
  [:div.chat-header [status-icon] [active-user]])

(defn chat []
  (let [hs (rf/subscribe [::subs/history])]
    [:div.chat 
     [chat-header]
     [:div.chat__body
      [history @hs] [message-input]]]))

(defn valid-name? [name]
  (<= (count name) 32))

(defn login []
  (let [name (ra/atom "")]
    (fn []
      (let [valid (valid-name? @name)]
        [:div.login {:class (when-not valid "login_invalid")}
         [:input.login__name-input
          {:type "Text"
           :value @name
           :tab-index 0
           :on-change (fn [e] (reset! name (-> e .-target .-value)))}]
         [:div.login__login-button
          (when valid
            {:on-click (fn [e] (rf/dispatch [::events/logged-in {::db/name @name}]))})
          [:span "Login"]]]))))

(defn app []
  (let [user (rf/subscribe [::subs/user])]
    (if @user
      [chat]
      [login])))