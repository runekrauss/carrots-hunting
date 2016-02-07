/**
 * This class moves an NPC to the outside of the field.
 * Furthermore, if the rabbit is beaten by a NPC, the
 * class will stop the game.
 * 
 * @author Rune Krauss
 * @author Steffen Gerken 
 * @author Niels Arbatschat
 */
public class Mover
{
    /**
     * Initial position of actor
     */
    private int initialPosition;
    /**
     * Increment of actor
     */
    private int increment;
    /**
     * Direction of rotation regarding the actor
     */
    private int directionOfRotation;
    /**
     * Rotation of an actor
     */
    private int rotation;
    /**
     * Name of the grid
     */
    private final String grid = "Grid";
    
    /**
     * Constructor for objects of class Mover
     */
    public Mover(int initialPosition, int increment, int directionOfRotation)
    {
       this.initialPosition = initialPosition;
       this.increment = increment;
       this.directionOfRotation = directionOfRotation;
    }
    
    /**
     * Throws the rabbit from field
     * @param rabbit Rabbit
     */
    public void throwFromField(Rabbit rabbit)
    {
        rabbit.setCellLocation(initialPosition, -1);
        rabbit.setRotation(directionOfRotation);
        rabbit.getSystem().playSound("plop.wav");
        rabbit.getSystem().playSound("gameover.wav");
        rabbit.getSystem().stop();
        System.out.println("Rabbit was removed...");
        System.out.print("Gameover...");
    }  
     /**
     * Throws the archer from field
     * @param archer Archer
     */
    public void throwFromField(Archer archer)
    {
        archer.getSystem().playSound("plop.wav");
        archer.setCellLocation(initialPosition, -1);
        archer.setRotation(directionOfRotation);
        initialPosition = initialPosition + increment;
        System.out.println("Archer was removed...");
    }
    /**
     * Checks whether grid is here
     * @param actor
     *      Specific actor like the rabbit
     * @param grid
     *      Specific grid like "ne" for north east
     */
    public boolean isGridHere(PI1GameActor actor, final String grid) {
        switch (grid) {
            case "se":
                rotation = 0;
                break;
            case "sw":
                rotation = 1;
                break;
            case "nw":
                rotation = 2;
                break;
            case "ne":
                rotation = 3;
                break;
            default:
                throw new IllegalArgumentException("Invalid grid: " + grid);
        }
        for (int i = 0; i < actor.getObjectsHere(this.grid).size(); i++) {
            if (actor.getObjectsHere(this.grid).get(i).getRotation() == rotation) {
                return true;
            }
        }
        return false;
    }
}
