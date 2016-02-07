import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Represents the like button.
 * 
 * @author Rune Krauss
 * @author Steffen Gerken
 * @author Niels Arbatschat
 */
public class LikeButton extends PI1GameActor 
{
    /**
     * Act - do whatever the LikeButton wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        if (Greenfoot.mouseClicked(this)) {
            Field field = (Field) getWorld();
            field.getLevel().like();
        }
    }    
}
