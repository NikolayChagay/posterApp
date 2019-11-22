package android.chagay.poster.network.model;

import androidx.annotation.StringDef;

@StringDef({
        NetworkRequest.POST_LIST,
        NetworkRequest.ADD_POST
})
public @interface NetworkRequest {
    String POST_LIST = "post_list";
    String ADD_POST = "add_post";
}



