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
import com.emupapps.the_broker.models.real_estates_owned.RealEstatesOwned;
import com.emupapps.the_broker.utils.interfaces.RealEstateClickHandler;
import com.bumptech.glide.Glide;

import java.util.List;

import static com.emupapps.the_broker.utils.Constants.BASE_URL;
import static com.emupapps.the_broker.utils.Constants.REAL_ESTATE_RENT;
import static com.emupapps.the_broker.utils.Constants.REAL_ESTATE_SALE;

public class RealEstatesOwnedAdapter extends RecyclerView.Adapter<RealEstatesOwnedAdapter.AdapterViewHolder> {

    private Context mContext;
    private List<RealEstatesOwned> mListMyRealEstates;
    private RealEstateClickHandler mClickHandler;

    public RealEstatesOwnedAdapter(Context context,
                                   List<RealEstatesOwned> listMyRealEstates,
                                   RealEstateClickHandler clickHandler) {
        mContext = context;
        mListMyRealEstates = listMyRealEstates;
        mClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_real_estate_owned, parent, false);
        view.setFocusable(true);
        return new AdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterViewHolder holder, int position) {
        if (mListMyRealEstates.get(position).getRealEstateOwned().getPhoto() != null){
            Glide.with(mContext)
                    .load(BASE_URL + mListMyRealEstates.get(position).getRealEstateOwned().getPhoto())
                    .into(holder.mRealEstate);
        }
        holder.mTitle.setText(mListMyRealEstates.get(position).getRealEstateOwned().getTitle());
        holder.mAddress.setText(mListMyRealEstates.get(position).getRealEstateOwned().getFull_address());
        if (mListMyRealEstates.get(position).getRealEstateOwned().getService().equals(REAL_ESTATE_SALE)){
            holder.mStatus.setText(mContext.getString(R.string.for_sale));
            holder.mAmount.setText(mContext.getString(R.string.price_amount,
                    mListMyRealEstates.get(position).getRealEstateOwned().getTotal_amount()));
        } else if (mListMyRealEstates.get(position).getRealEstateOwned().getService().equals(REAL_ESTATE_RENT)){
            holder.mStatus.setText(mContext.getString(R.string.for_rent));
            holder.mAmount.setText(mContext.getString(R.string.price_amount,
                    mListMyRealEstates.get(position).getRealEstateOwned().getMonthRent()));
        }
        holder.mCounter.setText(mListMyRealEstates.get(position).getOrder_num());
    }

    @Override
    public int getItemCount() {
        return mListMyRealEstates.size();
    }

    public class AdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView mRealEstate;
        TextView mTitle;
        TextView mAddress;
        TextView mStatus;
        TextView mAmount;
        TextView mCounter;

        public AdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mClickHandler.onClick(getAdapterPosition());
        }
    }
}
