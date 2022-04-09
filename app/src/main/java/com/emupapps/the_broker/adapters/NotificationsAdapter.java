package com.emupapps.the_broker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.emupapps.the_broker.R;
import com.emupapps.the_broker.models.notifications.Notification;

import java.util.List;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.AdapterViewHolder> {

    public  interface ItemOnClickHandler{
        void onClick(int position);
    }

    private Context mContext;
    private List<Notification> mListNotifications;

    public NotificationsAdapter(Context context, List<Notification> listNotifications) {
        mContext = context;
        mListNotifications = listNotifications;
    }

    @NonNull
    @Override
    public AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_notification, parent, false);
        view.setFocusable(true);
        return new AdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterViewHolder holder, int position) {
        holder.mTitle.setText(mListNotifications.get(position).getTitle());
        holder.mMessage.setText(mListNotifications.get(position).getMessage());
        holder.mDate.setText(mListNotifications.get(position).getUpdated_at().split(" ")[0]);
        holder.mTime.setText(mListNotifications.get(position).getUpdated_at().split(" ")[1]);
    }

    @Override
    public int getItemCount() {
        return mListNotifications.size();
    }

    public class AdapterViewHolder extends RecyclerView.ViewHolder {

        TextView mTitle;
        TextView mMessage;
        TextView mDate;
        TextView mTime;

        public AdapterViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
