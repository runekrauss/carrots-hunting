import greenfoot.*;
import java.util.List;

/**
 * The archer is the enemy of the rabbit.
 * It tries to kill the rabbit.
 * 
 * @author Rune Krauss
 * @author Steffen Gerken
 * @author Niels Arbatschat
 */
public class Archer extends Character implements Move
{
    /**
     * Checks whether the archer is out.
     */
    private boolean out;

    /**
     * Distance regarding movement pattern.
     */
    private final int CELL_DISTANCE = 2;

    /**
     * Ringbuffer regarding coord x.
     */
    private RingBuffer bufferX;

    /**
     * Ringbuffer regarding coord y.
     */
    private RingBuffer bufferY;

    /**
     * Rotation of the archer.
     */
    private int rotation = -1;
    
    /**
     * Status of the moved player.
     */
    private boolean playerMoved;

    /**
     * Constructor of the archer.
     */
    public Archer() {
        bufferX = new RingBuffer(CELL_DISTANCE + 1);
        bufferY = new RingBuffer(CELL_DISTANCE + 1);
    }

    /**
     * If an actor is not in motion, this method will be executed.
     * The same as act().
     */
    public void onAct() {
        Field field = (Field) getWorld();
        if (playerMoved) { 
            if ( !field.isTriggered() ) {
                if (!out) {
                    if (bufferX.size() > 0) {
                        addToTrail();
                        followTrail();
                    } else {
                        rotation = getRandomNumber(4);
                        switch (rotation) {
                            case 0:
                            moveIfPossible(rotation, "se", false);
                            break;
                            case 1:
                            moveIfPossible(rotation, "sw", false);
                            break;
                            case 2:
                            moveIfPossible(rotation, "nw", false);
                            break;
                            case 3:
                            moveIfPossible(rotation, "ne", false);
                            break;
                            default:
                            throw new IllegalArgumentException("Invalid rotation: " + rotation);
                        }
                    }
                }
            } else {
                Map map = field.getMap();
                CellLocation cellLocation = new CellLocation( getX(), getY() );
                rotation = map.getMovementDirection(cellLocation);
                if (rotation != -1) {
                    setRotation(rotation);
                    move();
                }
            }
        }
        playerMoved = false;
    }

    /**
     * If a movement is completes, this method will be executed.
     */
    public void onMoved() {
        Field field = (Field) getWorld();
        if ( isObjectHere("Rabbit") ) {
            List<Rabbit> rabbits = field.getObjects(Rabbit.class);
            for (Rabbit rabbit : rabbits) {
                kickOut(rabbit);
            }
        }
        if ( field.isTriggered() ) {
            if ( isObjectHere("Wolf") ) {
                bufferX.clear();
                bufferY.clear();
                field.setTriggered(false);
                System.out.println("Navigation map was deactivated...");
                field.removeObjects( field.getObjects(GridRed.class) );
            }
        }
        if ( checkForRabbit() ) {
            System.out.println("Movement pattern was activated...");
        }
    }

    /**
     * Gets random number for moving.
     * 
     * @param number 
     *      Number for creating a random number. 
     */
    private int getRandomNumber(int number) {
        return getSystem().getRandomNumber(number);
    }

    /**
     * Follows the trail.
     */
    public void followTrail()
    {
        assert bufferX.size() == bufferY.size() : "Ringbuffer must be the same regarding elements!";
        assert bufferX.size() >= 2 : "Ringbuffer contains at least two entries!";

        setCellLocation( bufferX.pop(), bufferY.pop() );
        if (bufferX.peek() - getX() > 0) {
            setRotation(0);
        }
        else if (bufferY.peek() - getY() > 0) {
            setRotation(1);
        }
        else if (bufferX.peek() - getX() < 0) {
            setRotation(2);
        }
        else {
            setRotation(3);
        }
    }

    /**
     * Adds rabbits current position to the trail.
     */
    public void addToTrail()
    {
        final List<Actor> rabbits = getWorld().getObjects("Rabbit");
        assert rabbits.size() == 1 : "There must exist one rabbit exactly!";
        final Actor rabbit = rabbits.get(0);
        bufferX.push( rabbit.getX() );
        bufferY.push( rabbit.getY() );
    }

    /**
     * Checks objects at a specific location.
     */
    private boolean isObjectAt(final int cellsInFront, final int cellsToRight, 
    final String name, final int rotation)
    {
        for (final Actor actor : getObjectsAt(cellsInFront, cellsToRight, name)) {
            if (actor.getRotation() == rotation) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks for a rabbit.
     * @return Status of the rabbit.
     */
    public boolean checkForRabbit()
    {
        for (int distance = 1; distance <= CELL_DISTANCE && 
        isObjectAt( (distance - 1) * 2, 0, "Grid", getRotation() ); ++distance) {
            final List<Actor> rabbits = getObjectsAt(distance * 2, 0, "Rabbit");
            if ( !rabbits.isEmpty() ) {
                assert rabbits.size() == 1 : "There must be only one rabbit!";
                final Actor rabbit = rabbits.get(0);
                for (int i = 1; i <= distance; ++i) {
                    bufferX.push(getX() + (rabbit.getX() - getX()) * i / distance);
                    bufferY.push(getY() + (rabbit.getY() - getY()) * i / distance);
                }
                return true;
            }
        }
        return false;
    }
    
    /**
     * Sets out the archer.
     */
    public void setOut() {
        out = true;
    }
    
    /**
     * Gets status of the archer.
     */
    public boolean isOut() {
        return out;
    }
    
    /**
     * Method is executed after a player moved.
     */
    public void onPlayerMoved() {
        playerMoved = true;
    }
}
