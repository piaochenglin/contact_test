package com.klip.android.contactstest.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by park
 * on 2017/12/8.
 */

//public class HttpUtil {
//
//    public static String sendHttpRequest(String address) {
//        // 不能这么写，报错，编译都过不去、********************************
//        // 因为代码比网络响应先结束，**************************************  这里是注释的原因，注意看
//        // 网络请求的数据还没有返回，代码就执行结束了**********************
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                HttpURLConnection connection = null;
//                BufferedReader reader = null;
//                try {
//                    URL url = new URL(address);
//                    connection = (HttpURLConnection) url.openConnection();
//                    connection.setRequestMethod("GET");
//                    connection.setReadTimeout(60000);
//                    connection.setConnectTimeout(60000);
//                    connection.setDoInput(true);
//                    connection.setDoOutput(true);
//                    InputStream in = connection.getInputStream();
//                    reader = new BufferedReader(new InputStreamReader(in));
//                    StringBuilder sb = new StringBuilder();
//                    String line = null;
//                    while ((line = reader.readLine()) != null) {
//                        sb.append(line);
//                    }
//                    return sb.toString();
//                } catch (Exception e) {
//                    e.getStackTrace();
//                    return e.getMessage();
//                } finally {
//                    if (connection != null) {
//                        connection.disconnect();
//                    }
//                    if (reader != null) {
//                        try {
//                            reader.close();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }
//        }).start();
//    }
//}
public class HttpUtil {

    public static void sendHttpRequest(final String address, final HttpCallbackListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                try {
                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setReadTimeout(60000);
                    connection.setConnectTimeout(60000);
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    InputStream in = connection.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                    if (listener != null) {
                        listener.onFinish(sb.toString());
                    }
                } catch (Exception e) {
                    e.getStackTrace();
                    if (listener != null) {
                        listener.onError(e);
                    }
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();

    }
}
