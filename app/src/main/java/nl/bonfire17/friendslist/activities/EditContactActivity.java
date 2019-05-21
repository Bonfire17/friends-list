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

import com.example.android.friendslist.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import nl.bonfire17.friendslist.compounds.EditContactCompound;
import nl.bonfire17.friendslist.data.DataProvider;
import nl.bonfire17.friendslist.data.ProviderResponse;
import nl.bonfire17.friendslist.models.Contact;
import nl.bonfire17.friendslist.models.User;

public class EditContactActivity extends AppCompatActivity {

    private Toolbar toolBar;
    private ActionBar actionBar;
    private EditContactCompound editCompound;
    private Button deleteButton;

    private DataProvider dataProvider;
    private JSONObject contactJSON;

    private User user;
    private Contact contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);

        editCompound = (EditContactCompound)findViewById(R.id.editUserPanel);
        deleteButton = (Button)findViewById(R.id.editButton);
        toolBar = (Toolbar) findViewById(R.id.toolbar);
        toolBar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolBar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back);

        deleteButton.setOnClickListener(new DeleteListener());

        user = (User)getIntent().getSerializableExtra("user");

        dataProvider = new DataProvider(this);

        //Check if an existing contact was selected or a new one
        int contactID = getIntent().getIntExtra("contactID", -1);
        if(contactID >= 0){
            contact = user.getContact(contactID);
            loadContact();
            actionBar.setTitle(R.string.edit);
        }else{
            actionBar.setTitle(R.string.add);
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
                sendContact();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Send coumpond input data to the server
    private void sendContact(){
        Map<String, String> params = new HashMap();
        params.put("Content-Type", "application/json; charset=utf-8");
        params.put("id", Integer.toString(user.getId()));
        params.put("firstname", editCompound.getFirstname());
        params.put("lastname", editCompound.getLastname());
        params.put("email", editCompound.getEmail());
        params.put("phonenumber", editCompound.getPhonenumber());

        String request = DataProvider.ADD_CONTACT;
        if(contact != null){
            params.put("contactId", Integer.toString(contact.getId()));
            request = DataProvider.EDIT_CONTACT;
        }

        dataProvider.request(request, params, new SuccessListener());
    }

    //Load contact data from the server
    private void loadContact(){
        editCompound.setContact(contact.getFirstname(), contact.getLastname(), contact.getEmail(), contact.getPhone());
    }

    //Delete contact
    private void deleteContact(){
        Map<String, String> params = new HashMap();
        params.put("Content-Type", "application/json; charset=utf-8");
        params.put("id", String.valueOf(user.getId()));
        params.put("contactId", String.valueOf(contact.getId()));
        dataProvider.request(DataProvider.DELETE_CONTACT, params, new SuccessListener());
    }

    //Listens for an delete button click
    class DeleteListener implements Button.OnClickListener{

        @Override
        public void onClick(View view) {
            if(contact != null){
                deleteContact();
            }
        }
    }

    class SuccessListener implements ProviderResponse.SuccessResponse{

        @Override
        public void error() {

        }

        @Override
        public void response(boolean success) {
            if(success){
                finish();
            }
        }
    }
}
