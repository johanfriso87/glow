package com.glowteam.Models;

import java.util.ArrayList;

public class CommentsTempItem {
    private ArrayList<CommentsItem> replay;
    private String comment;
    private String userId;
    private String videoId;
    private int __v;
    private String _id;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public ArrayList<CommentsItem> getReplay() {
        return replay;
    }

    public void setReplay(ArrayList<CommentsItem> replay) {
        this.replay = replay;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public int get__v() {
        return __v;
    }

    public void set__v(int __v) {
        this.__v = __v;
    }
}
