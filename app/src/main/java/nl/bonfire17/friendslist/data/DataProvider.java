package nl.bonfire17.friendslist.data;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import nl.bonfire17.friendslist.models.Contact;
import nl.bonfire17.friendslist.models.User;

/*
    This is the data provider class, it is used to load data from the server and process it.
*/

public class DataProvider {

    private Context ctx;

    //Enter your API URL here
    private final static String API_URL = "http://example-api.com/";

    //These are the request types of this application
    public final static String LOGIN = "login";
    public final static String GET_USER = "getUser";
    public final static String EDIT_CONTACT = "editContact";
    public final static String ADD_CONTACT = "addContact";
    public final static String DELETE_CONTACT = "deleteContact";
    public final static String GET_USERS = "getUsers";
    public final static String ADD_USER = "addUser";
    public final static String EDIT_USER = "editUser";
    public final static String DELETE_USER = "deleteUser";

    //Declare the DataProvider, context is used for the network singleton
    public DataProvider(Context ctx) {
        this.ctx = ctx;
    }

    //This is the main request method
    public void request(final String action, Map<String, String> parameters, final ProviderResponse providerResponse){
        //Build URL from request type
        String url = API_URL + "?a=" + action;

        //Always add application/json to the parameters
        parameters.put("Content-Type", "application/json; charset=utf-8");

        //Turn HashMap into json object
        JSONObject JSONparameters = new JSONObject(parameters);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, JSONparameters, new Response.Listener<JSONObject>() {

            //The response of this method will depend on the request type
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if(action == LOGIN){
                        ProviderResponse.LoginResponse loginProviderResponse = (ProviderResponse.LoginResponse)providerResponse;
                        loginProviderResponse.response(response.getBoolean("success"), response.getInt("id"));
                    }else if(action == GET_USER){
                        ProviderResponse.UserResponse userProviderResponse = (ProviderResponse.UserResponse)providerResponse;
                        JSONObject jsonResponse = response.getJSONObject("data");
                        userProviderResponse.response(new User(jsonResponse.getInt("id"), jsonResponse.getString("email"),
                                (jsonResponse.getInt("admin") == 1), getContactsFromJSON(jsonResponse.getJSONArray("contacts"))));
                    }else if(action == ADD_CONTACT || action == EDIT_CONTACT || action == DELETE_CONTACT || action == ADD_USER || action == EDIT_USER || action == DELETE_USER){
                        ProviderResponse.SuccessResponse successProviderResponse = (ProviderResponse.SuccessResponse)providerResponse;
                        successProviderResponse.response(response.getBoolean("success"));
                    }else if(action == GET_USERS){
                        ProviderResponse.UsersResponse userProviderResponse = (ProviderResponse.UsersResponse)providerResponse;
                        JSONArray jsonResponse = response.getJSONArray("data");
                        userProviderResponse.response(getUsersFromJSON(jsonResponse));
                    }

                } catch (JSONException e) {
                    providerResponse.error();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                providerResponse.error();
            }
        });

        //Add request to NetworkSingleton
        NetworkSingleton.getInstance(ctx).addToRequestQueue(jsonObjectRequest);
    }

    //Turn a JSONArray with contact data into an ArrayList with Contact instances
    private ArrayList<Contact> getContactsFromJSON(JSONArray data) throws JSONException {
        ArrayList<Contact> contacts = new ArrayList<>();
        for(int j = 0; j < data.length(); j++){
            JSONObject contact = data.getJSONObject(j);
            //Add contacts of a user to a list
            contacts.add(new Contact(Integer.parseInt(contact.getString("id")), contact.getString("firstname"),
                    contact.getString("lastname"), contact.getString("email"), contact.getString("phonenumber")));
        }
        return contacts;
    }

    //Turn a JSONArray with user data into an ArrayList with User instances
    private ArrayList<User> getUsersFromJSON(JSONArray data) throws  JSONException{
        ArrayList<User> users = new ArrayList<>();
        for(int j = 0; j < data.length(); j++){
            JSONObject user = data.getJSONObject(j);
            users.add(new User(user.getInt("id"), user.getString("email"), user.getInt("admin") == 1, getContactsFromJSON(user.getJSONArray("contacts"))));
        }
        return users;
    }
}
