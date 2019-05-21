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

import java.util.HashMap;
import java.util.Map;

import nl.bonfire17.friendslist.compounds.EditUserCompound;
import nl.bonfire17.friendslist.data.DataProvider;
import nl.bonfire17.friendslist.data.ProviderResponse;
import nl.bonfire17.friendslist.models.User;

public class EditUserActivity extends AppCompatActivity {

    private Toolbar toolBar;
    private ActionBar actionBar;

    private EditUserCompound editCompound;
    private Button deleteButton;

    private DataProvider dataProvider;

    private User user;
    private User editUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);
        editCompound = (EditUserCompound)findViewById(R.id.editUserPanel);
        deleteButton = (Button)findViewById(R.id.editButton);
        toolBar = (Toolbar) findViewById(R.id.toolbar);
        toolBar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolBar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
        deleteButton.setOnClickListener(new DeleteListener());

        dataProvider = new DataProvider(this);

        user = (User)getIntent().getSerializableExtra("user");
        editUser = (User)getIntent().getSerializableExtra("editUser");

        //Check if we are editing or adding a user
        if(editUser != null){
            editCompound.setPasswordVisibility(View.GONE);
            loadUser();
            actionBar.setTitle(R.string.edit);
        }else{
            actionBar.setTitle(R.string.add);
            deleteButton.setVisibility(View.GONE);
            editCompound.setChangePasswordVisibility(View.GONE);
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

    //Send user data to the server
    private void sendUser(){
        Map<String, String> params = new HashMap();
        params.put("Content-Type", "application/json; charset=utf-8");
        params.put("id", Integer.toString(user.getId()));
        params.put("email", editCompound.getEmail());
        params.put("password", editCompound.getPassword());
        params.put("admin", Boolean.toString(editCompound.getAdmin()));
        String request = DataProvider.ADD_USER;
        if(editUser != null){
            request = DataProvider.EDIT_USER;
            params.put("userId", Integer.toString(editUser.getId()));
        }
        params.put("changePassword", Boolean.toString(editCompound.getChangePassword()));
        dataProvider.request(request, params, new SuccessListener() );
    }

    //Load user data to the compound
    private void loadUser(){
        editCompound.setEmail(editUser.getEmail());
        editCompound.setAdmin(editUser.getIsAdmin());
    }

    //Delete user and recieve a response
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
