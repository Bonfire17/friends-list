package nl.bonfire17.friendslist.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.android.friendslist.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import nl.bonfire17.friendslist.data.DataProvider;
import nl.bonfire17.friendslist.data.ProviderResponse;
import nl.bonfire17.friendslist.models.User;

public class MainActivity extends AppCompatActivity {

    private Button loginBtn;
    private EditText emailEdit, passwordEdit;
    private TextView react;
    private ProgressBar progress;

    private DataProvider dataProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginBtn = (Button)findViewById(R.id.login);
        emailEdit = (EditText)findViewById(R.id.email);
        passwordEdit = (EditText)findViewById(R.id.password);
        react = (TextView)findViewById(R.id.react);
        progress = (ProgressBar)findViewById(R.id.loginProgress);

        loginBtn.setOnClickListener(new LoginButtonListener());

        dataProvider = new DataProvider(this);
    }

    class LoginButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            loginBtn.setEnabled(false);
            progress.setVisibility(View.VISIBLE);

            Map<String, String> parameters = new HashMap();
            parameters.put("email", emailEdit.getText().toString());
            parameters.put("password", passwordEdit.getText().toString());
            dataProvider.request(DataProvider.LOGIN, parameters, new LoginListener());
        }
    }

    class LoginListener implements ProviderResponse.LoginResponse {

        @Override
        public void response(boolean login, int id) {
            if(login){
                Intent intent = new Intent(MainActivity.this, MainMenuActivity.class);
                intent.putExtra("userID", id);
                startActivity(intent);

                progress.setVisibility(View.INVISIBLE);
                loginBtn.setEnabled(true);
            }else{
                react.setText(getResources().getString(R.string.error_password));
                progress.setVisibility(View.INVISIBLE);
                loginBtn.setEnabled(true);
            }
        }


        @Override
        public void error() {
            react.setText(getResources().getString(R.string.error_network));
            progress.setVisibility(View.INVISIBLE);
            loginBtn.setEnabled(true);
        }
    }
}
