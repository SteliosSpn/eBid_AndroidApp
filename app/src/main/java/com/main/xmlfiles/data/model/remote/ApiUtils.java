package com.main.xmlfiles.data.model.remote;

import android.content.Context;

import java.nio.file.FileSystem;

public class ApiUtils {

    private ApiUtils() {}

    public static final String BASE_URL = "https://10.0.2.2:8080/";

    public static APIService getAPIService(Context context) {

        return RetrofitClient.getClient(BASE_URL,context).create(APIService.class);
    }
}
