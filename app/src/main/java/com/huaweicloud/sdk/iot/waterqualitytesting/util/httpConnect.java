package com.huaweicloud.sdk.iot.waterqualitytesting.util;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class httpConnect {
    private final OkHttpClient client;

    public httpConnect() {
        client = new OkHttpClient();
    }

    /**
     *
     * @param url
     * @param mediaType
     * @param json
     * @return
     */
    public String POST(String url, MediaType mediaType, String json) throws IOException {

        RequestBody requestBody = RequestBody.create(mediaType, json);
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Content-Type", "application/json;charset=utf-8")
                .post(requestBody)
                .build();
        Response response=client.newCall(request).execute();
        Headers headers =response.headers();
        return headers.get("X-Subject-Token");

    }

    /**
     *
     * @param url
     * @param token
     * @return
     */
    public String GET(String url, String token) throws IOException {

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Content-Type", "application/json")
                .addHeader("X-Auth-Token", token)
                .get()
                .build();
        Response response=client.newCall(request).execute();
        return response.body().string();

    }

    private void disconnect(){

    }
}
