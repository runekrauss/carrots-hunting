import java.awt.Color;
import greenfoot.*;
import java.io.*;

/**
 * Represents the carrot counter of this game.
 * 
 * @author Rune Krauss
 * @author Steffen Gerken
 * @author Niels Arbatschat
 */
public class CarrotCounter extends Counter
{
    /**
     * Profit size of carrots to win a game
     */
    int amount = 0;

    /**
     * Status of the next level
     */
    boolean nextLevel = false;

    /**
     * This is the constructor of the carrot counter. 
     * Gets the respective image and sets value and 
     * target to zero. Furthermore, updates the counter.
     */
    public CarrotCounter(Color textColor)
    {
        super(textColor);
    }

    /**
     * Act - do whatever the rabbit wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
        super.act();
        if (nextLevel) {
            System.out.println("Congratulations, you earned " + val + " carrots and passed the level!");
            Field field = (Field) getWorld();
            try {
                field.checkNextLevel();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            amount = getWorld().getObjects("Carrot").size();
        }
        if (amount == 0){
            nextLevel = true;
        }
    }

    /**
     * Adds a value to the counter and increases the target.
     * @param score
     *      Value to increase the target.
     */
    public void add(int score)
    {
        target += score;
        System.out.println("Counter was increased...");
    }
}