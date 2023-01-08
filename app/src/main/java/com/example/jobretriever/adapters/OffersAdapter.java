package com.example.jobretriever.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jobretriever.R;
import com.example.jobretriever.fragments.OfferFragment;
import com.example.jobretriever.models.Offer;
import com.example.jobretriever.models.User;
import com.example.jobretriever.viewmodels.UserViewModel;

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
                .inflate(R.layout.offer_cardview, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OffersAdapter.ViewHolder holder, int position) {
        Offer offer = offers.get(position);
        User user = UserViewModel.getInstance().getUser().getValue();
        String userId = user != null ? user.getId() : "";

        holder.title.setText(offer.getTitle());
        holder.description.setText(offer.getDescription());
        holder.location.setText(offer.getLocation());
        holder.applicationStatus.setText(offer.getApplicationStatus(userId));
        String companyDurationStr = context.getString(R.string.company_duration, offer.getEmployer().getBusinessName(), offer.getDuration());
        holder.companyDuration.setText(companyDurationStr);
    }

    @Override
    public int getItemCount() {
        return offers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView title;
        private final TextView companyDuration;
        private final TextView location;
        private final TextView applicationStatus;
        private final TextView description;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            this.title = itemView.findViewById(R.id.row_title);
            this.location = itemView.findViewById(R.id.offer_location);
            this.companyDuration = itemView.findViewById(R.id.company_duration);
            this.applicationStatus = itemView.findViewById(R.id.application_status);
            this.description = itemView.findViewById(R.id.row_description);
        }

        @Override
        public void onClick(View view) {
            Offer offer = offers.get(this.getAdapterPosition());
            Bundle args = new Bundle();
            args.putString("offerId", offer.getId());
            activity.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, OfferFragment.class, args)
                    .commit();
        }
    }
}
