package ir11.co.tsco.canavaslearning.utils;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import ir11.co.tsco.canavaslearning.App;

/**
 * Created by Babax on 9/23/2015.
 */
public class VolleySingleton
{

    private static VolleySingleton sInstance = null;

    private RequestQueue mRequestQueue;


    public static synchronized VolleySingleton getInstance()
    {
        if ( sInstance == null )
        {
            sInstance = new VolleySingleton();
        }

        return sInstance;
    }


    public RequestQueue getRequestQueue()
    {
        if ( mRequestQueue == null )
        {
            mRequestQueue = Volley.newRequestQueue(App.getInstance());
        }
        return mRequestQueue;
    }

}
