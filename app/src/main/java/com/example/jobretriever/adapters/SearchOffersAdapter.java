package com.example.jobretriever.adapters;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jobretriever.R;
import com.example.jobretriever.models.Offer;

import java.util.ArrayList;

public class SearchOffersAdapter extends RecyclerView.Adapter<SearchOffersAdapter.ViewHolder> {
    private ArrayList<Offer> localDataSet;
    Context context;
    public SearchOffersAdapter(Context context,ArrayList<Offer> dataSet) {
        localDataSet = dataSet;
    }



    @NonNull
    @Override
    public SearchOffersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.search_offer_cardview, viewGroup, false);

        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull SearchOffersAdapter.ViewHolder holder, int position) {
        Offer offer = localDataSet.get(position);
        holder.title.setText(offer.getTitle());
        holder.description.setText(offer.getDescription());
        holder.jobThumbnail.setImageResource(R.drawable.thumbnail);

        holder.companyDuration.setText(offer.getEmployer().getBusinessName()+" | "+offer.getDuration());
    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }



    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView jobThumbnail;
        private final TextView title;
        private final TextView companyDuration;
        private final TextView description;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            jobThumbnail = itemView.findViewById(R.id.job_thumbnail);
            title = itemView.findViewById(R.id.row_title);
            this.companyDuration = itemView.findViewById(R.id.company_duration);
            this.description = itemView.findViewById(R.id.row_description);
        }
    }
}
