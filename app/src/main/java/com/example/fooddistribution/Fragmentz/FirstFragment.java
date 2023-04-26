package com.example.fooddistribution.Fragmentz;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.browser.customtabs.CustomTabsIntent;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.util.Pair;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.fooddistribution.Activities.StatusActivity;
import com.example.fooddistribution.Adaptorz.HomeListAdaptor;
import com.example.fooddistribution.Adaptorz.SliderAdaptor;
import com.example.fooddistribution.Models.BannerModel;
import com.example.fooddistribution.Models.SliderModel;
import com.example.fooddistribution.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.slider.Slider;
import com.google.gson.JsonObject;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;


public class FirstFragment extends Fragment {

    private DrawerLayout dl;
    private SliderAdaptor adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_first, container, false);

        Init(view);
        return view;
    }

    private void Init(View view) {
        handleDrawer(view);
        handleSlider(view);

        handleClicks(view);
        handleOfferList(view);


    }

    private void handleOfferList(View view) {

        String [] title = {
                "World Food Wastage","Calorie Wastage Per Person","Data In Food Waste","AI Food Wastage Management"
        };
        ArrayList<BannerModel> list = new ArrayList<>();
        list.add(new BannerModel("https://firebasestorage.googleapis.com/v0/b/excess-food-distribution.appspot.com/o/OtherImages%2Fbannerfour.jpg?alt=media&token=3a023fec-dd19-4639-b532-05103f7fecb2",
                "https://www.worldbank.org/en/topic/poverty/publication/food-price-watch-home",
                        "World Food Wastage","worldbank.org"
                ));
        list.add(new BannerModel("https://firebasestorage.googleapis.com/v0/b/excess-food-distribution.appspot.com/o/OtherImages%2Fbannersix.jpg?alt=media&token=77117a80-139c-4bf1-aadf-ff57d64a301c",
                    "https://www.worldbank.org/en/topic/poverty/publication/food-price-watch-home",
                            "Calorie Wastage Per Person","worldbank.org"
                    ));
        list.add(new BannerModel("https://firebasestorage.googleapis.com/v0/b/excess-food-distribution.appspot.com/o/OtherImages%2Fbannerfive.jpg?alt=media&token=eaa77176-2ff3-4686-974c-e2946378b060",
                "https://blogs.mathworks.com/headlines/2017/03/28/using-data-analytics-to-reduce-food-waste/",
                "Data In Food Waste","mathworks.com"
        ));
        list.add(new BannerModel("https://firebasestorage.googleapis.com/v0/b/excess-food-distribution.appspot.com/o/OtherImages%2Fbannerthree.jpg?alt=media&token=dedec984-e48d-4f39-9090-c7bbfc33c0b9",
                "https://www.analyticsvidhya.com/blog/2023/01/food-waste-management-ai-driven-food-waste-technologies/",
                "AI Food Wastage Management","analyticsvidhya.com"
        ));

        ListView listView = view.findViewById(R.id.donateHomeListView);
        listView.setEnabled(false);
        HomeListAdaptor adapter=new HomeListAdaptor(getActivity(),title,list);
        listView.setAdapter(adapter);
    }

    private void handleClicks(View view) {


        //finview
        ExtendedFloatingActionButton fab = view.findViewById(R.id.fabBtDonate);
        CardView foodCv = view.findViewById(R.id.foodHomeCV);
        CardView educationCv = view.findViewById(R.id.educationHomeCV);
        CardView waterCv = view.findViewById(R.id.waterHomeCV);
        CardView clothesCv = view.findViewById(R.id.clothesHomeCV);

        String foodUrl = "https://www.akshayapatra.org/#:~:text=The%20Akshaya%20Patra%20Foundation%20is,schools%20and%20Government%2Daided%20schools.";
        String educationUrl = "https://www.giveindia.org/education";
        String waterUrl = "https://www.wateraid.org/in/";
        String clothesUrl = "https://sadsindia.org/";
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        // set toolbar color and/or setting custom actions before invoking build()
        // Once ready, call CustomTabsIntent.Builder.build() to create a CustomTabsIntent
        CustomTabsIntent customTabsIntent = builder.build();
        // and launch the desired Url with CustomTabsIntent.launchUrl()

        foodCv.setOnClickListener(t ->{
            customTabsIntent.launchUrl(getContext(), Uri.parse(foodUrl));
        });
        educationCv.setOnClickListener(t ->{
            customTabsIntent.launchUrl(getContext(), Uri.parse(educationUrl));
        });
        waterCv.setOnClickListener(t ->{
            customTabsIntent.launchUrl(getContext(), Uri.parse(waterUrl));
        });
        clothesCv.setOnClickListener(t ->{
            customTabsIntent.launchUrl(getContext(), Uri.parse(clothesUrl));
        });


        //Onclick
        fab.setOnClickListener(t ->{
            Fragment fragment = new SecondFragment(); // replace MyFragment with the desired fragment class
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.idFragContainer,
                    fragment
            ).addToBackStack(null).commit();
            BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.idBotNavi);
            bottomNavigationView.setSelectedItemId(R.id.idBtmNavDonation); // replace R.id.navigation_my_fragment with the ID of the corresponding menu item in the BottomNavigationView
        });

    }

    private void handleSlider(View view) {
        SliderView sliderView = view.findViewById(R.id.imageSlider);
        ArrayList<SliderModel> list = new ArrayList<>();



        list.add(new SliderModel(
                "https://firebasestorage.googleapis.com/v0/b/excess-food-distribution.appspot.com/o/OtherImages%2Fsliderimageone.jpg?alt=media&token=53ee36f3-df61-4490-be71-4655793b0bc5",
                "Give A Little",
                "Help A Lot."
        ));
        list.add(new SliderModel(
                "https://firebasestorage.googleapis.com/v0/b/excess-food-distribution.appspot.com/o/OtherImages%2Fsliderimagethree.jpg?alt=media&token=acb62780-05ed-432b-b735-1810f71dd30c",
                "Help Spread Love",
                "by Donating Food."
        ));
        list.add(new SliderModel(
                "https://firebasestorage.googleapis.com/v0/b/excess-food-distribution.appspot.com/o/OtherImages%2Fsliderimagetwo.jpg?alt=media&token=20c338c5-d0de-4bce-b513-5b76be502a42",
                "Feed India",
                "Donate to Charity."
        ));

        adapter = new SliderAdaptor(getContext(),list);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
        sliderView.setSliderAdapter(adapter);
        sliderView.setScrollTimeInSec(5);
        sliderView.setAutoCycle(true);
        sliderView.startAutoCycle();
    }

    private void handleDrawer(View view) {
        ImageView hamburger = view.findViewById(R.id.hamburger_toolbar);
        hamburger.setOnClickListener(v ->{
            dl = getActivity().findViewById(R.id.drawerLayout);
            dl.openDrawer(Gravity.LEFT);
        });

    }
}