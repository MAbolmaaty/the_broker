package com.emupapps.the_broker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.emupapps.the_broker.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class RequestsAdapter extends RecyclerView.Adapter<RequestsAdapter.AdapterViewHolder> {

    private Context mContext;

    public RequestsAdapter(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_request, parent, false);
        return new AdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class AdapterViewHolder extends RecyclerView.ViewHolder {

        CircleImageView mUserImage;
        TextView mRequestType;
        TextView mUsername;
        TextView mDuration;
        ImageView mShow;
        ImageView mTime;

        public AdapterViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
