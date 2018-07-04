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

    public void sendNotification(){

    }
}
