package com.emupapps.the_broker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.emupapps.the_broker.databinding.ListItemSettingBinding;
import com.emupapps.the_broker.models.SettingItem;

import java.util.List;

public class SettingItemsAdapter extends
        RecyclerView.Adapter<SettingItemsAdapter.SettingItemsViewHolder> {

    public interface SettingItemClickHandler {
        void onClick(int position);
    }

    private List<SettingItem> mSettingItems;
    private SettingItemClickHandler mClickHandler;

    public SettingItemsAdapter(List<SettingItem> settingItems,
                               SettingItemClickHandler clickHandler) {
        mSettingItems = settingItems;
        mClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public SettingItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SettingItemsViewHolder(ListItemSettingBinding.inflate(LayoutInflater.
                from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SettingItemsViewHolder holder, int position) {
        holder.mBinding.settingIcon.setImageDrawable(mSettingItems.get(position).getIcon());
        holder.mBinding.settingTitle.setText(mSettingItems.get(position).getTitle());
        holder.mBinding.settingDescription.setText(mSettingItems.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return mSettingItems.size();
    }

    class SettingItemsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ListItemSettingBinding mBinding;

        public SettingItemsViewHolder(ListItemSettingBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
            binding.getRoot().setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mClickHandler.onClick(getAdapterPosition());
        }
    }
}
