package nl.bonfire17.friendslist.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.friendslist.R;

import java.util.HashMap;
import java.util.Map;

import nl.bonfire17.friendslist.data.DataProvider;
import nl.bonfire17.friendslist.data.ProviderResponse;

public class MainActivity extends AppCompatActivity {

    private Button loginButton;
    private EditText emailEdit, passwordEdit;
    private TextView react;
    private ProgressBar progress;

    private DataProvider dataProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginButton = (Button)findViewById(R.id.login);
        emailEdit = (EditText)findViewById(R.id.email);
        passwordEdit = (EditText)findViewById(R.id.password);
        react = (TextView)findViewById(R.id.react);
        progress = (ProgressBar)findViewById(R.id.loginProgress);

        loginButton.setOnClickListener(new LoginButtonListener());

        dataProvider = new DataProvider(this);
    }

    class LoginButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            loginButton.setEnabled(false);
            progress.setVisibility(View.VISIBLE);

            //Send a hashmap with request parameters to the DataProvider request method
            //Also send a response Listener that waits for a response
            Map<String, String> parameters = new HashMap();
            parameters.put("email", emailEdit.getText().toString());
            parameters.put("password", passwordEdit.getText().toString());
            dataProvider.request(DataProvider.LOGIN, parameters, new LoginListener());
        }
    }

    class LoginListener implements ProviderResponse.LoginResponse {

        @Override
        public void response(boolean login, int id) {
            //Check if credentials are correct
            if(login){
                //Start next activity
                Intent intent = new Intent(MainActivity.this, MainMenuActivity.class);
                intent.putExtra("userID", id);
                startActivity(intent);

                progress.setVisibility(View.INVISIBLE);
                loginButton.setEnabled(true);
            }else{
                //Display error msg
                react.setText(getResources().getString(R.string.error_password));
                progress.setVisibility(View.INVISIBLE);
                loginButton.setEnabled(true);
            }
        }


        @Override
        public void error() {
            react.setText(getResources().getString(R.string.error_network));
            progress.setVisibility(View.INVISIBLE);
            loginButton.setEnabled(true);
        }
    }
}
