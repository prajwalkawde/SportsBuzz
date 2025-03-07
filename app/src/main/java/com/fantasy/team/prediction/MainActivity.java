package com.fantasy.team.prediction;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.QueryPurchasesParams;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.fantasy.team.prediction.Fragments.PrimeGl.PrimeGLFragment;
import com.onesignal.OSNotificationOpenedResult;
import com.fantasy.team.prediction.Fragments.FantacyPredictionFrag.FantacyPredictionFragment;
import com.fantasy.team.prediction.Fragments.News.NewsFragment;
import com.fantasy.team.prediction.util.ApiConfig;
import com.fantasy.team.prediction.util.SaveSharedPreference;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.onesignal.OneSignal;
import com.suddenh4x.ratingdialog.AppRating;
import com.suddenh4x.ratingdialog.preferences.RatingThreshold;
//import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.fantasy.team.prediction.util.ApiConfig.GAMES_;
import static com.fantasy.team.prediction.util.ApiConfig.IMG;
import static com.fantasy.team.prediction.util.ApiConfig.SHOW_META_ADS;
import static com.fantasy.team.prediction.util.Utility.USER_NAME;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends Application implements BottomNavigationView.OnNavigationItemSelectedListener {
    private NetworkChangeReceiver networkChangeReceiver;
    public static String PROMOTIONAL_BANNER;
    public static String PROMOTIONAL_BANNER_URL;
    public static String PRIVACY_POLICY;
    public static String TERMS_CONDITIONS;
    public static String READ_ME;
    public static int MINIMUM_AMOUNT;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    public static final String BrodcastStringForAction = "checkinternet";
    private View noInternet, fragmentHome;
    private IntentFilter mIntentFilter;
    private Button retryButton;
    private CircleImageView float_button_icon;
    private String payment_Method, pnumber;
    private EditText pnumberEdittext;
    private RadioGroup radioGroup;
    private RadioButton radioButton1;
    private String username, email, total_earning;
    private TextView userTextV, emailTextV;
    private int selectedId;
    private TextView setMinimumAlert;
    BottomNavigationView bottomNavigationView;
    boolean doubleBackToExitPressedOnce = false;
    private BillingClient billingClient;
    boolean isPremium = false;

    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Window window = getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.app_color));

        billingClient = BillingClient.newBuilder(this).setListener(purchasesUpdatedListener).enablePendingPurchases().build();

        if (SHOW_META_ADS) {
            MetaAds.showAds(MainActivity.this);
            MetaAds.showBannerAd(MainActivity.this, MainActivity.this);
        } else {

            AdmobAds.showBannerAd(this, MainActivity.this);
        }

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        noInternet = findViewById(R.id.no_internet_design);
        fragmentHome = findViewById(R.id.main_layout);
        float_button_icon = findViewById(R.id.floatbuttonicon);
        retryButton = findViewById(R.id.restart_button);
        userTextV = findViewById(R.id.username_show);
        emailTextV = findViewById(R.id.email_show);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setOnItemSelectedListener(this);

        if (getIntent().getStringExtra("frag") != null && getIntent().getStringExtra("frag").equals("frag")) {
            bottomNavigationView.setSelectedItemId(R.id.primeGlBottomMenu);
        } else {
            bottomNavigationView.setSelectedItemId(R.id.fantacyPrediction);
        }

        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);
        OneSignal.initWithContext(this);
        OneSignal.setNotificationOpenedHandler(new OneSignal.OSNotificationOpenedHandler() {
            @Override
            public void notificationOpened(OSNotificationOpenedResult result) {
                startActivity(new Intent(mContext, MainActivity.class));

            }
        });
        OneSignal.setAppId("f48b62f0-17be-4a1f-9b87-21e505cf765d");
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();

        queryPurchase();
        new AppRating.Builder(this)
                .setMinimumLaunchTimes(5)
                .setMinimumDays(7)
                .setMinimumLaunchTimesToShowAgain(5)
                .setMinimumDaysToShowAgain(10)
                .setRatingThreshold(RatingThreshold.FOUR)
                .showIfMeetsConditions();


        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(BrodcastStringForAction);

        Intent serviceIntent = new Intent(getApplicationContext(), NetworkChangeReceiver.class);
        startService(serviceIntent);
        load_navigation_items();

        JsonArrayRequest request0 = new JsonArrayRequest(Request.Method.POST, GAMES_ + "minlimit.php", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject object = response.getJSONObject(i);
                        MINIMUM_AMOUNT = object.getInt("minimum_limit");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        RequestQueue queue0 = Volley.newRequestQueue(getApplicationContext());
        queue0.add(request0);

        String url = GAMES_ + "get_user_data.php?username=" + getSharedPreferences("MyPref", MODE_PRIVATE).getString(USER_NAME, getString(R.string.app_name));


        JsonArrayRequest getUserDataReq = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(JSONArray response) {
                Log.e("Req--->", response.toString());
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject object = response.getJSONObject(i);
                        total_earning = object.getString("earning");
                        email = object.getString("email");
                        username = object.getString("username");
                        Log.e("Req--->", object.getString("earning"));

                        userTextV.setText("Name: " + username);
                        emailTextV.setText("Email: " + email);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("Exception--->", e.getLocalizedMessage());

                    }
                }
            }
        }, error -> Log.e("Error======>", error.toString()));

        RequestQueue queue1 = Volley.newRequestQueue(this);
        queue1.add(getUserDataReq);
        load_app_setting();


        if (isOnline(this)) {
            if (noInternet != null)
                noInternet.setVisibility(View.VISIBLE);
            setVisibility_ON();
        } else {
            setVisibility_OFF();
        }
    }

    PurchasesUpdatedListener purchasesUpdatedListener = new PurchasesUpdatedListener() {
        @Override
        public void onPurchasesUpdated(@NonNull BillingResult billingResult, @Nullable List<Purchase> list) {

        }
    };

    private void queryPurchase() {

        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingServiceDisconnected() {

            }

            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {

                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    ExecutorService executorService = Executors.newSingleThreadExecutor();
                    executorService.execute(() -> {
                        Log.e("---->", "Success");
                        try {

                            billingClient.queryPurchasesAsync(
                                    QueryPurchasesParams.newBuilder().setProductType(BillingClient.ProductType.SUBS).build(),
                                    ((billingResult1, list) -> {

                                        for (Purchase purchase : list) {

                                            purchase.getProducts();
                                            for (int i = 0; i <= list.size(); i++) {

                                                Log.e("Purchase Main Screen:::", String.valueOf(list.get(i).getProducts()));
                                                if (String.valueOf(list.get(i).getProducts()).equals("[ads]")) {
                                                    Log.e("User removed ads:::", "Remove All Ads From App");
                                                    ApiConfig.removeAds = true;
                                                }

                                                if (String.valueOf(list.get(i).getProducts()).equals("[cricket]")) {
                                                    Log.e("subscription cricket", "Show Cricket Matches");
                                                    ApiConfig.showCricketTeamData = true;
                                                }

                                                if (String.valueOf(list.get(i).getProducts()).equals("[football]")) {
                                                    Log.e("subscription football", "Show Cricket Matches");
                                                    ApiConfig.showFootballTeamData = true;
                                                }

                                                if (String.valueOf(list.get(i).getProducts()).equals("[other]")) {
                                                    Log.e("subscription other", "Show Cricket Matches");
                                                    ApiConfig.showOtherGamesData = true;
                                                }

                                            }
                                            if (list != null && purchase.isAcknowledged()) {


                                                isPremium = true;
                                            }
                                        }
                                    })
                            );
                        } catch (Exception e) {
                            isPremium = false;
                            Toast.makeText(MainActivity.this, "Not Subscribed", Toast.LENGTH_SHORT).show();

                            Log.e("Exception---->", String.valueOf(e.getLocalizedMessage()));

                            Log.e("---->", "False");

                        }

                        runOnUiThread(() -> {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            if (isPremium) {
                                Toast.makeText(MainActivity.this, "Already Subscribed", Toast.LENGTH_SHORT).show();
                            }
                        });
                    });
                }
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        load_float_button();

        // Register the BroadcastReceiver
        networkChangeReceiver = new NetworkChangeReceiver();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeReceiver, intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Unregister the BroadcastReceiver
        if (networkChangeReceiver != null) {
            unregisterReceiver(networkChangeReceiver);
        }
    }

    public Boolean isOnline(Context c) {
        ConnectivityManager cm = (ConnectivityManager) c.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni != null && ni.isConnectedOrConnecting())
            return true;
        else
            return false;
    }

    // Show Floating Action Button Start//
    private void show_float_button() {
        if (ApiConfig.FLOATING_BUTTON_URL != null && !ApiConfig.FLOATING_BUTTON_URL.isEmpty()) {
            float_button_icon.setVisibility(View.VISIBLE);

        } else {
            float_button_icon.setVisibility(View.GONE);

        }

        if (ApiConfig.FLOATING_BUTTON_ICON != null) {
            float_button_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ApiConfig.FLOATING_BUTTON_URL != null && !ApiConfig.FLOATING_BUTTON_URL.isEmpty()) {
                        Uri uri = Uri.parse(ApiConfig.FLOATING_BUTTON_URL);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                        Glide.with(getApplicationContext()).load(IMG + ApiConfig.FLOATING_BUTTON_ICON).into(float_button_icon);

                    } else {
                        Toast.makeText(MainActivity.this, "Url not found", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    // Show Floating Action Button End//

    private void load_app_setting() {
        JsonArrayRequest request2 = new JsonArrayRequest(Request.Method.POST, GAMES_ + "app_setting.php", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject object = response.getJSONObject(i);
                        PRIVACY_POLICY = object.getString("privacy_policy");
                        TERMS_CONDITIONS = object.getString("terms");
                        READ_ME = object.getString("read_me");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue queue2 = Volley.newRequestQueue(getApplicationContext());
        queue2.add(request2);

    }

    //   Start For Send Payment Request //
    private void send_payment_request() {
        pnumber = pnumberEdittext.getText().toString();

        if (username != null && email != null && total_earning != null && payment_Method != null && !pnumber.isEmpty()) {
            // Instantiate the RequestQueue
            RequestQueue queue = Volley.newRequestQueue(MainActivity.this);

            // URL of your payment request API
            String url = GAMES_ + "payment_req.php";

            // Create a POST request using StringRequest
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("PaymentResponse", response);

                            if (response.equals("Request Send Successfully")) {
                                Toast.makeText(MainActivity.this, "Request Sent Successfully", Toast.LENGTH_SHORT).show();
                                recreate(); // Refresh the activity
                            } else {
                                Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(MainActivity.this, "Payment request failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e("PaymentError", error.toString());
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    // Add parameters to the request
                    Map<String, String> params = new HashMap<>();
                    params.put("username", username);
                    params.put("email", email);
                    params.put("user_points", total_earning);
                    params.put("payment_type", payment_Method);
                    params.put("payment_number", pnumber);
                    return params;
                }

                @Override
                public Map<String, String> getHeaders() {
                    // Add headers if needed
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/x-www-form-urlencoded");
                    return headers;
                }
            };

            // Add the request to the RequestQueue
            queue.add(stringRequest);
        } else {
            Toast.makeText(getApplicationContext(), "All fields are required.", Toast.LENGTH_SHORT).show();
        }
    }


    //   End Payment Request Code //
    private void load_navigation_items() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_rate_us) {
                    Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                } else if (id == R.id.nav_check_update) {
                    Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                } else if (id == R.id.nav_subscribe) {
                    //   bp.subscribe(MainActivity.this, SUBSCRIPTION_ID);
                } else if (id == R.id.nav_share_app) {
                    Intent share = new Intent(Intent.ACTION_SEND);
                    String linkshare = getResources().getString(R.string.app_name) + " - Fantasy Team Expert*\n" +
                            "Fantasy Sports Pro Tips and Expert Advice with Premium Teams For All Matches...! \uD83D\uDC47\uD83C\uDFFE Download the App Now \n" +
                            "https://play.google.com/store/apps/details?id=" + getPackageName();
                    String sybject = "Download App" + R.string.app_name;
                    share.setType("text/plain");
                    share.putExtra(Intent.EXTRA_SUBJECT, sybject);
                    share.putExtra(Intent.EXTRA_TEXT, linkshare);
                    startActivity(Intent.createChooser(share, "Share using"));
                } else if (id == R.id.nav_privacy_policy) {
                    if (PRIVACY_POLICY != null) {
                        Intent intent = new Intent(MainActivity.this, NavigationItemsActivity.class);
                        intent.putExtra("privacy_policy", PRIVACY_POLICY);
                        startActivity(intent);
                    }
                } else if (id == R.id.nav_read_me) {
                    if (READ_ME != null) {
                        Intent intent = new Intent(MainActivity.this, NavigationItemsActivity.class);
                        intent.putExtra("read_me", READ_ME);
                        startActivity(intent);
                    }
                } else if (id == R.id.nav_terms_and_conditions) {
                    if (TERMS_CONDITIONS != null) {
                        Intent intent = new Intent(MainActivity.this, NavigationItemsActivity.class);
                        intent.putExtra("terms", TERMS_CONDITIONS);
                        startActivity(intent);
                    }
                } else if (id == R.id.nav_contact_us) {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    String[] recipients = {getResources().getString(R.string.contact_email)};
                    intent.putExtra(Intent.EXTRA_EMAIL, recipients);
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Contact Us");
                    intent.setType("text/html");
                    intent.setPackage("com.google.android.gm");
                    startActivity(Intent.createChooser(intent, "Send mail"));
                } else if (id == R.id.refer_earn) {
                    startActivity(new Intent(MainActivity.this, Refer_EarnActivity.class));
                } else if (id == R.id.nav_login) {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                } else if (id == R.id.nav_logout) {
                    SaveSharedPreference.setLoggedIn(getApplicationContext(), false);
                    SaveSharedPreference.getUserName(getApplicationContext(), username);
                    SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                    pref.getString(USER_NAME, "");
                    pref.edit().clear().apply();
                    recreate();
                } else if (id == R.id.nav_news) {
                    startActivity(new Intent(MainActivity.this, NewsmActivity.class));
                } else if (id == R.id.nav_top_fantacy_apps) {
                    startActivity(new Intent(MainActivity.this, TopFantacyAppsActivity.class));
                } else if (id == R.id.nav_profile) {
                    startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                } else if (id == R.id.redeem_points) {
                    final Dialog dialog = new Dialog(MainActivity.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.withdraw_dialouge);
                    dialog.getWindow().setLayout(ViewPager.LayoutParams.FILL_PARENT, ViewPager.LayoutParams.WRAP_CONTENT);
                    TextView amounttext = dialog.findViewById(R.id.editText_amount);
                    setMinimumAlert = dialog.findViewById(R.id.setAlertMinim);
                    pnumberEdittext = dialog.findViewById(R.id.editText_googlepaynumber);
                    radioGroup = dialog.findViewById(R.id.radioGroup);

                    Button send_req = dialog.findViewById(R.id.send_request);
                    selectedId = radioGroup.getCheckedRadioButtonId();
                    radioButton1 = dialog.findViewById(selectedId);
                    setMinimumAlert.setText("Minimum " + String.valueOf(MINIMUM_AMOUNT) + " Coins Are Required For Redeem.");
                    amounttext.setText(total_earning);
                    dialog.show();
                    send_req.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                if (Integer.parseInt(total_earning) >= MINIMUM_AMOUNT) {
                                    send_payment_request();
                                } else {
                                    Toast.makeText(MainActivity.this, "Minimum " + MINIMUM_AMOUNT + " Coins Are Required.", Toast.LENGTH_SHORT).show();
                                }
                            } catch (NumberFormatException n) {
                                n.printStackTrace();
                            }
                        }
                    });
                    Toast.makeText(MainActivity.this, total_earning, Toast.LENGTH_SHORT).show();

