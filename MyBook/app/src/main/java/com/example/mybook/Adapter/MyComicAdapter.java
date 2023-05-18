package com.example.mybook.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybook.ChaptersActivity;
import com.example.mybook.Common.Common;
import com.example.mybook.Interface.IRecyclerCickListener;
import com.example.mybook.Model.Comic;
import com.example.mybook.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyComicAdapter extends RecyclerView.Adapter<MyComicAdapter.MyViewHolder> {

    Context context;
    List<Comic> comicList;
    LayoutInflater inflater;

    public MyComicAdapter(Context context, List<Comic> comicList) {
        this.context = context;
        this.comicList = comicList;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview = inflater.inflate(R.layout.comic_item,parent,false);
        return new MyViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Picasso.get().load(comicList.get(position).Image).into(holder.comic_image);
        holder.comic_name.setText(comicList.get(position).Name);

        //Su kien
        holder.setRecyclerCickListener(new IRecyclerCickListener() {
            @Override
            public void onClick(View view, int position) {

                //Luu tru truyen da chon
                Common.comicSelected = comicList.get(position);
                Intent intent = new Intent(context.getApplicationContext(), ChaptersActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);


            }
        });
    }

    @Override
    public int getItemCount() {
        return comicList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView comic_name;
        ImageView comic_image;

        IRecyclerCickListener recyclerCickListener;

        public void setRecyclerCickListener(IRecyclerCickListener recyclerCickListener) {
            this.recyclerCickListener = recyclerCickListener;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            comic_image = (ImageView) itemView.findViewById(R.id.image_comic);
            comic_name = (TextView) itemView.findViewById(R.id.comic_name);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            recyclerCickListener.onClick(view,getAdapterPosition());
        }
    }
}
