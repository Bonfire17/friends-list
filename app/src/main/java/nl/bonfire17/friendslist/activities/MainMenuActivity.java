package nl.bonfire17.friendslist.activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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

import nl.bonfire17.friendslist.AddButton;
import nl.bonfire17.friendslist.adapters.ContactAdapter;
import nl.bonfire17.friendslist.data.NetworkSingleton;
import nl.bonfire17.friendslist.models.Contact;

public class MainMenuActivity extends AppCompatActivity {

    private ListView lv;
    private Toolbar tb;
    private ActionBar ab;
    private DrawerLayout dl;
    private NavigationView nv;
    private SwipeRefreshLayout srl;
    private AddButton addButton;

    private int userID;
    private boolean isAdmin = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        tb = (Toolbar) findViewById(R.id.toolbar);
        lv = (ListView)findViewById(R.id.contactsView);
        dl = (DrawerLayout)findViewById(R.id.drawer_layout);
        srl = (SwipeRefreshLayout)findViewById(R.id.swiperefresh);
        nv = (NavigationView)findViewById(R.id.nav_view);
        addButton = (AddButton)findViewById(R.id.addButton);

        lv.setOnItemClickListener(new ItemListener());

        tb.setTitleTextColor(Color.WHITE);
        setSupportActionBar(tb);
        ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeAsUpIndicator(R.drawable.ic_options);
        ab.setTitle(R.string.app_name_space);

        srl.setOnRefreshListener(new RefreshListener());

        userID = getIntent().getIntExtra("userID", -1);

        //Check if the user is a admin
        if(getIntent().getIntExtra("admin", 0) == 1){
            isAdmin = true;
            Menu nav_menu = nv.getMenu();
            nav_menu.findItem(R.id.nav_admin).setVisible(true);
        }
        init();
    }

    public void onResume(){
        super.onResume();
        init();
    }

    //Init button and item listeners, load fresh data
    private void init(){
        nv.setNavigationItemSelectedListener(new NavigationViewListener());
        lv.setOnItemClickListener(new ItemListener());
        addButton.setOnClickListener(new AddButtonListener());
            if(userID == -1){
            finish();
        }else{
            loadData();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar, menu);
        return true;
    }

    //If a actionbar button is selected
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //Open side drawer
                dl.openDrawer(GravityCompat.START);
                return true;
            case R.id.action_add:
                //Start new activity
                Intent intent = new Intent(MainMenuActivity.this, EditContactActivity.class);
                intent.putExtra("userID", userID);
                intent.putExtra("newContact", true);
                startActivity(intent);
                return true;
            case R.id.action_refresh:
                loadData();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Load contacts into listview
    public void loadData(){

        String url = NetworkSingleton.getApiUrl() + "a=getContacts";

        //Add values to parameters
        Map<String, String> params = new HashMap();
        params.put("Content-Type", "application/json; charset=utf-8");
        params.put("id", Integer.toString(userID));
        JSONObject parameters = new JSONObject(params);

        //Start refreshing animation
        srl.setRefreshing(true);

        //Response of the jsonObjectRequest
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                JSONArray ja = null;
                ArrayList<Contact> contacts = new ArrayList<Contact>();
                try {
                    ja = response.getJSONArray("data");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //Loop through contact list in JSONArray
                for(int i = 0; i < ja.length(); i++){
                    //Add contact to list
                    try {
                        JSONObject jo = ja.getJSONObject(i);
                        contacts.add(new Contact(Integer.parseInt(jo.getString("id")), jo.getString("firstname"), jo.getString("lastname"), jo.getString("email"), jo.getString("phonenumber")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                lv.setAdapter(new ContactAdapter(MainMenuActivity.this, contacts));
                srl.setRefreshing(false);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        //Add request to NetworkSingleton
        NetworkSingleton.getInstance(MainMenuActivity.this).addToRequestQueue(jsonObjectRequest);
    }

    //NavigationViewListener listens for a onclick on a navigation item
    class NavigationViewListener implements NavigationView.OnNavigationItemSelectedListener{

        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            if(item.getItemId() == R.id.nav_logout){
                nv.setNavigationItemSelectedListener(null);
                finish();
            }else if(item.getItemId() == R.id.nav_admin && isAdmin){
                nv.setNavigationItemSelectedListener(null);
                Intent intent = new Intent(MainMenuActivity.this, UserAdminActivity.class);
                intent.putExtra("userID", userID);
                startActivity(intent);
            }

            return true;
        }
    }

    //ResfreshListener listens for a swipe up event
    class RefreshListener implements SwipeRefreshLayout.OnRefreshListener{

        @Override
        public void onRefresh() {
            loadData();

        }
    }

    //ItemListeners listen for an onclick listview item
    class ItemListener implements ListView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
            lv.setOnItemClickListener(null);
            Intent intent = new Intent(MainMenuActivity.this, EditContactActivity.class);
            intent.putExtra("userID", userID);
            intent.putExtra("newContact", false);
            intent.putExtra("contactID", id);
            startActivity(intent);
        }
    }

    //AddButton listens for a onclick of a custom view
    class AddButtonListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            addButton.setOnClickListener(null);
            Intent intent = new Intent(MainMenuActivity.this, EditContactActivity.class);
            intent.putExtra("userID", userID);
            intent.putExtra("newContact", true);
            startActivity(intent);
        }
    }
}
