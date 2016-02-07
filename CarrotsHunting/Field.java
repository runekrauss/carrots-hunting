import greenfoot.*;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.io.*;
import java.util.*;
import java.util.Arrays;
import java.awt.Color;

/**
 * The field represents the environment of this game. 
 * 
 * @author Rune Krauss
 * @author Steffen Gerken
 * @author Niels Arbatschat
 */
public class Field extends PI1GameWorld
{
    /**
     * Level of this game
     */
    private Level level;

    /**
     * Map of this game
     */
    private Map map;

    /**
     * Status of the map
     */
    private boolean triggered;

    /**
     * Name of the active level
     */
    private static String levelName = "level1";

    /**
     * Name of the active level
     */
    public static final String MAX_LEVEL = "level2";
    
    /**
     * Name of the level file
     */
    private final String LEVEL_NAME = "level.txt";

    /**
     * Constructor for objects of class Acker.
     * It's creates the field based on width, height, cell width and cell height.
     */
    public Field() throws FileNotFoundException
    {
        // width, height, cell width, cell height
        super(800, 600, 80, 60);
        setMaintainPaintOrder(false);
        setActOrder("Rabbit");
        try {
            prepare();
        } catch(InputMismatchException ime) {
            showException( ime.getMessage() );
        } catch(IllegalArgumentException iae) {
            showException( iae.getMessage() );
        } catch(FileNotFoundException fnfe) {
            showException( fnfe.getMessage() );
        }
        setMaintainPaintOrder(true);
        showLevelName();
        level.checkLikes(false);
    }

    /**
     * Prepares the world regarding the program start.
     * Creates objects at the beginning and add them to the world.
     */
    private void prepare() throws FileNotFoundException
    {
        if (levelName.equals("level1")) {
            level = new Level1(LEVEL_NAME);
            level.init(this);
        } else if (levelName.equals("level2")) {
            level = new Level2(LEVEL_NAME);
            level.init(this);
        }
    }

    /**
     * Gets current level.
     * @return Level of this game.
     */
    public Level getLevel() {
        return level;
    }

    /**
     * Gets the map.
     * @return Map or null if no map exists.
     */
    public Map getMap() {
        return triggered ? map : null;
    }

    /**
     * Gets the level name.
     * @return Name of the level
     */
    public static String getLevelName() {
        return levelName;
    }

    /**
     * Sets the map.
     * @param map
     *      Navigation map regarding the NPC
     */
    public void setMap(Map map) {
        this.map = map;
        map.visualize();
    }

    /**
     * Activates the runnability regarding the npc based on the map.
     * @param triggered Status of the runnability
     */
    public void setTriggered(boolean triggered){
        this.triggered = triggered;
    }

    /**
     * Gets status of the runnability regarding the npc.
     * @return Status of the runnability
     */
    public boolean isTriggered(){
        return triggered;
    }

    /**
     * Shows the throwed exception.
     * @param msg
     *      Exception message
     */
    private void showException(String msg){
        GreenfootImage img = new GreenfootImage( msg, 18, Color.RED, null );
        getBackground().drawImage(img, getWidth()/2-img.getWidth()/2, getHeight()/2-img.getHeight()/2);
    }

    /**
     * Checks the next level.
     */
    public void checkNextLevel() throws InterruptedException {
        if (levelName.equals(MAX_LEVEL)) {
            getSystem().playSound("fanfare.wav");
            getSystem().stop();
            System.out.println("Congratulations, you won the game!");
        } else {
            removeObjects(getObjects(""));
            try {
                Thread.sleep(1000);
                levelName = "level2";
                Greenfoot.setWorld(new Field());
            } catch (IOException e) {
                e.getMessage();
            }
        }
    }
    
    /**
     * Shows the name of the level.
     */
    private void showLevelName(){
        GreenfootImage img = null;
        if (levelName.equals("level1")) {
           img = new GreenfootImage("Level 1", 21, Color.BLACK, null); 
        } else if (levelName.equals("level2")) {
            img = new GreenfootImage("Level 2", 21, Color.BLACK, null); 
        }      
        getBackground().drawImage(img, 71, 21);
    }
}
