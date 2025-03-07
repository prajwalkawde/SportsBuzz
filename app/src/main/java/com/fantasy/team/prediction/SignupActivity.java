package com.fantasy.team.prediction;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
 
import com.fantasy.team.prediction.util.ApiConfig;
//import com.vishnusivadas.advanced_httpurlconnection.PutData;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    EditText password_edittext, username_edittext, email_edittext, contact_number_edittext, reference_code_edittext;
    String password, username, email, contact_number, reference_code;
    Button signUp;
    ProgressBar progressLayout;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        username_edittext = findViewById(R.id.username_edittext);
        email_edittext = findViewById(R.id.editText_email);
        contact_number_edittext = findViewById(R.id.contact_number_edittext);
        password_edittext = findViewById(R.id.password_edittext);
        reference_code_edittext = findViewById(R.id.reference_code);
        signUp = findViewById(R.id.signUp);
        progressLayout = findViewById(R.id.progressLayout);


        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = username_edittext.getText().toString();
                email = email_edittext.getText().toString();
                contact_number = contact_number_edittext.getText().toString();
                password = password_edittext.getText().toString();
                reference_code = reference_code_edittext.getText().toString();

                // Validate email format
                if (email.matches(emailPattern)) {
                    if (!username.isEmpty() && !contact_number.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                        progressLayout.setVisibility(View.VISIBLE);

                        // Instantiate the RequestQueue
                        RequestQueue queue = Volley.newRequestQueue(SignupActivity.this);

                        // URL of your signup API
                        String url = ApiConfig.GAMES_ + "signup.php";

                        // Create a POST request using StringRequest
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        progressLayout.setVisibility(View.GONE);
                                        Log.i("SignupResponse", response);

                                        if (response.equals("Sign Up Success")) {
                                            // Redirect to the login activity
                                            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                                            finish();
                                        } else {
                                            Toast.makeText(SignupActivity.this, response, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        progressLayout.setVisibility(View.GONE);
                                        Toast.makeText(SignupActivity.this, "Signup failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                        Log.e("SignupError", error.toString());
                                    }
                                }) {
                            @Override
                            protected Map<String, String> getParams() {
                                // Add parameters to the request
                                Map<String, String> params = new HashMap<>();
                                params.put("username", username);
                                params.put("email", email);
                                params.put("phone_number", contact_number);
                                params.put("password", password);
                                params.put("refer_code", reference_code);
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
                } else {
                    Toast.makeText(SignupActivity.this, "Invalid Email Address", Toast.LENGTH_SHORT).show();
                    email_edittext.setError("Invalid Email");
                }
            }
        });


    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void ShowHidePass(View view) {
        if (view.getId() == R.id.show_pass_btn) {

            if (password_edittext.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
                ((ImageView) (view)).setImageResource(R.drawable.hide_password);
                //Show Password
                password_edittext.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                ((ImageView) (view)).setImageResource(R.drawable.show_password);
                //Hide Password
                password_edittext.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        }
    }
}