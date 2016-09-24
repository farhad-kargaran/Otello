package ir11.co.tsco.canavaslearning.utils;

import android.content.SharedPreferences;

import ir11.co.tsco.canavaslearning.App;

public class PrefHandlerSec
{

    private static final String PREF_NAME = "sec_cfg";

    public static final String PROPERTY_TOKEN             = "PROPERTY_TOKEN";
    public static final String PROPERTY_QUEUE             = "PROPERTY_QUEUE";
    //public static final String PROPERTY_USER_IDENTIFIER = "PROPERTY_USER_IDENTIFIER";
    public static final String PROPERTY_PRIVATE_KEY       = "PRIVATE_KEY";
    public static final String PROPERTY_PUBLIC_KEY        = "PUBLIC_KEY";
    public static final String PROPERTY_DEVICE_IDENTIFIER = "DEVICE_IDENTIFIER";



    //public static final String PROPERTY_LAST_MAIN_ACTIVITY_ENABLED = "PROPERTY_LAST_MAIN_ACTIVITY_ENABLED";

    private static PrefHandlerSec singletonInstance;
    //private static SecurePreferences sharedPref = null;

    private static SharedPreferences sharedPref = null;

    private PrefHandlerSec()
    {

        sharedPref = new ObscuredPreferencesBuilder().setApplication(App.getInstance()).obfuscateValue(true).obfuscateKey(true).setSharePrefFileName(PREF_NAME).setSecret("!*g^&a(#p)").createSharedPrefs();

    }

    public static synchronized void initializeInstance()
    {
        if (singletonInstance == null)
        {
            singletonInstance = new PrefHandlerSec();
        }
    }

    public static synchronized PrefHandlerSec getInstance()
    {
        if (singletonInstance == null)
        {
            throw new IllegalStateException(PrefHandlerSec.class.getSimpleName() + " is not initialized, call initializeInstance(..) method first.");
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
