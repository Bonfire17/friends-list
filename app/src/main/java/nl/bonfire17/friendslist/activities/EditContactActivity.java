package nl.bonfire17.friendslist.activities;

import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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

import java.util.HashMap;
import java.util.Map;

import nl.bonfire17.friendslist.compounds.EditContactCompound;
import nl.bonfire17.friendslist.data.NetworkSingleton;
import nl.bonfire17.friendslist.models.Contact;

public class EditContactActivity extends AppCompatActivity {

    private Toolbar tb;
    private ActionBar ab;
    private EditContactCompound edit;
    private Button deleteButton;

    private JSONObject contactJSON;
    private Contact contact;

    private int userID;
    private int contactID = 0;
    private boolean newContact = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);

        edit = (EditContactCompound)findViewById(R.id.editUserPanel);

        deleteButton = (Button)findViewById(R.id.editButton);

        tb = (Toolbar) findViewById(R.id.toolbar);

        tb.setTitleTextColor(Color.WHITE);
        setSupportActionBar(tb);
        ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeAsUpIndicator(R.drawable.ic_back);

        deleteButton.setOnClickListener(new DeleteListener());

        userID = getIntent().getIntExtra("userID", -1);

        //Check if an existing contact was selected or a new one
        if(!getIntent().getBooleanExtra("newContact", true)){
            newContact = false;
            contactID = (int)getIntent().getLongExtra("contactID", -1);
            loadData();
            ab.setTitle(R.string.edit);
        }else{
            ab.setTitle(R.string.add);
            deleteButton.setVisibility(View.GONE);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_add_edit, menu);
        return true;
    }

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

    //Send coumpond input data to the server
    public void sendData(){
        String url;
        if(newContact){
            url = NetworkSingleton.getApiUrl() + "a=addContact";
        }else{
            url = NetworkSingleton.getApiUrl() + "a=editContact";
        }

        //Add values to parameters
        String firstname = edit.getFirstname();
        String lastname = edit.getLastname();
        String email = edit.getEmail();
        String phone = edit.getPhonenumber();
        Map<String, String> params = new HashMap();
        params.put("Content-Type", "application/json; charset=utf-8");
        params.put("id", String.valueOf(userID));
        params.put("firstname", firstname);
        params.put("lastname", lastname);
        params.put("email", email);
        params.put("phonenumber", phone);
        if(!newContact){
            params.put("contactId", String.valueOf(contactID));
        }

        JSONObject parameters = new JSONObject(params);

        //Response of the jsonObjectRequest
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
        NetworkSingleton.getInstance(EditContactActivity.this).addToRequestQueue(jsonObjectRequest);
    }

    //Load contact data from the server
    public void loadData(){
        String url = NetworkSingleton.getApiUrl() + "a=getContact";

        //Add values to parameters
        Map<String, String> params = new HashMap();
        params.put("Content-Type", "application/json; charset=utf-8");
        params.put("id", String.valueOf(userID));
        params.put("contactId", String.valueOf(contactID));

        JSONObject parameters = new JSONObject(params);

        //Response of the jsonObjectRequest
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
            try {
                contactJSON = response.getJSONObject("data");
                contact = new Contact(Integer.parseInt(contactJSON.getString("id")), contactJSON.getString("firstname"),
                        contactJSON.getString("lastname"), contactJSON.getString("email"), contactJSON.getString("phonenumber"));

                //load data into compound
                edit.setFirstname(contact.getFirstname());
                edit.setLastname(contact.getLastname());
                edit.setEmail(contact.getEmail());
                edit.setPhonenumber(contact.getPhone());
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
        NetworkSingleton.getInstance(EditContactActivity.this).addToRequestQueue(jsonObjectRequest);
    }

    //Delete contact
    public void deleteContact(){
        String url = NetworkSingleton.getApiUrl() + "a=deleteContact";

        //Add values to parameters
        Map<String, String> params = new HashMap();
        params.put("Content-Type", "application/json; charset=utf-8");
        params.put("id", String.valueOf(userID));
        params.put("contactId", String.valueOf(contactID));

        JSONObject parameters = new JSONObject(params);

        //Response of the jsonObjectRequest
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
        NetworkSingleton.getInstance(EditContactActivity.this).addToRequestQueue(jsonObjectRequest);
    }

    //Listens for an delete button click
    class DeleteListener implements Button.OnClickListener{

        @Override
        public void onClick(View view) {
            if(!newContact){
                deleteContact();
            }
        }
    }
}
