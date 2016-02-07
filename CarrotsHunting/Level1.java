import java.io.*;

/**
 * Level 2 of this game.
 * 
 * @author Rune Krauss
 * @author Steffen Gerken
 * @author Niels Arbatschat
 */
public class Level1 extends Level
{
    /**
     * Constructor for objects of class Level1
     */
    public Level1(String fileName) throws FileNotFoundException
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
        field.addObject (carrot2, 480, 480);
        Archer archer = new Archer();
        field.addObject (archer, 80, 540);
        archer.setRotation(3);
        Rabbit rabbit = new Rabbit();
        field.addObject (rabbit, 400, 300);
        Wolf wolf = new Wolf();
        field.addObject (wolf, 480, 360);
        System.out.println("Level 1 was created...");
    }
}
