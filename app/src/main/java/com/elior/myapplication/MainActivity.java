package com.elior.myapplication;

import android.app.SearchManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static String HI;
    private RecyclerView rv;
    private List<Model> list_data;
    private MyAdapter adapter;
    private android.support.v7.widget.SearchView searchView;
    private static Model ld;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();
        myRecyclerView();
        getData();
    }

    private void initUI() {
        rv = findViewById(R.id.recycler_view);

        list_data = new ArrayList<>();

        HI = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=31.4,34.1&radius=50000&sensor=true&rankby=prominence&types=&keyword=&key=" + getString(R.string.my_key);
    }

    private void myRecyclerView() {
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyAdapter(list_data, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (android.support.v7.widget.SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                adapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Nested Arrays
    private void getData() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, HI, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject mainObj = new JSONObject(response);
                    JSONArray list = mainObj.getJSONArray("results");
                    for (int i = 0; i < list.length(); i++) {
                        ld = new Model();
                        JSONObject elem = list.getJSONObject(i);
                        String name = elem.getString("name");
                        String id = elem.getString("id");
                        String vicinity = elem.getString("vicinity");
                        JSONObject elem2 = elem.getJSONObject("geometry");
                        JSONObject elem3 = elem2.getJSONObject("location");
                        double lat = elem3.getDouble("lat");
                        double lng = elem3.getDouble("lng");
                        ld.setName(name);
                        ld.setId(id);
                        ld.setVicinity(vicinity);
                        ld.setLat(lat);
                        ld.setLng(lng);
                        JSONArray prods = elem.getJSONArray("photos");
                        for (int j = 0; j < prods.length(); j++) {
                            JSONObject innerElem = prods.getJSONObject(j);
                            String photo = innerElem.getString("photo_reference");
                            ld.setPhoto_reference(photo);
                        }
                        JSONArray prods2 = elem.getJSONArray("types");
                        String[] arr = new String[prods2.length()];
                        for (int j = 0; j < prods2.length(); j++) {
                            arr[j] = prods2.getString(j);
                            ld.setTypes(arr);
                        }
                        list_data.add(ld);
                    }
                    rv.setAdapter(adapter);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    // String[]
//    private void getData() {
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, HI, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                try {
//                    JSONObject mainObj = new JSONObject(response);
//                    JSONArray list = mainObj.getJSONArray("results");
//                    for (int i = 0; i < list.length(); i++) {
//                        ld = new Model();
//                        JSONObject elem = list.getJSONObject(i);
//                        JSONArray prods = elem.getJSONArray("types");
//                        String[] arr = new String[prods.length()];
//                        for (int j = 0; j < prods.length(); j++) {
//                            arr[j] = prods.getString(j);
//                            ld.setTypes(arr);
//                        }
//                        list_data.add(ld);
//                    }
//                    rv.setAdapter(adapter);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//            }
//        });
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        requestQueue.add(stringRequest);
//    }

    // Nested Objects
//    private void getData() {
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, HI, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                try {
//                    JSONObject jsonObject = new JSONObject(response);
//                    JSONArray array = jsonObject.getJSONArray("results");
//                    for (int i = 0; i < array.length(); i++) {
//                        ld = new Model();
//                        JSONObject ob1 = array.getJSONObject(i);
//                        JSONObject ob2 = ob1.getJSONObject("geometry");
//                        JSONObject ob3 = ob2.getJSONObject("location");
//                        ld.setLat(ob3.getDouble("lat"));
//                        ld.setLng(ob3.getDouble("lng"));
//                        list_data.add(ld);
//                    }
//                    rv.setAdapter(adapter);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        });
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        requestQueue.add(stringRequest);
//    }

//    private static void getData() {
//        // Creating volley request obj
//        JsonArrayRequest movieReq = new JsonArrayRequest("https://api.androidhive.info/json/movies.json",
//                new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        // Parsing json
//                        for (int i = 0; i < response.length(); i++) {
//                            try {
//                                JSONObject obj = response.getJSONObject(i);
//                                ld.setTitle(obj.getString("title"));
//                                ld.setImage(obj.getString("image"));
//                                ld.setRating(((Number) obj.get("rating")).doubleValue());
//                                ld.setReleaseYear(obj.getInt("releaseYear"));
//                                // Genre is json array
//                                JSONArray genreArray = obj.getJSONArray("genre");
//                                ArrayList<String> genre = new ArrayList<String>();
//                                for (int j = 0; j < genreArray.length(); j++) {
//                                    genre.add(String.valueOf(genreArray.get(j)));
//                                    movie.setGenre(genre);
//                                }
//
//                                // adding movie to movies array
//                                mMovieListInternet.add(ld);
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//
//                        }
//
//                        // notifying list adapter about data changes
//                        // so that it renders the list view with updated data
//                        mMovieDBHelperInternet.addMoviesList(mMovieListInternet);
//                        mAdapterInternet.notifyDataSetChanged();
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        });
//        // Adding request to request queue
//        NearByApplication.addToRequestQueue(movieReq);
//    }

}

