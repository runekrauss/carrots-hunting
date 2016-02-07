import java.awt.Color;

/**
 * Represents the like counter of this game.
 * 
 * @author Rune Krauss
 * @author Steffen Gerken
 * @author Niels Arbatschat
 */
public class LikeCounter extends Counter
{
    /**
     * This is the constructor of the like counter. 
     * Gets the respective image and sets value and 
     * target to zero. Furthermore, updates the counter.
     */
    public LikeCounter(Color textColor)
    {
        super(textColor);
    }

    /**
     * Adds a value to the counter and increases the target.
     * @param score
     *      Value to increase the target.
     */
    public void add(int score)
    {
        target = score;
        System.out.println("Counter regarding likes was increased...");
    }

}