package nl.bonfire17.friendslist;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.android.friendslist.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserAdminActivity extends AppCompatActivity {

    private Toolbar tb;
    private ActionBar ab;
    private SwipeRefreshLayout srl;
    private ListView lv;

    private int userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_admin);

        tb = (Toolbar)findViewById(R.id.toolbar);
        tb.setTitleTextColor(Color.WHITE);
        setSupportActionBar(tb);
        ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeAsUpIndicator(R.drawable.ic_back);

        srl = (SwipeRefreshLayout)findViewById(R.id.swiperefresh);
        srl.setOnRefreshListener(new RefreshListener());

        lv = (ListView)findViewById(R.id.userView);

        userID = getIntent().getIntExtra("userID", -1);
        if(userID == -1){
            finish();
        }else{
            init();
        }
    }

    public void onResume(){
        super.onResume();
        init();
    }

    private void init(){
        lv.setOnItemClickListener(new ItemListener());
        loadData();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar, menu);
        return true;
    }

    //Check if a actionbar button is clicked
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_add:
                Intent intent = new Intent(UserAdminActivity.this, EditUserActivity.class);
                intent.putExtra("userID", userID);
                intent.putExtra("newUser", true);
                startActivity(intent);
                return true;
            case R.id.action_refresh:
                loadData();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Loads users from server
    private void loadData(){
        String url = NetworkSingleton.getApiUrl() + "a=getUsers";

        //Add values to parameters
        Map<String, String> params = new HashMap();
        params.put("Content-Type", "application/json; charset=utf-8");
        params.put("id", Integer.toString(userID));
        JSONObject parameters = new JSONObject(params);

        srl.setRefreshing(true);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                //Load JSONArray from JSONObject
                JSONArray ja = null;
                ArrayList<User> users = new ArrayList<User>();
                Log.d("TAG", response.toString());
                try {
                    if(response.getBoolean("success")){
                        ja = response.getJSONArray("data");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                /*
                    Add a user to a user list
                    Add contacts to user us from the list
                */
                //Loop through users from JSONArray
                for(int i = 0; i < ja.length(); i++){
                    try {
                        JSONObject jo = ja.getJSONObject(i);
                        ArrayList<Contact> contacts = new ArrayList<Contact>();

                        //Loop through contacts of a user
                        for(int j = 0; j < jo.getJSONArray("contacts").length(); j++){
                            JSONObject con = jo.getJSONArray("contacts").getJSONObject(j);
                            //Add contacts of a user to a list
                            contacts.add(new Contact(Integer.parseInt(con.getString("id")), con.getString("firstname"),
                                    con.getString("lastname"), con.getString("email"), con.getString("phonenumber")));
                        }

                        //Add user to a list with a contact list
                        users.add(new User(Integer.parseInt(jo.getString("id")), jo.getString("email"), (jo.getInt("admin") == 1 ? true : false), contacts));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                lv.setAdapter(new UserAdapter(UserAdminActivity.this, users));
                srl.setRefreshing(false);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        NetworkSingleton.getInstance(UserAdminActivity.this).addToRequestQueue(jsonObjectRequest);
    }

    //Listens for a refresh
    class RefreshListener implements SwipeRefreshLayout.OnRefreshListener{

        @Override
        public void onRefresh() {
            loadData();
        }
    }

    //Listens for an selected item
    class ItemListener implements ListView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
            lv.setOnItemClickListener(null);
            Intent intent = new Intent(UserAdminActivity.this, EditUserActivity.class);
            intent.putExtra("userID", userID);
            intent.putExtra("newUser", false);
            intent.putExtra("editUserID", id);
            startActivity(intent);
        }
    }
}
