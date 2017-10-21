package com.lostad.app.demo.event;


public class RefreshLikeEvent {
    public static final int TYPE_UPVOTE = 0;
    public static final int TYPE_UNUPVOTE = 1;
    public static final int TYPE_INIT = 2;

    public int type = 2;

    public RefreshLikeEvent(int type){
        this.type = type;
    }
}
