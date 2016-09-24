package ir11.co.tsco.canavaslearning.gameClasses;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ir11.co.tsco.canavaslearning.MainActivity;

/**
 * Created by Farhad on 5/1/2016.
 */
public class GetMove {


    public static List<nBoard> nodes;
    public static byte get(Bead[][] board, byte color, byte eColor)
    {
        byte res = 0;
        nodes = new ArrayList<>();

        List<Byte> valids = Helper.getValidIndexes(board,color);
        if(valids.size() == 0)return -1;
        res = valids.get(0);
        for(int i=0;i<valids.size();i++)
        {

            byte current = valids.get(i);


            //----Create a node
            nBoard nd = new nBoard();
            //----Create copy of parent
            for(byte y=0;y<8;y++)
                for(byte z=0;z<8;z++)
                {
                    if(board[y][z].color == 0)continue;
                    Bead b = new Bead(board[y][z].color,((byte)0));
                    nd.board[y][z] = b;
                }
            //----set base move
            nd.baseMove = current;
            //----add color to cell
            nd.board[current/8][current%8] = new Bead(color,((byte)0));
            //----effect prison beads
            List<Byte> prisoners =  Helper.getPrisonerBeads(nd.board,current,color);
            for(byte y = 0;y<prisoners.size();y++)
            {
                byte p = prisoners.get(y);
                nd.board[p/8][p%8] = new Bead(color,(byte)0);
            }

            nodes.add(nd);

        }
        int max = 80000;
        boolean humanTurn = false;
        for(int depth = 0;depth<6;depth++)
        {
            if(nodes.size() > max)break;
            humanTurn = !humanTurn;
            int s = nodes.size();
            for(int h=0;h<s;h++) {
                if(nodes.size() > max)break;
                List<Byte> val;
                if(humanTurn)
                    val = Helper.getValidIndexes(nodes.get(h).board, eColor);
                else
                    val = Helper.getValidIndexes(nodes.get(h).board, color);



                //if is leaf
                if(val.size() == 0)
                {


                    List<Byte> va;
                    if(humanTurn)
                        va = Helper.getValidIndexes(nodes.get(h).board, color);
                    else
                        va = Helper.getValidIndexes(nodes.get(h).board, eColor);



                    if(va.size() == 0)
                    {
                        byte colorC=0,colorE=0;
                        nodes.get(h).finished = true;
                        for(int ii=0;ii<8;ii++)
                            for(int jj=0;jj<8;jj++) {
                                if (nodes.get(h).board[ii][jj].color == color) colorC++;
                                if (nodes.get(h).board[ii][jj].color == eColor) colorE++;
                            }
                        if(colorC>=colorE)nodes.get(h).value = +200;
                        else nodes.get(h).value = +200;
                        if(humanTurn)nodes.get(h).color = eColor;
                        else nodes.get(h).color = color;
                        continue;
                    }

                    if(humanTurn)nodes.get(h).color = eColor;
                    else nodes.get(h).color = color;
                    continue;
                }

                //----Create a copy of node before deletion
                nBoard nd = new nBoard();
                //----Create copy
                for(byte y=0;y<8;y++)
                    for(byte z=0;z<8;z++)
                    {
                        if(nodes.get(h).board[y][z].color == 0)continue;
                        Bead b = new Bead(nodes.get(h).board[y][z].color,((byte)0));
                        nd.board[y][z] = b;
                    }
                //----set base move
                nd.baseMove = nodes.get(h).baseMove;
                //delete node
                nodes.remove(h);
                //----add color to cell




                for(int i=0;i<val.size();i++)
                {


                    if(nodes.size() > max)break;
                    byte current = val.get(i);
                    //----Create a node
                    nBoard ndd = new nBoard();
                    //----Create copy of parent
                    for(byte y=0;y<8;y++)
                        for(byte z=0;z<8;z++)
                        {
                            if(nd.board[y][z].color == 0)continue;
                            Bead b = new Bead(nd.board[y][z].color,((byte)0));
                            ndd.board[y][z] = b;
                        }
                    //----set base move
                    ndd.baseMove = nd.baseMove;
                    //----add color to cell
                    if(humanTurn)
                        ndd.board[current/8][current%8] = new Bead(eColor,((byte)0));
                    else
                        ndd.board[current/8][current%8] = new Bead(color,((byte)0));
                    //----effect prison beads

                    List<Byte> prisoners;
                    if(humanTurn)
                        prisoners =  Helper.getPrisonerBeads(ndd.board,current,eColor);
                    else prisoners =  Helper.getPrisonerBeads(ndd.board,current,color);
                    for(byte y = 0;y<prisoners.size();y++)
                    {
                        byte p = prisoners.get(y);
                        if(humanTurn)
                            ndd.board[p/8][p%8] = new Bead(eColor,(byte)0);
                        else  ndd.board[p/8][p%8] = new Bead(color,(byte)0);
                    }

                    if(humanTurn)ndd.color = eColor;
                    else ndd.color = color;
                    nodes.add(ndd);

                }


            }

        }


        int maxIndex = 0;
        for(int j=1;j<nodes.size();j++)
        {

            int value = 0;
            byte colorCount = 0;
            byte eColorCount = 0;
            if(nodes.get(j).finished == false) {
                for (byte y = 0; y < 8; y++)
                    for (byte z = 0; z < 8; z++) {

                        if (nodes.get(j).board[y][z].color == 0) continue;

                        byte ev = MainActivity.ev[y][z];
                        if (nodes.get(j).board[y][z].color == color) {
                            colorCount++;
                            value += ev;
                        } else {
                            eColorCount++;
                            value -= ev;
                        }
                    }
                nodes.get(j).value = value;
            }

            if(nodes.get(j).value > nodes.get(maxIndex).value)
                maxIndex = j;

        }
        byte xx = nodes.get(maxIndex).baseMove;
        Log.i("dddd","node size = " + nodes.size());
        nodes.clear();
        System.gc();
        return xx;



    }



}
class nBoard
{
    public  nBoard parent;
    public boolean finished = false;
    public static Bead EE = new Bead((byte)0,(byte)0);//EMPTY
    public static Bead BB = new Bead((byte)1,(byte)0);//BLACK
    public static Bead WW = new Bead((byte)2,(byte)0);//WHITE

    public  Bead[][] board = new Bead[][]{
            {EE, EE, EE, EE, EE, EE, EE, EE},
            {EE, EE, EE, EE, EE, EE, EE, EE},
            {EE, EE, EE, EE, EE, EE, EE, EE},
            {EE, EE, EE, EE, EE, EE, EE, EE},
            {EE, EE, EE, EE, EE, EE, EE, EE},
            {EE, EE, EE, EE, EE, EE, EE, EE},
            {EE, EE, EE, EE, EE, EE, EE, EE},
            {EE, EE, EE, EE, EE, EE, EE, EE},
    };

    byte baseMove;
    int value;
    byte color;
}
