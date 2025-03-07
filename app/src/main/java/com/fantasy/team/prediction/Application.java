package com.fantasy.team.prediction;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.fantasy.team.prediction.util.ApiConfig;

public class Application extends AppCompatActivity {
    public static Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(ApiConfig.SHOW_META_ADS) {
            MetaAds.loadAds(Application.this);
        }else{
            AdmobAds.loadAds(Application.this);
        }
    }
}
