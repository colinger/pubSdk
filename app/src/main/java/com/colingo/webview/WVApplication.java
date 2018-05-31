package com.colingo.webview;

import android.app.Application;

import cn.cs.callme.sdk.PubSDK;

/**
 * Created by colingo on 28/11/2017.
 */

public class WVApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //测试，111
        PubSDK.getInstance().init(this, "111");
        //正式, 默认appKey为，app的package
        //        PubSDK.getInstance().init(this);
        //此代码，可以放到Activity的OnCreate里，和Application中onCreate里
        PubSDK.getInstance().initTBCode();
    }
}
