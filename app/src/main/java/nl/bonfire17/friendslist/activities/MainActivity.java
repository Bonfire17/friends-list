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

import nl.bonfire17.friendslist.data.NetworkSingleton;

public class MainActivity extends AppCompatActivity {

    private Button loginBtn;
    private EditText emailEdit, passwordEdit;
    private TextView react;
    private ProgressBar progress;

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

    }

    class LoginButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            loginBtn.setEnabled(false);
            progress.setVisibility(View.VISIBLE);

            String url = NetworkSingleton.getApiUrl() + "a=login";

            //Add values to parameters
            String email = emailEdit.getText().toString();
            String password = passwordEdit.getText().toString();
            Map<String, String> params = new HashMap();
            params.put("Content-Type", "application/json; charset=utf-8");
            params.put("email", email);
            params.put("password", password);

            JSONObject parameters = new JSONObject(params);

            //Response of the jsonObjectRequest
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    try {
                        System.out.println(response.toString());
                        if(response.getBoolean("success")){
                            //Start new activity
                            Intent intent = new Intent(MainActivity.this, MainMenuActivity.class);
                            Log.i("LOGIN", response.toString());
                            intent.putExtra("userID", response.getInt("id") );
                            intent.putExtra("admin", response.getInt("admin") );
                            startActivity(intent);

                            progress.setVisibility(View.INVISIBLE);
                            loginBtn.setEnabled(true);
                        }else{
                            react.setText(getResources().getString(R.string.error_password));
                            progress.setVisibility(View.INVISIBLE);
                            loginBtn.setEnabled(true);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    react.setText(getResources().getString(R.string.error_network));
                    progress.setVisibility(View.INVISIBLE);
                    loginBtn.setEnabled(true);
                }
            });

            //Add request to NetworkSingleton
            NetworkSingleton.getInstance(MainActivity.this).addToRequestQueue(jsonObjectRequest);
        }
    }
}
