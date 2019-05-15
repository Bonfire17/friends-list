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

import nl.bonfire17.friendslist.AddButton;
import nl.bonfire17.friendslist.adapters.ContactAdapter;
import nl.bonfire17.friendslist.data.DataProvider;
import nl.bonfire17.friendslist.data.NetworkSingleton;
import nl.bonfire17.friendslist.data.ProviderResponse;
import nl.bonfire17.friendslist.models.Contact;
import nl.bonfire17.friendslist.models.User;

public class MainMenuActivity extends AppCompatActivity {

    private ListView listView;
    private Toolbar toolBar;
    private ActionBar actionBar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private AddButton addButton;
    private DataProvider dataProvider;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        toolBar = (Toolbar) findViewById(R.id.toolbar);
        listView = (ListView)findViewById(R.id.contactsView);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swiperefresh);
        navigationView = (NavigationView)findViewById(R.id.nav_view);
        addButton = (AddButton)findViewById(R.id.addButton);

        listView.setOnItemClickListener(new ItemListener());

        toolBar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolBar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_options);
        actionBar.setTitle(R.string.app_name_space);

        dataProvider = new DataProvider(this);


        swipeRefreshLayout.setOnRefreshListener(new RefreshListener());
        init();
    }

    public void onResume(){
        super.onResume();
        init();
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
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.action_add:
                //Start new activity
               newContact();
                return true;
            case R.id.action_refresh:
                loadContacts();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    /*
        Private custom methods
    */
    //Init button and item listeners, load fresh data
    private void init(){
        navigationView.setNavigationItemSelectedListener(new NavigationViewListener());
        listView.setOnItemClickListener(new ItemListener());
        addButton.setOnClickListener(new AddButtonListener());
        loadContacts();
    }

    //Load contacts into listview, method is also used to refresh list
    private void loadContacts(){
        swipeRefreshLayout.setRefreshing(true);
        Map<String, String> parameters = new HashMap<>();
        parameters.put("id", Integer.toString(getIntent().getIntExtra("userID", -1)));
        parameters.put("userId", Integer.toString(getIntent().getIntExtra("userID", -1)));
        dataProvider.request(DataProvider.GET_USER, parameters, new UserListener());
    }

    //Start new contact activity
    private void newContact(){
        Intent intent = new Intent(MainMenuActivity.this, EditContactActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }

    //Start edit contact activity
    private void editContact(long id){
        Intent intent = new Intent(MainMenuActivity.this, EditContactActivity.class);
        intent.putExtra("user", user);
        intent.putExtra("contactID", (int)id);
        startActivity(intent);
    }

    //NavigationViewListener listens for a onclick on a navigation item
    class NavigationViewListener implements NavigationView.OnNavigationItemSelectedListener{

        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            if(item.getItemId() == R.id.nav_logout){
                navigationView.setNavigationItemSelectedListener(null);
                finish();
            }else if(item.getItemId() == R.id.nav_admin && user.getIsAdmin()){
                navigationView.setNavigationItemSelectedListener(null);
                Intent intent = new Intent(MainMenuActivity.this, UserAdminActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }

            return true;
        }
    }

    //ResfreshListener listens for a swipe up event
    class RefreshListener implements SwipeRefreshLayout.OnRefreshListener{

        @Override
        public void onRefresh() {
            loadContacts();

        }
    }

    //ItemListeners listen for an onclick listview item
    class ItemListener implements ListView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
            listView.setOnItemClickListener(null);
            editContact(id);
        }
    }

    //AddButton listens for a onclick of a custom view
    class AddButtonListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            addButton.setOnClickListener(null);
            newContact();
        }
    }

    class UserListener implements ProviderResponse.UserResponse{

        @Override
        public void response(User responseUser) {
            user = responseUser;
            if(user.getIsAdmin()){
                Menu nav_menu = navigationView.getMenu();
                nav_menu.findItem(R.id.nav_admin).setVisible(true);
            }
            listView.setAdapter(new ContactAdapter(MainMenuActivity.this, user.getContacts()));
            swipeRefreshLayout.setRefreshing(false);
        }

        @Override
        public void error(){
            swipeRefreshLayout.setRefreshing(false);
        }
    }
}
