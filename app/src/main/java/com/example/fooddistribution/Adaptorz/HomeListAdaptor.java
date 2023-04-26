package com.example.fooddistribution.Adaptorz;

import android.app.Activity;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.browser.customtabs.CustomTabsIntent;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.example.fooddistribution.Models.BannerModel;
import com.example.fooddistribution.R;

import java.util.ArrayList;

public class HomeListAdaptor extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] bannerTitle;
    private final ArrayList<BannerModel> list;

    public HomeListAdaptor(Activity context, String[] bannerTitle, ArrayList<BannerModel> list) {
        super(context, R.layout.homelist_item, bannerTitle);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.bannerTitle = bannerTitle;
        this.list = list;

    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.homelist_item, null,true);

        TextView title = (TextView) rowView.findViewById(R.id.tvTitle);
        TextView source = (TextView) rowView.findViewById(R.id.tvSource);
        TextView readmore = (TextView) rowView.findViewById(R.id.tvReadMore);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.image);
        CardView cardView = rowView.findViewById(R.id.cardView);



        title.setText(bannerTitle[position]);
        Glide.with(context)
                .load(list.get(position).getImageUrl())
                .into(imageView);

        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        CustomTabsIntent customTabsIntent = builder.build();

        readmore.setOnClickListener(t->{
            customTabsIntent.launchUrl(getContext(), Uri.parse(list.get(position).getWebsiteUrl()));
        });
        cardView.setOnClickListener(t->{
            customTabsIntent.launchUrl(getContext(), Uri.parse(list.get(position).getWebsiteUrl()));
        });

        source.setText(list.get(position).getSource());


        return rowView;

    };


}

