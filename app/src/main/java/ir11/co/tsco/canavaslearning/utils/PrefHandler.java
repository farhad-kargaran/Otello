package ir11.co.tsco.canavaslearning.utils;

import android.content.Context;
import android.content.SharedPreferences;

import ir11.co.tsco.canavaslearning.App;

public class PrefHandler
{
    public static final String PROPERTY_VERSIONCODE = " PROPERTY_VERSIONCODE";

    private static final String PREF_NAME = "cfg";
    public static final String PROPERTY_THEME_CHANGED           = "PROPERTY_THEME_CHANGED";

    public static final String PROPERTY_LAST_QUEUE_ID           = "PROPERTY_QUEUE_ID";
    public static final String PROPERTY_SERVICE_IMPORTED             = "SERVICE_IMPORTED";

    private static PrefHandler singletonInstance;
    private static SharedPreferences sharedPref = null;

    private PrefHandler()
    {
        sharedPref = App.getInstance().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

    }

    public static synchronized void initializeInstance()
    {
        if (singletonInstance == null)
        {
            singletonInstance = new PrefHandler();
        }
    }

    public static synchronized PrefHandler getInstance()
    {
        if (singletonInstance == null)
        {
            throw new IllegalStateException(PrefHandler.class.getSimpleName() + " is not initialized, call initializeInstance(..) method first.");
        }
        return singletonInstance;
    }

    public void setPreference(String key, Object value)
    {
        SharedPreferences.Editor editor = sharedPref.edit();

        if (value instanceof Integer)
            editor.putInt(key, (Integer) value);
        else if (value instanceof String)
            editor.putString(key, (String) value);
        else if (value instanceof Boolean)
            editor.putBoolean(key, (Boolean) value);
        else if (value instanceof Long)
            editor.putLong(key, (Long) value);

        editor.apply();
    }

    public int getInt(String key, int defaultValue)
    {
        return sharedPref.getInt(key, defaultValue);
    }

    public String getString(String key, String defaultValue)
    {
        return sharedPref.getString(key, defaultValue);
    }

    public boolean getBoolean(String key, boolean defaultValue)
    {
        return sharedPref.getBoolean(key, defaultValue);
    }

    public long getLong(String key, long defaultValue)
    {
        return sharedPref.getLong(key, defaultValue);
    }

    public static void clear()
    {
        sharedPref.edit().clear().apply();



    }

}
