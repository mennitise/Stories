package com.fiuba.stories.stories.utils;

import com.fiuba.stories.stories.StoriesApp;
import com.google.firebase.messaging.FirebaseMessaging;

public class MyFirebaseCloudMessagingConfigure {

    private StoriesApp app;

    public MyFirebaseCloudMessagingConfigure(StoriesApp app){
        this.app = app;
    }

    public void suscribeTopics(){
        FirebaseMessaging.getInstance().subscribeToTopic("news");
        String topic = this.app.userLoggedIn.getEmail().replace('.', '-').replace('@','_');
        FirebaseMessaging.getInstance().subscribeToTopic(topic);
    }

    public void unsuscribeTopics(){
        FirebaseMessaging.getInstance().unsubscribeFromTopic("news");
        String topic = this.app.userLoggedIn.getEmail().replace('.', '-').replace('@','_');
        FirebaseMessaging.getInstance().unsubscribeFromTopic(topic);
    }

    // -------------------------------------------------

    public static void sendFriendInvitation(String myName, String myFriendUsername){
        String titleNotification = "You received a friend request";
        String descNotification = myName+" has added you";
        String topicNotification = myFriendUsername.replace('.', '-').replace('@','_');
        postNotification(titleNotification, descNotification, topicNotification);
    }

    public static void aceptFriendInvitation(String myName, String myFriendUsername){
        String titleNotification = "Friend request accepted";
        String descNotification = myName+" has acept you. Now are friends!";
        String topicNotification = myFriendUsername.replace('.', '-').replace('@','_');
        postNotification(titleNotification, descNotification, topicNotification);
    }

    public static void reactToStory(String myName, String myFriendUsername){
        String titleNotification = "Reactions";
        String descNotification = myName+" reacted to your story.";
        String topicNotification = myFriendUsername.replace('.', '-').replace('@','_');
        postNotification(titleNotification, descNotification, topicNotification);
    }

    public static void commentStory(String myName, String myFriendUsername) {
        String titleNotification = "Comments";
        String descNotification = myName+" commented on your story.";
        String topicNotification = myFriendUsername.replace('.', '-').replace('@','_');
        postNotification(titleNotification, descNotification, topicNotification);
    }

    public static void chatFriend(String myName, String myFriendUsername, String message) {
        String titleNotification = "Chat";
        String descNotification = myName+": "+message;
        String topicNotification = myFriendUsername.replace('.', '-').replace('@','_');
        postNotification(titleNotification, descNotification, topicNotification);
    }

    private static void postNotification(String titleNotification, String descNotification, String topicNotification){
        UtilCallbacks util = new UtilCallbacks();
        AppServerRequest.postMessageToTopic(titleNotification,descNotification,topicNotification, util.getCallbackRequestPostNotification());
    }
}
