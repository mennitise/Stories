package com.fiuba.stories.stories;

import java.util.Map;

public class MessageSend extends Message {

    private Map time;

    public MessageSend() {
    }

    public MessageSend(Map time) {
        this.time = time;
    }

    public MessageSend(String username, String message, Map time) {
        super(username, message);
        this.time = time;
    }

    public Map getTime() {
        return time;
    }

    public void setTime(Map time) {
        this.time = time;
    }
}
