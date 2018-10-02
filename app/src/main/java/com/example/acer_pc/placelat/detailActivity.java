package com.example.acer_pc.placelat;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class detailActivity extends AppCompatActivity {
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private List<Fragment> list;
    private JSONObject result;
    private String name,yelpName, yelpAddress, city,state;
    private String addressArray;
    private String yelpUrl;
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mydetail);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.
        queue = Volley.newRequestQueue(this);
        final Intent intent = getIntent();
        String placeid = intent.getStringExtra("id");
        String name = intent.getStringExtra("name");
        setTitle(name);



        yelpName = name.trim();
        yelpName = yelpName.replace(" ","20%");
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

//        Intent intent = getIntent();
//        String placeid = intent.getStringExtra("placeid");
//        String name = intent.getStringExtra("name");
//        setTitle(name);
        list = new ArrayList<>();
        final info myinfo = new info();
        list.add(myinfo);
        list.add(photo.createPhoto(placeid));
        final map mymap = new map();
        list.add(mymap);
        final reviews fragment = new reviews();
        list.add(fragment);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        tabLayout.getTabAt(0).setIcon(R.drawable.info_outline);
        tabLayout.getTabAt(1).setIcon(R.drawable.photos);
        tabLayout.getTabAt(2).setIcon(R.drawable.map);
        tabLayout.getTabAt(3).setIcon(R.drawable.review);
        String placeUrl = "https://maps.googleapis.com/maps/api/place/details/json?placeid="+placeid+"&key=AIzaSyCUEZ1nnYe-kq4goeI0G9_iaKJwDShq6sc";
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, placeUrl, (JSONObject) null, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject response) {
                try {
                    result = response.getJSONObject("result");

                    addressArray = result.getString("formatted_address");
                    myinfo.show(result);
                    String address = result.getString("formatted_address");
                    mymap.show(intent.getStringExtra("lat"),intent.getStringExtra("lon"),address);


                    //final JSONArray array = result.getJSONArray("reviews");
                    StringRequest request = new StringRequest(Request.Method.GET, getYelp(addressArray),new Response.Listener<String>() {


                        public void onResponse(String response) {
                            try {

                                if (!response.equals("error")) {
                                    JSONArray yelpArray = new JSONArray(response);
                                    fragment.show(result, yelpArray);
                                }
                                else {
                                    fragment.showGoogle(result);
                                    System.out.println("no Yelp");
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }

                    },new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();

                        }
                    });
                    queue.add(request);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.println(response);

            }

        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        queue.add(jsonRequest);
    }
    public String getYelp(String addressArray) throws JSONException {
        yelpAddress = addressArray.split(",")[0];
        city = addressArray.split(",")[1];
        state = addressArray.split(",")[2].split(" ")[1];
        System.out.println(state);
        System.out.println(city);
        System.out.println(yelpAddress);
        //http://csci571-hw8-nodejs.us-east-2.elasticbeanstalk.com

        yelpUrl = "http://csci571-wang.us-east-1.elasticbeanstalk.com/yelpmatch?name="+yelpName+"&address1="+yelpAddress+"&city="+city+"&state="+state+"";
        System.out.println(yelpUrl);
        return yelpUrl;
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return list.get(position);
        }

        @Override
        public int getCount() {
            return 4;
        }
    }
}
