package com.fantasy.team.prediction;

import static com.fantasy.team.prediction.util.ApiConfig.BANNER_AD_ID;
import static com.fantasy.team.prediction.util.ApiConfig.INTERSTITIAL_AD_ID;

import android.app.Activity;
import android.content.Context;
import android.widget.LinearLayout;

 
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;

public class MetaAds {

    public static InterstitialAd interstitialAd;
    public static AdView adView;

    public static void loadAds(Context context) {

        AudienceNetworkAds.initialize(context);
        interstitialAd = new InterstitialAd(context, INTERSTITIAL_AD_ID);
        InterstitialAdListener interstitialAdListener = new InterstitialAdListener() {

            @Override
            public void onError(Ad ad, AdError adError) {

            }

            @Override
            public void onAdLoaded(Ad ad) {

            }

            @Override
            public void onAdClicked(Ad ad) {

            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }

            @Override
            public void onInterstitialDisplayed(Ad ad) {

            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                MetaAds.interstitialAd = null;
                loadAds(context);
            }
        };
        // load the ad
        interstitialAd.loadAd(
                interstitialAd.buildLoadAdConfig()
                        .withAdListener(interstitialAdListener)
                        .build());

    }

    public static void showAds(Context context) {
        // Check if interstitialAd has been loaded successfully
        if (interstitialAd == null || !interstitialAd.isAdLoaded()) {
            return;
        }
        // Check if ad is already expired or invalidated, and do not show ad if that is the case. You will not get paid to show an invalidated ad.
        if (interstitialAd.isAdInvalidated()) {
            return;
        }
        // Show the ad
        interstitialAd.show();
    }

    public static void showBannerAd(Context context, Activity activity) {
        adView = new AdView(context, BANNER_AD_ID, AdSize.BANNER_HEIGHT_50);

        // Find the Ad Container
        LinearLayout adContainer = (LinearLayout) activity.findViewById(R.id.banner_container);

        // Add the ad view to your activity layout
        adContainer.addView(adView);

        // Request an ad
        adView.loadAd();
    }
}
