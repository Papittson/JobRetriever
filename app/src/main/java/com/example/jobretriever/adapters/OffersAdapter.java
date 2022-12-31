package com.example.jobretriever.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jobretriever.R;
import com.example.jobretriever.fragments.OfferFragment;
import com.example.jobretriever.models.Offer;

import java.util.List;

public class OffersAdapter extends RecyclerView.Adapter<OffersAdapter.ViewHolder> {
    private final Context context;
    private final FragmentActivity activity;
    private final List<Offer> offers;

    public OffersAdapter(Context context, FragmentActivity activity, List<Offer> offers) {
        this.context = context;
        this.activity = activity;
        this.offers = offers;
    }

    @NonNull
    @Override
    public OffersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.search_offer_cardview, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OffersAdapter.ViewHolder holder, int position) {
        Offer offer = offers.get(position);

        holder.title.setText(offer.getTitle());
        holder.description.setText(offer.getDescription());
        // holder.jobThumbnail.setImageResource(R.drawable.thumbnail);
        String companyDurationStr = context.getString(R.string.company_duration, offer.getEmployer().getBusinessName(), offer.getDuration());
        holder.companyDuration.setText(companyDurationStr);
    }

    @Override
    public int getItemCount() {
        return offers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ImageView jobThumbnail;
        private final TextView title;
        private final TextView companyDuration;
        private final TextView description;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            jobThumbnail = itemView.findViewById(R.id.job_thumbnail);
            title = itemView.findViewById(R.id.row_title);
            this.companyDuration = itemView.findViewById(R.id.company_duration);
            this.description = itemView.findViewById(R.id.row_description);
        }

        @Override
        public void onClick(View view) {
            int position = this.getAdapterPosition();
            Bundle args = new Bundle();
            args.putInt("offerIndex", position);
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, OfferFragment.class, args).commit();
        }
    }
}
