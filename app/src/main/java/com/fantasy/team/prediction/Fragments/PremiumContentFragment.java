package com.fantasy.team.prediction.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import com.fantasy.team.prediction.R;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.fantasy.team.prediction.MainActivity;
import com.fantasy.team.prediction.Model.Match_Model;
import com.fantasy.team.prediction.Model.Match_details;
 
import com.fantasy.team.prediction.util.ApiConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PremiumContentFragment extends Fragment {



    public PremiumContentFragment() {
        // Required empty public constructor
    }

    View view;
    WebView webView;
    Match_Model matchModel;
    String ss,position;
    ProgressBar progressBar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_premium_content, container, false);

        view.findViewById(R.id.btn_subscribe).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.getContext().startActivity(new Intent(requireContext(), MainActivity.class).putExtra("frag","frag"));
                getActivity().finish();
            }
        });



        final Intent intent = getActivity().getIntent();
        ss =  intent.getStringExtra("id");
        position = intent.getStringExtra("position");

        Log.d("Category==>", position);


        if(ApiConfig.showCricketTeamData && position.equals("29")){
            view.findViewById(R.id.card_free_user).setVisibility(View.GONE);
            view.findViewById(R.id.rl_contentLayout).setVisibility(View.VISIBLE);
        }
        matchModel =  new Match_Model();

        showPost();

        webView = view.findViewById(R.id.web_view);
        webView.getSettings().setJavaScriptEnabled(true);

        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
        webView.setLongClickable(false);

        return view;
    }


    public void showPost(){
        progressBar = view.findViewById(R.id.progressloadinfo);
        progressBar.setVisibility(View.VISIBLE);
        ArrayList<Match_details> match_details = new ArrayList<>();
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, ApiConfig.GAMES_+"getmandp.php?id="+ss, null, new Response.Listener<JSONArray>() {
            @Override

            public void onResponse(JSONArray response) {
                Log.e("Rq",String.valueOf(ApiConfig.GAMES_+"getmandp.php?id="+ss));
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject object = response.getJSONObject(i);
                        Match_details matchModel = new Match_details();
                        matchModel.setId(object.getString("id"));
                        matchModel.setPremium_details(object.getString("premium_detail"));
                        match_details.add(matchModel);


                        if(!matchModel.getPremium_details().isEmpty()) {
                            webView.loadDataWithBaseURL((String)
                                    null, "<html><head><style type=\"text/css\">@font-face {font-family: MyFont;}body {font-family: MyFont;font-size: medium;text-align: justify;}</style></head><body>"
                                    + matchModel.getPremium_details() +
                                    "</body></html>", "text/html", "UTF-8", (String) null);
                        }else{
                            Toast.makeText(view.getContext(), "No Details Found", Toast.LENGTH_SHORT).show();
                        }
                        progressBar.setVisibility(View.GONE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(view.getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue queue = Volley.newRequestQueue(requireContext());
        queue.add(request);

    }
}