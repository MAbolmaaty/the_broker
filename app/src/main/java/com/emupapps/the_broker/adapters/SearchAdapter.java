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
import com.emupapps.the_broker.models.search.response.RealEstate;
import com.emupapps.the_broker.utils.interfaces.RealEstateClickHandler;
import com.bumptech.glide.Glide;

import java.util.List;

import static com.emupapps.the_broker.utils.Constants.BASE_URL;
import static com.emupapps.the_broker.utils.Constants.REAL_ESTATE_RENT;
import static com.emupapps.the_broker.utils.Constants.REAL_ESTATE_SALE;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.AdapterViewHolder> {

    private Context mContext;
    private List<RealEstate> mListRealEstates;
    private RealEstateClickHandler mClickHandler;

    public SearchAdapter(Context context,
                         List<RealEstate> listRealEstates,
                         RealEstateClickHandler clickHandler) {
        mContext = context;
        mListRealEstates = listRealEstates;
        mClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_search, parent, false);
        view.setFocusable(true);
        return new AdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterViewHolder holder, int position) {
        holder.mTitle.setText(mListRealEstates.get(position).getTitle());
        holder.mAddress.setText(mListRealEstates.get(position).getFull_address());
        if (mListRealEstates.get(position).getService().equals(REAL_ESTATE_SALE)) {
            holder.mPrice.setText(mContext.getString(R.string.price_amount, mListRealEstates.get(position).getTotal_amount()));
        } else if (mListRealEstates.get(position).getService().equals(REAL_ESTATE_RENT)){
            if (mListRealEstates.get(position).getPrice_for_month() != null) {
                holder.mPrice.setText(String.format(mContext.getResources().getString(R.string.price_amount), mListRealEstates.get(position).getPrice_for_month()));
            } else if (mListRealEstates.get(position).getPrice_for_3month() != null) {
                holder.mPrice.setText(String.format(mContext.getResources().getString(R.string.price_amount), mListRealEstates.get(position).getPrice_for_3month()));
            } else if (mListRealEstates.get(position).getPrice_for_6month() != null) {
                holder.mPrice.setText(String.format(mContext.getResources().getString(R.string.price_amount), mListRealEstates.get(position).getPrice_for_6month()));
            } else if (mListRealEstates.get(position).getPrice_for_12month() != null) {
                holder.mPrice.setText(String.format(mContext.getResources().getString(R.string.price_amount), mListRealEstates.get(position).getPrice_for_12month()));
            }
        }
        if (mListRealEstates.get(position).getPhoto() != null)
            Glide.with(mContext).load(BASE_URL + mListRealEstates.get(position).getPhoto()).into(holder.mRealEstate);
        if (mListRealEstates.get(position).getService().equals(REAL_ESTATE_SALE)) {
            holder.mStatus.setText(mContext.getString(R.string.for_sale));
        } else if (mListRealEstates.get(position).getService().equals(REAL_ESTATE_RENT)) {
            holder.mStatus.setText(mContext.getString(R.string.for_rent));
        }
    }

    @Override
    public int getItemCount() {
        return mListRealEstates.size();
    }

    public class AdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView mRealEstate;
        TextView mTitle;
        TextView mAddress;
        TextView mStatus;
        TextView mPrice;

        public AdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this::onClick);
        }

        @Override
        public void onClick(View v) {
            mClickHandler.onClick(getAdapterPosition());
        }
    }
}
