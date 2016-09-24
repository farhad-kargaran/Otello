package ir11.co.tsco.canavaslearning.framework;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.SoundPool;

import java.io.IOException;
import java.io.InputStream;

import ir11.co.tsco.canavaslearning.MainActivity;


/**
 * Created by Farhad on 4/4/2016.
 */
public class Assets {

    private static SoundPool soundPool;
    public static Bitmap wb1,wb2,wb3,wb4,wb5,wb6,wb7,wb8,wb9,wb10,wb11,bw1,bw2,bw3,bw4,bw5,bw6,bw7,bw8,bw9,bw10,bw11,board;
    public static int moveID;

    public static void load() {
        //moveID = loadSound("drop.ogg");
        board = loadBitmap("boardd.png", false);
        bw1 = loadBitmap("bw1.png", true);
        bw2 = loadBitmap("bw2.png", true);
        bw3 = loadBitmap("bw3.png", true);
        bw4 = loadBitmap("bw4.png", true);
        bw5 = loadBitmap("bw5.png", true);
        bw6 = loadBitmap("bw6.png", true);
        bw7 = loadBitmap("bw7.png", true);
        bw8 = loadBitmap("bw8.png", true);
        bw9 = loadBitmap("bw9.png", true);
        bw10= loadBitmap("bw10.png", true);
        bw11= loadBitmap("bw11.png", true);

        wb1 = loadBitmap("wb1.png", true);
        wb2 = loadBitmap("wb2.png", true);
        wb3 = loadBitmap("wb3.png", true);
        wb4 = loadBitmap("wb4.png", true);
        wb5 = loadBitmap("wb5.png", true);
        wb6 = loadBitmap("wb6.png", true);
        wb7 = loadBitmap("wb7.png", true);
        wb8 = loadBitmap("wb8.png", true);
        wb9 = loadBitmap("wb9.png", true);
        wb10= loadBitmap("wb10.png", true);
        wb11= loadBitmap("wb11.png", true);
    }

    private static Bitmap loadBitmap(String filename, boolean transparency) {

        InputStream inputStream = null;
        try {
            inputStream = MainActivity.assets.open(filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        if (transparency) {
            options.inPreferredConfig = Bitmap.Config.ARGB_4444;
        } else {
            options.inPreferredConfig = Bitmap.Config.RGB_565;
        }
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null,
                new BitmapFactory.Options());
        return bitmap;
    }

    private static int loadSound(String filename) {
        int soundID = 0;
        if (soundPool == null) {
            soundPool = new SoundPool(25, AudioManager.STREAM_MUSIC, 0);
        }
        try {
            soundID = soundPool.load(MainActivity.assets.openFd(filename),1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return soundID;
    }

    public static void playSound(int soundID) {
        soundPool.play(soundID, 1, 1, 1, 0, 1);
    }
}
