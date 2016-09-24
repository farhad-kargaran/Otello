package ir11.co.tsco.canavaslearning.gameClasses;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Farhad on 4/26/2016.
 */
public class Helper {

    public static List<Byte> getPrisonerBeads(Bead[][] board, byte index, byte color)
    {
        List<Byte> res = new ArrayList<>();
        List<Byte> temp = new ArrayList<>();
        byte eColor = 1;
        if(color == 1)eColor = 2;

        byte i = (byte) (index/8);
        byte j = (byte) (index%8);

        //to right
        temp.clear();
        for(byte x = (byte) (j+1); x<=7; x++)
        {
            if(board[i][x].color == eColor) {
                byte in = (byte) ((i * 8) + (x));
                temp.add(in);
            }
            else if(board[i][x].color == color)
            {
                for(byte a = 0;a<temp.size();a++)
                    if (!res.contains(temp.get(a)))
                        res.add(temp.get(a));
                break;
            }
            else break;

        }

        //to left
        temp.clear();
        for(byte x = (byte) (j-1); x>=0; x--)
        {
            if(board[i][x].color == eColor) {
                byte in = (byte) ((i * 8) + (x));
                temp.add(in);
            }
            else if(board[i][x].color == color)
            {
                for(byte a = 0;a<temp.size();a++)
                    if (!res.contains(temp.get(a)))
                        res.add(temp.get(a));
                break;
            }
            else break;

        }
        // to up
        temp.clear();
        for(byte x = (byte) (i-1); x>=0;x--)
        {

            if(board[x][j].color == eColor) {
                byte in = (byte) ( (x*8) + j );
                temp.add(in);
            }
            else if(board[x][j].color == color)
            {
                for(byte a = 0;a<temp.size();a++)
                    if (!res.contains(temp.get(a)))
                        res.add(temp.get(a));
                break;
            }
            else break;

        }
        // to down
        temp.clear();
        for(byte x = (byte) (i+1); x<=7;x++)
        {

            if(board[x][j].color == eColor) {
                byte in = (byte) ( (x*8) + j );
                temp.add(in);
            }
            else if(board[x][j].color == color)
            {
                for(byte a = 0;a<temp.size();a++)
                    if (!res.contains(temp.get(a)))
                        res.add(temp.get(a));
                break;
            }
            else break;

        }

        // to up left
        temp.clear();
        byte tempJ = j;
        for(byte x = (byte) (i-1); x>=0;x--)
        {
            tempJ--;

            if(tempJ < 0)break;
            if(board[x][tempJ].color == eColor) {
                byte in = (byte) ( (x*8) + tempJ );
                temp.add(in);
            }
            else if(board[x][tempJ].color == color)
            {
                for(byte a = 0;a<temp.size();a++)
                    if (!res.contains(temp.get(a)))
                        res.add(temp.get(a));
                break;
            }
            else break;

        }
        // to up right
        temp.clear();
        tempJ = j;
        for(byte x = (byte) (i-1); x>=0;x--)
        {
            tempJ++;
            if(tempJ > 7)break;
            if(board[x][tempJ].color == eColor) {
                byte in = (byte) ( (x*8) + tempJ );
                temp.add(in);
            }
            else if(board[x][tempJ].color == color)
            {
                for(byte a = 0;a<temp.size();a++)
                    if (!res.contains(temp.get(a)))
                        res.add(temp.get(a));
                break;
            }
            else break;

        }

        // to down left
        temp.clear();
        tempJ = j;
        for(byte x = (byte) (i+1); x<=7;x++)
        {
            tempJ--;
            if(tempJ < 0)break;
            if(board[x][tempJ].color == eColor) {
                byte in = (byte) ( (x*8) + tempJ );
                temp.add(in);
            }
            else if(board[x][tempJ].color == color)
            {
                for(byte a = 0;a<temp.size();a++)
                    if (!res.contains(temp.get(a)))
                        res.add(temp.get(a));
                break;
            }
            else break;
        }

        // to down right
        temp.clear();
        tempJ = j;
        for(byte x = (byte) (i+1); x<=7;x++)
        {
            tempJ++;
            if(tempJ > 7)break;
            if(board[x][tempJ].color == eColor) {
                byte in = (byte) ( (x*8) + tempJ );
                temp.add(in);
            }
            else if(board[x][tempJ].color == color)
            {
                for(byte a = 0;a<temp.size();a++)
                    if (!res.contains(temp.get(a)))
                        res.add(temp.get(a));
                break;
            }
            else break;
        }



        return  res;
    }
    public static List<Byte> getValidIndexes(Bead[][] board,byte color)
    {
        //region body
        byte eColor = 1;
        if(color == 1)eColor = 2;
        List<Byte> res = new ArrayList<>();

        for(byte i=0;i<8;i++)
            for(byte j=0;j<8;j++)
            {
                if(board[i][j].color == color)
                {

                    //to right
                    for(byte x = (byte) (j+1); x<=7; x++)
                    {
                        if(board[i][x].color == color)break;
                        if(board[i][x].color == eColor)continue;
                        if(board[i][x].color == 0 && board[i][x-1].color == eColor)
                        {
                            if (!res.contains((byte) ((i*8) + x)))
                                res.add((byte) ((i*8) + x));
                            break;
                        }
                        else break;

                    }
                    //to left
                    for(byte x = (byte) (j-1); x>=0; x--)
                    {
                        if(board[i][x].color == color)break;
                        if(board[i][x].color == eColor)continue;
                        if(board[i][x].color == 0 && board[i][x+1].color == eColor)
                        {
                            if (!res.contains((byte) ((i*8) + x)))
                                res.add((byte) ((i*8) + x));
                            break;
                        }
                        else break;

                    }




                    // to up
                    for(byte x = (byte) (i-1); x>=0;x--)
                    {
                        if(board[x][j].color == color)break;
                        if(board[x][j].color == eColor)continue;
                        if(board[x][j].color == 0 && board[x+1][j].color == eColor)
                        {
                            if (!res.contains((byte) ((x*8) + j)))
                                res.add((byte) ((x*8) + j));
                            break;
                        }
                        else break;

                    }
                    // to down
                    for(byte x = (byte) (i+1); x<=7;x++)
                    {
                        if(board[x][j].color == color)break;
                        if(board[x][j].color == eColor)continue;
                        if(board[x][j].color == 0 && board[x-1][j].color == eColor)
                        {
                            if (!res.contains((byte) ((x*8) + j)))
                                res.add((byte) ((x*8) + j));
                            break;
                        }
                        else break;

                    }

                    // to up left
                    byte tempJ = j;
                    for(byte x = (byte) (i-1); x>=0;x--)
                    {
                        tempJ--;
                        if(tempJ<0)break;
                        if(board[x][tempJ].color == color)break;
                        if(board[x][tempJ].color == eColor)continue;
                        if((tempJ+1) <=7)
                        if(board[x][tempJ].color == 0 && board[x+1][tempJ+1].color == eColor)
                        {
                            if (!res.contains((byte) ((x*8) + tempJ)))
                                res.add((byte) ((x*8) + tempJ));
                            break;
                        }
                        else break;
                    }



                    // to up right
                    tempJ = j;
                    for(byte x = (byte) (i-1); x>=0;x--)
                    {
                        tempJ++;
                        if(tempJ>7)break;
                        if(board[x][tempJ].color == color)break;
                        if(board[x][tempJ].color == eColor)continue;
                        if((tempJ-1) >=0)
                        if(board[x][tempJ].color == 0 && board[x+1][tempJ-1].color == eColor)
                        {
                            if (!res.contains((byte) ((x*8) + tempJ)))
                                res.add((byte) ((x*8) + tempJ));
                            break;
                        }
                        else break;
                    }

                    // to down left
                    tempJ = j;
                    for(byte x = (byte) (i+1); x<=7;x++)
                    {
                        tempJ--;
                        if(tempJ<0)break;
                        if(board[x][tempJ].color == color)break;
                        if(board[x][tempJ].color == eColor)continue;
                        if((tempJ+1)<=7)
                        if(board[x][tempJ].color == 0 && board[x-1][tempJ+1].color == eColor)
                        {
                            if (!res.contains((byte) ((x*8) + tempJ)))
                                res.add((byte) ((x*8) + tempJ));
                            break;
                        }
                        else break;
                    }

                    // to down right
                    tempJ = j;
                    for(byte x = (byte) (i+1); x<=7;x++)
                    {
                        tempJ++;
                        if(tempJ>7)break;
                        if(board[x][tempJ].color == color)break;
                        if(board[x][tempJ].color == eColor)continue;
                        if((tempJ-1)>=0)
                        if(board[x][tempJ].color == 0 && board[x-1][tempJ-1].color == eColor)
                        {
                            if (!res.contains((byte) ((x*8) + tempJ)))
                                res.add((byte) ((x*8) + tempJ));
                            break;
                        }
                        else break;
                    }

                }
            }

        return  res;

        //endregion
    }
    public static List<Cell> getValidCells(Bead[][] board,byte color)
    {
        //region body
        byte eColor = 1;
        if(color == 1)eColor = 2;
        List<Cell> res = new ArrayList<>();

        for(byte i=0;i<8;i++)
            for(byte j=0;j<8;j++)
            {
                if(board[i][j].color == color)
                {

                    //to right
                    for(byte x = (byte) (j+1); x<=7; x++)
                    {
                        if(board[i][x].color == color)break;
                        if(board[i][x].color == eColor)continue;
                        if(board[i][x].color == 0 && board[i][x-1].color == eColor)
                        {
                            if (!res.contains(new Cell(i, x)))
                                res.add(new Cell(i, x));
                            break;
                        }
                        else break;

                    }
                    //to left
                    for(byte x = (byte) (j-1); x>=0; x--)
                    {
                        if(board[i][x].color == color)break;
                        if(board[i][x].color == eColor)continue;
                        if(board[i][x].color == 0 && board[i][x+1].color == eColor)
                        {
                            if (!res.contains(new Cell(i, x)))
                                res.add(new Cell(i, x));
                            break;
                        }
                        else break;

                    }
                    // to up
                    for(byte x = (byte) (i-1); x>=0;x--)
                    {
                        if(board[x][j].color == color)break;
                        if(board[x][j].color == eColor)continue;
                        if(board[x][j].color == 0 && board[x+1][j].color == eColor)
                        {
                            if (!res.contains(new Cell(x, j)))
                                res.add(new Cell(x, j));
                            break;
                        }
                        else break;

                    }
                    // to down
                    for(byte x = (byte) (i+1); x<=7;x++)
                    {
                        if(board[x][j].color == color)break;
                        if(board[x][j].color == eColor)continue;
                        if(board[x][j].color == 0 && board[x-1][j].color == eColor)
                        {
                            if (!res.contains(new Cell(x, j)))
                                res.add(new Cell(x, j));
                            break;
                        }
                        else break;

                    }

                    // to up left
                    byte tempJ = j;
                    for(byte x = (byte) (i-1); x>=0;x--)
                    {
                        tempJ--;
                        if(tempJ<0)break;
                        if(board[x][tempJ].color == color)break;
                        if(board[x][tempJ].color == eColor)continue;
                        if((tempJ + 1)<=7)
                        if(board[x][tempJ].color == 0 && board[x+1][tempJ+1].color == eColor)
                        {
                            if (!res.contains(new Cell(x, tempJ)))
                                res.add(new Cell(x, tempJ));
                            break;
                        }
                        else break;
                    }
                    // to up right
                    tempJ = j;
                    for(byte x = (byte) (i-1); x>=0;x--)
                    {
                        tempJ++;
                        if(tempJ>7)break;
                        if(board[x][tempJ].color == color)break;
                        if(board[x][tempJ].color == eColor)continue;
                        if((tempJ - 1)>= 0)
                        if(board[x][tempJ].color == 0 && board[x+1][tempJ-1].color == eColor)
                        {
                            if (!res.contains(new Cell(x, tempJ)))
                                res.add(new Cell(x, tempJ));
                            break;
                        }
                        else break;
                    }

                    // to down left
                    tempJ = j;
                    for(byte x = (byte) (i+1); x<=7;x++)
                    {
                        tempJ--;
                        if(tempJ<0)break;
                        if(board[x][tempJ].color == color)break;
                        if(board[x][tempJ].color == eColor)continue;
                        if((tempJ + 1)<=7)
                        if(board[x][tempJ].color == 0 && board[x-1][tempJ+1].color == eColor)
                        {
                            if (!res.contains(new Cell(x, tempJ)))
                                res.add(new Cell(x, tempJ));
                            break;
                        }
                        else break;
                    }

                    // to down right
                    tempJ = j;
                    for(byte x = (byte) (i+1); x<=7;x++)
                    {
                        tempJ++;
                        if(tempJ>7)break;
                        if(board[x][tempJ].color == color)break;
                        if(board[x][tempJ].color == eColor)continue;
                        if((tempJ - 1) >= 0)
                        if(board[x][tempJ].color == 0 && board[x-1][tempJ-1].color == eColor)
                        {
                            if (!res.contains(new Cell(x, tempJ)))
                                res.add(new Cell(x, tempJ));
                            break;
                        }
                        else break;
                    }

                }
            }

        return  res;

        //endregion
    }


}
