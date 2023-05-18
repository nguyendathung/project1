package com.example.mybook.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybook.Common.Common;
import com.example.mybook.Interface.IRecyclerCickListener;
import com.example.mybook.Model.Chapter;
import com.example.mybook.R;
import com.example.mybook.ViewComic;

import java.util.List;

public class MyChapterAdapter extends RecyclerView.Adapter<MyChapterAdapter.MyViewHolder> {

    Context context;
    List<Chapter> chapterslist;
    LayoutInflater inflater;

    public MyChapterAdapter(Context context, List<Chapter> chapterslist) {
        this.context = context;
        this.chapterslist = chapterslist;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview = inflater.inflate(R.layout.chapter_item  ,parent,false);
        return new MyViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.txt_chapter_numb.setText(chapterslist.get(position).Name);

        holder.setRecyclerCickListener(new IRecyclerCickListener() {
            @Override
            public void onClick(View view, int position) {
                Common.chapterSelected = chapterslist.get(position);
                Common.chapterIndex  = position;
                Intent intent = new Intent(context.getApplicationContext(), ViewComic.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return chapterslist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txt_chapter_numb;
        IRecyclerCickListener recyclerCickListener;

        public void setRecyclerCickListener(IRecyclerCickListener recyclerCickListener) {
            this.recyclerCickListener = recyclerCickListener;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_chapter_numb = (TextView) itemView.findViewById(R.id.txt_chapter_numb);

            itemView.setOnClickListener(this);


        }

        @Override
        public void onClick(View view) {
            recyclerCickListener.onClick(view,getAdapterPosition());
        }
    }
}
