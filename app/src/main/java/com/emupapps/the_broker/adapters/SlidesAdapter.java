package com.emupapps.the_broker.adapters;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.emupapps.the_broker.R;
import com.emupapps.the_broker.models.slides.Slide;
import com.bumptech.glide.Glide;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.List;

import static com.emupapps.the_broker.utils.Constants.BASE_URL;

public class SlidesAdapter extends SliderViewAdapter<SlidesAdapter.AdapterViewHolder> {

    private Context mContext;
    private List<Slide> mListSlides;

    public SlidesAdapter(Context context, List<Slide> listSlides) {
        mContext = context;
        mListSlides = listSlides;
    }

    @Override
    public AdapterViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_slide,
                null);
        return new AdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdapterViewHolder viewHolder, int position) {
        Glide.with(mContext).load(mListSlides.get(position).getImage()).
                into(viewHolder.mBackground);
    }

    @Override
    public int getCount() {
        return mListSlides.size();
    }

    class AdapterViewHolder extends SliderViewAdapter.ViewHolder {

        ImageView mBackground;

        public AdapterViewHolder(View itemView) {
            super(itemView);
            mBackground = itemView.findViewById(R.id.background);
        }
    }
}
