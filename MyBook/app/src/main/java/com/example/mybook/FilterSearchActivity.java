package com.example.mybook;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybook.Adapter.MyComicAdapter;
import com.example.mybook.Common.Common;
import com.example.mybook.Model.Comic;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FilterSearchActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    RecyclerView recyclerView_filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_search);

        recyclerView_filter = (RecyclerView) findViewById(R.id.recycler_filter_search);
        recyclerView_filter.setHasFixedSize(true);
        recyclerView_filter.setLayoutManager(new GridLayoutManager(this,2));




        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_nav);
        bottomNavigationView.inflateMenu(R.menu.main_menu);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.action_filter:
                        showFilterDialog();
                        break;
                    case R.id.action_search:
                        showSearchDialog();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

    }

    private void showSearchDialog() {
        AlertDialog.Builder alertDialog =  new AlertDialog.Builder(FilterSearchActivity.this);
        alertDialog.setTitle("Nhập sách cần tìm");

        LayoutInflater inflater = this.getLayoutInflater();
        View search_layout = inflater.inflate(R.layout.dialog_search,null);

        EditText edit_search = (EditText) search_layout.findViewById(R.id.edt_search);
        alertDialog.setView(search_layout);
        alertDialog.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.setPositiveButton("Tìm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                fetchSearchComic(edit_search.getText().toString());
            }
        });
        alertDialog.show();
    }

    private void fetchSearchComic(String query) {
        List<Comic> comic_Search = new ArrayList<>();
        for(Comic comic:Common.comicList){
            if(comic.Name.contains(query))
                comic_Search.add(comic);
        }
        if(comic_Search.size() > 0)
            recyclerView_filter.setAdapter(new MyComicAdapter(getBaseContext(),comic_Search));
        else
            Toast.makeText(this,"Không có",Toast.LENGTH_SHORT).show();
    }

    private void showFilterDialog() {
        AlertDialog.Builder alertDialog =  new AlertDialog.Builder(FilterSearchActivity.this);
        alertDialog.setTitle("Chọn thể loại");

        LayoutInflater inflater = this.getLayoutInflater();
        View filter_layout = inflater.inflate(R.layout.dialog_options,null);

        AutoCompleteTextView txt_theloai = (AutoCompleteTextView) filter_layout.findViewById(R.id.txt_theloai);
        ChipGroup chipGroup = (ChipGroup)filter_layout.findViewById(R.id.chipGroup);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item, Common.categories);
        txt_theloai.setAdapter(adapter);
        txt_theloai.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                txt_theloai.setText("");


                Chip chip = (Chip)inflater.inflate(R.layout.chip_item, null, false);
                chip.setText(((TextView)view).getText());
                chip.setOnCloseIconClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        chipGroup.removeView(v);
                    }
                });
                chipGroup.addView(chip);

            }
        });
        alertDialog.setView(filter_layout);
        alertDialog.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.setPositiveButton("Lọc", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                List<String> filter_key = new ArrayList<>();
                StringBuilder filter_query = new StringBuilder("");
                for(int j =0;j<chipGroup.getChildCount();j++)
                {
                    Chip chip = (Chip) chipGroup.getChildAt(j);
                    filter_key.add(chip.getText().toString());
                }
                Collections.sort(filter_key);
                for(String key:filter_key){
                    filter_query.append(key).append(",");
                }
                filter_query.setLength(filter_query.length()-1);

                fetchFilterTheLoai(filter_query.toString());
            }
        });
        alertDialog.show();
    }

    private void fetchFilterTheLoai(String query) {
        List<Comic> comic_filtered = new ArrayList<>();
        for(Comic comic:Common.comicList){
            if(comic.Category != null) {
                if (comic.Category.contains(query))
                    comic_filtered.add(comic);
            }
        }
        if(comic_filtered.size() > 0)
            recyclerView_filter.setAdapter(new MyComicAdapter(getBaseContext(),comic_filtered));
        else
            Toast.makeText(this,"Không có",Toast.LENGTH_SHORT).show();
    }
}