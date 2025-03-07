package com.fantasy.team.prediction;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.fantasy.team.prediction.Fragments.InfoFragmnet.InfoFragment;
import com.fantasy.team.prediction.Fragments.PlayerFragment.PlayerFragment;
import com.fantasy.team.prediction.Fragments.PremiumContentFragment;
 
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import static com.fantasy.team.prediction.util.ApiConfig.SHOW_META_ADS;

public class MatchDetailsActivity extends Application {
    TabLayout tabLayout1;
    TabItem n_tab1, n_tab2,n_tab3;
//    InterstitialAd mInterstitialAd;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_details);



        tabLayout1 = findViewById(R.id.tabLayout1);
        n_tab1 = findViewById(R.id.tab1);
        n_tab2 = findViewById(R.id.tab2);
        n_tab3 = findViewById(R.id.tab3);


        InfoFragment infoFragment = new InfoFragment();
        PlayerFragment playerFragment = new PlayerFragment();
        PremiumContentFragment premiumContentFragment = new PremiumContentFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container_details, infoFragment).commit();

        tabLayout1.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if (tab.getPosition() == 0)
                {
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_container_details, infoFragment).commit();
                }else if(tab.getPosition() ==1){
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_container_details, playerFragment).commit();
                }
                else{
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_container_details, premiumContentFragment).commit();

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(SHOW_META_ADS) {
            MetaAds.showBannerAd(MatchDetailsActivity.this, MatchDetailsActivity.this);
        }else{

            AdmobAds.showBannerAd(this,MatchDetailsActivity.this);
        }
    }

    @Override
    public void onBackPressed() {
        if(SHOW_META_ADS){
            MetaAds.showAds(MatchDetailsActivity.this);
        }else{
            AdmobAds.showAds(MatchDetailsActivity.this);
        }
        super.onBackPressed();
    }
}