package android.chagay.poster.network;

import android.app.IntentService;
import android.chagay.poster.model.Answer;
import android.chagay.poster.model.Post;
import android.chagay.poster.data.db.DbSchema;
import android.chagay.poster.network.model.GsonHolder;
import android.chagay.poster.data.content.PostsTable;
import android.chagay.poster.data.content.request.InterfaceRequestTable;

import android.chagay.poster.data.content.request.RequestTable;
import android.chagay.poster.network.model.NetworkRequest;
import android.chagay.poster.network.model.Request;
import android.chagay.poster.network.model.RequestStatus;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.IOException;
import java.util.List;

import static android.chagay.poster.data.db.DbSchema.BASE_CONTENT_URI;

public class NetworkService extends IntentService {

    private static final String TAG = "Network_service";

    private static final String REQUEST_KEY = "request";
    private static final String REQUEST_POST = "post";

    private static final int POST_LIST = 1;
    private static final int ADD_POST  = 2;

    @SuppressWarnings("unused")
    public NetworkService() {
        super(NetworkService.class.getName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Request request = GsonHolder.getGson().fromJson(intent.getStringExtra(REQUEST_KEY), Request.class);

        if (request.getStatus() == RequestStatus.IN_PROGRESS) {
            return;
        }
        request.setStatus(RequestStatus.IN_PROGRESS);
        this.getContentResolver().insert(Uri.parse(DbSchema.BASE_CONTENT_URI + "/" + InterfaceRequestTable.TABLE_NAME), RequestTable.toContentValues(request));
        this.getContentResolver().notifyChange(Uri.parse(DbSchema.BASE_CONTENT_URI + "/" + InterfaceRequestTable.TABLE_NAME), null);

        if (TextUtils.equals(NetworkRequest.POST_LIST, request.getRequest())) {
            executePostsList(request);
        }

        if (TextUtils.equals(NetworkRequest.ADD_POST, request.getRequest())) {
            Post post = GsonHolder.getGson().fromJson(intent.getStringExtra(REQUEST_POST), Post.class);
            executeNewPost(request, post.getUserId(), post.getTitle(), post.getBody());
        }
    }

    public static void fetchPostsList(@NonNull Context context, @NonNull Request request) {
        Intent intent = new Intent(context, NetworkService.class);
        intent.putExtra(REQUEST_KEY, GsonHolder.getGson().toJson(request));

        context.startService(intent);
    }

    public static void newPost(@NonNull Context context, @NonNull Request request, @NonNull Post post) {
        Intent intent = new Intent(context, NetworkService.class);
        intent.putExtra(REQUEST_KEY, GsonHolder.getGson().toJson(request));
        intent.putExtra(REQUEST_POST, GsonHolder.getGson().toJson(post));

        context.startService(intent);
    }

    private void executePostsList(@NonNull Request request) {
        try {
            sendMyBroadcast(POST_LIST, null, Broadcast.STATUS_START);

            List<Post> postList = ApiFactory.getJsonApi()
                    .getAllPosts()
                    .execute()
                    .body();

            if (postList != null){
                PostsTable.clear(this);
                PostsTable.save(this, postList);

                request.setStatus(RequestStatus.SUCCESS);
                this.getContentResolver().notifyChange(Uri.parse(BASE_CONTENT_URI + "/" + InterfaceRequestTable.TABLE_NAME), null);

                sendMyBroadcast(POST_LIST, String.valueOf(postList.size()), Broadcast.STATUS_FINISH);
                Log.i(TAG, "Write to cash DB counts = " + postList.size());
            }

        } catch (IOException e) {
            request.setStatus(RequestStatus.ERROR);
            request.setError(e.getMessage());
            sendMyBroadcast(POST_LIST, "ERROR : " + e.getMessage(), Broadcast.STATUS_ERROR);
            Log.i(TAG, "Error while getting posts list: " + e.getCause() + ", " +  e.getMessage());
        } finally {
            this.getContentResolver().insert(Uri.parse(DbSchema.BASE_CONTENT_URI + "/" + InterfaceRequestTable.TABLE_NAME), RequestTable.toContentValues(request));
            this.getContentResolver().notifyChange(Uri.parse(BASE_CONTENT_URI + "/" + InterfaceRequestTable.TABLE_NAME), null);
        }
    }

    private void executeNewPost(@NonNull Request request, int userId, @NonNull String title, @NonNull String body) {
        try {
            sendMyBroadcast(ADD_POST, null, Broadcast.STATUS_START);
            Log.i(TAG, "Start adding new post");

            Answer answer = ApiFactory.getJsonApi()
                    .addNewPost(new Post(userId,title, body))
                    .execute()
                    .body();

            if (answer != null) {
                request.setStatus(RequestStatus.SUCCESS);
                this.getContentResolver().notifyChange(Uri.parse(BASE_CONTENT_URI + "/" + InterfaceRequestTable.TABLE_NAME), null);

                sendMyBroadcast(ADD_POST, answer.getAnswer(), Broadcast.STATUS_FINISH);
                Log.i(TAG, "Added new post: " + answer.getAnswer());
            }

        } catch (IOException e) {
            request.setStatus(RequestStatus.ERROR);
            request.setError(e.getMessage());
            sendMyBroadcast(ADD_POST, "ERROR : " + e.getMessage(), Broadcast.STATUS_ERROR);
            Log.i(TAG, "Error while adding new post: " + e.getCause() + ", " +  e.getMessage());
        } finally {
            this.getContentResolver().insert(Uri.parse(DbSchema.BASE_CONTENT_URI + "/" + InterfaceRequestTable.TABLE_NAME), RequestTable.toContentValues(request));
            this.getContentResolver().notifyChange(Uri.parse(BASE_CONTENT_URI + "/" + InterfaceRequestTable.TABLE_NAME), null);
        }
    }

    private void sendMyBroadcast(int task, String result, int status) {
        Intent intent = new Intent(Broadcast.BROADCAST_ACTION);
        intent.putExtra(Broadcast.PARAM_TASK, task);
        intent.putExtra(Broadcast.PARAM_RESULT, result);
        intent.putExtra(Broadcast.PARAM_STATUS, status);
        sendBroadcast(intent);
    }
}