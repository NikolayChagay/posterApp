package android.chagay.poster.data.db;

import android.chagay.poster.model.Post;
import android.chagay.poster.data.content.PostTable;
import android.chagay.poster.data.content.PostsTable;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DbManager {

    private static DbManager sInstance;

    private SQLiteDatabase mDatabase;
    private SQLiteOpenHelper mOpenHelper;
    private List<Post> mPosts;

    private DbManager (Context context) {
        mPosts = new ArrayList<>();
        mOpenHelper = new PostBaseHelper(context);
        mDatabase = mOpenHelper.getReadableDatabase();
    }

    public static DbManager get(Context context) {
        if (sInstance == null) {
            sInstance = new DbManager(context);
        }
        return sInstance;
    }

    public List<Post> getPosts() {
        fetchCashedPosts();
        return mPosts;
    }

    private void open() {
        if (!mDatabase.isOpen()) {
            mDatabase = mOpenHelper.getReadableDatabase();
        }
    }

    private void close() {
        if (!mDatabase.isOpen()) { return; }
        mDatabase.close();
    }

    private void fetchCashedPosts() {

        try {
            open();
            mPosts.clear();

            Cursor cursor = mDatabase.query(
                    PostTable.TABLE_NAME,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null);

            if (cursor.moveToFirst()) {
                do {
                    mPosts.add(PostsTable.fromCursor(cursor));

                } while (cursor.moveToNext());
            }

        } finally {
            close();
        }

    }
}
