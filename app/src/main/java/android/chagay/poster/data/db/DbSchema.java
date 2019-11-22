package android.chagay.poster.data.db;

import android.net.Uri;

public class DbSchema {

    public static final String CONTENT_AUTHORITY = "android.chagay.retrofit.loaders";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String DATABASE_NAME = "postBase.db";

}
