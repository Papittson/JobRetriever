package com.example.jobretriever.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jobretriever.R;
import com.example.jobretriever.models.Applicant;
import com.example.jobretriever.models.Application;
import com.example.jobretriever.models.Offer;

import java.util.List;

public class ApplicationsAdapter extends RecyclerView.Adapter<ApplicationsAdapter.ViewHolder> {
    private final Context context;
    private final FragmentActivity activity;
    private final List<Application> applications;

    public ApplicationsAdapter(Context context, FragmentActivity activity, List<Application> applications) {
        this.context = context;
        this.activity = activity;
        this.applications = applications;
    }

    @NonNull
    @Override
    public ApplicationsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.offer_cardview, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ApplicationsAdapter.ViewHolder holder, int position) {
        Application application = applications.get(position);
        Offer offer = application.getOffer();
        Applicant applicant = application.getApplicant();

        holder.title.setText(offer.getTitle());
        holder.description.setText(context.getString(R.string.applied_by_user, applicant.getFirstname(), applicant.getLastname()));
        holder.location.setText(offer.getLocation());
        holder.applicationStatus.setText(offer.getApplicationStatus(applicant.getId()));
        String companyDurationStr = context.getString(R.string.company_duration, offer.getEmployer().getBusinessName(), offer.getDuration());
        holder.companyDuration.setText(companyDurationStr);
    }

    @Override
    public int getItemCount() {
        return applications.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final TextView companyDuration;
        private final TextView location;
        private final TextView applicationStatus;
        private final TextView description;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.title = itemView.findViewById(R.id.row_title);
            this.location = itemView.findViewById(R.id.offer_location);
            this.companyDuration = itemView.findViewById(R.id.company_duration);
            this.applicationStatus = itemView.findViewById(R.id.application_status);
            this.description = itemView.findViewById(R.id.row_description);
        }
    }
}
