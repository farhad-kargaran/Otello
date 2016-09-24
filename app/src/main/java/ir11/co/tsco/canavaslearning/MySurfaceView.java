package ir11.co.tsco.canavaslearning;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import ir11.co.tsco.canavaslearning.Tasks.AddToQueue;
import ir11.co.tsco.canavaslearning.Tasks.getDetail;
import ir11.co.tsco.canavaslearning.Tasks.getStatus;
import ir11.co.tsco.canavaslearning.framework.Assets;
import ir11.co.tsco.canavaslearning.framework.Painter;
import ir11.co.tsco.canavaslearning.gameClasses.Bead;
import ir11.co.tsco.canavaslearning.gameClasses.Cell;
import ir11.co.tsco.canavaslearning.gameClasses.GetMove;
import ir11.co.tsco.canavaslearning.gameClasses.Helper;

/**
 * Created by Farhad on 4/23/2016.
 */
public class MySurfaceView extends SurfaceView implements View.OnTouchListener,Runnable,OnPlayed
{

    private Object mPauseLock;
    private boolean mPaused;
    private boolean mFinished;

    private Rect gameImageSrc;
    private Rect gameImageDst;
    private Bitmap gameImage;
    private Painter painter;
    private Canvas gameCanvas;
    private Thread gameThread;
    private int restTime = 15;
    private volatile boolean running = false;
    private  Activity ac;


