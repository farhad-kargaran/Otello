package ir11.co.tsco.canavaslearning.gameClasses;

/**
 * Created by Farhad on 4/26/2016.
 */
public class Bead {
    public byte color;
    public byte animIndex;
    public byte col;
    public boolean isFlying = false;
    public int x=-10,y = -10;


    public Bead(byte color, byte animIndex) {
        this.color = color;
        this.animIndex = animIndex;
    }


}
