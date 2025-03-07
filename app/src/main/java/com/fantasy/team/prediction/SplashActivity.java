package com.fantasy.team.prediction;

import static com.fantasy.team.prediction.util.ApiConfig.BANNER_AD_ID;
import static com.fantasy.team.prediction.util.ApiConfig.GAMES_;
import static com.fantasy.team.prediction.util.ApiConfig.INTERSTITIAL_AD_ID;
import static com.fantasy.team.prediction.util.ApiConfig.SHOW_META_ADS;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
 

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class SplashActivity extends AppCompatActivity {
    private NetworkChangeReceiver networkChangeReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
//        Intent i = new Intent(SplashActivity.this, MainActivity.class);
//        i.putExtra("sa","s");
//        //Intent is used to switch from one activity to another.
//        startActivity(i);
//        //invoke the SecondActivity.
//        finish();
        if (!NotificationPermissionHelper.isNotificationPermissionGranted(this)) {
            NotificationPermissionHelper.requestNotificationPermission(this);

        }
//        requestNotificationPermission(this);
        JsonArrayRequest request3 = new JsonArrayRequest(Request.Method.POST, GAMES_ + "ads.php", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject object = response.getJSONObject(i);

                        if(object.getString("ads").equals("meta")){
                            SHOW_META_ADS = true;
                            MetaAds.loadAds(getApplicationContext());
                        }else{
                            SHOW_META_ADS = false;
                            AdmobAds.loadAds(getApplicationContext());

                        }
                        BANNER_AD_ID = object.getString("banner_ad");
                        INTERSTITIAL_AD_ID = object.getString("interstitial_ad");

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                                i.putExtra("sa","s");
                                //Intent is used to switch from one activity to another.
                                startActivity(i);
                                //invoke the SecondActivity.
                                finish();
                                //the current activity will get finished.
                            }
                        },3000);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SplashActivity.this, error.networkResponse.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue queue3 = Volley.newRequestQueue(getApplicationContext());
        queue3.add(request3);


    }

    private static final String CHANNEL_ID = "my_channel_id"; // Change this to your desired channel ID

    // Check if the notification permission is granted
    public static boolean isNotificationPermissionGranted(Context context) {

        return NotificationManagerCompat.from(context).areNotificationsEnabled();
    }

    // Request notification permission
    public static void requestNotificationPermission(final Context context) {
        if (!isNotificationPermissionGranted(context)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Enable Notifications");
            builder.setMessage("Please enable notifications to receive updates.");

            builder.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    openNotificationSettings(context);
                }
            });

            builder.setNegativeButton("Cancel", null);

            builder.show();
        }
    }



    @Override
    protected void onStop() {
        super.onStop();
        // Unregister the BroadcastReceiver
        if (networkChangeReceiver != null) {
            unregisterReceiver(networkChangeReceiver);
        }
    }

    // Open the device's notification settings
    private static void openNotificationSettings(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(context);
        }

        Intent intent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
        intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.getPackageName());
        context.startActivity(intent);
    }

    // Create a notification channel (for Android Oreo and higher)
    @RequiresApi(api = Build.VERSION_CODES.O)
    private static void createNotificationChannel(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                "My Channel Name", // Change this to your desired channel name
                NotificationManager.IMPORTANCE_DEFAULT
        );
        notificationManager.createNotificationChannel(channel);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Register the BroadcastReceiver
        networkChangeReceiver = new NetworkChangeReceiver();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeReceiver, intentFilter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
            } else {
                // Permission denied
            }
        }
    }
}