package ir11.co.tsco.canavaslearning.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.hardware.display.DisplayManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.PowerManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import ir11.co.tsco.canavaslearning.App;

/**
 * Created by rasooli on 2015-07-23.
 */
public class Helper
{
    public static final int FLAG_TAG_BR = 1;
    public static final int FLAG_TAG_BOLD = 2;
    public static final int FLAG_TAG_ITALIC = 3;
    public static final int FLAG_TAG_STRIKE = 4;
    public static final int FLAG_TAG_UNDERLINE = 5;
    public static final int FLAG_TAG_COLOR = 10;
    public static final int FLAG_TAG_ALL = FLAG_TAG_BR | FLAG_TAG_BOLD | FLAG_TAG_COLOR|FLAG_TAG_ITALIC|FLAG_TAG_STRIKE|FLAG_TAG_UNDERLINE;

    public static float density = 1;
    static Resources res;

    static
    {
        res = App.getInstance().getResources();
        density = res.getDisplayMetrics().density;
        checkDisplaySize();
    }

    @SuppressWarnings ( "deprecation" )
    private static void WakeUp()
    {
        PowerManager pm = ( PowerManager ) App.getInstance().getApplicationContext().getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "Phone WakeUp");
        wakeLock.acquire(10000);
    }

    public static boolean hasInternet()
    {
        if ( !isNetworkAvailable() )
        {
            return false;
        }
        Runtime runtime = Runtime.getRuntime();
        try
        {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int exitValue = ipProcess.waitFor();
            if ( exitValue == 0 )
            {
                return true;
            }
        }
        catch ( Exception e )
        {
        }
        try
        {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 salam.ir");
            int exitValue = ipProcess.waitFor();
            if ( exitValue == 0 )
            {
                return true;
            }
        }
        catch ( Exception e )
        {
        }
        if ( checkInternet("http://www.salam.ir/") )
        {
            return true;
        }
        if ( checkInternet("http://www.google.com/") )
        {
            return true;
        }
        return checkInternet("http://www.bing.com/") || DeviceInfo.FeatureInfo.isEmulator();
    }

    private static boolean checkInternet(String url)
    {
        // Otherwise an exception may be thrown on invalid SSL certificates:
        url = url.replaceFirst("^https", "http");
        try
        {
            if ( Build.VERSION.SDK_INT > 9 )
            {
                android.os.StrictMode.ThreadPolicy policy = new android.os.StrictMode.ThreadPolicy.Builder().permitAll().build();
                android.os.StrictMode.setThreadPolicy(policy);
            }
            HttpURLConnection connection = ( HttpURLConnection ) new URL(url).openConnection();
            connection.setConnectTimeout(AppConstant.ConnectionPingTimeout);
            connection.setReadTimeout(AppConstant.ConnectionPingTimeout);
            connection.setRequestMethod("HEAD");
            // http://stackoverflow.com/questions/17638398/androids-httpurlconnection-throws-eofexception-on-head-requests
            connection.setRequestProperty("Accept-Encoding", "");
            //connection.setRequestProperty("Connection", "close");
            int responseCode = connection.getResponseCode();
            return (200 <= responseCode && responseCode <= 399);
        }
        catch ( Exception ex )
        {
            Helper.showErrorMessage("Helper -> hasInternet", ex);
            return false;
        }
    }

    public static void showErrorMessage(String location, Exception ex)
    {
        if ( ex == null )
        {
            android.util.Log.e(AppConstant.TAG, location + " , ex = null ");
        }
        else
        {
            android.util.Log.e(AppConstant.TAG, location + " : " + (ex.getMessage() != null ? ex.getMessage() : ex.getClass() != null ? ex.getClass().toString() : "empty class name exception"));


        }
    }
    public static boolean isNetworkAvailable()
    {
        try
        {
//            Log.d("bbx", "is NetworkAvailable ?");
            ConnectivityManager cm = ( ConnectivityManager ) App.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netActive = cm.getActiveNetworkInfo();
            if ( netActive != null && (netActive.isAvailable() || netActive.isConnectedOrConnecting()) )
            {
                return true;
            }
        }
        catch ( Exception e )
        {
            Helper.showErrorMessage("Helper -> isNetworkAvailable", e);
        }
        return false;
    }

    public static boolean isNetworkOnline()
    {
        try
        {
            ConnectivityManager cm = ( ConnectivityManager ) App.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if ( netInfo != null && (netInfo.isConnectedOrConnecting() || netInfo.isAvailable()) )
            {
                return true;
            }
            netInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if ( netInfo != null && netInfo.isConnectedOrConnecting() )
            {
                return true;
            }
            else
            {
                netInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                if ( netInfo != null && netInfo.isConnectedOrConnecting() )
                {
                    return true;
                }
            }
        }
        catch ( Exception e )
        {
            Helper.showErrorMessage("Helper -> isNetworkOnline", e);
            return true;
        }
        return false;
    }

    public static boolean isRoaming()
    {
        try
        {
            ConnectivityManager cm = ( ConnectivityManager ) App.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if ( netInfo != null )
            {
                return netInfo.isRoaming();
            }
        }
        catch ( Exception e )
        {
        }
        return false;
    }

    public static boolean isConnectedToWiFi()
    {
        try
        {
            ConnectivityManager cm = ( ConnectivityManager ) App.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if ( netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED )
            {
                return true;
            }
        }
        catch ( Exception e )
        {
        }
        return false;
    }

    public static AppConstant.NetType getCurrentNetwork()
    {
        if ( isNetworkOnline() )
        {
            if ( isConnectedToWiFi() )
            {
                return AppConstant.NetType.WIFI;
            }
            else if ( isRoaming() )
            {
                return AppConstant.NetType.ROAMING;
            }
            else
            {
                return AppConstant.NetType.MOBILE_DATA;
            }
        }
        return AppConstant.NetType.NOT_ACCESS;
    }




    public static String getTopTask()
    {
        ActivityManager am = ( ActivityManager ) App.getInstance().getSystemService(Activity.ACTIVITY_SERVICE);
        ////android.util.Log.d(AppConstant.TAG, am.getRunningTasks(1).get(0).topActivity.getShortClassName());
        return am.getRunningTasks(1).get(0).topActivity.getPackageName();
    }

    private static long lastNotifyShow = 0;


    public static String getAndroidId()
    {
        return Settings.Secure.getString(App.getInstance().getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static String sizeOf(Object object)
    {
        try
        {
            if ( object == null )
            {
                return "-1";
            }
            // Special output stream use to write the content
            // of an output stream to an internal byte array.
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            // Output stream that can write object
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            // Write object and close the output stream
            objectOutputStream.writeObject(object);
            objectOutputStream.flush();
            objectOutputStream.close();
            // Get the byte array
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            return byteArray == null ? "0" : String.valueOf(byteArray.length / 1024) + " K byte";
        }
        catch ( Exception e )
        {
            Helper.showErrorMessage("Helper -> sizeOf", e);
        }
        return "0";
    }

    public static boolean isScreenOn(Context context)
    {
        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH )
        {
            DisplayManager dm = ( DisplayManager ) context.getSystemService(Context.DISPLAY_SERVICE);
            boolean screenOn = false;
            for ( Display display : dm.getDisplays() )
            {
                if ( display.getState() != Display.STATE_OFF )
                {
                    screenOn = true;
                }
            }
            return screenOn;
        }
        else
        {
            PowerManager pm = ( PowerManager ) context.getSystemService(Context.POWER_SERVICE);
            //noinspection deprecation
            return pm.isScreenOn();
        }
    }


    public static void copyDB()
    {
        try
        {
            @SuppressLint ( "SdCardPath" ) File db = new File("/data/data/" + App.getInstance().getPackageName() + "/databases/sam.sqlite");
            File s = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/sam.sqlite");
            copy(db, s);
        }
        catch ( Exception ee )
        {
        }
    }

    private static void copy(File src, File dst) throws IOException
    {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);
        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ( (len = in.read(buf)) > 0 )
        {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }

    public static void Toast_It(String text, boolean lengthShort)//true = short, false = long
    {
        if ( lengthShort )
        {
            Toast.makeText(App.getInstance(), text, Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(App.getInstance(), text, Toast.LENGTH_LONG).show();
        }
    }

    public static float convertDpToPixel(float dp, Context context)
    {
        Resources resources = App.getInstance().getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return dp * (metrics.densityDpi / 160f);
    }

    @SafeVarargs
    @TargetApi ( Build.VERSION_CODES.HONEYCOMB ) // API 11
    public static < T > void executeAsyncTask(AsyncTask< T, ?, ? > asyncTask, T... params)
    {
        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB )
        {
            asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
        }
        else
        {
            asyncTask.execute(params);
        }
    }



    public static File getInternalCacheDir()
    {
        try
        {
            File file = App.getInstance().getCacheDir();
            if ( file != null )
            {
                return file;
            }
        }
        catch ( Exception e )
        {
            showErrorMessage("getInternalCacheDir", e);
        }
        return null;
    }

    public static File getExternalCacheDir()
    {
        if ( Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) )
        {
            try
            {
                File file = App.getInstance().getExternalCacheDir();
                if ( file != null )
                {
                    return file;
                }
            }
            catch ( Exception e )
            {
                showErrorMessage("getExternalCacheDir", e);
            }
        }
        return null;
    }

    public static long getCurrentTime()
    {
        return TimeUnit.SECONDS.convert(System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }

    public static boolean isDeviceSupportCamera()
    {
        if ( App.getInstance().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA) )
        {
            return true;
        }
        Toast.makeText(App.getInstance(), "no camera found", Toast.LENGTH_LONG).show();
        return false;
    }

    public static File generatePicFile()
    {
        try
        {
            File storageDir = getTakePicDir();
            @SuppressLint ( "SimpleDateFormat" ) String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            return new File(storageDir, "IMG_" + timeStamp + ".jpg");
        }
        catch ( Exception e )
        {
            showErrorMessage("Helper - > generatePicturePath", e);
        }
        return null;
    }

    private static File getTakePicDir()
    {
        File file = null;
        if ( Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) )
        {
            file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Gap");
            if ( file != null )
            {
                if ( !file.mkdirs() )
                {
                    if ( !file.exists() )
                    {
                        return null;
                    }
                }
            }
        }
        return file;
    }


    private static boolean isMyServiceRunning(Class< ? > serviceClass)
    {
        ActivityManager manager = ( ActivityManager ) App.getInstance().getSystemService(Context.ACTIVITY_SERVICE);
        for ( ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE) )
        {
            if ( serviceClass.getName().equals(service.service.getClassName()) )
            {
                return true;
            }
        }
        return false;
    }

    public static String milliSecondsToTimer(long milliseconds)
    {
        String finalTimerString = "";
        String secondsString = "";
        // Convert total duration into time
        int hours = ( int ) (milliseconds / (1000 * 60 * 60));
        int minutes = ( int ) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = ( int ) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);
        // Add hours if there
        if ( hours > 0 )
        {
            finalTimerString = hours + ":";
        }
        // Prepending 0 to seconds if it is one digit
        if ( seconds < 10 )
        {
            secondsString = "0" + seconds;
        }
        else
        {
            secondsString = "" + seconds;
        }
        finalTimerString = finalTimerString + minutes + ":" + secondsString;
        // return timer string
        return finalTimerString;
    }



    public static String getIMEI_MD5(Context context, String append)
    {
        String IMEI = "", imei = "", mac = "", android_id = "";
        try
        {
            imei = (( TelephonyManager ) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        }
        catch ( Exception ex )
        {
            ex.printStackTrace();
        }
        try
        {
            mac = getMACAddress("wlan0");
        }
        catch ( Exception ex )
        {
            ex.printStackTrace();
        }
        try
        {
            android_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        catch ( Exception ex )
        {
            ex.printStackTrace();
        }
        IMEI = md5(imei + mac + android_id + append);
        return IMEI;
    }

    public static String md5(String in)
    {
        MessageDigest digest;
        try
        {
            digest = MessageDigest.getInstance("MD5");
            digest.reset();
            digest.update(in.getBytes());
            byte[] a = digest.digest();
            int len = a.length;
            StringBuilder sb = new StringBuilder(len << 1);
            for ( byte anA : a )
            {
                sb.append(Character.forDigit((anA & 0xf0) >> 4, 16));
                sb.append(Character.forDigit(anA & 0x0f, 16));
            }
            return sb.toString();
        }
        catch ( NoSuchAlgorithmException e )
        {
            e.printStackTrace();
        }
        return null;
    }




    public static int getAppVersionCode()
    {
        int ver = 0;
        try
        {
            PackageInfo pInfo = App.getInstance().getPackageManager().getPackageInfo(App.getInstance().getPackageName(), 0);
            ver = pInfo.versionCode;
        }
        catch ( Exception e )
        {
        }
        return ver;
    }

    public static String getAppVersionName()
    {
        String ver = "";
        try
        {
            PackageInfo pInfo = App.getInstance().getPackageManager().getPackageInfo(App.getInstance().getPackageName(), 0);
            ver = pInfo.versionName;
        }
        catch ( PackageManager.NameNotFoundException e )
        {
        }
        return ver;
    }

    private static final char[] hexDigits = "0123456789abcdef".toCharArray();

    public static String md5(File file)
    {
        String md5 = "";
        try
        {
            FileInputStream fis = new FileInputStream(file);
            byte[] bytes = new byte[4096];
            int read = 0;
            MessageDigest digest = MessageDigest.getInstance("MD5");
            while ( (read = fis.read(bytes)) != -1 )
            {
                digest.update(bytes, 0, read);
            }
            byte[] messageDigest = digest.digest();
            StringBuilder sb = new StringBuilder(32);
            for ( byte b : messageDigest )
            {
                sb.append(hexDigits[(b >> 4) & 0x0f]);
                sb.append(hexDigits[b & 0x0f]);
            }
            md5 = sb.toString();
        }
        catch ( Exception e )
        {
            showErrorMessage("Helper -> md5", e);
        }
        return md5;
    }



    public static void showKeyboard(View view)
    {
        if ( view == null )
        {
            return;
        }
        InputMethodManager inputManager = ( InputMethodManager ) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    public static void hideKeyboard(View view)
    {
        if ( view == null )
        {
            return;
        }
        InputMethodManager imm = ( InputMethodManager ) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if ( !imm.isActive() )
        {
            return;
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static DisplayMetrics displayMetrics = new DisplayMetrics();

    public static float getPixelsInCM(float cm, boolean isX)
    {
        return (cm / 2.54f) * (isX ? displayMetrics.xdpi : displayMetrics.ydpi);
    }

    private static Boolean isTablet = false;//null;
    public static boolean usingHardwareInput;

    public static boolean isTablet()
    {
        if ( isTablet == null )
        {
            //isTablet = App.getInstance().getResources().getBoolean(R.bool.isTablet);
        }
        return isTablet;
    }

    public static Point displaySize = new Point();

    public static void checkDisplaySize()
    {
        try
        {
            Configuration configuration = App.getInstance().getResources().getConfiguration();
            usingHardwareInput = configuration.keyboard != Configuration.KEYBOARD_NOKEYS && configuration.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_NO;
            WindowManager manager = ( WindowManager ) App.getInstance().getSystemService(Context.WINDOW_SERVICE);
            if ( manager != null )
            {
                Display display = manager.getDefaultDisplay();
                if ( display != null )
                {
                    display.getMetrics(displayMetrics);
                    if ( Build.VERSION.SDK_INT < 13 )
                    {
                        displaySize.set(display.getWidth(), display.getHeight());
                    }
                    else
                    {
                        display.getSize(displaySize);
                    }
                    Log.e("tmessages", "display size = " + displaySize.x + " " + displaySize.y + " " + displayMetrics.xdpi + "x" + displayMetrics.ydpi);
                }
            }
        }
        catch ( Exception e )
        {
            //Log.e("tmessages", e.getMessage());
        }
    }



    public static String convertArabicCharacter(String text)
    {
        if ( text == null )
        {
            return null;
        }
        return text.replace("ي", "ی").replace("ك", "ک").replace("ة‎", "ه");
    }


    public static String formatFileSize(long size)
    {
        if (size == 0)
        {
            return "ـــ";
        }
        else if ( size < 1024 )
        {
            return String.format("%d B", size);
        }
        else if ( size < 1024 * 1024 )
        {
            return String.format("%.1f KB", size / 1024.0f);
        }
        else if ( size < 1024 * 1024 * 1024 )
        {
            return String.format("%.1f MB", size / 1024.0f / 1024.0f);
        }
        else
        {
            return String.format("%.1f GB", size / 1024.0f / 1024.0f / 1024.0f);
        }
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuilder sbuf = new StringBuilder();
        for(int idx=0; idx < bytes.length; idx++) {
            int intVal = bytes[idx] & 0xff;
            if (intVal < 0x10) sbuf.append("0");
            sbuf.append(Integer.toHexString(intVal).toUpperCase());
        }
        return sbuf.toString();
    }

    /**
     * Get utf8 byte array.
     * @param str
     * @return  array of NULL if error was found
     */
    public static byte[] getUTF8Bytes(String str) {
        try { return str.getBytes("UTF-8"); } catch (Exception ex) { return null; }
    }

    /**
     * Load UTF8withBOM or any ansi text file.
     * @param filename
     * @return
     * @throws java.io.IOException
     */
    public static String loadFileAsString(String filename) throws java.io.IOException {
        final int BUFLEN=1024;
        BufferedInputStream is = new BufferedInputStream(new FileInputStream(filename), BUFLEN);
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(BUFLEN);
            byte[] bytes = new byte[BUFLEN];
            boolean isUTF8=false;
            int read,count=0;
            while((read=is.read(bytes)) != -1) {
                if (count==0 && bytes[0]==(byte)0xEF && bytes[1]==(byte)0xBB && bytes[2]==(byte)0xBF ) {
                    isUTF8=true;
                    baos.write(bytes, 3, read-3); // drop UTF8 bom marker
                } else {
                    baos.write(bytes, 0, read);
                }
                count+=read;
            }
            return isUTF8 ? new String(baos.toByteArray(), "UTF-8") : new String(baos.toByteArray());
        } finally {
            try{ is.close(); } catch(Exception ex){}
        }
    }

    /**
     * Returns MAC address of the given interface name.
     * @param interfaceName eth0, wlan0 or NULL=use first interface
     * @return  mac address or empty string
     */
    public static String getMACAddress(String interfaceName) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                if (interfaceName != null) {
                    if (!intf.getName().equalsIgnoreCase(interfaceName)) continue;
                }
                byte[] mac = intf.getHardwareAddress();
                if (mac==null) return "";
                StringBuilder buf = new StringBuilder();
                for (int idx=0; idx<mac.length; idx++)
                    buf.append(String.format("%02X:", mac[idx]));
                if (buf.length()>0) buf.deleteCharAt(buf.length()-1);
                return buf.toString();
            }
        } catch (Exception ex) { } // for now eat exceptions
        return "";
        /*try {
            // this is so Linux hack
            return loadFileAsString("/sys/class/net/" +interfaceName + "/address").toUpperCase().trim();
        } catch (IOException ex) {
            return null;
        }*/
    }

    /**
     * Get IP address from first non-localhost interface
     * @param ipv4  true=return ipv4, false=return ipv6
     * @return  address or empty string
     */
    public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        boolean isIPv4 = sAddr.indexOf(':')<0;

                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                                return delim<0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) { } // for now eat exceptions
        return "";
    }

}
