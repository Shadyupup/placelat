package com.example.acer_pc.placelat;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class reviews extends Fragment {
    private  List<oneReviews> list, yelpList,listRate, yelpListRate;
    private  RecyclerView recyclerView;
    private  reviewAdapter adapter,adapterYelp;
    String name,photo_url,rating, text, time;
    private JSONArray mdata,yelpData;
    private Spinner reviewSpinner, sortSpinner;
    private TextView noRes;
    String selectedItem;


    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.review_fragment, container, false);
        listRate = new ArrayList<>();
        list = new ArrayList<>();
        yelpList = new ArrayList<>();
        yelpListRate = new ArrayList<>();
        recyclerView = (RecyclerView)rootView.findViewById(R.id.recyclerViewreviews);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        reviewSpinner = (Spinner)rootView.findViewById(R.id.googleSpinner);
        sortSpinner = (Spinner)rootView.findViewById(R.id.rate);
        noRes = (TextView)rootView.findViewById(R.id.no);

        if (mdata != null) {
            noRes.setVisibility(View.GONE);
            for (int i = 0; i < mdata.length(); i++) {
                try {
                    JSONObject obj = mdata.getJSONObject(i);
                    //System.out.println(obj);
                    String time = obj.getString("time");
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Long tm = Long.valueOf(time+"000");
                    Date dt = new Date(tm);
                    String date = simpleDateFormat.format(dt);

                    oneReviews review = new oneReviews(
                            obj.getString("author_name"),
                            obj.getString("rating"),
                            date,
                            obj.getString("text"),
                            obj.getString("profile_photo_url"),
                            obj.getString("author_url")
                    );

                    list.add(review);
                    listRate = new ArrayList<>(list);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        if (yelpData != null) {
            noRes.setVisibility(View.GONE);
            for (int i = 0; i < yelpData.length(); i++) {
                try {
                    JSONObject obj = yelpData.getJSONObject(i);
                    JSONObject user = obj.getJSONObject("user");
                    oneReviews reviews = new oneReviews(
                            user.getString("name"),
                            obj.getString("rating"),
                            obj.getString("time_created"),
                            obj.getString("text"),
                            user.getString("image_url"),
                            obj.getString("url")
                    );
                    yelpList.add(reviews);
                    yelpListRate = new ArrayList<>(yelpList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }



        adapter = new reviewAdapter(getContext(), list);
        recyclerView.setAdapter(adapter);

        reviewSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedItem = parent.getItemAtPosition(position).toString();
                if (selectedItem.equals("Yelp reviews")) {
                    if (yelpData == null){
                        noRes.setVisibility(View.VISIBLE);
                    }

                    adapterYelp = new reviewAdapter(getContext(),yelpList);
                    recyclerView.setAdapter(adapterYelp);

                }
                else {
                    if (mdata == null) {
                        noRes.setVisibility(View.VISIBLE);
                    }
                    adapter = new reviewAdapter(getContext(), list);
                    recyclerView.setAdapter(adapter);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectItem = parent.getItemAtPosition(position).toString();

                if (selectItem.contains("Highest rating")) {
                    System.out.println("HRating");
                    if (selectedItem.contains("Google reviews")) {
                        sortHighRate(listRate);
                        adapter = new reviewAdapter(getContext(), listRate);
                        recyclerView.setAdapter(adapter);

                    }
                    else {

                        sortHighRate(yelpListRate);
                        adapter = new reviewAdapter(getContext(), yelpListRate);
                        recyclerView.setAdapter(adapter);

                    }

                }
                else if (selectItem.contains("Lowest rating")) {
                    System.out.println("LRating");
                    if (selectedItem.contains("Google reviews")) {
                        sortLowRate(listRate);
                        adapter = new reviewAdapter(getContext(), listRate);
                        recyclerView.setAdapter(adapter);
                    }
                    else {
                        sortLowRate(yelpListRate);
                        adapter = new reviewAdapter(getContext(), yelpListRate);
                        recyclerView.setAdapter(adapter);

                    }


                }
                //return new Date(timeb).getTime() - new Date(timea).getTime();
                else if (selectItem.contains("Most recent")) {
                    System.out.println("Most");
                    if (selectedItem.contains("Google reviews")) {
                        sortMostRecent(listRate);
                        adapter = new reviewAdapter(getContext(), listRate);
                        recyclerView.setAdapter(adapter);
                    }
                    else {
                        sortMostRecent(yelpListRate);
                        adapter = new reviewAdapter(getContext(), yelpListRate);
                        recyclerView.setAdapter(adapter);

                    }

                }
                else if (selectItem.toString().contains("Least recent")) {
                    System.out.println("Least");
                    if (selectedItem.contains("Google reviews")) {
                        sortLeastRecent(listRate);
                        adapter = new reviewAdapter(getContext(), listRate);
                        recyclerView.setAdapter(adapter);
                    }
                    else {
                        sortLeastRecent(yelpListRate);
                        adapter = new reviewAdapter(getContext(), yelpListRate);
                        recyclerView.setAdapter(adapter);
                    }
                }
                else {
                    if (selectedItem.contains("Google reviews")) {
                        adapter = new reviewAdapter(getContext(), list);
                        recyclerView.setAdapter(adapter);
                    }
                    else {
                        adapter = new reviewAdapter(getContext(), yelpList);
                        recyclerView.setAdapter(adapter);
                    }

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //System.out.println("")




        return rootView;
    }
    public void sortHighRate(List<oneReviews> list) {
        Collections.sort(list, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                oneReviews one = (oneReviews)o1;
                oneReviews two = (oneReviews)o2;
                return Integer.parseInt(two.getRating()) - Integer.parseInt(one.getRating());
            }


        });

    }
    public void sortLowRate(List<oneReviews> list) {
        Collections.sort(list, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                oneReviews one = (oneReviews)o1;
                oneReviews two = (oneReviews)o2;
                return Integer.parseInt(one.getRating()) - Integer.parseInt(two.getRating());
            }


        });

    }
    public void sortMostRecent(List<oneReviews> list){
        Collections.sort(list, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                oneReviews one = (oneReviews)o1;
                oneReviews two = (oneReviews)o2;
                Long timeTwo = Timestamp.valueOf(two.getTime()).getTime();
                Long timeOne = Timestamp.valueOf(one.getTime()).getTime();

                if (timeOne > timeTwo) {
                    return -1;
                }
                else if (timeOne == timeTwo) {
                    return 0;
                }else {
                    return 1;
                }
            }

        });

    }
    public void sortLeastRecent(List<oneReviews> list) {
        Collections.sort(list, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                oneReviews one = (oneReviews)o1;
                oneReviews two = (oneReviews)o2;
                Long timeTwo = Timestamp.valueOf(two.getTime()).getTime();
                Long timeOne = Timestamp.valueOf(one.getTime()).getTime();
                if (timeOne > timeTwo) {
                    return 1;
                }
                else if (timeOne == timeTwo) {
                    return 0;
                }else {
                    return -1;
                }
            }

        });

    }


    public void show(JSONObject array, JSONArray yelp) throws JSONException {
        if (array.has("reviews")){
            mdata = array.getJSONArray("reviews");

        }
        else {
            mdata = null;
        }
        yelpData = yelp;
        System.out.println("success " + yelpData);

    }
    public void showGoogle(JSONObject array) throws JSONException {
        if (array.has("reviews")){
            mdata = array.getJSONArray("reviews");

        }
        else {
            mdata = null;
        }
        System.out.println("Google Review" + mdata);

    }

}

