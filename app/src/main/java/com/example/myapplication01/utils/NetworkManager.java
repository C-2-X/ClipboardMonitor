package com.example.myapplication01.utils;

import android.util.Log;
import com.example.myapplication01.model.ClipData;
import com.google.gson.Gson;
import java.io.IOException;
import java.net.URLEncoder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/* JADX INFO: loaded from: classes5.dex */
public class NetworkManager {
    private static final String TAG = "NetworkManager";
    private static NetworkManager instance;
    private final OkHttpClient client = new OkHttpClient();
    private final Gson gson = new Gson();

    public interface NetworkCallback {
        void onFailure(String str);

        void onSuccess();
    }

    private NetworkManager() {
    }

    public static synchronized NetworkManager getInstance() {
        if (instance == null) {
            instance = new NetworkManager();
        }
        return instance;
    }

    public void sendReport(String url, String method, ClipData data, final NetworkCallback callback) {
        String url2;
        String json = this.gson.toJson(data);
        RequestBody body = RequestBody.create(json, MediaType.parse("application/json; charset=utf-8"));
        Request.Builder builder = new Request.Builder().url(url).addHeader("Content-Type", "application/json");
        if ("POST".equalsIgnoreCase(method)) {
            builder.post(body);
        } else if ("GET".equalsIgnoreCase(method)) {
            try {
                String encodedJson = URLEncoder.encode(json, "UTF-8");
                if (url.contains("?")) {
                    url2 = url + "&data=" + encodedJson;
                } else {
                    url2 = url + "?data=" + encodedJson;
                }
                builder.url(url2);
                builder.get();
            } catch (Exception e) {
                e.printStackTrace();
                builder.get();
            }
        } else {
            builder.method(method, body);
        }
        this.client.newCall(builder.build()).enqueue(new Callback() { // from class: com.example.myapplication01.utils.NetworkManager.1
            @Override // okhttp3.Callback
            public void onFailure(Call call, IOException e2) {
                Log.e(NetworkManager.TAG, "Request failed: " + e2.getMessage());
                callback.onFailure(e2.getMessage());
            }

            @Override // okhttp3.Callback
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    Log.i(NetworkManager.TAG, "Request successful: " + response.code());
                    callback.onSuccess();
                } else {
                    Log.e(NetworkManager.TAG, "Request failed with code: " + response.code());
                    callback.onFailure("HTTP " + response.code());
                }
                response.close();
            }
        });
    }
}
