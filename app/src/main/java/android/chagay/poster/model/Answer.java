package android.chagay.poster.model;

import android.chagay.poster.network.model.GsonHolder;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Answer implements Serializable {

// Output
//    {
//        id: 101,
//        title: 'foo',
//        body: 'bar',
//        userId: 1
//    }

    @SerializedName("id")
    private int id;

    @SerializedName("title")
    private String title;

    @SerializedName("body")
    private String body;

    @SerializedName("userId")
    private int userId;

    public Answer(int id, String title, String body, int userId) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getAnswer() {
      return   GsonHolder.getGson().toJson(this);
    }
}