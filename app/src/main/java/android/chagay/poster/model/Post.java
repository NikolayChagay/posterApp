package android.chagay.poster.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.UUID;

public class Post implements Serializable {

    private UUID mUUID;

    @SerializedName("userId")
    private int mUserId;

    @SerializedName("id")
    private int mId;

    @SerializedName("title")
    private String mTitle;

    @SerializedName("body")
    private String mBody;

    private int mStatus;

    public Post() {
        this (UUID.randomUUID());
    }
    public Post(UUID UUID) {
        mUUID = UUID;
        mUserId = -1;
        mId = -1;
        mTitle = "";
        mBody = "";
        mStatus = 0; // Новое
    }

    public Post(int userId, String title, String body) {
        mUserId = userId;
        mTitle = title;
        mBody = body;
        mStatus = 0;
    }


    public UUID getUUID() {
        return mUUID;
    }

    public Integer getUserId() {
        return mUserId;
    }

    public void setUserId(Integer userId) {
        mUserId = userId;
    }

    public Integer getPostId() {
        return mId;
    }

    public void setPostId(Integer postId) {
        mId = postId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getBody() {
        return mBody;
    }

    public void setBody(String body) {
        mBody = body;
    }

    public Integer getStatus() {
        return mStatus;
    }

    public void setStatus(Integer status) {
        mStatus = status;
    }


}