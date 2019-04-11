package nl.bonfire17.friendslist;

import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.android.friendslist.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EditUserActivity extends AppCompatActivity {

    private Toolbar tb;
    private ActionBar ab;

    private EditUserCompound edit;
    private Button deleteButton;

    private int userID;
    private int editUserID;
    private boolean newUser = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        edit = (EditUserCompound)findViewById(R.id.editUserPanel);
        deleteButton = (Button)findViewById(R.id.editButton);
        tb = (Toolbar) findViewById(R.id.toolbar);

        tb.setTitleTextColor(Color.WHITE);
        setSupportActionBar(tb);
        ab = getSupportActionBar();

        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeAsUpIndicator(R.drawable.ic_back);

        deleteButton.setOnClickListener(new DeleteListener());

        userID = getIntent().getIntExtra("userID", -1);

        //Check if this from wil add a new user or modify an old one
        if(!getIntent().getBooleanExtra("newUser", true)){
            newUser = false;
            editUserID = (int)getIntent().getLongExtra("editUserID", -1);
            edit.setPasswordVisibility(View.GONE);
            loadData();
            ab.setTitle(R.string.edit);
        }else{
            ab.setTitle(R.string.add);
            deleteButton.setVisibility(View.GONE);
            edit.setChangePasswordVisibility(View.GONE);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_add_edit, menu);
        return true;
    }

    //Check if a actionbar button is clicked
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_check:
                sendData();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Send from data to the server
    public void sendData(){
        String url;
        if(newUser){
            url = NetworkSingleton.getApiUrl() + "a=addUser";
        }else{
            url = NetworkSingleton.getApiUrl() + "a=editUser";
        }

        //Add values to parameters
        String email = edit.getEmail();
        String password = edit.getPassword();
        boolean admin = edit.getAdmin();
        Map<String, String> params = new HashMap();
        params.put("Content-Type", "application/json; charset=utf-8");
        params.put("id", String.valueOf(userID));
        params.put("email", email);
        params.put("password", password);
        params.put("admin", Boolean.toString(admin));
        if(!newUser){
            params.put("userId", String.valueOf(editUserID));
        }
        Log.i("TAG", Boolean.toString(edit.getChangePassword()));
        if(edit.getChangePassword()){
            params.put("changePassword", Boolean.toString(true));
        }else{
            params.put("changePassword", Boolean.toString(false));
        }

        JSONObject parameters = new JSONObject(params);

        //Response of the jsonObjectRequest
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.i("TAG", response.toString());
                    if(response.getBoolean("success")){
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        //Add request to NetworkSingleton
        NetworkSingleton.getInstance(EditUserActivity.this).addToRequestQueue(jsonObjectRequest);
    }

    //Load form data from the server
    public void loadData(){
        String url = NetworkSingleton.getApiUrl() + "a=getUser";

        //Add values to parameters
        Map<String, String> params = new HashMap();
        params.put("Content-Type", "application/json; charset=utf-8");
        params.put("id", String.valueOf(userID));
        params.put("userId", String.valueOf(editUserID));

        JSONObject parameters = new JSONObject(params);

        //Response of the JsonObjectRequest
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject userJSON = response.getJSONObject("data");
                    ArrayList<Contact> contacts = new ArrayList<Contact>();
                    for(int j = 0; j < userJSON.getJSONArray("contacts").length(); j++){
                        JSONObject con = userJSON.getJSONArray("contacts").getJSONObject(j);
                        contacts.add(new Contact(Integer.parseInt(con.getString("id")), con.getString("firstname"), con.getString("lastname"),
                                con.getString("email"), con.getString("phonenumber")));
                    }
                    User user = new User(Integer.parseInt(userJSON.getString("id")), userJSON.getString("email"), (userJSON.getInt("admin") == 1 ? true : false), contacts);

                    edit.setEmail(user.getEmail());
                    edit.setAdmin(user.getIsAdmin());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        //Add request to NetworkSingleton
        NetworkSingleton.getInstance(EditUserActivity.this).addToRequestQueue(jsonObjectRequest);
    }

    //Deletes a user
    public void deleteContact(){
        String url = NetworkSingleton.getApiUrl() + "a=deleteUser";

        //Add values to parameters
        Map<String, String> params = new HashMap();
        params.put("Content-Type", "application/json; charset=utf-8");
        params.put("id", String.valueOf(userID));
        params.put("userId", String.valueOf(editUserID));

        JSONObject parameters = new JSONObject(params);

        //Response of JsonObjectRequest
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    if(response.getBoolean("success")){
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        //Add request to NetworkSingleton
        NetworkSingleton.getInstance(EditUserActivity.this).addToRequestQueue(jsonObjectRequest);
    }

    //Listens if the delete button is clicked
    class DeleteListener implements Button.OnClickListener{

        @Override
        public void onClick(View view) {
            if(!newUser){
                deleteButton.setEnabled(false);
                deleteContact();
            }
        }
    }
}
