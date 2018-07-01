package com.fiuba.stories.stories;

public class MessageReceive extends Message {

    private Long time;

    public MessageReceive() {
    }

    public MessageReceive(Long time) {
        this.time = time;
    }

    public MessageReceive(String username, String message, Long time) {
        super(username, message);
        this.time = time;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}
