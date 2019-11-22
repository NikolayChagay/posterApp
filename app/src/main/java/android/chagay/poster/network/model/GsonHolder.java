package android.chagay.poster.network.model;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

public class GsonHolder {

    private static final Gson GSON = new Gson();

    @NonNull
    public static Gson getGson() {
        return GSON;
    }
}
