package ir11.co.tsco.canavaslearning;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.graphics.Point;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import io.socket.client.Socket;
import ir11.co.tsco.canavaslearning.framework.Assets;
import ir11.co.tsco.canavaslearning.framework.Utils;
import ir11.co.tsco.canavaslearning.gameClasses.Bead;

public class MainActivity extends AppCompatActivity {

    //WebServiceInfo


    public static byte newgame;

    private LinearLayout lnGame;
    public static MySurfaceView sGame;

    public static int GAME_WIDTH = 1000;
    public static int GAME_HEIGHT = 1000;
    private int leftRightMarginForSurgaceview = 20;

    public static AssetManager assets;
    public static Activity activity;
    public static byte cpuColor = 2;

    public static boolean playWithCPU = true;
    public static boolean gameLocked = false;
    public static boolean boardHasWinner = false;

    public static boolean displayAvailableCells = true;
    public static Bead EE = new Bead((byte)0,(byte)0);//EMPTY
    public static Bead BB = new Bead((byte)1,(byte)0);//BLACK
    public static Bead WW = new Bead((byte)2,(byte)0);//WHITE
    private ImageView iv;
    private CheckBox chk;
    private TextView tv,tvTimer,tvblack,tvwhite;
    private CountDownTimer cdTimer;
    public  static byte blackScore=0,whiteScore=0;

    public static boolean blackTurn = true;
    public static boolean loadpre = false;
    public static  Bead[][] pre = new Bead[][]{
            {WW, WW, WW, WW, WW, WW, WW, BB},
            {WW, WW, WW, WW, WW, WW, BB, BB},
            {WW, WW, BB, BB, BB, WW, BB, BB},
            {WW, BB, WW, BB, BB, WW, BB, BB},
            {WW, WW, BB, BB, WW, WW, BB, BB},
            {WW, WW, WW, WW, BB, WW, BB, BB},
            {BB, BB, EE, WW, WW, WW, BB, BB},
            {BB, BB, BB, BB, BB, BB, BB, WW},
    };
    public static  Bead[][] board = new Bead[][]{
            {EE, EE, EE, EE, EE, EE, EE, EE},
            {EE, EE, EE, EE, EE, EE, EE, EE},
            {EE, EE, EE, EE, EE, EE, EE, EE},
            {EE, EE, EE, WW, BB, EE, EE, EE},
            {EE, EE, EE, BB, WW, EE, EE, EE},
            {EE, EE, EE, EE, EE, EE, EE, EE},
            {EE, EE, EE, EE, EE, EE, EE, EE},
            {EE, EE, EE, EE, EE, EE, EE, EE},
    };
    public static  byte[][] ev = new byte[][]{
            {8, -2, 3, 3, 3, 3, -2, 8},
            {-2, 1, 2, 2, 2, 2, 1, -2},
            {3,  2, 1, 1, 1, 1, 2,  3},
            {3, 2, 1, 1, 1, 1, 2, 3},
            {3, 2, 1, 1, 1, 1, 2, 3},
            {3, 2, 1, 1, 1, 1, 2, 3},
            {-2, 1, 2, 2, 2, 2, 1, -2},
            {8, -2, 3, 3, 3, 3, -2, 8},
    };

    public static boolean blackTurnBackup = true;
    public static  Bead[][] prev = new Bead[][]{
            {EE, EE, EE, EE, EE, EE, EE, EE},
            {EE, EE, EE, EE, EE, EE, EE, EE},
            {EE, EE, EE, EE, EE, EE, EE, EE},
            {EE, EE, EE, WW, BB, EE, EE, EE},
            {EE, EE, EE, BB, WW, EE, EE, EE},
            {EE, EE, EE, EE, EE, EE, EE, EE},
            {EE, EE, EE, EE, EE, EE, EE, EE},
            {EE, EE, EE, EE, EE, EE, EE, EE},
    };

