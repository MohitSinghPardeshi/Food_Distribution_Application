package com.example.fooddistribution.Adaptorz;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.fooddistribution.Models.SliderModel;
import com.example.fooddistribution.R;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.List;

public class SliderAdaptor extends
        SliderViewAdapter<SliderAdaptor.SliderAdapterVH> {

    private Context context;
    private List<SliderModel> mSliderItems;

    public SliderAdaptor(Context context, List<SliderModel> mSliderItems) {
        this.context = context;
        this.mSliderItems = mSliderItems;
    }

    public void renewItems(List<SliderModel> sliderItems) {
        this.mSliderItems = sliderItems;
        notifyDataSetChanged();
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_item, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, final int position) {

        Glide.with(viewHolder.itemView)
                .load(mSliderItems.get(position).getImageUrl())
                .into(viewHolder.imageViewBackground);

        viewHolder.headText.setText(mSliderItems.get(position).getHeadTxt());
        viewHolder.subText.setText(mSliderItems.get(position).getSubTxt());
    }

    @Override
    public int getCount() {
        //slider view count could be dynamic size
        return mSliderItems.size();
    }

    public void addData(List<SliderModel> list){
        mSliderItems = list;
        notifyDataSetChanged();
    }

    class SliderAdapterVH extends ViewHolder {

        View itemView;
        ImageView imageViewBackground;
        TextView headText;
        TextView subText;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider);
            headText = itemView.findViewById(R.id.tv_headertext_auto_image_slider);
            subText = itemView.findViewById(R.id.tv_subtext_auto_image_slider);
            this.itemView = itemView;
        }
    }

}