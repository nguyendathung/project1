package com.example.mybook;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.mybook.Adapter.MyComicAdapter;
import com.example.mybook.Adapter.MySliderAdapter;
import com.example.mybook.Common.Common;
import com.example.mybook.Interface.IBannerLoadDone;
import com.example.mybook.Interface.IComicLoadDone;
import com.example.mybook.Model.Comic;
import com.example.mybook.Service.PicassoLoadingService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;
import ss.com.bannerslider.Slider;

public class MainActivity extends AppCompatActivity implements IBannerLoadDone, IComicLoadDone {

    Slider slider;
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView_comic;
    TextView txt_comic;
    ImageView btn_filter_search;

    //Database
    DatabaseReference banners,comics;

    //Listener
    IBannerLoadDone bannerListener;
    IComicLoadDone comicListener;

    android.app.AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Init Database

        banners = FirebaseDatabase.getInstance().getReference("Banners");
        comics = FirebaseDatabase.getInstance().getReference("Comic");

        //Init Listener

        bannerListener = this;
        comicListener = this;

        btn_filter_search = (ImageView)findViewById(R.id.btn_show_filter_search);
        btn_filter_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,FilterSearchActivity.class));
            }
        });

        slider = (Slider) findViewById(R.id.slider);
        Slider.init(new PicassoLoadingService());

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.linearlayout_to_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.columnColor,
                R.color.white);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadBanner();
                loadComic();
            }
        });

            swipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    loadBanner();
                    loadComic();
                }
            });

            recyclerView_comic = (RecyclerView) findViewById(R.id.recycler_truyen);
            recyclerView_comic.setHasFixedSize(true);
            recyclerView_comic.setLayoutManager(new GridLayoutManager(this,2));

            txt_comic = (TextView) findViewById(R.id.txt_truyen);

    }

    private void loadComic() {
        //Show Dialog
        alertDialog = new SpotsDialog.Builder().setContext(this)
                .setCancelable(false)
                .setMessage("Vui lòng đợi...")
                .build();

        if(!swipeRefreshLayout.isRefreshing())
            alertDialog.show();
        comics.addListenerForSingleValueEvent(new ValueEventListener() {
            List<Comic> comic_load = new ArrayList<>();
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot comicSnapShot:snapshot.getChildren()){
                    Comic comic = comicSnapShot.getValue(Comic.class);
                    comic_load.add(comic);
                }
                comicListener.onComicLoadDoneListener(comic_load);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this,""+error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadBanner() {
        banners.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> bannerList  = new  ArrayList<>();
                for(DataSnapshot bannerSnapShot:snapshot.getChildren()){
                    String image = bannerSnapShot.getValue(String.class);
                    bannerList.add(image);
                }

                //Call listener
                bannerListener.onBannerLoadDoneListener(bannerList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this,""+error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBannerLoadDoneListener(List<String> banners) {
        slider.setAdapter(new MySliderAdapter(banners));

    }

    @Override
    public void onComicLoadDoneListener(List<Comic> comicList) {
        Common.comicList = comicList;

        recyclerView_comic.setAdapter(new MyComicAdapter(getBaseContext(),comicList));
        txt_comic.setText(new StringBuilder("Sách mới (").append(comicList.size())
        .append(")"));

        if(!swipeRefreshLayout.isRefreshing())
            alertDialog.dismiss();
    }
}