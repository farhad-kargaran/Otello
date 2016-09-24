package ir11.co.tsco.canavaslearning;

import android.content.Context;
import android.widget.Toast;

import ir11.co.tsco.canavaslearning.utils.PrefHandler;
import ir11.co.tsco.canavaslearning.utils.PrefHandlerSec;

/**
 * Created by Farhad on 4/9/2016.
 */
public class App extends android.app.Application {

    private static App mInstance;
    public static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        context = this;

        PrefHandlerSec.initializeInstance();
        PrefHandler.initializeInstance();

    }
    public static void Show(String txt)
    {
        Toast.makeText(context,txt,Toast.LENGTH_SHORT).show();
    }

    public static synchronized App getInstance() {
        return App.mInstance;

    }
}
