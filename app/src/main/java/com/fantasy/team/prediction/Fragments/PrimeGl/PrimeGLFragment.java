package com.fantasy.team.prediction.Fragments.PrimeGl;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import com.fantasy.team.prediction.R;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ConsumeParams;
import com.android.billingclient.api.ConsumeResponseListener;
import com.android.billingclient.api.ProductDetails;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.QueryProductDetailsParams;
import com.fantasy.team.prediction.Security;
 
import com.fantasy.team.prediction.SplashActivity;
import com.fantasy.team.prediction.util.ApiConfig;
import com.google.firebase.crashlytics.internal.model.ImmutableList;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PrimeGLFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    View view;
    boolean isSuccess = false;
    private BillingClient billingClient;
    private ImageView ivFb,ivTwitter,ivTelegram,ivWhatsapp,ivYoutube;
    private Button buttonCricket,buttonFootball,buttonRemoveAds,buttonOther;

    @SuppressLint("ResourceAsColor")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_prime_g_l, container, false);


        init();
        socailAvailability();





        return view;
    }

    @SuppressLint("ResourceAsColor")
    private void init() {
        buttonCricket = view.findViewById(R.id.btn_cricket_sub);
        buttonFootball = view.findViewById(R.id.btn_football_sub);
        buttonOther = view.findViewById(R.id.btn_other_sub);
        buttonRemoveAds = view.findViewById(R.id.btn_removeads_sub);
        ivFb = view.findViewById(R.id.iv_fb);
        ivTelegram = view.findViewById(R.id.iv_telegram);
        ivTwitter = view.findViewById(R.id.iv_twitter);
        ivYoutube = view.findViewById(R.id.iv_yt);
        ivWhatsapp = view.findViewById(R.id.iv_whatsapp);


        billingClient = BillingClient.newBuilder(requireContext())
                .setListener(purchasesUpdatedListener)
                .enablePendingPurchases()
                .build();

        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingServiceDisconnected() {

            }
            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {



                Log.e("Billing Res F:",String.valueOf(billingResult.getResponseCode()));
                ExecutorService executorService = Executors.newSingleThreadExecutor();
                executorService.execute(() -> {
                    QueryProductDetailsParams queryProductDetailsParams =
                            QueryProductDetailsParams.newBuilder()
                                    .setProductList(
                                            ImmutableList.from(
                                                    QueryProductDetailsParams.Product.newBuilder()
                                                            .setProductId("cricket_subscription_base")
                                                            .setProductType(BillingClient.ProductType.SUBS)
                                                            .build()))
                                    .build();

                    billingClient.queryProductDetailsAsync(
                            queryProductDetailsParams,
                            (billingResult1, productDetailsList) -> {

                                Log.e("ListData->",productDetailsList.get(0).getName());

                                for(ProductDetails productDetails : productDetailsList){
                                    for(int i=0;i<=(productDetails.getSubscriptionOfferDetails().size());i++){
                                        String subName = null;
                                        if(i==0){
                                            subName = productDetails.getName();
                                            Toast.makeText(requireContext(), subName, Toast.LENGTH_SHORT).show();
                                        }
                                        int index =  i ;

                                        String phases;
                                        String formattedPrice = productDetails.getSubscriptionOfferDetails().get(i).getPricingPhases().getPricingPhaseList().get(0).getFormattedPrice();
                                        String billingPeriod = productDetails.getSubscriptionOfferDetails().get(i).getPricingPhases().getPricingPhaseList().get(0).getBillingPeriod();
                                        int recurrenceMode =  productDetails.getSubscriptionOfferDetails().get(i).getPricingPhases().getPricingPhaseList().get(0).getRecurrenceMode();
                                        String n, duration,bp;
                                        bp = billingPeriod;
                                        n = billingPeriod.substring(2,3);
                                        int nPhases = productDetails.getSubscriptionOfferDetails().get(i).getPricingPhases().getPricingPhaseList().size();
                                    }
                                }
                            }
                    );
                });

            }
        });


        buttonCricket.setOnClickListener(v -> {
            subscribeProduct("cricket");
        });

        buttonFootball.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subscribeProduct("football");
            }
        });

        buttonOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subscribeProduct("other");
            }
        });
        buttonRemoveAds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subscribeProduct("ads");
            }
        });

        if(ApiConfig.removeAds){
            buttonRemoveAds.setText("Subscribed");
            buttonRemoveAds.setBackgroundColor(R.color.greay);
            buttonRemoveAds.setClickable(false);

        }

        if(ApiConfig.showCricketTeamData){
            buttonCricket.setText("Subscribed");
            buttonCricket.setBackgroundColor(R.color.greay);
            buttonCricket.setClickable(false);

        }

        if(ApiConfig.showFootballTeamData){
            buttonFootball.setText("Subscribed");
            buttonFootball.setBackgroundColor(R.color.greay);
            buttonFootball.setClickable(false);

        }

        if(ApiConfig.showOtherGamesData){
            buttonOther.setText("Subscribed");
            buttonOther.setBackgroundColor(R.color.greay);
            buttonOther.setClickable(false);
        }
    }

    private void socailAvailability() {


        ivFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        ivTelegram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });


        ivTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        ivYoutube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        ivWhatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }


    private void subscribeProduct(String productId) {
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingServiceDisconnected() {
                Toast.makeText(getContext(), "Service Disconnected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {


                QueryProductDetailsParams queryProductDetailsParams = QueryProductDetailsParams.newBuilder().setProductList(ImmutableList.from(QueryProductDetailsParams.Product.newBuilder().setProductId(productId).setProductType(BillingClient.ProductType.SUBS).build())).build();
                Log.e("queryProductParams:",String.valueOf(queryProductDetailsParams));


                billingClient.queryProductDetailsAsync(queryProductDetailsParams, (billingResult1, list) -> {
                    Log.e("queryProductParams:", String.valueOf(list.size()));

                    for (ProductDetails productDetails : list) {
                        String offerToken = productDetails.getSubscriptionOfferDetails().get(0).getOfferToken();

                        ImmutableList productDetailsParamsList = ImmutableList.from(BillingFlowParams.ProductDetailsParams.newBuilder().setProductDetails(productDetails).setOfferToken(offerToken).build());

                        BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                                .setProductDetailsParamsList(productDetailsParamsList)
                                .build();
                        billingClient.launchBillingFlow(requireActivity(), billingFlowParams);
                    }
                });
            }
        });

    }


    private boolean verifyValidSignature(String signedData, String signature){
        try{
            String base64Key ="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjKiurDBUahdhMbQlLNt1QubJM9/bHMX4aIu+kblC1K6Z/TjRHLA6IB6zK+ElW1A9cWxouFptm6pfmje9Z0E60u3S9mAo98JC/9e8Baw9IAPsvEBMg/Jm5Un1Tbt5KVjZtIqB75VJlOOTOfXMb1ydlU6mqEPK1heCtY64y6YzN+81FBFzuo/Qxrdnecg5Q6IxeMd07zltc9umS2QeXj/8RgsE5Zo5+ITCuVSFzCSQgLuLe+xpk5DruHikLcfdiMur2t/2BZiDh/vRglbGvNfZu0DQUK3sgKIUmlDzSK5IXlTF6TAHfvXyNB4zQHXlOkQG7ICedWGkpzhIDzRo4DDrkwIDAQAB";
            return  Security.verifyPurchase(base64Key,signedData, signature);
        }catch (IOException e){
            return  false;
        }
    }

    AcknowledgePurchaseResponseListener acknowledgePurchaseResponseListener = billingResult ->
    {
      if(billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK){
          isSuccess = true;
      }
    };

    void handlePurchase(final Purchase purchase){
          ConsumeParams consumeParams = ConsumeParams.newBuilder().setPurchaseToken(purchase.getPurchaseToken()).build();

          ConsumeResponseListener listener = (billingResult, s) -> {
             if(billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK)
             {
                 Toast.makeText(requireContext(), "Subscribed Successfully", Toast.LENGTH_SHORT).show();
             }
          };


          billingClient.consumeAsync(consumeParams,listener);

          if(purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED){
              startActivity(new Intent(requireActivity(), SplashActivity.class));
              requireActivity().finish();
              if(!verifyValidSignature(purchase.getOriginalJson(),purchase.getSignature())){
//                Toast.makeText(getContext(), "Invalid Purchase", Toast.LENGTH_SHORT).show();
                  return;
              }
              Log.e("Purchase Listener: ",String.valueOf(purchase.getOriginalJson()));
              if(!purchase.isAcknowledged()){
                  AcknowledgePurchaseParams acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                          .setPurchaseToken(purchase.getPurchaseToken())
                          .build();
                  billingClient.acknowledgePurchase(acknowledgePurchaseParams,acknowledgePurchaseResponseListener);
                  isSuccess = true;
              }else{
                  Toast.makeText(getContext(), "Already Subscribed", Toast.LENGTH_SHORT).show();
              }

          }else if(purchase.getPurchaseState() == Purchase.PurchaseState.PENDING){
              Toast.makeText(getContext(), "Subscription Pending", Toast.LENGTH_SHORT).show();
          }
          else if(purchase.getPurchaseState() == Purchase.PurchaseState.UNSPECIFIED_STATE){
              Toast.makeText(getContext(), "UNSPECIFIED_STATE", Toast.LENGTH_SHORT).show();
          }
    }


    private PurchasesUpdatedListener purchasesUpdatedListener = (billingResult, Purchase) -> {


        if(billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && Purchase != null){
            for(Purchase purchase: Purchase){
                handlePurchase(purchase);
            }
        }else if(billingResult.getResponseCode() == BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED){
            Toast.makeText(getContext(), "Already Subscribed", Toast.LENGTH_SHORT).show();
            isSuccess = true;
        }else if(billingResult.getResponseCode() == BillingClient.BillingResponseCode.FEATURE_NOT_SUPPORTED){
            Toast.makeText(getContext(), "FEATURE_NOT_SUPPORTED", Toast.LENGTH_SHORT).show();
        }else if(billingResult.getResponseCode() == BillingClient.BillingResponseCode.BILLING_UNAVAILABLE){
            Toast.makeText(getContext(), "BILLING_UNAVAILABLE", Toast.LENGTH_SHORT).show();
        }else if(billingResult.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED){
            Toast.makeText(getContext(), "USER_CANCELED", Toast.LENGTH_SHORT).show();
        }
        else if(billingResult.getResponseCode() == BillingClient.BillingResponseCode.DEVELOPER_ERROR){
            Toast.makeText(getContext(), "DEVELOPER_ERROR", Toast.LENGTH_SHORT).show();
        }
        else if(billingResult.getResponseCode() == BillingClient.BillingResponseCode.ITEM_UNAVAILABLE){
            Toast.makeText(getContext(), "ITEM_UNAVAILABLE", Toast.LENGTH_SHORT).show();
        }
        else if(billingResult.getResponseCode() == BillingClient.BillingResponseCode.NETWORK_ERROR){
            Toast.makeText(getContext(), "NETWORK_ERROR", Toast.LENGTH_SHORT).show();
        }
        else if(billingResult.getResponseCode() == BillingClient.BillingResponseCode.SERVICE_DISCONNECTED){
            Toast.makeText(getContext(), "SERVICE_DISCONNECTED", Toast.LENGTH_SHORT).show();
        }else {

            Toast.makeText(getContext(), "Error :"+String.valueOf(billingResult.getResponseCode()), Toast.LENGTH_SHORT).show();
        }

    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(billingClient!=null){
            billingClient.endConnection();
        }

    }
}