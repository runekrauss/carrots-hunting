import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Super class of movable characters.
 * 
 * @author Rune Krauss
 * @author Steffen Gerken
 * @author Niels Arbatschat
 */
public abstract class Character extends PI1GameActor
{
    /**
     * Status of movement
     */
    private boolean note;

    /**
     * Speed of movement
     */
    private final double SPEED = 0.1;

    /**
     * Counter regarding movement
     */
    private int i;

    /**
     * Status of waiting
     */
    private boolean stay;

    /**
     * Counter regarding waiting
     */
    private int delay;

    /**
     * The actor moves if possible.
     * 
     * @param direction 
     *      Specific direction like 0 for south east.
     * @param grid 
     *      checks matching fields regarding actors.    
     */
    public void moveIfPossible(final int direction, final String grid, final boolean sound) {
        Field field = (Field) getWorld();
        Mover mover = ( (Field) getWorld() ).getLevel().getMover();
        if (mover.isGridHere(this, grid)) {
            setRotation(direction);
            move();
        } else if (sound) {
            getSystem().playSound("au.wav");
        } else {
            return;
        }
    }
    
    /**
     * Kicks out the actor.
     */
    public void kickOut(Character character) {
        Mover mover = ( (Field) getWorld() ).getLevel().getMover();
        if (character instanceof Archer) {
            Archer archer = (Archer) character;
            mover.throwFromField(archer);
        } else if (character instanceof Rabbit) {
            Rabbit rabbit = (Rabbit) character;
            mover.throwFromField(rabbit);
        }
    }

    /**
     * Tells the object to move one step, but it only notices.
     * This method is not executed immediately.
     */
    public void move() {
        note = true;
        i = 0;
    }

    /**
     * Tells the object to do nothing while the duration of a step.
     */
    public void stay() {
        stay = true;
        delay = 20;
    }

    /**
     * If an actor is not in motion, this method will be executed.
     * The same as act().
     */
    public abstract void onAct();

    /**
     * If a movement is completes, this method will be executed.
     */
    public abstract void onMoved();

    /**
     * Distinguishes two cases regarding the status.
     * If an actor is not in motion, calls onAct().
     * If an actor is in motion after move(), an actor moves forward a little bit.
     * If a movement is completes, calls onMoved().
     */
    public final void act() {
        if (note && !stay) {
            if (i < 20) {
                move(SPEED);
                i++;
            } else {
                onMoved();
                note = false;
            }
        } else {
            if (stay) {
                if (delay > 0) {
                    delay--;
                } else {
                    stay = false;
                }
            } else {
                onAct();
            }
        }
    }
}
