package ir11.co.tsco.canavaslearning.framework;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

import ir11.co.tsco.canavaslearning.App;


/**
 * Created by Farhad on 4/9/2016.
 */
public class Utils {

    public static Point get_Device_Size_in_pixel()
    {
        WindowManager wm = (WindowManager) App.context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return  size;
    }
    public static int dpToPx(int dp)
    {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int pxToDp(int px)
    {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }
}
