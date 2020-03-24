package com.example.myapplication.ui.app;

import android.app.Application;

import com.example.myapplication.network.ApiClient;
import com.example.myapplication.ui.SessionManager;
import com.example.myapplication.ui.model.ApiConfig;


public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SessionManager.getInstance().init(this);

        ApiConfig config = ApiConfig.builder().context(this).baseUrl(SessionManager.getInstance().getKeySaveCityName())
                .auth(SessionManager.getInstance().getKeySaveToken())
                .build();
        ApiClient.getInstance().init(config);
    }

}
