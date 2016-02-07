import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.Color;

/**
 * Represents the basis counter of this game.
 * 
 * @author Rune Krauss
 * @author Steffen Gerken
 * @author Niels Arbatschat
 */
public class Counter extends PI1GameActor
{
    /**
     * Value of the counter
     */
    protected int val;

    /**
     * Target of the counter
     */
    protected int target;

    /**
     * Background of the counter
     */
    protected GreenfootImage bg;

    /**
     * Color of the counter
     */
    protected final Color color = new Color(0, 0, 0, 0);

    /**
     * Text color of the counter
     */
    protected Color textColor;

    /**
     * This is the constructor of the counter. 
     * Gets the respective image and sets value and 
     * target to zero. Furthermore, updates the counter.
     */
    public Counter(Color textColor) {
        this.textColor = textColor;
        bg = getImage();
        val = 0;
        target = 0;
        update();
    }

    /**
     * Act - do whatever the Counter wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
        if (val < target) {
            val++;
            update();
        }
        else if (val > target) {
            val--;
            update();
        }
    }    

    /**
     * Updates the counter. First, creates images regarding background and text. 
     * Afterwards, draws and sets the respective image.
     */
    private void update()
    {
        GreenfootImage img = new GreenfootImage(bg);
        GreenfootImage txt = new GreenfootImage(val + "", 20, textColor, color);
        img.drawImage(txt, 
            ( img.getWidth() - txt.getWidth() ) / 2, ( img.getHeight() - txt.getHeight() ) / 2);
        setImage(img);
    }  

    /**
     * Gets the value of the counter.
     */
    public int getVal()
    {
        return val;
    }

    /**
     * Sets the value of the counter.
     * 
     * @param valNew
     *      New value for the counter.
     */
    public void setVal(int valNew)
    {
        target = valNew;
        val = valNew;
        update();
    }
}
