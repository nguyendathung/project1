package com.example.mybook;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybook.Adapter.MyChapterAdapter;
import com.example.mybook.Common.Common;
import com.example.mybook.Model.Comic;

public class ChaptersActivity extends AppCompatActivity {

    RecyclerView recyclerView_chapter;
    TextView txt_chapter_name;
    LinearLayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapters);

        //View

        txt_chapter_name=(TextView) findViewById(R.id.txt_chapter_name);
        recyclerView_chapter = (RecyclerView) findViewById(R.id.recycler_chapter);
        recyclerView_chapter.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView_chapter.setLayoutManager(layoutManager);
        recyclerView_chapter.addItemDecoration(new DividerItemDecoration(this,layoutManager.getOrientation()));


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(Common.comicSelected.Name);
        //set icon
        toolbar.setNavigationIcon(R.drawable.ic_baseline_chevron_left_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        fetchChapter(Common.comicSelected);

    }

    private void fetchChapter(Comic comicSelected) {
        if(comicSelected.Chapters != null){

        Common.chapterList = comicSelected.Chapters;
        recyclerView_chapter.setAdapter(new MyChapterAdapter(this,comicSelected.Chapters));
        txt_chapter_name.setText(new StringBuilder("Chương (").append(comicSelected.Chapters.size())
        .append(")"));

        }
        else{
            Toast.makeText(this,"Đang được cập nhật...",Toast.LENGTH_SHORT).show();
        }

    }
}