    public MySurfaceView(Context context) {
        super(context);
    }
    public MySurfaceView(Context context, int gameWidth, int gameHeight, Activity act)
    {
        super(context);
        Assets.load();
        ac = act;

        gameImage = Bitmap.createBitmap(gameWidth, gameHeight, Bitmap.Config.ARGB_4444);
        //gameImage.setDensity(Bitmap.DENSITY_NONE);
        //The Rect gameImageSrc will be used to specify which region of the gameImage should be drawn to the screen.
        gameImageSrc = new Rect(0, 0, gameImage.getWidth(), gameImage.getHeight());
        //The Rect gameImageDst will be used to specify how the gameImage should be scaled when drawn to the screen.
        gameImageDst = new Rect();
        gameCanvas = new Canvas(gameImage);
        painter = new Painter(gameCanvas);
        SurfaceHolder holder = getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {

                setOnTouchListener(MySurfaceView.this);
                initGame();
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

                pauseGame();
            }
        });


    }
    @Override
    public void run() {
        while (!mFinished ) {
            updateAndRender();
        }
        /*while (!mFinished ) {
            updateAndRender();
            // Log.i("aaaa","updateAndRender() called");
            try {
                //Thread.sleep(restTime);
            } catch (Exception e) {
                e.printStackTrace();
            }
            synchronized (mPauseLock) {
                while (mPaused) {
                    try {
                        mPauseLock.wait();
                    } catch (InterruptedException e) {
                    }
                }
            }
        }*/
    }


    private void initGame() {

        MainActivity.newgame++;

        mPauseLock = new Object();
        start();
    }

    public void start() {


        if (MainActivity.newgame == 1) {

            synchronized (mPauseLock) {
                mPaused = true;
            }
            mPaused = false;


            if (MainActivity.loadpre) {
                for (int i = 0; i < 8; i++)
                    for (int j = 0; j < 8; j++) {
                        Bead b = new Bead(MainActivity.pre[i][j].color, MainActivity.pre[i][j].animIndex);
                        MainActivity.board[i][j] = b;
                    }
            } else {
                for (int i = 0; i < 8; i++)
                    for (int j = 0; j < 8; j++) {
                        Bead b = new Bead(MainActivity.EE.color, MainActivity.EE.animIndex);
                        MainActivity.board[i][j] = b;
                    }
                Bead b = new Bead(MainActivity.WW.color, MainActivity.WW.animIndex);
                MainActivity.board[3][3] = b;
                b = new Bead(MainActivity.WW.color, MainActivity.WW.animIndex);
                MainActivity.board[4][4] = b;
                b = new Bead(MainActivity.BB.color, MainActivity.BB.animIndex);
                MainActivity.board[3][4] = b;
                b = new Bead(MainActivity.BB.color, MainActivity.BB.animIndex);
                MainActivity.board[4][3] = b;
            }


            for (int i = 0; i < 8; i++)
                for (int j = 0; j < 8; j++) {
                    Bead b = new Bead(MainActivity.board[i][i].color, MainActivity.board[i][i].animIndex);
                    MainActivity.prev[i][j] = b;
                }

            prisoners = new ArrayList<>();
        }
        mFinished = false;
        gameThread = new Thread(this, "Game Thread");
        gameThread.start();
    }
    public void pause()
    {
        synchronized (mPauseLock) {
            mPaused = true;
        }
        //pauseGame();
    }
    public void resume()
    {
        synchronized (mPauseLock) {
            mPaused = false;
            mPauseLock.notifyAll();
        }
    }
    public void stop()
    {

        pauseGame();


    }
    byte move;

   int cantPlay = 0;
    private void updateAndRender()
    {

        if(MainActivity.boardHasWinner == false) {


            renderBackground();
            if(prisoners.size()>0)
                updateAnimBeads();


            if(!MainActivity.gameLocked && MainActivity.playWithCPU && MainActivity.boardHasWinner == false)
                if( (MainActivity.blackTurn && MainActivity.cpuColor == 1) ||  (!MainActivity.blackTurn && MainActivity.cpuColor == 2) )
                {
                    move = moveIt();
                    if(move == -1)
                    {
                        cantPlay++;
                        changeTurn();

                        if(cantPlay >= 2)
                        displayresult();
                        else
                        {
                            MainActivity.activity.runOnUiThread(new Runnable() {
                                public void run() {


                                    if(mListener!=null)
                                        mListener.onEvent(false, MainActivity.blackTurn);

                                    if(hasEmpty())
                                    App.Show(getResources().getString(R.string.nomove));
                                }
                            });
                        }
                    }

                    else {
                       cantPlay = 0;
                        MainActivity.gameLocked = true;
                        MainActivity.displayAvailableCells = false;

                        if (MainActivity.blackTurn) {

                            Bead b = new Bead((byte) 1, (byte) 0);
                            b.col = (byte) (move % 8);
                            b.isFlying = true;
                            MainActivity.board[move / 8][move % 8] = b;
                        } else {
                            Bead w = new Bead((byte) 2, (byte) 0);
                            w.col = (byte) (move % 8);
                            w.isFlying = true;
                            MainActivity.board[move / 8][move % 8] = w;
                        }
                    }
                    //update for displaying animations

                }


            renderBeads();


            if(MainActivity.playWithCPU == false|| (MainActivity.blackTurn && MainActivity.cpuColor == 2) ||  (!MainActivity.blackTurn && MainActivity.cpuColor == 1) )
                if (MainActivity.displayAvailableCells)
                    renderAvailableCells();


            renderGameImage();
        }
        //pause();

    }
    private boolean hasEmpty()
    {
        for(byte i=0;i<8;i++)
            for(byte j=0;j<8;j++)
                if(MainActivity.board[i][j].color == 0) return true;

        return false;
    }

    private void renderAvailableCells()
    {
        Log.i("uuu","blackturn = "+MainActivity.blackTurn);
        //region body
        List<Cell> c;
        if(MainActivity.blackTurn)
            c = Helper.getValidCells(MainActivity.board, (byte) 1);
        else
            c = Helper.getValidCells(MainActivity.board, (byte) 2);

        for(byte i=0;i<c.size();i++)
        {
            cantPlay = 0;
            Cell cell = c.get(i);
            Point p = getXYAvailable(cell.row,cell.col);
            //painter.drawCircle(p.x,p.y,5,5,5,Color.BLACK);
            painter.drawString("+",p.x,p.y);
        }

        if(c.size() == 0)
        {
            cantPlay++;
            if(cantPlay >=2)
            displayresult();
            else
            {
                changeTurn();
                MainActivity.activity.runOnUiThread(new Runnable() {
                    public void run() {

                        if(mListener!=null)
                            mListener.onEvent(false, MainActivity.blackTurn);
                        if(hasEmpty())
                        App.Show(getResources().getString(R.string.nomove));
                    }
                });
            }
        }
        //endregion
    }

    private void displayresult()
    {
        if(MainActivity.boardHasWinner)return;
        //region body
        MainActivity.boardHasWinner = true;
        MainActivity.gameLocked = true;
        if(MainActivity.blackScore > MainActivity.whiteScore)
        {

            MainActivity.activity.runOnUiThread(new Runnable() {
                public void run() {

                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.activity).create();
                    // alertDialog.setTitle(getResources().getString(R.string.you_loose));
                    alertDialog.setMessage(getResources().getString(R.string.black_win));
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getResources().getString(R.string.ok),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();

                }
            });
        }
        if(MainActivity.blackScore < MainActivity.whiteScore)
        {

            MainActivity.activity.runOnUiThread(new Runnable() {
                public void run() {

                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.activity).create();
                    // alertDialog.setTitle(getResources().getString(R.string.you_loose));
                    alertDialog.setMessage(getResources().getString(R.string.white_win));
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getResources().getString(R.string.ok),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();

                }
            });

        }
        if(MainActivity.blackScore == MainActivity.whiteScore)
        {

            MainActivity.activity.runOnUiThread(new Runnable() {
                public void run() {

                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.activity).create();
                    // alertDialog.setTitle(getResources().getString(R.string.you_loose));
                    alertDialog.setMessage(getResources().getString(R.string.tied));
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getResources().getString(R.string.ok),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();

                }
            });
        }
        //endregion
    }
    int animindex = 0;
    int animationCounter = 2;
    List<Byte> prisoners;
    private void updateAnimBeads()
    {


        if(animindex == 12)return;
        Byte color = 2;
        if(MainActivity.blackTurn) color = 1;

        for(byte i=0;i<prisoners.size();i++)
        {
            byte row = (byte) (prisoners.get(i)/8);
            byte col = (byte) (prisoners.get(i)%8);


            Bead b;
            if(animationCounter==11)
                b= new Bead((byte)color,(byte)0);
            else
            b= new Bead((byte)color,(byte)animindex);
            MainActivity.board[row][col] = b;
        }

        animationCounter--;
        if(animationCounter == 0)
        {
            animindex++;
            animationCounter = 2;
        }
        if(animindex == 12)
        {
            changeTurn();
            MainActivity.displayAvailableCells = true;
            prisoners.clear();
            MainActivity.displayAvailableCells = true;
            MainActivity.gameLocked = false;
            //Turn finished so:

            MainActivity.activity.runOnUiThread(new Runnable() {
                public void run() {

                    if(mListener!=null)
                        mListener.onEvent(false, MainActivity.blackTurn);


                }
            });

        }


    }
    private  void renderBeads()
    {
        //region body
        for(byte i=0;i<8;i++)
            for(byte j=0;j<8;j++)
            {
               /* if(i == 0  &&  j == 1)
                    Log.i("ffff","MainActivity.Board[" + i + "][" + j+"].color +  = " +MainActivity.board[i][j].color);*/

                Bead bead = MainActivity.board[i][j];

                if(bead.color == 0)continue;

                Point xy = getXY(i, j);
                if(bead.isFlying == false) {

                    if (bead.color == 1) {
                        if (bead.animIndex == 0)
                            painter.drawImage(Assets.wb11, xy.x, xy.y);
                        if (bead.animIndex == 1)
                            painter.drawImage(Assets.wb1, xy.x, xy.y);
                        if (bead.animIndex == 2)
                            painter.drawImage(Assets.wb2, xy.x, xy.y);
                        if (bead.animIndex == 3)
                            painter.drawImage(Assets.wb3, xy.x, xy.y);
                        if (bead.animIndex == 4)
                            painter.drawImage(Assets.wb4, xy.x, xy.y);
                        if (bead.animIndex == 5)
                            painter.drawImage(Assets.wb5, xy.x, xy.y);
                        if (bead.animIndex == 6)
                            painter.drawImage(Assets.wb6, xy.x, xy.y);
                        if (bead.animIndex == 7)
                            painter.drawImage(Assets.wb7, xy.x, xy.y);
                        if (bead.animIndex == 8)
                            painter.drawImage(Assets.wb8, xy.x, xy.y);
                        if (bead.animIndex == 9)
                            painter.drawImage(Assets.wb9, xy.x, xy.y);
                        if (bead.animIndex == 10)
                            painter.drawImage(Assets.wb10, xy.x, xy.y);
                        if (bead.animIndex == 11)
                            painter.drawImage(Assets.wb11, xy.x, xy.y);


                    } else {
                        if (bead.animIndex == 0)
                            painter.drawImage(Assets.bw11, xy.x, xy.y);
                        if (bead.animIndex == 1)
                            painter.drawImage(Assets.bw1, xy.x, xy.y);
                        if (bead.animIndex == 2)
                            painter.drawImage(Assets.bw2, xy.x, xy.y);
                        if (bead.animIndex == 3)
                            painter.drawImage(Assets.bw3, xy.x, xy.y);
                        if (bead.animIndex == 4)
                            painter.drawImage(Assets.bw4, xy.x, xy.y);
                        if (bead.animIndex == 5)
                            painter.drawImage(Assets.bw5, xy.x, xy.y);
                        if (bead.animIndex == 6)
                            painter.drawImage(Assets.bw6, xy.x, xy.y);
                        if (bead.animIndex == 7)
                            painter.drawImage(Assets.bw7, xy.x, xy.y);
                        if (bead.animIndex == 8)
                            painter.drawImage(Assets.bw8, xy.x, xy.y);
                        if (bead.animIndex == 9)
                            painter.drawImage(Assets.bw9, xy.x, xy.y);
                        if (bead.animIndex == 10)
                            painter.drawImage(Assets.bw10, xy.x, xy.y);
                        if (bead.animIndex == 11)
                            painter.drawImage(Assets.bw11, xy.x, xy.y);
                    }
                }
                else
                {
                    int s1 = 15;
                    int s2 = 15;
                    if(bead.color == 1)
                    {


                        int step = 2;

                        int x = bead.x;
                        int y = bead.y;

                        if(x == xy.x && y < xy.y)
                        {
                            y+=4;
                            if(y>xy.y)y = xy.y;
                        }
                        else if(y == xy.y && x < xy.x)
                        {
                            x+=4;
                            if(x>xy.x)x = xy.x;
                        }
                        else {


                            if(bead.col <3) step = s1;
                            else step = s2;


                            if(xy.x > xy.y) {
                                if (x < xy.x) x += step;
                                if (y < xy.y) y += (step * xy.y) / xy.x;
                            }
                            else
                            {
                                if (y < xy.y) y += step;
                                if (x < xy.x) x += (step * xy.x) / xy.y;
                            }
                        }


                        if(x>xy.x)x = xy.x;
                        if(y>xy.y)y = xy.y;

                        MainActivity.board[i][j].x = x;
                        MainActivity.board[i][j].y = y;

                        painter.drawImage(Assets.wb11, x, y);
                        if(bead.x == xy.x && bead.y == xy.y)
                        {
                            MainActivity.board[i][j].isFlying = false;
                            animationCounter = 2;
                            animindex = 1;
                            this.prisoners = Helper.getPrisonerBeads(MainActivity.board,move,MainActivity.cpuColor);
                        }
                    }
                    else
                    {
                        int step = 2;

                        int x = bead.x;
                        int y = bead.y;

                        if(x == xy.x && y < xy.y)
                        {
                            y+=4;
                            if(y>xy.y)y = xy.y;
                        }
                        else if(y == xy.y && x < xy.x)
                        {
                            x+=4;
                            if(x>xy.x)x = xy.x;
                        }
                        else {


                            if(bead.col <3) step = s1;
                            else step = s2;

                            if(xy.x > xy.y) {
                                if (x < xy.x) x += step;
                                if (y < xy.y) y += (step * xy.y) / xy.x;
                            }
                            else
                            {
                                if (y < xy.y) y += step;
                                if (x < xy.x) x += (step * xy.x) / xy.y;
                            }
                        }


                        if(x>xy.x)x = xy.x;
                        if(y>xy.y)y = xy.y;

                        MainActivity.board[i][j].x = x;
                        MainActivity.board[i][j].y = y;

                        painter.drawImage(Assets.bw11, x, y);
                        if(bead.x == xy.x && bead.y == xy.y)
                        {
                            MainActivity.board[i][j].isFlying = false;
                            animationCounter = 2;
                            animindex = 1;
                            this.prisoners = Helper.getPrisonerBeads(MainActivity.board,move,MainActivity.cpuColor);
                        }

                    }

                }

            }
//endregion
    }
    private Point getXY(byte row,byte col)
    {
        int x,y;
        x = (col * (MainActivity.GAME_WIDTH/8))+24;
        y = (row * (MainActivity.GAME_WIDTH/8))+24;

        if(col == 1)x = x - 3;
        if(col == 2)x = x - 8;
        if(col == 3)x = x - 13;
        if(col == 4)x = x - 18;
        if(col == 5)x = x - 23;
        if(col == 6)x = x - 28;
        if(col == 7)x = x - 33;

        if(row == 1)y = y - 3;
        if(row == 2)y = y - 8;
        if(row == 3)y = y - 13;
        if(row == 4)y = y - 18;
        if(row == 5)y = y - 23;
        if(row == 6)y = y - 28;
        if(row == 7)y = y - 33;

        Point target = new Point(x,y);
        return  target;
    }
    private Point getXYAvailable(byte row,byte col)
    {
        int x,y;
        x = (col * (MainActivity.GAME_WIDTH/8))+60;
        y = (row * (MainActivity.GAME_WIDTH/8))+95;
        if(col == 1)x = x - 3;
        if(col == 2)x = x - 8;
        if(col == 3)x = x - 13;
        if(col == 4)x = x - 18;
        if(col == 5)x = x - 23;
        if(col == 6)x = x - 28;
        if(col == 7)x = x - 33;

        if(row == 1)y = y - 3;
        if(row == 2)y = y - 8;
        if(row == 3)y = y - 13;
        if(row == 4)y = y - 18;
        if(row == 5)y = y - 23;
        if(row == 6)y = y - 28;
        if(row == 7)y = y - 33;
        Point target = new Point(x,y);
        return  target;
    }
    private void renderBackground()
    {
        //region body

      /*  int lineStorke = 4;
        painter.setColor(Color.rgb(121, 150, 124));
        painter.fillRect(0, 0, MainActivity.GAME_WIDTH, MainActivity.GAME_HEIGHT);

        //Draw Border

        painter.drawLine(0,0,MainActivity.GAME_WIDTH,0,Color.BLACK,lineStorke+3);
        painter.drawLine(0,0,0,MainActivity.GAME_HEIGHT,Color.BLACK,lineStorke+3);

        painter.drawLine(0,MainActivity.GAME_HEIGHT-1 ,MainActivity.GAME_WIDTH,MainActivity.GAME_HEIGHT-1,Color.BLACK,lineStorke+3);
        painter.drawLine(MainActivity.GAME_WIDTH-1,0,MainActivity.GAME_WIDTH-1,MainActivity.GAME_HEIGHT,Color.BLACK,lineStorke+3);

        int step = MainActivity.GAME_WIDTH / 8;
        for(int i=1;i<=7;i++)
        {
            //horizontal
            painter.drawLine(0,step*i,MainActivity.GAME_WIDTH,step*i,Color.BLACK,lineStorke-3);
            //vertical
            painter.drawLine(step*i,0,step*i,MainActivity.GAME_HEIGHT,Color.BLACK,lineStorke);

        }*/
        painter.drawImage(Assets.board, 0, 0);
        //endregion
    }


    private  void changeTurn()
    {
        MainActivity.blackTurn = !MainActivity.blackTurn;
    }
    private byte moveIt()
    {

        byte color = MainActivity.cpuColor;
        byte eColor = 1;
        if(color ==1)eColor = 2;
        byte x = GetMove.get(MainActivity.board,color,eColor);


       /* List<Byte> valids = Helper.getValidIndexes(MainActivity.board,MainActivity.cpuColor);
        Random a = new Random();
        Byte x = valids.get((byte) a.nextInt(valids.size()));*/
        return x;
    }

    private void renderGameImage() {
        Canvas screen = getHolder().lockCanvas();
        if (screen != null) {
            screen.getClipBounds(gameImageDst);
            screen.drawBitmap(gameImage, gameImageSrc, gameImageDst, null);
            getHolder().unlockCanvasAndPost(screen);
        }
    }
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(MainActivity.boardHasWinner || MainActivity.gameLocked)return  false;

        if(event.getAction() == MotionEvent.ACTION_UP) {

            Log.i("ffff","touch up");
            int scaledX = (int) ((event.getX() / v.getWidth()) * MainActivity.GAME_WIDTH);
            int scaledY = (int) ((event.getY() / v.getHeight()) * MainActivity.GAME_HEIGHT);
            byte col = (byte) (scaledX / (MainActivity.GAME_WIDTH/8));
            byte row = (byte) (scaledY / (MainActivity.GAME_WIDTH/8));

            if(row > 7 || col > 7 || row < 0 || col < 0) return false;

            byte index = (byte) ((row*8) + col);
            Byte color = 2;
            if(MainActivity.blackTurn) color = 1;
            List<Byte> valids = Helper.getValidIndexes(MainActivity.board,color);


            if(valids.contains(index))
            {
                Log.i("ffff","is valid and index = " + index + " ,row = " + row + " , col = " + col);
                MainActivity.gameLocked = true;
                MainActivity.displayAvailableCells = false;

                if(MainActivity.blackTurn) {

                    Bead b = new Bead((byte)1,(byte)0);
                    MainActivity.board[row][col] = b;
                }
                else
                {
                    Bead w = new Bead((byte)2,(byte)0);
                    MainActivity.board[row][col] = w;
                }
                //update for displaying animations
                animationCounter = 2;
                animindex = 1;
                this.prisoners = Helper.getPrisonerBeads(MainActivity.board,index,color);
                // updateBeads(Helper.getPrisonerBeads(MainActivity.board,index,color));

            }
            else
            {
                Log.i("ffff","is NOT valid and index = " + index + " ,row = " + row + " , col = " + col);
            }

        }
        else if(event.getAction() == MotionEvent.ACTION_MOVE || event.getAction() == MotionEvent.ACTION_DOWN) {
            //Log.i("ffff",event.getAction() + "     up="+MotionEvent.ACTION_UP+ " down="+MotionEvent.ACTION_DOWN+ " move="+MotionEvent.ACTION_MOVE);
            int scaledX = (int) ((event.getX() / v.getWidth()) * MainActivity.GAME_WIDTH);
            int scaledY = (int) ((event.getY() / v.getHeight()) * MainActivity.GAME_HEIGHT);
            return true;
        }
        else  return true;



        return true;
    }


  public void restart()
  {

  }
    private void pauseGame() {
       /* synchronized (mPauseLock) {
            mPaused = true;
        }*/
        mFinished = true;
        while (gameThread.isAlive()) {
            try {
                gameThread.join();
                break;
            } catch (InterruptedException e) {
            }
        }
    }

    private  OnPlayed mListener;
    public void setOnPlayed(OnPlayed eventListener) {
        mListener = eventListener;
    }
    @Override
    public void onEvent(boolean firstTouch, boolean color) {

    }



    public void addQueue(String user_id)
    {
        new AddToQueue(user_id,"Othello","A");
    }
    public void getStatus(String user_id)
    {
        if(user_id.length()<4)return;
        new getStatus(user_id, true);
    }

    public void getDetail(String game_id)
    {
        if(game_id.length()<4)return;
        new getDetail(game_id);
    }


}