//                    if (total_earning != null)

                } else {
                    Toast.makeText(MainActivity.this, "Minimum Require Points is " + String.valueOf(MINIMUM_AMOUNT), Toast.LENGTH_SHORT).show();
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_purchase) {
//            bp.subscribe(MainActivity.this, SUBSCRIPTION_ID);
        } else {
            if (ApiConfig.TELEGRAM_BANNER_URL != null && !ApiConfig.TELEGRAM_BANNER_URL.isEmpty()) {
                Uri uri = Uri.parse(ApiConfig.TELEGRAM_BANNER_URL);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            } else {
                Toast.makeText(this, "No Url Found", Toast.LENGTH_SHORT).show();
            }
        }
        // Handle item selectio
        return onOptionsItemSelected(item);
    }


    public BroadcastReceiver myReciver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(BrodcastStringForAction)) {
                if (intent.getStringExtra("online_status").equals("true")) {
                    setVisibility_ON();
                } else {
                    setVisibility_OFF();
                }
            }
        }
    };


    @Override
    public void onResume() {
        super.onResume();


        if (SaveSharedPreference.getLoggedStatus(getApplicationContext())) {
            navigationView.getMenu().findItem(R.id.nav_logout).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_terms_and_conditions).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_read_me).setVisible(true);
            navigationView.getMenu().findItem(R.id.refer_earn).setVisible(true);
            navigationView.getMenu().findItem(R.id.redeem_points).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_login).setVisible(false);
            // perform action when user is already logged in
        } else {
            navigationView.getMenu().findItem(R.id.nav_terms_and_conditions).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_read_me).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_logout).setVisible(false);
            navigationView.getMenu().findItem(R.id.refer_earn).setVisible(false);
