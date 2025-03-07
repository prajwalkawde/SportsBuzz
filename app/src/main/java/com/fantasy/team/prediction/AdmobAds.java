package com.fantasy.team.prediction;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;


 
import com.fantasy.team.prediction.util.ApiConfig;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

public class AdmobAds {
    public static InterstitialAd mInterstitialAd;
    public static AdRequest adRequest;


    public static void loadAds(Context context) {


        MobileAds.initialize(context, initializationStatus -> {
        });

        if(!ApiConfig.removeAds) {
            adRequest = new AdRequest.Builder().build();
            InterstitialAd.load(context, ApiConfig.INTERSTITIAL_AD_ID, adRequest,
                    new InterstitialAdLoadCallback() {
                        @Override
                        public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                            // The mInterstitialAd reference will be null until
                            // an ad is loaded.
                            mInterstitialAd = interstitialAd;
                            Log.i("TAG", "onAdLoaded");
                        }

                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            // Handle the error
                            Log.i("TAG", loadAdError.getMessage());
                            mInterstitialAd = null;
                        }
                    });
        }
    }

    public static void showAds(Activity activity) {
        if(!ApiConfig.removeAds) {
            if (mInterstitialAd != null) {
                mInterstitialAd.show(activity);
            }
        }
    }

    public static void showBannerAd(Context context, Activity activity) {
        if(!ApiConfig.removeAds) {
            AdView adView = new AdView(context);
            adView.setAdUnitId(ApiConfig.BANNER_AD_ID);
            adView.setAdSize(AdSize.BANNER);

            LinearLayout adContainer = (LinearLayout) activity.findViewById(R.id.banner_container);

            adContainer.addView(adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
        }
    }
}
