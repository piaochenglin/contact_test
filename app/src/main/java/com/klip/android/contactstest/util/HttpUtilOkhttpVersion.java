package com.klip.android.contactstest.util;


import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * project: ContactsTest
 * Created by piaochenglin
 * date: 2017/12/8.
 * email: pchl16@163.com
 */

public class HttpUtilOkhttpVersion {

    public static void sendOkhttpRequest(final String address, final Callback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        // 本地计算机的ip地址
                        .url(address)
                        .build();
                client.newCall(request).enqueue(callback);
            }
        }).start();
    }
}