//            navigationView.getMenu().findItem(R.id.nav_profile).setVisible(false);
            navigationView.getMenu().findItem(R.id.redeem_points).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_login).setVisible(true);
            // perform action when user is not logged in
        }
    }


    @Override
    public void onDestroy() {
        unregisteredBrodcastReciver();
        super.onDestroy();
    }

    protected void unregisteredBrodcastReciver() {
        try {
            unregisterReceiver(myReciver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    @Override
    protected void onRestart() {
        super.onRestart();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // For Android 13 (API level 33) and above
            registerReceiver(myReciver, mIntentFilter, RECEIVER_NOT_EXPORTED);
        } else {
            // For devices below Android 13
            registerReceiver(myReciver, mIntentFilter);
        }
    }


    @Override
    public void onBackPressed() {
        if (bottomNavigationView.getSelectedItemId() == R.id.fantacyPrediction) {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }
            this.doubleBackToExitPressedOnce = true;

            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler(Looper.getMainLooper()).postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
        } else {
            bottomNavigationView.setSelectedItemId(R.id.fantacyPrediction);
        }
    }


    FantacyPredictionFragment livescoreFragment = new FantacyPredictionFragment();
    NewsFragment newsFragment = new NewsFragment();
    PrimeGLFragment primeGLFragment = new PrimeGLFragment();

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.fantacyPrediction) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, livescoreFragment).commit();
            return true;
       // } else if (id == R.id.newsBottomMenu) {
        //    getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, newsFragment).commit();
        //    return true;
        } else if (id == R.id.primeGlBottomMenu) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, primeGLFragment).commit();
            return true;
        }
        return false;
    }
    //Sort Matches Time//

    // Load Floating Action Button Start//
    private void load_float_button() {
        JsonArrayRequest request1 = new JsonArrayRequest(Request.Method.POST, GAMES_ + "banners.php", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject object = response.getJSONObject(i);
                        ApiConfig.TELEGRAM_BANNER = object.getString("t_banner_image");
                        ApiConfig.TELEGRAM_BANNER_URL = object.getString("t_banner_link");
                        ApiConfig.FLOATING_BUTTON_URL = object.getString("web_url");
                        ApiConfig.FLOATING_BUTTON_ICON = object.getString("float_icon");
                        Glide.with(getApplicationContext()).load(IMG + ApiConfig.FLOATING_BUTTON_ICON).into(float_button_icon);
                        show_float_button();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, error -> {
        });
        RequestQueue queue1 = Volley.newRequestQueue(getApplicationContext());
        queue1.add(request1);
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        payment_Method = "";
        int id = view.getId();

        if (id == R.id.googlepaybtn) {
            if (checked)
                payment_Method = "Google Pay";
            pnumberEdittext.setVisibility(View.VISIBLE);
            pnumberEdittext.setHint("Enter Gpay Number/Upi");
        } else if (id == R.id.paytmbtn) {
            if (checked)
                payment_Method = "Paytm";
            pnumberEdittext.setVisibility(View.VISIBLE);
            pnumberEdittext.setHint("Enter Paytm Number/Upi");
        }
        // Check which radio button was clicked

        Toast.makeText(getApplicationContext(), payment_Method, Toast.LENGTH_SHORT).show();
    }

    public void setVisibility_ON() {
        if (noInternet != null)
            noInternet.setVisibility(View.GONE);
        fragmentHome.setVisibility(View.VISIBLE);
    }

    public void setVisibility_OFF() {
        noInternet.setVisibility(View.VISIBLE);
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });
        fragmentHome.setVisibility(View.GONE);
    }
}