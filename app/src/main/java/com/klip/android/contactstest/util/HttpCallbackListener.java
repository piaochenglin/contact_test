package com.klip.android.contactstest.util;

/**
 * Created by park
 * on 2017/12/8.
 */

public interface HttpCallbackListener {
    void onFinish(String response);

    void onError(Exception e);
}
