package com.fantasy.team.prediction;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
 
import com.fantasy.team.prediction.util.Ex;
import com.fantasy.team.prediction.util.SaveSharedPreference;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
//import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.fantasy.team.prediction.util.ApiConfig.API_Forgot_Pass;
import static com.fantasy.team.prediction.util.ApiConfig.GAMES_;
import static com.fantasy.team.prediction.util.Ex.isValidMail;
import static com.fantasy.team.prediction.util.Utility.USER_NAME;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    TextView textView,skip,forgotPassword;
    EditText password_edittext,username_edittext;
    String username, password,email;
    Button login;
    RelativeLayout progressLayout;
    private ProgressDialog progressDialog;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        textView = findViewById(R.id.register);
        skip = findViewById(R.id.skip);

        username_edittext = findViewById(R.id.username_edittext);
        password_edittext = findViewById(R.id.password_edittext);
        login = findViewById(R.id.login);
        forgotPassword = findViewById(R.id.forgotPassword);
        progressBar = findViewById(R.id.progressLayout);
        progressDialog = new ProgressDialog(LoginActivity.this);


//        if(SaveSharedPreference.getLoggedStatus(getApplicationContext())) {
//            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//            startActivity(intent);
//            finish();
//        }

        Intent getInt = new Intent();
        String a = getIntent().getStringExtra("sa");

        if(a != null)
        if(a.equals("s")) {
            if (restorePrefData()) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }
        }

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,SignupActivity.class));
            }
        });

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,MainActivity.class));
                savePrefsData();
                finish();
            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = username_edittext.getText().toString();
                password = password_edittext.getText().toString();

                if (!username.isEmpty() && !password.isEmpty()) {
                    progressBar.setVisibility(View.VISIBLE);

                    // Instantiate the RequestQueue
                    RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);

                    // URL of your login API
                    String url = GAMES_ + "login.php";

                    // Create a request body with the username and password
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    progressBar.setVisibility(View.GONE);
                                    Log.i("LoginResponse", response);

                                    if (response.equals("Login Success")) {
                                        // Save user data in SharedPreferences
                                        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                                        SharedPreferences.Editor editor = pref.edit();
                                        editor.putString(USER_NAME, username);
                                        editor.apply();

                                        // Set logged-in status
                                        SaveSharedPreference.setLoggedIn(LoginActivity.this, true);

                                        // Navigate to the next activity
                                        Intent intent = new Intent(LoginActivity.this, FirstScreenActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(LoginActivity.this, response, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(LoginActivity.this, "Login failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                    Log.e("LoginError", error.toString());
                                }
                            }) {
                        @Override
                        protected Map<String, String> getParams() {
                            // Add parameters to the request
                            Map<String, String> params = new HashMap<>();
                            params.put("username", username);
                            params.put("password", password);
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
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(LoginActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_forgetpassword);
                dialog.getWindow().setLayout(ViewPager.LayoutParams.FILL_PARENT, ViewPager.LayoutParams.WRAP_CONTENT);
                final EditText editText_forgetPassword = dialog.findViewById(R.id.editText_forget_password);
                Button buttonForgetPassword = dialog.findViewById(R.id.button_forgetPassword);
                buttonForgetPassword.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String stringForgetPassword = editText_forgetPassword.getText().toString();
                        editText_forgetPassword.setError(null);
                        if (!isValidMail(stringForgetPassword) || stringForgetPassword.isEmpty()) {
                            editText_forgetPassword.requestFocus();
                            editText_forgetPassword.setError(getResources().getString(R.string.please_enter_email));
                        } else {
                            if (Ex.isConnectionEnable(LoginActivity.this)) {
                                forgetPassword(stringForgetPassword);
                            } else {
                               // Toast.makeText(LoginActivity.this, getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
                            }
                            dialog.dismiss();
                        }
                    }
                });
                dialog.show();
            }
        });
    }
//
//    public void Validate_form() {
//        username_edittext.setError(null);
//        password_edittext.setError(null);
//
//        if (!isValidMail(username) || username.isEmpty()) {
//            password_edittext.requestFocus();
//            username_edittext.setError(getResources().getString(R.string.app_name));
//        } else if (password.equals("") || password.isEmpty()) {
//            password_edittext.requestFocus();
//            //password_edittext.setError(getResources().getString(R.string.PasswordEditTest_error));
//        } else {
//           username_edittext.clearFocus();
//            password_edittext.clearFocus();
//
//            username_edittext.setText("");
//            password_edittext.setText("");
//
//            if (Ex.isConnectionEnable(LoginActivity.this)) {
//                login(email,password);
//            } else {
//              //  Toast.makeText(this, getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
//            }
//        }
//    }

    public void forgetPassword(String sendEmail_forget_password) {

        progressDialog.show();
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);

        String forgetPassword_url = API_Forgot_Pass + "&email=" + sendEmail_forget_password;

        Log.e("fURL",forgetPassword_url);
       AsyncHttpClient client = new AsyncHttpClient();
        client.get(forgetPassword_url, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                Log.d("Response", new String(responseBody));
                String res = new String(responseBody);

                try {
                    JSONObject jsonObject = new JSONObject(res);

                    JSONArray jsonArray = jsonObject.getJSONArray("spin");

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject object = jsonArray.getJSONObject(i);
                        String msg = object.getString("msg");
                        String success = object.getString("success");
Log.e("fURL1",msg + success);
                        if (success.equals("1")) {
                            Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }

                    }

                    progressDialog.dismiss();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
            progressDialog.dismiss();
            }

        });
    }
    public void ShowHidePass(View view) {
        if(view.getId()==R.id.show_pass_btn){
            if(password_edittext.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                ((ImageView)(view)).setImageResource(R.drawable.hide_password);
                //Show Password
                password_edittext.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
            else{
                ((ImageView)(view)).setImageResource(R.drawable.show_password);
                //Hide Password
                password_edittext.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        }
    }

    private boolean restorePrefData() {


        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
        Boolean isIntroActivityOpnendBefore = pref.getBoolean("isIntroOpnend",false);
        return  isIntroActivityOpnendBefore;



    }

    private void savePrefsData() {

        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isIntroOpnend",true);
        editor.apply();


    }
}