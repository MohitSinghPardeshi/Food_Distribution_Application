package com.example.fooddistribution.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.fooddistribution.AndroidConstants.SaveSharedPreference;
import com.example.fooddistribution.Fragmentz.FirstFragment;
import com.example.fooddistribution.Fragmentz.SecondFragment;
import com.example.fooddistribution.Fragmentz.ThirdFragment;
import com.example.fooddistribution.Models.NGOz;
import com.example.fooddistribution.Models.UserModel;
import com.example.fooddistribution.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import android.os.AsyncTask;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity {

    ArrayList<NGOz> ngolist = new ArrayList<>();
    private BottomNavigationView bottomNavigationView;
    private Dialog mDialog;

    private WebView webView;


    private DrawerLayout dl;
    private NavigationView nav;

    private final Fragment firstFrag = new FirstFragment();
    private final Fragment secondFrag = new SecondFragment();
    private final Fragment thirdFrag = new ThirdFragment();

    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private StorageReference firebaseStorage;

    private static final String TAG = "WebScrapingTAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.idBotNavi);
        getSupportFragmentManager().beginTransaction().replace(R.id.idFragContainer,
                firstFrag
        ).addToBackStack(null).commit();
        bottomNavigationView.setSelectedItemId(R.id.idBtmNavHome);

        dl = findViewById(R.id.drawerLayout);
        nav = findViewById(R.id.navViewNV);


        try {
            Init();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private void Init() throws IOException {

        //firebase
        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance().getReference();

        handleFirebaseUserData();

        handleFragments();
        handleDrawerLayoutClicks();
//        new ScrapeTask().execute();
//        handleWebView();

    }

    private void handleWebView() {
        webView = findViewById(R.id.web_view);
        webView.getSettings().setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                // scroll to the bottom of the page to load all the data
                // loop to keep scrolling down until all data is loaded
                view.evaluateJavascript("window.scrollTo(0, document.body.scrollHeight);", null);

                // delay execution of JavaScript to ensure page is fully loaded
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // extract NGO data after delay
                        view.evaluateJavascript("(function() { " +
                                        "var blocks = document.querySelectorAll('div.ngo-found-block');" +
                                        "var data = '';" +
                                        "for (var i = 0; i < blocks.length; i++) {" +
                                        "   data += blocks[i].textContent.trim()" +
                                        "}" +
                                        "return data;" +
                                        "})()",
                                new ValueCallback<String>() {
                                    @Override
                                    public void onReceiveValue(String value) {
                                        // log the NGO data
                                        value = value.replace("\\n","").replace("\r", "").trim();
                                        String[] arr = value.split("More Details »");

                                        for(String str : arr){
                                            if(str.equals(""))continue;
                                            String address = "";
                                            String sectors = "";
                                            String[] parts = str.split("Address:");
                                            String ngoName = parts[0].trim();
                                            if(parts.length>1){
                                                parts = parts[1].split("Sectors working in:");
                                                address = parts[0].trim();
                                                sectors = "";
                                            }
                                            if (parts.length >= 2) {
                                                sectors = parts[1].trim();
                                            }
                                            NGOz ngoobj = new NGOz(ngoName,address,sectors);
                                            ngolist.add(ngoobj);
                                            Log.d(TAG, "NGO data: " + ngoobj.getAddress() + "|" + ngoobj.getNgoName() +"|" + ngoobj.getSector());
                                        }
                                        view.evaluateJavascript("window.scrollTo(0, document.body.scrollHeight);", null);

                                    }
                                });

                    }
                }, 2000); // delay execution by 2 seconds


            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Log.e(TAG, "WebView error: " + description);
            }
        });

        webView.loadUrl("https://www.oneindia.com/ngos-in-nashik-376.html#");
    }

    private class ScrapeTask extends AsyncTask<Void, Void, ArrayList<String>> {
        @Override
        protected ArrayList<String> doInBackground(Void... voids) {
            ArrayList<String> ngoList = new ArrayList<>();

            try {



                Document doc = Jsoup.connect("https://www.oneindia.com/ngos-in-nashik-376.html#page-24")
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/107.0.0.0 Safari/537.36")
                        .get();
                Elements blocks = doc.select("div.ngo-found-block");

                String ngodetail = blocks.text();
                String[] ngoblk = ngodetail.split("More Details »");
                for (String str : ngoblk) {

                    String[] parts = str.split("Address:");
                    String ngoName = parts[0].trim();

                    parts = parts[1].split("Sectors working in:");
                    String address = parts[0].trim();
                    String sectors = "";
                    if (parts.length >= 2) {
                        sectors = parts[1].trim();
                    }
                    ngoList.add("NGO name: " + ngoName + "\nNGO Address: " + address + "\nNGO Sector: " + sectors);
                }


            } catch (IOException e) {
                Log.e("ScrapeTask", "Error scraping website", e);
            }

            return ngoList;
        }

        @Override
        protected void onPostExecute(ArrayList<String> ngoList) {
            for (String ngoInfo : ngoList) {
                Log.d("ScrapeTask", ngoInfo);
            }
        }
    }

    public static boolean isValidContextForGlide(final Context context) {
        if (context == null) {
            return false;
        }
        if (context instanceof Activity) {
            final Activity activity = (Activity) context;
            if (activity.isDestroyed() || activity.isFinishing()) {
                return false;
            }
        }
        return true;
    }


    private void handleFirebaseUserData() {
        CollectionReference collectionReference = firebaseFirestore.collection("donor").document(mAuth.getUid()).collection("user_person_info");
        collectionReference.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {

                            DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                            UserModel userModel = documentSnapshot.toObject(UserModel.class);
                            if(userModel == null){
                                return;
                            }
                            SaveSharedPreference.setUserModel(MainActivity.this,userModel);
                            ImageView imageview = nav.findViewById(R.id.drawerProfileCV);
                            TextView name = nav.findViewById(R.id.nameDrawerTV);
                            TextView email = nav.findViewById(R.id.emailDrawerTV);
                            if(imageview!=null && isValidContextForGlide(MainActivity.this)){
                                Glide.with(MainActivity.this)
                                        .load(userModel.profileImage)
                                        .placeholder(R.drawable.profile_image) // Replace with your placeholder drawable
                                        .into(imageview);
                            }
                            name.setText(userModel.getFname() + " " + userModel.getLname());
                            email.setText(userModel.getEmail());

                        } else {

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Error In MainActivity" + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void handleDrawerLayoutClicks() {

        mDialog = new Dialog(this);
        mDialog.setContentView(R.layout.logout_popup);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView no = mDialog.findViewById(R.id.noTV);
        TextView yes = mDialog.findViewById(R.id.yesTV);
        TextView text = mDialog.findViewById(R.id.textTV);
        yes.setOnClickListener(view -> {
            SaveSharedPreference.setUserName(this, "");
            SaveSharedPreference.setUserModel(this,new UserModel("","","","","","","","","",""));
            startActivity(new Intent(this, SignInActivity.class));
        });
        no.setOnClickListener(view -> {
            mDialog.dismiss();
        });
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_my_Activities) {

                } else if (id == R.id.nav_aboutus) {

                } else {
                    mDialog.show();
                }
                dl.closeDrawer(GravityCompat.START);
                return true;
            }
        });

    }

    @Override
    public void onBackPressed() {

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.idBotNavi);
        int seletedItemId = bottomNavigationView.getSelectedItemId();
        if (seletedItemId == R.id.idBtmNavHome) {
            finish();
            return;
        } else if (R.id.idBtmNavHome != seletedItemId) {
            bottomNavigationView.setSelectedItemId(R.id.idBtmNavHome);
        } else if (dl.isDrawerOpen(GravityCompat.START)) {
            dl.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void handleFragments() {
        final BottomNavigationView.OnNavigationItemSelectedListener navListener =
                item -> {
                    Fragment selectedFragment = firstFrag;

                    switch (item.getItemId()) {
                        case R.id.idBtmNavHome:
                            selectedFragment = firstFrag;
                            break;
                        case R.id.idBtmNavDonation:
                            selectedFragment = secondFrag;
                            break;
                        case R.id.idBtmNavHistory:
                            selectedFragment = thirdFrag;
                            break;
                        default:
                            return super.onOptionsItemSelected(item);

                    }

                    getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                            .replace(R.id.idFragContainer, selectedFragment)
                            .commit();
                    return true;
                };
        BottomNavigationView bottomNav = findViewById(R.id.idBotNavi);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
    }
}