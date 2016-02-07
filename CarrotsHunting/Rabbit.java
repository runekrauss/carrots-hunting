import greenfoot.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the main game actor of this game.
 * 
 * @author Rune Krauss
 * @author Steffen Gerken
 * @author Niels Arbatschat
 */
public class Rabbit extends Character
{   
    /** Excreted archers */
    private int excretedArchers = 0;

    /**
     * If an actor is not in motion, this method will be executed.
     * The same as act().
     */
    public void onAct() {
        if ( getSystem().wasKeyPressed("right") ) {
            moveIfPossible(0, "se", true);
        }
        else if ( getSystem().wasKeyPressed("up") ) {
            moveIfPossible(3, "ne", true);
        }
        else if ( getSystem().wasKeyPressed("left") ) {
            moveIfPossible(2, "nw", true);
        }
        else if ( getSystem().wasKeyPressed("down") ) {
            moveIfPossible(1, "sw", true);
        }      
    }

    /**
     * If a movement is completes, this method will be executed.
     */
    public void onMoved() {
        getSystem().playSound("step.wav");
        Field field = (Field) getWorld();
        if ( isObjectHere("Archer") ) {
            List<Archer> archers = field.getObjects(Archer.class);
            for (Archer archer : archers) {
                if ( getX() == archer.getX() && getY() == archer.getY() ) {
                    kickOut(archer);
                    archer.setOut();
                }
                if (archer.isOut()) {
                    excretedArchers++;
                }
            }
        }
        if (field.isTriggered() && field.getObjects(Archer.class).size() == excretedArchers) {
            field.setTriggered(false);
            System.out.println("Navigation map was deactivated...");
            field.removeObjects( field.getObjects(GridRed.class) );
        }
        if ( isObjectHere("Carrot") ) {
            List<Carrot> carrots = field.getObjects(Carrot.class);
            for (int i = 0; i < carrots.size(); i++) {
                Carrot carrot = carrots.get(i);
                if (getX() == carrot.getX() && getY() == carrot.getY()) {
                    getWorld().removeObject(carrot);
                    carrots.remove(i);
                }
            }
            getSystem().playSound("slurp.wav");
            System.out.println("Carrot was eaten by the rabbit...");
            List<CarrotCounter> carrotCounter = getWorld().getObjects(CarrotCounter.class);
            carrotCounter.get(0).add(1);
        }
        if ( isObjectHere("Wolf") && !field.isTriggered() ) {
            getSystem().playSound("scream.wav");
            Map map = new Map(field, 12, 0);
            field.setMap(map);
            field.setTriggered(true);
        }
        List<Move> moves = getWorld().getObjects(Move.class);
        for (Move move : moves) {
            Archer archer = (Archer) move;
            archer.onPlayerMoved();
        }
        stay();
    }
}