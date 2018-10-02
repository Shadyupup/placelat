package com.example.acer_pc.placelat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;


public class info extends Fragment {
    TextView address, phone, price, weburl, website;
    TableRow row1, row2, row3, row4, row5,row6;
    private RatingBar ratingBar = null;
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.info, container, false);
        address = (TextView)rootView.findViewById(R.id.textView2);
        phone = (TextView)rootView.findViewById(R.id.textView4);
        price = (TextView)rootView.findViewById(R.id.textView6);
        ratingBar = (RatingBar) rootView.findViewById(R.id.ratingbar);
        weburl = (TextView)rootView.findViewById(R.id.textView10);
        website =(TextView)rootView.findViewById(R.id.textView12);
        row1 = (TableRow)rootView.findViewById(R.id.address);
        row2 = (TableRow)rootView.findViewById(R.id.phone);
        row3 = (TableRow)rootView.findViewById(R.id.price);
        row4 = (TableRow)rootView.findViewById(R.id.rate);
        row5 = (TableRow)rootView.findViewById(R.id.weburl);
        row6 = (TableRow)rootView.findViewById(R.id.website);

        return rootView;

    }
    public void show(JSONObject array) {
        try {

            JSONObject obj=array.getJSONObject("geometry");
            JSONObject obj1 = obj.getJSONObject("location");
            String lat = obj1.getString("lat");
            String lon = obj1.getString("lng");

            if (array.has("formatted_address")) {
                String addressText = array.getString("formatted_address");
                address.setText(addressText);
            }
            else {
                row1.setVisibility(View.GONE);

            }
            if (array.has("formatted_phone_number")) {
                String number = array.getString("formatted_phone_number");
                phone.setText(number);
            }
            else {
                row2.setVisibility(View.GONE);

            }
            if (array.has("price_level")) {
                int num = Integer.parseInt(array.getString("price_level"));
                String word = "";
                for (int i = 0; i < num; i++) {
                    word += '$';
                }
                price.setText(word);
            }
            else {
                row3.setVisibility(View.GONE);

            }
            if (array.has("rating")) {
                // rating.setText();
                System.out.println(array.getString("rating"));
                float num = Float.parseFloat(array.getString("rating"));
                System.out.println("rating   " + num);
                ratingBar.setRating(num);
                ratingBar.setStepSize(0.1f);

            }
            else {
                row4.setVisibility(View.GONE);

            }
            if (array.has("url")) {

                weburl.setText(array.getString("url"));
            }
            else {
                row5.setVisibility(View.GONE);

            }
            if (array.has("website")) {
                website.setText(array.getString("website"));
            }
            else {
                row6.setVisibility(View.GONE);

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }



}
