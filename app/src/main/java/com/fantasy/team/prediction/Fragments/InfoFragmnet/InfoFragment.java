package com.fantasy.team.prediction.Fragments.InfoFragmnet;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.fantasy.team.prediction.R;


import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.fantasy.team.prediction.Model.Match_Model;
import com.fantasy.team.prediction.Model.Match_details;
 
import com.fantasy.team.prediction.util.ApiConfig;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class InfoFragment extends Fragment {

    String ss,team1imgUrl,team2imgUrl,teamNameA,teamNameB;
    View view;
    WebView webView,team1Web,team2Web;
    ImageView team1img,team2Img;
    Match_Model matchModel;
    TextView tvTeamNameA,tvTeamNameB;
//    AdView mAdView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_info, container, false);
        webView = view.findViewById(R.id.web_view);
        team1img = view.findViewById(R.id.ivTeam1);
        team2Img = view.findViewById(R.id.ivTeam2);
        tvTeamNameA = view.findViewById(R.id.tvTeam1Name);
        tvTeamNameB = view.findViewById(R.id.tvTeam2Name);
        team1Web = view.findViewById(R.id.tvMatchDetail1);
        team2Web = view.findViewById(R.id.tvMatchDetail2);

//        progressBar = view.findViewById(R.id.progressloadinfo);



        final Intent intent = getActivity().getIntent();
        ss =  intent.getStringExtra("id");
        team1imgUrl = intent.getStringExtra("team1");
        team2imgUrl = intent.getStringExtra("team2");
        teamNameA = intent.getStringExtra("name1");
        teamNameB = intent.getStringExtra("name2");

        tvTeamNameA.setText(teamNameA);
        tvTeamNameB.setText(teamNameB);

        Glide.with(getContext()).load(ApiConfig.IMG +team1imgUrl ).into(team1img);
        Glide.with(getContext()).load(ApiConfig.IMG +team2imgUrl ).into(team2Img);

        showPost();
        matchModel =  new Match_Model();
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

    @Override
    public void onPause() {
        super.onPause();
    }

    public void showPost(){

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
                        matchModel.setTeam1(object.getString("team1"));
                        matchModel.setTeam2(object.getString("team2"));
                        matchModel.setMatch_details(object.getString("match_details"));
                        match_details.add(matchModel);


                        if(!matchModel.getMatch_details().isEmpty()) {
                            webView.loadDataWithBaseURL((String)
                                    null, "<html><head><style type=\"text/css\">@font-face {font-family: MyFont;}body {font-family: MyFont;font-size: medium;text-align: justify;}</style></head><body>"
                                    + matchModel.getMatch_details() +
                                    "</body></html>", "text/html", "UTF-8", (String) null);

                            team1Web.loadDataWithBaseURL(
                                    (String)
                                            null, "<html><head><style type=\"text/css\">@font-face {font-family: MyFont;}body {font-family: MyFont;font-size: medium;text-align: justify;}</style></head><body>"
                                            + matchModel.getTeam1() +
                                            "</body></html>", "text/html", "UTF-8", (String) null
                            );

                            team2Web.loadDataWithBaseURL(
                                    (String)
                                            null, "<html><head><style type=\"text/css\">@font-face {font-family: MyFont;}body {font-family: MyFont;font-size: medium;text-align: justify;}</style></head><body>"
                                            + matchModel.getTeam2() +
                                            "</body></html>", "text/html", "UTF-8", (String) null
                            );
                        }else{
                            Toast.makeText(view.getContext(), "No Details Found", Toast.LENGTH_SHORT).show();
                        }
//                        progressBar.setVisibility(View.GONE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                progressBar.setVisibility(View.GONE);
                Toast.makeText(view.getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue queue = Volley.newRequestQueue(requireContext());
        queue.add(request);

    }


}