package android.chagay.poster.network;

public interface Broadcast {
    String BROADCAST_ACTION = "android.chagay.poster.post_service_broadcast";

    int STATUS_START  = 100;
    int STATUS_FINISH = 200;
    int STATUS_ERROR  = 300;

    String PARAM_TASK    = "task";
    String PARAM_RESULT  = "result";
    String PARAM_STATUS  = "status";
}
