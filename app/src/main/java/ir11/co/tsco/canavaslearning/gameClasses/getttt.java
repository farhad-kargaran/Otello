/*
package ir11.co.tsco.canavaslearning.gameClasses;

import java.util.List;

import ir11.co.tsco.canavaslearning.MainActivity;

*/
/**
 * Created by Farhad on 5/3/2016.
 *//*

public class getttt {

    public static int search(Bead[][] s, int depth, byte color)
    {
        if (depth == 0 || reachedFinal(s))
            return evaluation(s, color);
        else
        {
            if (color == MainActivity.cpuColor)
            {

                List<Byte> valids = Helper.getValidIndexes(s,color);
                for(int i=0;i<valids.size();i++)
                {
                    Bead[][] ss = new Bead[8][8];
                    for(byte x=0;x<8;x++)
                        for(byte y=0;y<8;y++)
                        {
                            Bead b = new Bead(s[x][y].color, (byte) 0);
                            ss[x][y] = b;
                        }
                    byte current = valids.get(i);
                    ss[current/8][current%8] = new Bead(color,((byte)0));

                    List<Byte> prisoners =  Helper.getPrisonerBeads(ss,current,color);
                    for(byte y = 0;y<prisoners.size();y++)
                    {
                        byte p = prisoners.get(y);
                        ss[p/8][p%8] = new Bead(color,(byte)0);
                    }
                    search(ss,depth-1, (byte) 2);
                }



            }
            else
            {
                List<Byte> valids = Helper.getValidIndexes(s,color);
                for(int i=0;i<valids.size();i++)
                {
                    Bead[][] ss = new Bead[8][8];
                    for(byte x=0;x<8;x++)
                        for(byte y=0;y<8;y++)
                        {
                            Bead b = new Bead(s[x][y].color, (byte) 0);
                            ss[x][y] = b;
                        }
                    byte current = valids.get(i);
                    ss[current/8][current%8] = new Bead(color,((byte)0));

                    List<Byte> prisoners =  Helper.getPrisonerBeads(ss,current,color);
                    for(byte y = 0;y<prisoners.size();y++)
                    {
                        byte p = prisoners.get(y);
                        ss[p/8][p%8] = new Bead(color,(byte)0);
                    }
                    search(ss,depth-1, (byte) 1);
                }

            }
        }

    }
    private static int evaluation(Bead[][] s, byte co)
    {
        int color = 1;
        int ecolor = 2;
        if(MainActivity.cpuColor == 2)
        {
            color = 2;
            ecolor = 1;
        }
        int  c =0 ;
        int ec = 0;
        for(byte i=0;i<8;i++)
            for(byte j=0;j<8;j++)
            {
                if(s[i][i].color == color) c++;
                if(s[i][i].color == ecolor) ec++;
            }

        //if(co == MainActivity.cpuColor)return (c - ec);
        //else return (ec)
        int v = c - ec;
        return v;
    }
    private static boolean reachedFinal(Bead[][] s)
    {


        List<Byte> res1 =  Helper.getValidIndexes(s, (byte) 1);
        List<Byte> res2 =  Helper.getValidIndexes(s, (byte) 2);
        if(res1.size() == 0 && res2.size()==0)return true;



        return false;
    }
}
*/
