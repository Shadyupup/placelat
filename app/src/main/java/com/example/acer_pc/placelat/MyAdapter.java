package com.example.acer_pc.placelat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{
    private LayoutInflater inflater;
    private SharedPreferences share;
    List<mainresult> data = Collections.emptyList();
    private Context context;
    public MyAdapter(Context context, List<mainresult> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.resultitem, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        mainresult current = data.get(position);
        holder.address.setText(current.getAddress());
        holder.name.setText(current.getName());
        Picasso.with(context).load(current.getImageUrl()).into(holder.icon);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //System.out.println("hello");
                Intent intent = new Intent(context,detailActivity.class);
                intent.putExtra("id",data.get(position).getPlaceId());
                intent.putExtra("name",data.get(position).getName());
                intent.putExtra("lat",data.get(position).getLat());
                intent.putExtra("lon",data.get(position).getLon());
                intent.putExtra("address",data.get(position).getAddress());

                context.startActivity(intent);
                //System.out.println(data.get(position).getPlaceId());

            }

        });
        holder.buttonFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //System.out.println("2333333chufale");
                holder.buttonFav.setImageResource(R.drawable.heart_fill_red);
                share = context.getSharedPreferences("me",Context.MODE_PRIVATE);
                String findFav = "|" + data.get(position).getName()+ "|" + data.get(position).getAddress()+ "|" + data.get(position).getImageUrl() + "|";
                SharedPreferences.Editor editor = share.edit();
                editor.putString("key",findFav);
                editor.commit();
                Toast.makeText(context, data.get(position).getName()+" was added to favorites", Toast.LENGTH_SHORT).show();



            }
        });



    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView name, address;
        ImageView icon;
        ImageButton buttonFav;

        public MyViewHolder(View itemView) {
            super(itemView);
            name = (TextView)itemView.findViewById(R.id.name);
            address = (TextView)itemView.findViewById(R.id.address);
            icon = (ImageView)itemView.findViewById(R.id.icon);
            buttonFav = (ImageButton)itemView.findViewById(R.id.fav);


        }
    }
}