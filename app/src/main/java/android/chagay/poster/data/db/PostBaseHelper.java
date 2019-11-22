package android.chagay.poster.data.db;

import android.chagay.poster.data.content.PostTable;
import android.chagay.poster.data.content.request.InterfaceRequestTable;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class PostBaseHelper  extends SQLiteOpenHelper  {

    private static final int VERSION = 1;

    public PostBaseHelper(@Nullable Context context) {
        super(context, DbSchema.DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(PostTable.CREATE_TABLE);
        db.execSQL(InterfaceRequestTable.CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(PostTable.DROP_TABLE);
        db.execSQL(InterfaceRequestTable.DROP_TABLE);
        onCreate(db);
    }
}
