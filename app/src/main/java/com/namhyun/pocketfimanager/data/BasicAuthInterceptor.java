package com.namhyun.pocketfimanager.data;

import android.support.annotation.NonNull;

import java.io.IOException;

import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by namhyun on 2016-09-27.
 */

public class BasicAuthInterceptor implements Interceptor {
    private final String credentials;

    public BasicAuthInterceptor(@NonNull String id, @NonNull String password) {
        credentials = Credentials.basic(id, password);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request()
                .newBuilder()
                .header("Authorization", credentials)
                .build();
        return chain.proceed(request);
    }
}
