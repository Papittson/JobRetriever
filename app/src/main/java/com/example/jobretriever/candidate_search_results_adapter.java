package com.example.jobretriever;

import android.app.Application;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jobretriever.models.Offer;

import java.util.Collections;
import java.util.List;

public class candidate_search_results_adapter extends RecyclerView.Adapter<candidate_search_results_adapter.ViewHolder> {

    @NonNull
    List<Offer> offers = Collections.emptyList();
    private Context context;

    public candidate_search_results_adapter(List<Offer> offers, Application application) {
        this.offers = offers;
        this.context = application;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_search_jobs, parent, false);
        candidate_search_results_adapter.ViewHolder vH = new ViewHolder(view);
        return vH;
    }

    @Override
    public void onBindViewHolder(@NonNull candidate_search_results_adapter.ViewHolder vH, int position) {
        vH.getTitle().setText(offers.get(position).getJobTitle());
        vH.getDesc1().setText(offers.get(position).getDuration());
        vH.getDesc2().setText(offers.get(position).getSield());
    }

    @Override
    public int getItemCount() {
        return offers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public ImageView getThumb() {return itemView.findViewById(R.id.job_thumbnail);}
        public TextView getTitle() {return itemView.findViewById(R.id.row_title);}
        public TextView getDesc1() {return itemView.findViewById(R.id.row_description1);}
        public TextView getDesc2() {return itemView.findViewById(R.id.row_description2);}
    }
}
