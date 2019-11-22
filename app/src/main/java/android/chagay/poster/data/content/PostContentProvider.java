package android.chagay.poster.data.content;

import android.chagay.poster.data.db.DbSchema;
import android.chagay.poster.data.db.PostBaseHelper;
import android.chagay.poster.data.content.request.InterfaceRequestTable;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class PostContentProvider extends ContentProvider{


    private static final int POST_LISTS_TABLE = 1;
    private static final int REQUEST_TABLE = 2;

    private static final UriMatcher URI_MATCHER;

    static {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        URI_MATCHER.addURI(DbSchema.CONTENT_AUTHORITY, PostTable.TABLE_NAME, POST_LISTS_TABLE);
        URI_MATCHER.addURI(DbSchema.CONTENT_AUTHORITY, InterfaceRequestTable.TABLE_NAME, REQUEST_TABLE);
    }

    private PostBaseHelper mBaseHelper;

    @Override
    public boolean onCreate() {
        mBaseHelper = new PostBaseHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] what, @Nullable String where, @Nullable String[] args, @Nullable String order) {
        SQLiteDatabase db = mBaseHelper.getWritableDatabase();

        return db.query(
                getType(uri),
                what,
                where,
                args,
                null,
                null,
                order);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (URI_MATCHER.match(uri)) {
            case POST_LISTS_TABLE:
                return PostTable.TABLE_NAME;
            case REQUEST_TABLE:
                return InterfaceRequestTable.TABLE_NAME;
            default:
                return "";
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        SQLiteDatabase db = mBaseHelper.getWritableDatabase();
        long id = db.insertWithOnConflict(getType(uri), null, values, SQLiteDatabase.CONFLICT_REPLACE);
        return ContentUris.withAppendedId(uri, id);

    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        SQLiteDatabase db = mBaseHelper.getWritableDatabase();

        int sumInserted = 0;
        db.beginTransaction();

        try {
            for (ContentValues cv : values) {
                long id = db.insertWithOnConflict(getType(uri), null, cv, SQLiteDatabase.CONFLICT_REPLACE);
                if (id > 0) {
                    sumInserted++;
                }
            }
            db.setTransactionSuccessful();

        } finally {
            db.endTransaction();

        }
        return sumInserted;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String what, @Nullable String[] args) {
        SQLiteDatabase db = mBaseHelper.getWritableDatabase();
        return db.delete(getType(uri), what, args);
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String what, @Nullable String[] args) {
       SQLiteDatabase db = mBaseHelper.getWritableDatabase();
       return db.update(getType(uri), values, what, args);
    }
}
