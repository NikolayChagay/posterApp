package android.chagay.poster.data.content.request;

import android.chagay.poster.data.db.DbSchema;
import android.chagay.poster.network.model.NetworkRequest;
import android.chagay.poster.network.model.Request;
import android.chagay.poster.network.model.RequestStatus;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;

public class RequestTable {

    private static final String REQUEST = "request";
    private static final String STATUS = "status";
    private static final String ERROR = "error";

    private static final Uri URI = DbSchema.BASE_CONTENT_URI.buildUpon().appendPath(InterfaceRequestTable.TABLE_NAME).build();

    @NonNull
    public static ContentValues toContentValues(@NonNull Request request) {
        ContentValues values = new ContentValues();
        values.put(REQUEST, request.getRequest());
        values.put(STATUS, request.getStatus().name());
        values.put(ERROR, request.getError());
        return values;
    }

    @NonNull
    public static Request fromCursor(@NonNull Cursor cursor) {
        @NetworkRequest String request = cursor.getString(cursor.getColumnIndex(REQUEST));
        RequestStatus status = RequestStatus.valueOf(cursor.getString(cursor.getColumnIndex(STATUS)));
        String error = cursor.getString(cursor.getColumnIndex(ERROR));
        return new Request(request, status, error);
    }

    public static void clear(Context context) {
        context.getContentResolver().delete(URI, null, null);
    }
}