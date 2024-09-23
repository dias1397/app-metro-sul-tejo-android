package com.diasjoao.metrosultejo.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.diasjoao.metrosultejo.R;
import com.diasjoao.metrosultejo.data.model.News;
import com.diasjoao.metrosultejo.ui.news.NewsActivity;
import com.diasjoao.metrosultejo.ui.tariffs.TariffsDetailsActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private Context context;
    private List<News> newsList;

    public NewsAdapter(Context context, List<News> newsList) {
        this.context = context;
        this.newsList = newsList;
    }

    public static class NewsViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewNews;
        TextView textViewNewsTitle;
        TextView textViewNewsDate;

        public NewsViewHolder(View itemView) {
            super(itemView);
            imageViewNews = itemView.findViewById(R.id.news_image);
            textViewNewsTitle = itemView.findViewById(R.id.news_title);
            textViewNewsDate = itemView.findViewById(R.id.news_date);
        }
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_news, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        News news = newsList.get(position);

        holder.textViewNewsTitle.setText(news.getTitle());
        holder.textViewNewsDate.setText(news.getDate());

        if (news.getImageUrl() != null && !news.getImageUrl().isEmpty()) {
            Picasso.get().load(news.getImageUrl()).into(holder.imageViewNews);
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, NewsActivity.class);
            intent.putExtra("NEWS_TITLE", news.getTitle());
            intent.putExtra("NEWS_DETAILS", news.getDetails());
            intent.putExtra("NEWS_IMAGE_URL", news.getImageUrl());
            intent.putExtra("NEWS_DATE", news.getDate());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }
}
