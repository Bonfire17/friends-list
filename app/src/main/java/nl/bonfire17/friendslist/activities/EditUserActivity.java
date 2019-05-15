package nl.bonfire17.friendslist.activities;

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

import nl.bonfire17.friendslist.compounds.EditUserCompound;
import nl.bonfire17.friendslist.data.DataProvider;
import nl.bonfire17.friendslist.data.NetworkSingleton;
import nl.bonfire17.friendslist.data.ProviderResponse;
import nl.bonfire17.friendslist.models.Contact;
import nl.bonfire17.friendslist.models.User;

public class EditUserActivity extends AppCompatActivity {

    private Toolbar tb;
    private ActionBar ab;

    private EditUserCompound edit;
    private Button deleteButton;

    private User user;
    private User editUser;

    private DataProvider dataProvider;

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

        dataProvider = new DataProvider(this);

        user = (User)getIntent().getSerializableExtra("user");
        editUser = (User)getIntent().getSerializableExtra("editUser");

        if(editUser != null){
            edit.setPasswordVisibility(View.GONE);
            loadUser();
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
                sendUser();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void sendUser(){
        Map<String, String> params = new HashMap();
        params.put("Content-Type", "application/json; charset=utf-8");
        params.put("id", Integer.toString(user.getId()));
        params.put("email", edit.getEmail());
        params.put("password", edit.getPassword());
        params.put("admin", Boolean.toString(edit.getAdmin()));
        String request = DataProvider.ADD_USER;
        if(editUser != null){
            request = DataProvider.EDIT_USER;
            params.put("userId", Integer.toString(editUser.getId()));
        }
        params.put("changePassword", Boolean.toString(edit.getChangePassword()));
        dataProvider.request(request, params, new SuccessListener() );
    }

    private void loadUser(){
        edit.setEmail(editUser.getEmail());
        edit.setAdmin(editUser.getIsAdmin());
    }

    private void deleteUser(){
        Map<String, String> params = new HashMap();
        params.put("Content-Type", "application/json; charset=utf-8");
        params.put("id", String.valueOf(user.getId()));
        params.put("userId", String.valueOf(editUser.getId()));
        dataProvider.request(DataProvider.DELETE_USER, params, new SuccessListener());
    }

    //Listens if the delete button is clicked
    class DeleteListener implements Button.OnClickListener{

        @Override
        public void onClick(View view) {
            if(editUser != null){
                deleteButton.setEnabled(false);
                deleteUser();
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
