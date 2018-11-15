package com.hiteshsample;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    ListView listview;
    Adapter adapter;
    ProgressDialog prg;
    LinearLayout itemLL;
    boolean b = true;
    ArrayList<Data> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listview = (ListView)findViewById(R.id.myLV);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        prg = new ProgressDialog(this);
        prg.setMessage("Loading...");
        prg.setCancelable(false);
        setSupportActionBar(toolbar);
        itemLL = (LinearLayout)findViewById(R.id.itemLL);
        itemLL.setVisibility(View.GONE);
        findViewById(R.id.item1LL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ContactUsActivity.class));
            }
        });

        findViewById(R.id.item2LL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ContactUsActivity.class));
            }
        });

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemLL.getVisibility() == View.VISIBLE) {
                    itemLL.setVisibility(View.GONE);
                } else {
                    itemLL.setVisibility(View.VISIBLE);
                }
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        if(Utils.hasConnection(getApplicationContext())){
            invokeWS();
        }
        else{
            Utils.commonAlert(this,"Internet connection not found...");
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.sort) {
            if(dataList != null && dataList.size()>0 && adapter!=null) {
                Collections.sort(dataList, new Comparator<Data>() {
                    @Override
                    public int compare(Data lhs, Data rhs) {
                        return (lhs.name.toString().compareTo(rhs.name.toString()));
                    }
                });
                adapter.notifyDataSetInvalidated();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.explore) {
            Toast.makeText(getApplicationContext(),"Explore the World",Toast.LENGTH_SHORT).show();

        } else if (id == R.id.favourite) {
            Toast.makeText(getApplicationContext(),"Your favourite list",Toast.LENGTH_SHORT).show();

        } else if (id == R.id.cart) {
            Toast.makeText(getApplicationContext(),"Lets go to your cart",Toast.LENGTH_SHORT).show();

        } else if (id == R.id.setting) {
            Toast.makeText(getApplicationContext(),"Let's go to setting",Toast.LENGTH_SHORT).show();

        } else if (id == R.id.logout) {
            Toast.makeText(getApplicationContext(),"Account has been Logout",Toast.LENGTH_SHORT).show();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void invokeWS(){
        prg.show();
        JSONObject jsonObject = new JSONObject();
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Api-Key","8ymu2sjxvgrmzjmnmqnmya85");
        String restApiUrl = "https://api.gettyimages.com/v3/search/images/creative";
        org.apache.http.entity.StringEntity entity = null;
        try {
            /*try {
                //jsonObject.put("userid","hiteshnayak4@hotmail.com");
            } catch (JSONException e) {
                e.printStackTrace();
            }*/
            entity = new org.apache.http.entity.StringEntity(jsonObject.toString());
            entity.setContentType("application/json");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        client.get(getApplicationContext(),
                restApiUrl,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode,
                                          org.apache.http.Header[] headers,
                                          byte[] responseBody) {
                        // Hide Progress Dialog
                        try {
                            prg.hide();
                            String response = new String(responseBody, "UTF-8");
                            // JSON Object
                            JSONObject obj = new JSONObject(response);
                            JSONArray imageArray = obj.getJSONArray("images");
                            dataList = new ArrayList<Data>();
                            Data data;

                            for(int i = 0;i<15;i++){
                                data = new Data();
                                JSONObject imageObj = imageArray.getJSONObject(i);
                                JSONArray urlArray = imageObj.getJSONArray("display_sizes");
                                for(int j =0;j<urlArray.length();j++){
                                    JSONObject urlObj = urlArray.getJSONObject(j);
                                    String url = urlObj.getString("uri");
                                    data.setImageUrl(url);
                                    break;
                                }
                                String title = imageObj.getString("title");
                                data.setName(title);
                                dataList.add(data);
                            }
                            adapter = new Adapter(getApplicationContext(),R.layout.adapter_list,dataList);
                            listview.setAdapter(adapter);

                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }

                    // When the response returned by REST has Http response code other than '200'
                    @Override
                    public void onFailure(int statusCode,
                                          org.apache.http.Header[] headers,
                                          byte[] responseBody,
                                          java.lang.Throwable error) {
                        prg.hide();
                    }
                });
    }
}