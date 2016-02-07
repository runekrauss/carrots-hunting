import java.io.*;

/**
 * Level 1 of this game.
 * 
 * @author Rune Krauss
 * @author Steffen Gerken
 * @author Niels Arbatschat
 */
public class Level2 extends Level
{
    /**
     * Constructor for objects of class Level2
     */
    public Level2(String fileName) throws FileNotFoundException
    {
        super(fileName);
    }
    
    /**
     * Initializes a level based on 3d array.
     * Furthermore, sets the origin and creates the mover for moving objects.
     * 
     * @param field
     *      Specific field to do operations like setting the origin.
     */
    public void init(Field field) throws IllegalArgumentException {
        super.init(field);
        Carrot carrot = new Carrot();
        field.addObject (carrot, 240, 420);
        Carrot carrot2 = new Carrot();
        field.addObject (carrot2, 561, 541);
        Carrot carrot3 = new Carrot();
        field.addObject (carrot3, 81, 181);
        Archer archer = new Archer();
        field.addObject (archer, 80, 540);
        archer.setRotation(3);
        Archer archer2 = new Archer();
        field.addObject (archer2, 161, 241);
        archer2.setRotation(0);
        Rabbit rabbit = new Rabbit();
        field.addObject (rabbit, 400, 300);
        Wolf wolf = new Wolf();
        field.addObject (wolf, 480, 360);
        System.out.println("Level 2 was created...");
    }
}
