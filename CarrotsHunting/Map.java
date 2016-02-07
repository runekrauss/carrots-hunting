import java.util.*;

/**
 * Represents the navigation map of this game.
 * If the rabbit visits the wolf, the map will be activated.
 * 
 * @author Rune Krauss 
 * @author Steffen Gerken
 * @author Niels Arbatschat
 */
public class Map
{
    /**
     * Field of this game
     */
    private Field field;
    /**
     * Target of the map regarding x and y
     */
    private CellLocation target;
    /**
     * Mapping from a cell location to rotations
     */
    private HashMap<CellLocation, HashSet<Integer>> map;

    /**
     * Constructor for the map. 
     * Initializes the map.
     * Builds the map and filters all routes.
     */
    public Map(Field field, int targetX, int targetY)
    {
        this.field = field;
        target = new CellLocation(targetX, targetY);
        map = new HashMap<CellLocation, HashSet<Integer>>();
        System.out.println("Navigation map was activated...");
        buildMap();
        filterRoutes();
    }
    
    /**
     * Builds the map based on the grids.
     * Furthermore, encodes directions of movement.
     */
    private void buildMap() {
        List<Grid> grids = field.getObjects(Grid.class);
        for ( Grid grid : grids) {
            int x = grid.getX();
            int y = grid.getY();
            int rotation = grid.getRotation();
            CellLocation cellLocation = new CellLocation(x, y);
            HashSet<Integer> rotations = map.get(cellLocation);
            if (rotations == null) {
                rotations = new HashSet<>();
            }
            rotations.add(rotation);
            map.put(cellLocation, rotations);
        }
    }
    
    /**
     * Visualizes this map with red grids.
     * Iterates the map and adds an object for every 
     * available moving direction of each cell.
     */
    public void visualize() {
        field.removeObjects( field.getObjects(GridRed.class) );
        for ( CellLocation cellLocation : map.keySet() ) {
            for ( int rotation : map.get(cellLocation) ) {
                GridRed gridRed = new GridRed();
                field.addObject(gridRed, 0, 0);
                gridRed.setCellLocation( cellLocation.getX(), cellLocation.getY() );
                gridRed.setRotation(rotation);
            }
        }
    }
    
    /**
     * Filters out all routes.
     * Deletes all outgoing directions of the target cell.
     * Adds the coordinates of the target cell in the queue.
     * Takes the oldest element from the queue.
     * Checks neighbours regarding the possible rotations to the specific cell.
     * Deletes all irrelevant rotations regarding the specific cell and adds the 
     * coordinates of the neighbour cell in the queue.
     * If the queue still contains elements, will takes the oldest element from the queue 
     * and so on.
     */
    private void filterRoutes() {
        LinkedList<CellLocation> linkedList = new LinkedList<>();
        map.remove(target);
        linkedList.add(target);
        while ( !linkedList.isEmpty() ) {
            CellLocation cellLocation = linkedList.poll();
            for (int i = 0; i < 4; i++) {
                CellLocation neighbour = null;
                switch (i) {
                    case 0:
                        neighbour = new CellLocation( cellLocation.getX()+2, cellLocation.getY() );
                        break;
                    case 1:
                        neighbour = new CellLocation( cellLocation.getX(), cellLocation.getY()+2 );
                        break;
                    case 2:
                        neighbour = new CellLocation( cellLocation.getX()-2, cellLocation.getY() );
                        break;
                    case 3:
                        neighbour = new CellLocation( cellLocation.getX(), cellLocation.getY()-2 );
                        break;
                    default:
                        break;
                }
                HashSet<Integer> neighbours = map.get(neighbour);
                if ( map.containsKey(neighbour) && neighbours.contains( (i + 2) % 4 ) ) {
                    neighbours.clear();
                    neighbours.add( (i + 2) % 4 );
                    linkedList.add(neighbour);
                }
            }
        }
    }
    
    /**
     * Returns the movement direction regarding a cell.
     * @param cellLocation
     *      Cell location of an actor.
     * @return Possible rotation respectivelly -1 if no rotation exists.
     */
    public int getMovementDirection(CellLocation cellLocation) {
        if (map.containsKey(cellLocation) && map.get(cellLocation).size() == 1) {
            for ( int rotation : map.get(cellLocation) ) {
                return rotation;
            }
        }
        return -1;
    }
}