    public static Socket socket;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        activity = this;
        newgame = 0;
        initial();








    }



    private void initial()
    {
        tv = (TextView)findViewById(R.id.tv);
        tvblack = (TextView)findViewById(R.id.tvblack);
        tvwhite = (TextView)findViewById(R.id.tvwhite);
        tvTimer = (TextView)findViewById(R.id.tvTimer);
        iv = (ImageView)findViewById(R.id.iv);


        chk = (CheckBox) findViewById(R.id.chk);
        chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                playWithCPU = b;
            }
        });
        assets = getAssets();
        Point device_screen = Utils.get_Device_Size_in_pixel();
        int margin = Utils.dpToPx(leftRightMarginForSurgaceview);
        int device_width = device_screen.x;
        int newWidth = device_width - (margin * 2);
        int newHeight = (newWidth * GAME_HEIGHT) / GAME_WIDTH;


        lnGame = (LinearLayout)findViewById(R.id.lnGames);
        lnGame.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, newHeight));
        sGame = new MySurfaceView(this, GAME_WIDTH, GAME_HEIGHT,this);

        lnGame.addView(sGame);



        sGame.setOnPlayed(new OnPlayed() {
            @Override
            public void onEvent(final boolean firstTouch, final boolean BlackTurn) {

                if (BlackTurn) {
                    Log.i("eeee","-----> " + blackTurn);
                    tv.setText(getResources().getString(R.string.blackturn));
                    iv.setImageBitmap(Assets.bw1);
                    startTimer();

                } else {
                    Log.i("eeee","-----> " + blackTurn);
                    tv.setText(getResources().getString(R.string.whiteturn));
                    iv.setImageBitmap(Assets.wb1);
                    startTimer();
                }
                updateScores();
            }
        });
        if(blackTurn) {
            tv.setText(getResources().getString(R.string.blackturn));
            iv.setImageBitmap(Assets.bw1);
        }
        else
        {
            tv.setText(getResources().getString(R.string.whiteturn));
            iv.setImageBitmap(Assets.wb1);
        }

        updateScores();

    }
    private void hideTimer()
    {
        tvTimer.setVisibility(View.INVISIBLE);
        if(cdTimer!=null)
            cdTimer.cancel();
    }
    private void startTimer()
    {
        tvTimer.setVisibility(View.VISIBLE);
        if(cdTimer!=null)cdTimer.cancel();
        cdTimer = new CountDownTimer(90000,500)
        {

            @Override
            public void onTick(long l) {


                if(boardHasWinner)
                {
                    tvTimer.setVisibility(View.INVISIBLE);
                    cdTimer.cancel();
                    return;
                }

                tvTimer.setVisibility(View.VISIBLE);
                Log.i("1010","" + l);
                if(l>=1000) {
                    long x = l/1000;
                    x++;
                    tvTimer.setText("" + x);
                }
                else tvTimer.setText("1");
            }

            @Override
            public void onFinish() {

                //tvTimer.setText("0");

                tvTimer.setVisibility(View.VISIBLE);
                if(blackTurn) {

                    // App.Show(getResources().getString(R.string.timeup_red));
                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                    alertDialog.setTitle(getResources().getString(R.string.you_loose));
                    alertDialog.setMessage(getResources().getString(R.string.timeup_black));
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getResources().getString(R.string.ok),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Restart(tv);
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
                else
                {
                    //App.Show(getResources().getString(R.string.timeup_yellow));
                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                    alertDialog.setTitle(getResources().getString(R.string.you_loose));
                    alertDialog.setMessage(getResources().getString(R.string.timeup_white));
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getResources().getString(R.string.ok),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Restart(tv);
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }



            }
        }.start();
    }


    private void updateScores()
    {
       blackScore = 0;
        whiteScore = 0;
        for(byte i=0;i<8;i++)
            for(byte j=0;j<8;j++)
            {
                 if(MainActivity.board[i][j].color == 1)
                     blackScore++;
                if(MainActivity.board[i][j].color == 2)
                    whiteScore++;
            }

        tvblack.setText(getResources().getString(R.string.black_score) + ": " + blackScore);
        tvwhite.setText(getResources().getString(R.string.white_score) + ":" + whiteScore);
    }
    public void Restart(View v)
    {
        System.gc();
        sGame.restart();
        hideTimer();
        gameLocked = false;
        boardHasWinner = false;
        blackTurn = true;
        displayAvailableCells = true;

        if(loadpre) {
            for (int i = 0; i < 8; i++)
                for (int j = 0; j < 8; j++) {
                    Bead b = new Bead(pre[i][j].color, pre[i][j].animIndex);
                        board[i][j] = b;
                }
        }
        else
        {
            for(int i=0;i<8;i++)
                for(int j=0;j<8;j++) {
                    Bead b = new Bead(EE.color, EE.animIndex);
                        board[i][j] = b;
                }
            Bead b = new Bead(WW.color, WW.animIndex);
            board[3][3] = b;
            b = new Bead(WW.color, WW.animIndex);
            board[4][4] = b;
            b = new Bead(BB.color, BB.animIndex);
            board[3][4] = b;
            b = new Bead(BB.color, BB.animIndex);
            board[4][3] = b;
        }

        iv.setImageBitmap(Assets.bw1);
        tv.setText(getResources().getString(R.string.blackturn));
        tvTimer.setVisibility(View.INVISIBLE);
        if(cdTimer!=null)
            cdTimer.cancel();

        chk.setChecked(true);
        playWithCPU = true;
        updateScores();

    }

    public static String _id = "";
    public static String game_id = "";
    public static String queue_id = "";





}
