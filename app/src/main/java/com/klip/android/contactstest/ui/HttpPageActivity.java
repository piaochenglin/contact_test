package com.klip.android.contactstest.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.klip.android.contactstest.R;
import com.klip.android.contactstest.model.Article;
import com.klip.android.contactstest.util.HttpCallbackListener;
import com.klip.android.contactstest.util.HttpUtil;
import com.klip.android.contactstest.util.HttpUtilOkhttpVersion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.media.CamcorderProfile.get;
import static com.klip.android.contactstest.util.HttpUtil.sendHttpRequest;

public class HttpPageActivity extends AppCompatActivity {
    TextView http_content;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http_page);
        Button send_request = findViewById(R.id.send_request);
        send_request.setOnClickListener(sendRequestListener);
        Button okhttp_page = findViewById(R.id.okhttp_page);
        okhttp_page.setOnClickListener(okHttpListener);
        http_content = findViewById(R.id.http_content);
    }

    private View.OnClickListener sendRequestListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
//            sendRequestWithUrlConnection();
            HttpUtil.sendHttpRequest("https://www.baidu.com", new HttpCallbackListener() {
                @Override
                public void onFinish(String response) {
                    showResponse(response);
                }

                @Override
                public void onError(Exception e) {

                }
            });
        }
    };
    private View.OnClickListener okHttpListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
//            sendRequestWithOkHttp();
            HttpUtilOkhttpVersion.sendOkhttpRequest("http://192.168.100.246:3000/articles", new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String res = response.body().string();
                    parseJSONWithGSON(res);
                }
            });
        }
    };

    private void sendRequestWithUrlConnection() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                try {
                    URL url = new URL("https://www.baidu.com");
//                    URL url = new URL("https://api.ikidane-nippon.com/v1/areas?&lang=ja");

                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(60000);
                    connection.setReadTimeout(60000);

                    InputStream in = connection.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response_content = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response_content.append(line);
                    }
                    showResponse(response_content.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (connection != null) {
                        connection.disconnect();
                    }
                }

            }
        }).start();
    }

    //    .url("http://localhost:3000/articles")
    private void sendRequestWithOkHttp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        // 本地计算机的ip地址
                        .url("http://192.168.100.246:3000/articles")
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
//                    showResponse(responseData);
//                    parseJSONWithJSONObject(responseData);
                    parseJSONWithGSON(responseData);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void parseJSONWithGSON(String jsonData) {
        Gson gson = new Gson();
        StringBuilder sb = new StringBuilder();
        ArrayList<Article> articles = gson.fromJson(jsonData, new TypeToken<ArrayList<Article>>() {
        }.getType());
        for (Article article : articles) {
            sb.append(article.getId() + article.getTitle() + article.getText() + "\n");
        }
        showResponse(sb.toString());
    }

    private void parseJSONWithJSONObject(String jsonData) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            StringBuilder builder = new StringBuilder();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int id = jsonObject.getInt("id");
                String title = jsonObject.getString("title");
                String text = jsonObject.getString("text");
                builder.append(id + title + text + "\n");
            }
            showResponse(builder.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showResponse(final String response) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                http_content.setText(response);
            }
        });
    }
}
