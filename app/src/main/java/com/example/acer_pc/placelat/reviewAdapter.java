package com.example.acer_pc.placelat;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

public class reviewAdapter extends RecyclerView.Adapter<reviewAdapter.MyViewHolder> {
    private LayoutInflater inflater;
    List<oneReviews> data = Collections.emptyList();
    private Context context;
    private MyViewHolder holder;
    private int position;

    public reviewAdapter(Context context, List<oneReviews> data) {
        System.out.println("233333");
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
        System.out.println("233333" + data);
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.singlereview, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        oneReviews current = data.get(position);
        holder.name.setText(current.getName());
        holder.time.setText(current.getTime());
        holder.text.setText(current.getText());
        Picasso.with(context).load(current.getPhoto_url()).into(holder.image);
        float num = Float.parseFloat(current.getRating());
        holder.rating.setRating(num);
        //holder.rating.setStepSize(1f);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView name, time, text;
        RatingBar rating;
        ImageView image;

        public MyViewHolder(View itemView) {
            super(itemView);
            name = (TextView)itemView.findViewById(R.id.reviewname);
            time = (TextView)itemView.findViewById(R.id.time);
            text = (TextView)itemView.findViewById(R.id.reviewText);
            image = (ImageView)itemView.findViewById(R.id.reviewImage);
            rating = (RatingBar)itemView.findViewById(R.id.ratingbar2);
        }
    }

}
