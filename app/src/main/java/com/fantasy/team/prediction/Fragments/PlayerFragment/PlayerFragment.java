package com.fantasy.team.prediction.Fragments.PlayerFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.fantasy.team.prediction.R;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.fantasy.team.prediction.Adapters.TeamPreviewAdapter;
import com.fantasy.team.prediction.Model.TeamPreviewModel;
 
import com.fantasy.team.prediction.util.ApiConfig;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PlayerFragment extends Fragment {

    View view;
    ProgressBar progressBar;
//    AdView mAdView;
    LinearLayout linearLayout;
    RecyclerView recyclerView;
    List<TeamPreviewModel> teamPreviewModelList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_player, container, false);

        progressBar = view.findViewById(R.id.progressloadinfo);
        recyclerView = view.findViewById(R.id.teamViewrecycler);
        linearLayout = view.findViewById(R.id.no_team_available_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

//        mAdView = view.findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);

//        mAdView.setAdListener(new AdListener() {
//            @Override
//            public void onAdLoaded() {
//                Log.e("ADERROR","Ad Loaded Successfully");
//
//                // Code to be executed when an ad finishes loading.
//            }
//
//            @Override
//            public void onAdFailedToLoad(LoadAdError adError) {
//                Log.e("ADERROR",adError.toString());
//                // Code to be executed when an ad request fails.
//            }
//
//            @Override
//            public void onAdOpened() {
//                // Code to be executed when an ad opens an overlay that
//                // covers the screen.
//            }
//
//            @Override
//            public void onAdClicked() {
//                // Code to be executed when the user clicks on an ad.
//            }
//
//            @Override
//            public void onAdClosed() {
//                // Code to be executed when the user is about to return
//                // to the app after tapping on an ad.
//            }
//        });

        loadTeamPreview();
        return view;
    }

    private void loadTeamPreview() {
        final Intent intent = getActivity().getIntent();
        String id =  intent.getStringExtra("id");
        teamPreviewModelList = new ArrayList<>();
        JsonArrayRequest request3 = new JsonArrayRequest(Request.Method.POST, ApiConfig.GAMES_ + "team_preview.php?id="+id, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                teamPreviewModelList.clear();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject object = response.getJSONObject(i);
                        TeamPreviewModel teamPreviewModel = new TeamPreviewModel();
                         teamPreviewModel.setTeam_preview_image(object.getString("team_images"));
                        teamPreviewModel.setId(object.getString("id"));
                        teamPreviewModel.setTeam_preview_text(object.getString("team_info"));
                        teamPreviewModelList.add(teamPreviewModel);
                        progressBar.setVisibility(View.GONE);
                        String images = object.getString("team_images");
                         //Toast.makeText(view.getContext(), response.toString(), Toast.LENGTH_SHORT).show();
                        if(images.isEmpty()){
                            //Toast.makeText(view.getContext(), "Team Info Not Available.", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            linearLayout.setVisibility(View.VISIBLE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(view.getContext(),"ErroR", Toast.LENGTH_SHORT).show();
                    }
                    TeamPreviewAdapter adapter = new TeamPreviewAdapter(teamPreviewModelList);
                    recyclerView.setAdapter(adapter);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);
            }
        });
        RequestQueue queue3 = Volley.newRequestQueue(view.getContext());
        queue3.add(request3);
    }
}