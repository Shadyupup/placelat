package com.example.acer_pc.placelat;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResponse;
import com.google.android.gms.location.places.PlacePhotoResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class photo extends Fragment {
    String placeId;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private GeoDataClient mGeoDataClient;
    List<Bitmap> imageList;
    List<Bitmap> photoList = new ArrayList<Bitmap>();
    PlacePhotoMetadataBuffer photoMetadataBuffer;

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        placeId = (String) getArguments().get("placeid");
//        mGeoDataClient = Places.getGeoDataClient(getActivity(), null);
//        photoList=getPhotos(placeId);
//
//    }
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.photo, container, false);
        placeId = (String)getArguments().get("placeid");
//        mGeoDataClient=Places.getGeoDataClient(getActivity(),null);
//        getPhotos(placeId,rootView);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerPhoto);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mGeoDataClient=Places.getGeoDataClient(getActivity(),null);
        imageList=new ArrayList<Bitmap>();
        getPhotos();
//        iniphoto();
//        iniphoto(placeId);

        return rootView;
    }

    private void initview(View rootView){
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerPhoto);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
//        iniphoto(placeId);
    }

    private void iniphoto(){
        mAdapter = new MyAdapter(getActivity(),photoList);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void getPhotos(){
        final Task<PlacePhotoMetadataResponse> photoMetadataResponse = mGeoDataClient.getPlacePhotos(placeId);
        photoMetadataResponse.addOnCompleteListener(new OnCompleteListener<PlacePhotoMetadataResponse>() {
            @Override
            public void onComplete(@NonNull Task<PlacePhotoMetadataResponse> task) {
                // Get the list of photos.
                PlacePhotoMetadataResponse photos = task.getResult();
                // Get the PlacePhotoMetadataBuffer (metadata for all of the photos).
                PlacePhotoMetadataBuffer photoMetadataBuffer = photos.getPhotoMetadata();
                // Get the first photos in the list.
                for (int i = 0; i < photoMetadataBuffer.getCount();i++){
                    PlacePhotoMetadata photoMetadata = photoMetadataBuffer.get(i);
                    Task<PlacePhotoResponse> photoResponse = mGeoDataClient.getPhoto(photoMetadata);
                    photoResponse.addOnCompleteListener(new OnCompleteListener<PlacePhotoResponse>() {
                        @Override
                        public void onComplete(@NonNull Task<PlacePhotoResponse> task) {
                            PlacePhotoResponse photo = task.getResult();
                            Bitmap bitmap = photo.getBitmap();
                            imageList.add(bitmap);
                            mAdapter = new MyAdapter(getActivity(),imageList);
                            mRecyclerView.setAdapter(mAdapter);
                        }
                    });
                }
                photoMetadataBuffer.release();
            }
        });
    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{
        List<Bitmap> imageList;
        private LayoutInflater inflater;
        private Context context;
        public MyAdapter(Context context, List<Bitmap> pictureList) {
            this.context = context;
            inflater = LayoutInflater.from(context);
            imageList = pictureList;
        }

        @Override
        public int getItemCount() {
            return imageList == null? 0 : imageList.size();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.photoitem, parent, false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
            Bitmap current = imageList.get(position);
            holder.photo.setImageBitmap(current);
//            Picasso.with(context).load(String.valueOf(current)).into(holder.photos);
        }

        class ViewHolder extends RecyclerView.ViewHolder{
            ImageView photo;
            public ViewHolder(View itemView) {
                super(itemView);
                photo = (ImageView)itemView.findViewById(R.id.photo);
            }
        }
    }
    public static photo createPhoto(String placeid) {
        photo pt = new photo();
        Bundle bundle = new Bundle();
        bundle.putString("placeid",placeid);
        pt.setArguments(bundle);
        return pt;
    }
}
