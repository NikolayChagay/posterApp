package android.chagay.poster.data.content.request;

public interface InterfaceRequestTable {
    String TABLE_NAME = "request";

    String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" +
            " _ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            Cols.REQUEST + ", " +
            Cols.STATUS + ", " +
            Cols.ERROR +
            ")";

    final class Cols {
        static final String REQUEST = "request";
        static final String STATUS = "status";
        static final String ERROR = "error";
    }

    String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

}
