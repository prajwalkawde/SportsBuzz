package com.fantasy.team.prediction.Adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fantasy.team.prediction.MatchDetailsActivity;
import com.fantasy.team.prediction.Model.Match_Model;

import com.fantasy.team.prediction.R;
import com.fantasy.team.prediction.util.Utility;
import com.fantasy.team.prediction.util.font.BoldTextView;
import com.fantasy.team.prediction.util.font.MediumTextView;
import com.fantasy.team.prediction.util.font.RegularTextView;
import com.fantasy.team.prediction.util.ApiConfig;

import java.util.ArrayList;
import java.util.List;

public class CommonAdapter extends RecyclerView.Adapter<CommonAdapter.Viewholder> {


    private List<Match_Model> matchModel_list = new ArrayList<>();

    public CommonAdapter(List<Match_Model> matchModel_list, Context mContext) {
        this.matchModel_list = matchModel_list;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_home_cricket, parent, false);
        return new Viewholder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, @SuppressLint("RecyclerView") int position) {

        if (matchModel_list.get(position).getPlayer1_profile() != null && !matchModel_list.get(position).getPlayer1_profile().isEmpty()) {
            Glide.with(holder.team1_image.getContext()).load(ApiConfig.IMG + matchModel_list.get(position).getPlayer1_profile()).into(holder.team1_image);
            Log.e("MSG1", ApiConfig.IMG + matchModel_list.get(position).getPlayer1_profile());
        }
        if (matchModel_list.get(position).getPlayer1_profile() != null && !matchModel_list.get(position).getPlayer1_profile().isEmpty()) {
            Glide.with(holder.team2_image.getContext()).load(ApiConfig.IMG + matchModel_list.get(position).getPlayer2_profile()).into(holder.team2_image);
            Log.e("MSG2", ApiConfig.IMG + matchModel_list.get(position).getPlayer1_profile());
        }

        holder.txtTeam1Name.setText(matchModel_list.get(position).getPlayer1_name());
        holder.txtTeam2Name.setText(matchModel_list.get(position).getPlayer2_name());

        holder.txtSeriesName.setText(matchModel_list.get(position).getMatch_name());
        holder.txtViewsCount.setText(matchModel_list.get(position).getViews());
        holder.txtDTIME.setText(matchModel_list.get(position).getMatch_time());
        String tp;
        String teamComingSoon;
        String finalTeam;
        if (matchModel_list.get(position).getTeam_status().equals("Team Coming Soon")) {
            teamComingSoon = "&#10004;";
        }else{
            teamComingSoon = "&#10060;";
        }

        if (matchModel_list.get(position).getTeam_status().equals("Final Team is Published")) {
            finalTeam = "&#10004;";
        }else{
            finalTeam = "&#10060;";
        }

        if (matchModel_list.get(position).getTeam_preview().equals("yes")) {
            tp= "&#10004;";
        }else{
            tp ="&#10060;";
        }
//         holder.txtNote.setText(Html.fromHtml("Preview: "+tp+" | Team Coming Soon: "+teamComingSoon+" | Final Team: "+finalTeam));
//        if (matchModel_list.get(position).getMatch_progress().equals("Covering")) {
//            holder.txtNote.setText(Html.fromHtml("Preview: &#10004; | Pre Toss Team: &#10004; | Final Team: &#10004;"));
//        }

//        if (matchModel_list.get(position).getTeam_status().equals("Team Coming Soon")) {
//            holder.txtNote.setText(Html.fromHtml("Preview: &#10060; | Pre Toss Team: &#10060; | Final Team: &#10060;"));
//        }

        if (matchModel_list.get(position).getTeam_status().equals("Pre-Toss Team is Published")) {
            holder.txtNote.setText(Html.fromHtml("Preview: &#10004; | Pre Toss Team: &#10004; | Final Team: &#10060;"));
        }
//
        if (matchModel_list.get(position).getTeam_status().equals("Final Team is Published")) {
            holder.txtNote.setText(Html.fromHtml("Preview: &#10004; | Pre Toss Team: &#10004; | Final Team: &#10004;"));
        }



        CountDownTimer unused = holder.timer = new CountDownTimer(Utility.convertTimeInMillis("dd-MM-yyyy HH:mm:ss", matchModel_list.get(position).getMatch_time()) - System.currentTimeMillis(), 1000) {
            public void onTick(long j) {
                holder.dateTv.setText(Utility.updateTimeRemaining(j));
            }

            public void onFinish() {
                holder.dateTv.setText("00:00:00");//replace(" ", "\n"));
                holder.dateTv.setText(matchModel_list.get(position).getMatch_time().replace(" ", "\n"));//replace(" ", "\n"));
            }
        }.start();

        holder.itemView.setOnClickListener(v -> {
            if (!matchModel_list.get(position).getTeam_status().equals("Team Coming Soon")) {
                Intent intent = new Intent(v.getContext(), MatchDetailsActivity.class);
                intent.putExtra("id", matchModel_list.get(position).getMatchId());
                intent.putExtra("position", matchModel_list.get(position).getMatchCategory());
                intent.putExtra("team1",matchModel_list.get(position).getPlayer1_profile());
                intent.putExtra("team2",matchModel_list.get(position).getPlayer2_profile());
                intent.putExtra("name2",matchModel_list.get(position).getPlayer2_name());
                intent.putExtra("name1",matchModel_list.get(position).getPlayer1_name());

                v.getContext().startActivity(intent);
            } else {

                LayoutInflater inflater = LayoutInflater.from(holder.itemView.getContext());
                View v1 = inflater.inflate(R.layout.item_click_dialog, null);
                Button dismiss = v1.findViewById(R.id.dismiss);
                final AlertDialog alertDialog = new AlertDialog.Builder(holder.itemView.getContext())
                        .setView(v1)
                        .setCancelable(false)
                        .create();
                alertDialog.show();

                dismiss.setOnClickListener(v2 -> alertDialog.cancel());
            }
        });

    }

    @Override
    public int getItemCount() {
        return matchModel_list.size();
    }


    public class Viewholder extends RecyclerView.ViewHolder {
        private CountDownTimer timer;
        // New Layout
        private final RegularTextView txtSeriesName;
        private final BoldTextView txtTeam1Name;
        private final BoldTextView txtTeam2Name;
        private final BoldTextView txtNote;
        private final MediumTextView dateTv;
        private final MediumTextView txtViewsCount;
        private final MediumTextView txtDTIME;
        private final ImageView team1_image;
        private final ImageView team2_image;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            txtSeriesName = itemView.findViewById(R.id.txtSerisName);
            txtTeam1Name = itemView.findViewById(R.id.txtTeam1Name);
            txtTeam2Name = itemView.findViewById(R.id.txtTeam2Name);
            team1_image = itemView.findViewById(R.id.imgTeam1);
            team2_image = itemView.findViewById(R.id.imgTeam2);
            dateTv = itemView.findViewById(R.id.txtDate);
            txtNote = itemView.findViewById(R.id.txtNote2);
            txtViewsCount = itemView.findViewById(R.id.txtViewsCount);
            txtDTIME = itemView.findViewById(R.id.txtNote);

        }
    }
}
