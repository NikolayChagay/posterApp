package android.chagay.poster.data.content;

public interface PostTable {

    String TABLE_NAME = "posts";

    String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" +
            " _ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            Cols.UUID + ", " +
            Cols.ID_USER + ", " +
            Cols.ID + ", " +
            Cols.TITLE + ", " +
            Cols.BODY + ", " +
            Cols.STATUS +
            ")";

    final class Cols {
        static final String UUID = "UUID";
        static final String ID_USER = "userId";
        static final String ID = "id";
        static final String TITLE = "title";
        static final String BODY = "body";
        static final String STATUS = "status";
    }

    String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
}
