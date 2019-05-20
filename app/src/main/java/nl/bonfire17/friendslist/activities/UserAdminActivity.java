package nl.bonfire17.friendslist.activities;

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

import nl.bonfire17.friendslist.data.DataProvider;
import nl.bonfire17.friendslist.data.NetworkSingleton;
import nl.bonfire17.friendslist.adapters.UserAdapter;
import nl.bonfire17.friendslist.data.ProviderResponse;
import nl.bonfire17.friendslist.models.Contact;
import nl.bonfire17.friendslist.models.User;

public class UserAdminActivity extends AppCompatActivity {

    private Toolbar toolBar;
    private ActionBar actionBar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView listView;

    private DataProvider dataProvider;

    private User user;
    private ArrayList<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_admin);

        toolBar = (Toolbar)findViewById(R.id.toolbar);
        toolBar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolBar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back);

        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(new RefreshListener());

        listView = (ListView)findViewById(R.id.userView);

        user = (User)getIntent().getSerializableExtra("user");

        dataProvider = new DataProvider(this);
    }

    public void onResume(){
        super.onResume();
        init();
    }

    private void init(){
        listView.setOnItemClickListener(new ItemListener());
        loadUsers();
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
                newUser();
                return true;
            case R.id.action_refresh:
                loadUsers();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Loads users from server
    private void loadUsers(){
        swipeRefreshLayout.setRefreshing(true);
        Map<String, String> parameters = new HashMap<>();
        parameters.put("id", Integer.toString(user.getId()));
        dataProvider.request(DataProvider.GET_USERS, parameters, new UsersListener());
    }

    //Add a new user by opening the EditUserActivity
    private void newUser(){
        Intent intent = new Intent(UserAdminActivity.this, EditUserActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }

    //Edit a existing user by opening the EditUserActivity and send the user to be edited
    private void editUser(int id){
        Intent intent = new Intent(UserAdminActivity.this, EditUserActivity.class);
        intent.putExtra("user", user);
        for(int i = 0; i < users.size(); i++){
            if(users.get(i).getId() == id){
                intent.putExtra("editUser", users.get(i));
            }
        }
        startActivity(intent);
    }

    //Listens for a refresh
    class RefreshListener implements SwipeRefreshLayout.OnRefreshListener{

        @Override
        public void onRefresh() {
            loadUsers();
        }
    }

    //Listens for an selected item
    class ItemListener implements ListView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
            listView.setOnItemClickListener(null);
            editUser((int)id);
        }
    }

    class UsersListener implements ProviderResponse.UsersResponse{

        @Override
        public void error() {

        }

        @Override
        public void response(ArrayList<User> responseUsers) {
            users = responseUsers;
            listView.setAdapter(new UserAdapter(UserAdminActivity.this, users));
            swipeRefreshLayout.setRefreshing(false);
        }
    }
}
