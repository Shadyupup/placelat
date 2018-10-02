package com.example.acer_pc.placelat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class result extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    List<mainresult> mDataList;
    private List<mainresult> data2;
    private RecyclerView.Adapter mAdapter;
    private List<JSONObject>nextTokenArray;
    private Button next, previous;
    private int pageNum;
    private  String nextPage;
    private RequestQueue queue;
    private TextView noRes;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);

        //初始化页面
        initView();
        //初始化数据
        iniData();

    }

    //    初始化页面
    private void initView(){
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void iniData(){
        noRes = (TextView)findViewById(R.id.no);
        queue = Volley.newRequestQueue(this);
        next = (Button)findViewById(R.id.next);
        previous = (Button) findViewById(R.id.previous);
        next.setEnabled(false);
        previous.setEnabled(false);

        pageNum = 0;
        nextTokenArray = new ArrayList<>();
        mDataList = new ArrayList<mainresult>();

        Intent intent = getIntent();
        String message = intent.getStringExtra("resultdata");

        try {
            JSONObject jsonObject = new JSONObject(message);
            if (jsonObject.has("next_page_token")) {
                nextPage = jsonObject.getString("next_page_token");
                System.out.println("Shoudaole" + next);
                next.setEnabled(true);
            }
            nextTokenArray.add(jsonObject);
            JSONArray array = jsonObject.getJSONArray("results");
            if (array.length()!=0){
                noRes.setVisibility(View.GONE);
            }
            for (int i = 0; i < array.length(); i++) {
                JSONObject result = array.getJSONObject(i);
                JSONObject result2 = result.getJSONObject("geometry");
                JSONObject obj1 = result2.getJSONObject("location");
                mainresult resultitem = new mainresult(
                        result.getString("place_id"),
                        result.getString("name"),
                        result.getString("vicinity"),
                        result.getString("icon"),
                        obj1.getString("lat"),
                        obj1.getString("lng")
                );
                mDataList.add(resultitem);
            }
            mAdapter = new MyAdapter(this,mDataList);
            mRecyclerView.setAdapter(mAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //请求nextPage
                pageNum += 1;
                previous.setEnabled(true);
                if (pageNum == 2) {
                    next.setEnabled(false);
                }
                String urlNext= null;
                try {
                    urlNext = "http://csci571-wang.us-east-1.elasticbeanstalk.com/next?tokenid=" + nextTokenArray.get(pageNum - 1).getString("next_page_token") + "";
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.println(urlNext);
//                final ProgressDialog progressDialog = new ProgressDialog(getBaseContext());
//                progressDialog.setMessage("Fetching results");
//                progressDialog.show();
                StringRequest stringRequest = new StringRequest(Request.Method.GET, urlNext, new Response.Listener<String>() {

                    public void onResponse(String response) {
                        JSONObject jsonObject = null;
//                        progressDialog.dismiss();
                        try {

                            jsonObject = new JSONObject(response);
                            nextTokenArray.add(jsonObject);
                            getShow(jsonObject);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //System.out.println("拿到下一页" + response);



                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
                queue.add(stringRequest);
            }
        });
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pageNum -= 1;
                next.setEnabled(true);
                if (pageNum == 0) {
                    previous.setEnabled(false);
                }
                try {
                    getShow(nextTokenArray.get(pageNum));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void getShow(JSONObject jsonObject) throws JSONException {
        data2 = new ArrayList<>();
        //JSONObject jsonObject = new JSONObject(response);
        JSONArray array = jsonObject.getJSONArray("results");
        for (int i = 0; i < array.length(); i++) {

            JSONObject result = array.getJSONObject(i);
            JSONObject result2 = result.getJSONObject("geometry");
            JSONObject obj1 = result2.getJSONObject("location");

            mainresult current = new mainresult(
                    result.getString("icon"),
                    result.getString("name"),
                    result.getString("vicinity"),
                    result.getString("place_id"),
                    obj1.getString("lat"),
                    obj1.getString("lng")
            );
            data2.add(current);

        }
        mAdapter = new MyAdapter(this,data2);
        mRecyclerView.setAdapter(mAdapter);


    }
    public List<mainresult> getData() {
        return mDataList;
    }


}
