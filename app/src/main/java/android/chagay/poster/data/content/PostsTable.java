package android.chagay.poster.data.content;

import android.chagay.poster.model.Post;
import android.chagay.poster.data.db.DbSchema;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class PostsTable {

    private static final Uri URI = DbSchema.BASE_CONTENT_URI.buildUpon().appendPath(PostTable.TABLE_NAME).build();

    public static void save(Context context, @NonNull List<Post> posts) {
        ContentValues[] values = new ContentValues[posts.size()];
        for (int i = 0; i < posts.size(); i++) {
            values[i] = toContentValues(posts.get(i));
        }

        context.getContentResolver().bulkInsert(URI, values);
    }

    @NonNull
    private static ContentValues toContentValues(Post post) {
        ContentValues values = new ContentValues();
        values.put(PostTable.Cols.UUID, post.getUUID().toString());
        values.put(PostTable.Cols.ID, post.getPostId());
        values.put(PostTable.Cols.ID_USER, post.getUserId());
        values.put(PostTable.Cols.TITLE, post.getTitle());
        values.put(PostTable.Cols.BODY, post.getBody());
        values.put(PostTable.Cols.STATUS, post.getStatus());

        return values;
    }

    @NonNull
    public static Post fromCursor(@NonNull Cursor cursor) {
        Post p = new Post();

        p.setPostId(cursor.getInt(cursor.getColumnIndex(PostTable.Cols.ID)));
        p.setUserId(cursor.getInt(cursor.getColumnIndex(PostTable.Cols.ID_USER)));
        p.setTitle(cursor.getString(cursor.getColumnIndex(PostTable.Cols.TITLE)));
        p.setBody(cursor.getString(cursor.getColumnIndex(PostTable.Cols.BODY)));
        p.setStatus(cursor.getInt(cursor.getColumnIndex(PostTable.Cols.STATUS)));

        return p;
    }

    public static void clear(Context context) {
        context.getContentResolver().delete(URI, null, null);
    }
}
