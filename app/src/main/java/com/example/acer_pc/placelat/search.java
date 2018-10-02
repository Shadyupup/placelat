package com.example.acer_pc.placelat;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

import org.json.JSONException;
import org.json.JSONObject;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBufferResponse;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

public class search extends Fragment {
    String from = "";//选了哪个按钮
    String userDefLoc = "";//用户定义地址
    String keyword, category,location,whichButton;
    RequestQueue sRequestQueue;
    double distance;
    private RadioButton buttonHere, buttonOther;
    double currentLat;
    double currentLng;
    String lat;
    String lng;
    int checkedbutton;
    public static final String TAG = "AutoCompleteActivity";
    private static final int AUTO_COMP_REQ_CODE = 2;
    private TextView validation1, validation2;
    protected GeoDataClient geoDataClient;
    private GeoDataClient mGoogleApiClient;
    private placeAutoCompleteAdapter mplaceAutoCompleteAdapter;

    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(-40, -168), new LatLng(71, 136));

    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Context context=getContext();
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        final EditText mykeyword = (EditText) rootView.findViewById(R.id.keyword);

        final Spinner mycategory = (Spinner) rootView.findViewById(R.id.category);

        final EditText mydistance = (EditText) rootView.findViewById(R.id.distance);
//        String distance = mydistance.getText().toString();

        final AutoCompleteTextView userdefloc = (AutoCompleteTextView)rootView.findViewById(R.id.userdefloc);
        mGoogleApiClient = Places.getGeoDataClient(getActivity(), null);
        mplaceAutoCompleteAdapter = new placeAutoCompleteAdapter(getActivity(), mGoogleApiClient, LAT_LNG_BOUNDS, null);
        userdefloc.setAdapter(mplaceAutoCompleteAdapter);

        userdefloc.setEnabled(false);
        final RadioGroup radgroup = (RadioGroup) rootView.findViewById(R.id.myradiogroup);
        final RadioButton radioButton = (RadioButton)rootView.findViewById(radgroup.getCheckedRadioButtonId());
        radgroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId) {
                    case R.id.current:
                        checkedbutton=checkedId;
                        userdefloc.setEnabled(false);
                        userdefloc.setText("");
                        break;
                    case R.id.other:
                        checkedbutton=checkedId;
                        userdefloc.setEnabled(true);
                        break;

                    default:
                        break;
                }
            }
        });

        validation1 = (TextView) rootView.findViewById(R.id.validation1);
        validation2 = (TextView) rootView.findViewById(R.id.validation2);
        validation1.setVisibility(View.INVISIBLE);
        validation2.setVisibility(View.INVISIBLE);

        buttonHere = (RadioButton) rootView.findViewById(R.id.current);
        sRequestQueue =  Volley.newRequestQueue(getActivity().getBaseContext());

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, "http://ip-api.com/json", (JSONObject)null, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject response) {
                try {
                    lat = response.getString("lat");
                    lng = response.getString("lon");
                }catch(JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        sRequestQueue.add(getRequest);
        Button search=(Button) rootView.findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyword = mykeyword.getText().toString();
                category = mycategory.getSelectedItem().toString();

                if (mydistance.getText().toString().equals("")) {
                    distance = 16093 ;
                }
                else {
                    distance= Integer.parseInt(mydistance.getText().toString())*1609.3;
                }
                userDefLoc = userdefloc.getText().toString();
                if (userDefLoc!=""){
//                    if (userDefLoc.equals("") || userDefLoc.trim().equals("")) {
//                        validation2.setVisibility(View.VISIBLE);
//                        Toast.makeText(getActivity(), "Please fix all fields with errors", Toast.LENGTH_SHORT).show();
//
//                    }
                    System.out.println("asasasasasasasasasa");
                }
                else {
                    System.out.println("eergergergregerge");
                }

                if (keyword.equals("") || keyword.trim().equals("")) {
                    validation1.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity(), "Please fix all fields with errors", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (radgroup.getCheckedRadioButtonId() == R.id.other) {

                    if (userDefLoc.equals("") || userDefLoc.trim().equals("")) {
                        validation2.setVisibility(View.VISIBLE);
                        Toast.makeText(getActivity(), "Please fix all fields with errors", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                //String searchUrl ="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=34.0223, -118.285117&radius=16090&type=cafe&keyword=usc&key=AIzaSyCUEZ1nnYe-kq4goeI0G9_iaKJwDShq6sc";
//                String searchUrl = "http://mywebsiteforwork.us-west-1.elasticbeanstalk.com/process_post?keyword="+keyword+"&category="+category+"&distance="+distance+"&from1="+userDefLoc+"&mylocation_lat="+lat+","+"&mylocation_lon="+lng;
                String searchUrl = "http://csci571-wang.us-east-1.elasticbeanstalk.com/login?latitude=" + lat + "&longtitude=" + lng + "&keyword=" + keyword + "&category=" + category + "&distance=" + distance + "&autocomplete=" + userDefLoc + "";
                if (!keyword.equals("")&&(!keyword.trim().equals("")) && radgroup.getCheckedRadioButtonId() == R.id.current
                        || !keyword.equals("") &&(!keyword.trim().equals("")) && radgroup.getCheckedRadioButtonId() == R.id.other && !userDefLoc.equals("")) {
                    final ProgressDialog progressDialog = new ProgressDialog(getContext());//1.创建一个ProgressDialog的实例
                    progressDialog.setMessage("Fetching results");//3.设置显示内容
                    progressDialog.setCancelable(true);//4.设置可否用back键关闭对话框
                    progressDialog.show();//5.将ProgessDialog显示出来
                    JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, searchUrl, null, new Response.Listener<JSONObject>() {
                        public void onResponse(JSONObject response) {
//                        try {
//                            JSONArray myResult = response.getJSONArray("results");
//                            Log.d("TAG", myResult.toString());
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                        Log.v("lag", String.valueOf(currentLat));
//                        Log.v("lng", String.valueOf(currentLng));
                            //往手机传数据
                            Intent i = new Intent(getActivity(), result.class);
                            i.putExtra("resultdata", response.toString());
                            startActivity(i);
                            progressDialog.dismiss();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                        }
                    });
                    sRequestQueue.add(jsonRequest);

                }
            }

        });
        Button clearBut = rootView.findViewById(R.id.clear);
        clearBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mykeyword.getText().clear();
                mydistance.getText().clear();
                validation1.setVisibility(View.INVISIBLE);
                validation2.setVisibility(View.INVISIBLE);
                buttonHere.setChecked(true);
                mycategory.setSelection(0);
            }
        });
        return rootView;

    }

//    private AdapterView.OnItemClickListener mAutocompleteClickListener
//            = new AdapterView.OnItemClickListener() {
//        @Override
//        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            final PlaceArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapter.getItem(position);
//            final String placeId = String.valueOf(item.placeId);
//            Log.i(LOG_TAG, "Selected: " + item.description);
//            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
//                    .getPlaceById(mGoogleApiClient, placeId);
//            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
//            Log.i(LOG_TAG, "Fetching details for ID: " + item.placeId);
//        }
//    };
}
