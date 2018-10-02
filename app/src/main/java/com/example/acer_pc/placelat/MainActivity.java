package com.example.acer_pc.placelat;

import android.Manifest;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
//import com.google.gson.JsonParser;

public class MainActivity extends AppCompatActivity {
    String from = "";//选了哪个按钮
    String userDefLoc = "";//用户定义地址
    public static RequestQueue sRequestQueue;
    private LocationManager locationManager;
    private String locationProvider;
    static double currentLat;
    static double currentLng;
    static double lat;
    static double lng;

    static{
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//请求权限
        //int permissionCheck = ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.ACCESS_FINE_LOCATION);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);


        list = new ArrayList<>();
        list.add(new search());
        list.add(new favor());
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        tabLayout.getTabAt(0).setIcon(R.drawable.search);
        tabLayout.getTabAt(1).setIcon(R.drawable.heart_fill_white);


//        TextView newTab = (TextView) LayoutInflater.from(this).inflate(R.layout.activity_main, null);
//        newTab.setText("SEARCH");
//        newTab.setCompoundDrawablesWithIntrinsicBounds(R.drawable.search, 0, 0, 0);
//        tabLayout.getTabAt(0).setCustomView(newTab);

//        try {
//            String serviceString = Context.LOCATION_SERVICE;// 获取的是位置服务
//            LocationManager locationManager = (LocationManager) getSystemService(serviceString);// 调用getSystemService()方法来获取LocationManager对象
//            String provider = LocationManager.GPS_PROVIDER;// 指定LocationManager的定位方法
//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                // TODO: Consider calling
//                //    ActivityCompat#requestPermissions
//                // here to request the missing permissions, and then overriding
//                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                //                                          int[] grantResults)
//                // to handle the case where the user grants the permission. See the documentation
//                // for ActivityCompat#requestPermissions for more details.
//                return;
//            }
//            Location location = locationManager.getLastKnownLocation(provider);// 调用getLastKnownLocation()方法获取当前的位置信息
//            currentLat = location.getLatitude();//获取纬度
//            currentLng = location.getLongitude();//获取经度
//        } catch (Exception e) {
//            String serviceString = Context.LOCATION_SERVICE;// 获取的是位置服务
//            LocationManager locationManager = (LocationManager) getSystemService(serviceString);// 调用getSystemService()方法来获取LocationManager对象
//            String provider = LocationManager.NETWORK_PROVIDER;// 指定LocationManager的定位方法
//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                // TODO: Consider calling
//                //    ActivityCompat#requestPermissions
//                // here to request the missing permissions, and then overriding
//                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                //                                          int[] grantResults)
//                // to handle the case where the user grants the permission. See the documentation
//                // for ActivityCompat#requestPermissions for more details.
//                return;
//            }
//            Location location = locationManager.getLastKnownLocation(provider);// 调用getLastKnownLocation()方法获取当前的位置信息
//            currentLat = location.getLatitude();//获取纬度
//            currentLng = location.getLongitude();//获取经度
////            currentLat=34.024031;
////            currentLng=-118.294603;
//        }
//        Log.v("lat", String.valueOf(currentLng));

    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

//    /**
//     * A placeholder fragment containing a simple view.
//     */
//    public static class PlaceholderFragment extends Fragment {
//        /**
//         * The fragment argument representing the section number for this
//         * fragment.
//         */
//        private static final String ARG_SECTION_NUMBER = "section_number";
//
//        public PlaceholderFragment() {
//        }
//
//        /**
//         * Returns a new instance of this fragment for the given section
//         * number.
//         */
//        public static PlaceholderFragment newInstance(int sectionNumber) {
//            PlaceholderFragment fragment = new PlaceholderFragment();
//            Bundle args = new Bundle();
//            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
//            fragment.setArguments(args);
//            return fragment;
//        }
//
//        @Override
//        public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                                 Bundle savedInstanceState) {
////            final View rootView = inflater.inflate(R.layout.fragment_search, container, false);
//            switch(getArguments().getInt(ARG_SECTION_NUMBER)){
//                case 1:
//                    final View rootView = inflater.inflate(R.layout.fragment_search, container, false);
//                    Button search=(Button) rootView.findViewById(R.id.search);
//                    search.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//
//                            final EditText mykeyword = (EditText) rootView.findViewById(R.id.keyword);
//                            String keyword = mykeyword.getText().toString();
//
//                            final Spinner mycategory = (Spinner) rootView.findViewById(R.id.category);
//                            String category = mycategory.getSelectedItem().toString();
//
//                            final EditText mydistance = (EditText) rootView.findViewById(R.id.distance);
//                            String distance = mydistance.getText().toString();
//
//                            RadioGroup radgroup = (RadioGroup) rootView.findViewById(R.id.myradiogroup);
//
////                    for (int i = 0; i < radgroup.getChildCount(); i++) {
////                        final RadioButton rd = (RadioButton) radgroup.getChildAt(i);
////                        String from=rd.getText().toString();
////                        if (rd.isChecked()) {
////                            from=rd.getText().toString();
////                        }
////                    }
////                    Log.v("aaaaaaaaaaaaaaaaaaaa",from);
//////                boolean test=from.toString().equals("Current Location");
//////                if (!from.equals("Current Location")){
////                    final EditText userdefloc = (EditText) findViewById(R.id.userdefloc);
////                    userDefLoc = userdefloc.getText().toString();
//////                }
////
//                            lat=currentLat;
//                            lng=currentLng;
//
//                            sRequestQueue = Volley.newRequestQueue(getActivity().getApplication() );
//                            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+lat+","+lng+"&radius="+distance+"&type="+category+"&keyword="+keyword+"&key=AIzaSyAi8ocz0-7lxSn873sq0LU5Gra84mckRBQ", null,
//                                    new Response.Listener<JSONObject>() {
//                                        @Override
//                                        public void onResponse(JSONObject response) {
//                                            //JSONArray recordMap= JSON.parseArray(para.getString("recordMap").toString());
//
//                                            try {
//                                                JSONArray myResult=response.getJSONArray("results");
//                                                Log.d("TAG", myResult.toString());
//                                            } catch (JSONException e) {
//                                                e.printStackTrace();
//                                            }
//
//                                            Log.v("lag", String.valueOf(currentLat));
//                                            Log.v("lng", String.valueOf(currentLng));
//                                            //往手机传数据
//                                            Intent i=new Intent(getActivity(),result.class);
//                                            i.putExtra("txt",response.toString());
//                                            startActivity(i);
//
//                                        }
//                                    }, new Response.ErrorListener() {
//                                @Override
//                                public void onErrorResponse(VolleyError error) {
//                                    Log.e("TAG", error.getMessage(), error);
//                                }
//                            });
//                            sRequestQueue.add(jsonObjectRequest);
//
//
//                        }
//                    });
//                    return rootView;
//                case 2:
//                    final View favorView = inflater.inflate(R.layout.fragment_favor, container, false);
//                    return favorView;
//            }
//
//
//
//            return null;
//
//        }
//
//    }

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
            // Show 3 total pages.
            return 2;
        }
    }

}
