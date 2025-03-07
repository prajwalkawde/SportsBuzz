package com.fantasy.team.prediction.Adapters;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fantasy.team.prediction.Model.NewsModel;
import com.fantasy.team.prediction.NewsActivity;
 
import com.fantasy.team.prediction.util.ApiConfig;

import java.text.SimpleDateFormat;
import java.util.List;
import com.fantasy.team.prediction.R;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.Viewholder> {

    List<NewsModel> news_Model_list;

    public NewsAdapter(List<NewsModel> news_Model_list) {
        this.news_Model_list = news_Model_list;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item, parent, false);
        return new Viewholder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, @SuppressLint("RecyclerView") int position) {
        holder.newsHead.setText(news_Model_list.get(position).getNewsHead());
        Glide.with(holder.itemView.getContext()).load(ApiConfig.IMG + news_Model_list.get(position).getNewsImg()).into(holder.newsImg);
        holder.newsCategory.setText(news_Model_list.get(position).getNewsCategory());

        String myDate = news_Model_list.get(position).getNewsDt();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

//        Date date1 = null;
//        try {
//            date1 = sdf.parse(myDate);
//
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//
//        if(date1!=null) {
//            long millis = date1.getTime();
//
//
//            Locale LocaleBylanguageTag = Locale.forLanguageTag("en");
//
//
//            TimeAgoMessages messages = new TimeAgoMessages.Builder().withLocale(LocaleBylanguageTag).build();
//
//            String text = TimeAgo.using(millis, messages);
//
//            holder.newsDt.setText(text);
//        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), NewsActivity.class);
                intent.putExtra("content", news_Model_list.get(position).getNewsContent());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return news_Model_list.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        ImageView newsImg;
        TextView newsHead, newsDt, newsCategory;
        TextView newsContent;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            newsImg = itemView.findViewById(R.id.newsImg);
            newsHead = itemView.findViewById(R.id.newsHead);
            newsContent = itemView.findViewById(R.id.newsContent);
            newsDt = itemView.findViewById(R.id.newsDate);
            newsCategory = itemView.findViewById(R.id.newsCateg);
        }
    }
}
