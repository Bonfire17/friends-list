package nl.bonfire17.friendslist.data;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class NetworkSingleton {
    private static NetworkSingleton instance;
    private RequestQueue rq;
    private static Context ctx;

    private NetworkSingleton(Context ctx){
        this.ctx = ctx;
        rq = getRequestQueue();
    }

    public static synchronized NetworkSingleton getInstance(Context context) {
        if (instance == null) {
            instance = new NetworkSingleton(context);
        }
        return instance;
    }

    private RequestQueue getRequestQueue() {
        if (rq == null) {
            rq = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return rq;

    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
}
