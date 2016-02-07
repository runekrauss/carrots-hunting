import greenfoot.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Ein <code>Actor</code> ("Akteur") ist ein Objekt, welches in der Greenfoot-Welt existiert.
 * Jeder <code>Actor</code> hat in der Welt eine Position (x und y Koordinate) und eine 
 * Darstellung in Form eines Bildes.
 *
 * <p>Von der Klasse <code>PI1GameActor</code> kann kein Objekt erstellt werden.
 * Sie dient als abstrakte Oberklasse, von der speziellere Unterklassen abgeleitet werden 
 * können. Jedes Objekt, welches in der Welt erscheinen soll, muss von dieser Klasse
 * abgeleitet werden. Die Unterklassen können ihre eigene Erscheinung und ihr Verhalten dann
 * selber festlegen.</p>
 *
 * <p>Die wichtigste Eigenschaft dieser Klasse ist die <code>act</code> Methode. Diese 
 * Methode wird automatisch aufgerufen, wenn in der Greenfoot-Entwicklungsumgebung der Button
 * "Act" oder "Run" gedrückt wird. Die <code>act</code> Methode ist zunächst leer und hat 
 * daher keine Auswirkungen. Sie sollte aber in Unterklassen überschrieben werden, um die 
 * Aktionen des Objektes zu implementieren.</p>
 *
 * <p>Diese Klasse ist die Basis für Akteure, die in einer isometrischen Perspektive 
 * dargestellt werden. Sie rastet Objekte automatisch auf ein isometrisches Gitter ein und
 * passt die Zeichenreihenfolge so an, dass Verdeckungen richtig dargestellt werden. Dadurch
 * wird die Welt zu einem Raster von Zellen.</p>
 * 
 * <p>Diese Klasse nutzt auch einige Konventionen: Endet der Dateiname des Bildes zu einem 
 * Akteur auf "-se.png", wird beim Drehen des Akteurs erwartet, dass es auch Bilder mit
 * gleichem Dateinamen, aber den Endungen "-sw.png", "-nw.png" und "-ne.png" gibt. Diese
 * werden dann automatisch bei einer Drehung in die entsprechende Richtung verwendet. Bei
 * Akteuren, die sich drehen, sollte man deshalb entweder Graphiken mit allen vier Endungen
 * bereitstellen oder nur eine einzige, die keine dieser Endungen verwendet. Für passive
 * Akteure (Gitter, Wände usw.) ist das egal. Kommt im Dateinamen eines Bildes "floor" vor, 
 * wird angenommen, dass es sich um ein Bodenelement handelt, das im Hintergrund gezeichnet
 * werden muss. Dies kann auch mit <code>setIsFloor</code> manuell gesetzt werden.</p>
 * 
 * @see PI1GameWorld
 * @author Thomas Röfer
 * @version 1.0
 */
public abstract class PI1GameActor extends Actor
{
    /** Dateierweiterungen für Bilder, um verschiedene Drehrichtungen darzustellen. */
    private static final String[] EXTENSIONS = {"-se.png", "-sw.png", "-nw.png", "-ne.png"};
    
    /** In welche Zellrichtung ist "Vorne" für die vier möglichen Rotationen (x-Koordinaten)? */
    private static final int[] STEPS_X = {1, 0, -1, 0}; 

    /** In welche Zellrichtung ist "Vorne" für die vier möglichen Rotationen (y-Koordinaten)? */
    private static final int[] STEPS_Y = {0, 1, 0, -1};
    
    /** Lässt <code>setLocation</code> Akteure auf feste Zellenkoordinaten einrasten? */
    private static boolean snapToGrid = true;

    /** Die horizontale Position des Ursprungs dieses Akteurs in Pixeln. */
    private int originX;

    /** Die vertikale Position des Ursprungs dieses Akteurs in Pixeln. */
    private int originY;

    /** Die Rotation dieses Akteurs im Bereich 0 (Südost) bis 3 (Nordost). */
    private int rotation = 0;
    
    /** Ist dieser Akteur Teil des Bodens? Dann wird er vor allen anderen Objekten gezeichnet. */
    private boolean isFloor;
    
    /**
     * Konstruiert einen neuen Akteur.
     * Setzt die Standardwerte für den Ursprung und ob der Akteur Teil des Bodens ist.
     */
    PI1GameActor()
    {
        originX = getImage().getWidth() / 2;
        originY = getImage().getHeight() / 2;
        isFloor = getImage().toString().contains("floor");
    }
    
    /**
     * Liefert die Systemschnittstelle von Greenfoot zurück. Darüber können z.B.
     * die Tastatur und die Maus abgefragt, Sounds abgespielt und die Welt 
     * ausgetauscht werden.
     * 
     * @return Ein Objekt, über das mit der Greenfoot-Umgebung kommuniziert 
     *         werden kann.
     */
    public GreenfootSystem getSystem()
    {
        return GreenfootSystem.getInstance();
    }
    
    /**
     * Gibt eine Referenz auf die Welt zurück, in der sich das Objekt befindet.
     *
     * @return Die (Greenfoot) Welt.
     */
    public PI1GameWorld getWorld()
    {
        return (PI1GameWorld) super.getWorld();
    }
    
    /**
     * Liefert die Koordinate der Zellenposition auf der Nordwest-Südost-Achse.
     * Negative Koordinaten sind nordwestlicher als der Ursprung in der Welt, positive
     * südöstlicher.
     * 
     * @throws IllegalStateException Falls das Objekt noch nicht in die Welt eingefügt worden ist.
     */
    public int getX()
    {
        return calcCellLocation(getWorldX(), getWorldY())[0];
    }
    
    /**
     * Liefert die Koordinate der Zellenposition auf der Nordost-Südwest-Achse.
     * Negative Koordinaten sind nordöstlicher als der Ursprung in der Welt, positive
     * südwestlicher.
     * 
     * @throws IllegalStateException Falls das Objekt noch nicht in die Welt eingefügt worden ist.
     */
    public int getY()
    {
        return calcCellLocation(getWorldX(), getWorldY())[1];
    }
    
    /**
     * Setzt das Objekt auf eine neue Position in Pixelkoordinaten.
     * Diese Methode wird von Greenfoot selbst verwendet, wenn Objekte mit der Maus platziert
     * werden und wenn ein Objekt zur Welt hinzugefügt wird. Daher muss sie weiterhin in
     * Pixelkoordinaten arbeiten. Zum Platzieren in Zellenkoordinaten kann die Methode
     * <code>setCellLocation</code> verwendet werden.
     * 
     * <p>Diese Implementierung rastet das platzierte Objekt auf dem isometrischen Raster ein und
     * sortiert es in die Liste der gezeichneten Objekte so ein, dass Verdeckungen richtig
     * dargestellt werden.</p>
     *
     * @param x                      Horizontale Position in der Welt in Pixeln.
     * @param y                      Vertikale Position in der Welt in Pixeln.
     * @throws IllegalStateException Falls das Objekt noch nicht in die Welt eingefügt worden ist.
     */
    @SuppressWarnings("unchecked")
    public void setLocation(final int x, final int y)
    {
        final int prevX = getWorldX();
        final int prevY = getWorldY();
        
        if (snapToGrid) {
            final int[] cellLocation = calcCellLocation(x, y);
            final int[] worldLocation = calcWorldLocation(cellLocation[0], cellLocation[1]);
            super.setLocation(worldLocation[0], worldLocation[1]);
        }
        else {
            super.setLocation(x, y);
        }
        
        // Wenn sie sich geändert haben, muss dieser Aktor neu einsortiert werden, damit Verdeckung richtig
        // funktioniert. Diese Bedingung ist auch wichtig, weil der nachfolgende Code diese Methode indirekt
        // wieder aufruft. Ohne diese Abfrage gäbe es eine Endlosrekursion. 
        if (getWorld().getMaintainPaintOrder() && (getWorldX() != prevX || getWorldY() != prevY)) {
            
            // Liest für Aktoren, die neu wieder eingefügt werden müssen
            final List<PI1GameActor> toReadd = new ArrayList<PI1GameActor>();
            
            // Dieses Element muss auf jeden Fall neu einsortiert werden. Da sonst nur Objekte neu
            // eingefügt werden, die davor gezeichnet werden müssen, muss es das erste sein.
            if (getDepth() > Integer.MIN_VALUE) {
                toReadd.add(this);
            }
            
            // Durchlaufe alle Aktoren und lösche diejenigen aus der Liste, die hinter diesem Aktor angezeigt
            // werden sollen. Übrig bleiben die Aktoren, die entfernt und hinter diesem Aktor wieder hinzugefügt
            // werden müssen. Referenzpunkt jedes Aktors ist wieder seine untere Kante. Bei Aktoren mit gleicher
            // unterer Kante werden größere Aktoren nach vorne gestellt, damit Bodenplatten verdeckt werden.
            final List<PI1GameActor> actors = getWorld().getObjects(PI1GameActor.class);
            for (final PI1GameActor other : actors) {
                if (getDepth() < other.getDepth()) {
                    toReadd.add(other);
                }
            }
            
            // Lösche danach alle Aktoren, die davor angezeigt werden müssen, und füge sie wieder ein.
            final World world = getWorld();
            
            // Koordinaten merken, da sie nur innerhalb der Welt abgefragt werden können.
            final int worldX[] = new int[toReadd.size()];
            final int worldY[] = new int[toReadd.size()];
            for (int i = 0; i < toReadd.size(); ++i) {
                worldX[i] = toReadd.get(i).getWorldX();
                worldY[i] = toReadd.get(i).getWorldY();
            }
            
            // Alle Aktoren löschen, die neu eingetragen werden müssen
            world.removeObjects(toReadd);

            // Das Einrasten auf feste Zellen wird deaktiviert, damit sich an der Position der Aktoren
            // nichts ändert.
            final boolean snap = snapToGrid;
            snapToGrid = false;
            
            // Neu einfügen
            for (int i = 0; i < toReadd.size(); ++i) {
                world.addObject(toReadd.get(i), worldX[i], worldY[i]);
            }
            
            // Einrasten wieder auf alten Zustand setzen
            snapToGrid = snap;
        }
    }
    
    /**
     * Setzt die Rotation des Akteurs.
     * In der isometrischen Welt gibt es nur vier Rotationen. Im Uhrzeigersinn:
     * 0 = Südost, 1 = Südwest, 2 = Nordwest, 3 = Nordost.
     * Nach einer Heuristik, die am Anfang dieser Datei beschrieben ist, werden
     * für verschiedene Richtungen unterschiedlich Bilder verwendet.
     * 
     * @param rotation Die Rotation. Sie wird automatisch in den Bereich 0..3 bewegt.
     */
    public void setRotation(final int rotation)
    {
        // In Bereich 0..3 bewegen
        final int normalizedRotation = rotation & 3;
        
        // Wenn sich Richtung wirklich geändert hat, Bild austauschen, wenn welche pro Richtung existieren.
        if (this.rotation != normalizedRotation) {
            final String[] parts = getImage().toString().split(EXTENSIONS[this.rotation]);
            
            // Wenn Endung zu bisheriger Richtung gefunden wurde, sollte es auch Bilder
            // für andere Richtungen geben.
            if (parts.length > 1) {
                final String baseName = parts[0].split(" ")[3];
                setImage(baseName + EXTENSIONS[normalizedRotation]);
            }
        }

        // Neuen Wert übernehmen.
        this.rotation = normalizedRotation;
    }
    
    /** Liefert die Rotation im Bereich 0 (Südost) bis 3 (Nordost). */
    public int getRotation()
    {
        return rotation;
    }
    
    /**
     * Bewegt den Akteur um eine ganze Anzahl von Zellen nach vorne.
     * Vorne ist die Richtung, in die seine Rotation zeigt.
     * 
     * @param distance Die Anzahl an Zellen, die der Akteur bewegt wird.
     * @throws IllegalStateException Falls das Objekt noch nicht in die Welt eingefügt worden ist.
     */
    public void move(final int distance)
    {
        move((double) distance);
    }
    
    /**
     * Bewegt den Akteur um eine Anzahl von Zellen nach vorne.
     * Vorne ist die Richtung, in die seine Rotation zeigt.
     * Die Anzahl von Zellen kann auch eine reelle Zahl sein, muss aber groß
     * genug sein, so dass sich der Akteur dadurch tatsächlich ein Stück auf
     * dem Bildschirm bewegt. Es wird versucht, Ungenauigkeiten von 
     * Fließkommaarithmetik auszugleichen.
     * 
     * @param distance               Die Anzahl an Zellen, die der Akteur bewegt wird.
     * @throws IllegalStateException Falls das Objekt noch nicht in die Welt eingefügt worden ist.
     */
    public void move(final double distance)
    {
        final int[] cellLocation = calcCellLocation(getWorldX(), getWorldY());
        final int[] worldLocation = calcWorldLocation(cellLocation[0], cellLocation[1]);
        final int[] targetWorldLocation = calcWorldLocation(cellLocation[0] + STEPS_X[rotation],
                cellLocation[1] + STEPS_Y[rotation]);
        final int stepX = targetWorldLocation[0] - worldLocation[0];
        final int stepY = targetWorldLocation[1] - worldLocation[1];
        
        final double factor;
        if (worldLocation[0] != getWorldX() && distance != 0) {
            final int diffX = getWorldX() - worldLocation[0];
            final int diffY = getWorldY() - worldLocation[1];
            final double steps = Math.round((double) diffX * Math.signum(stepX) * 2 / ((double) getWorld().getCellWidth() * distance)) + 1;
            factor = steps * distance;
        }
        else {
            factor = distance;
        }
        
        snapToGrid = false;
        setLocation(worldLocation[0] + round((int) (stepX * factor * 2), 2) / 2,
                worldLocation[1] + round((int) (stepY * factor * 2), 2) / 2);
        snapToGrid = true;
    }

    /**
     * Dreht den Akteur. Eine Drehung um 1 bedeutet eine um 90°.
     * 
     * @param direction Um wie viele 90°-Schritte und in welche Richtung soll gedreht werden?
     */
    public void turn(final int direction)
    {
        setRotation(rotation + direction);
    }

    /**
     * Setzt den Ursprung im Bild des Akteurs.
     * Der Ursprung ist der Punkt des Bildes, der in der Mitte einer Zelle angezeigt werden
     * soll. Wird diese Methode nicht verwendet, liegt der Ursprung des Bildes in seiner Mitte.
     * 
     * @param x Die horizontale Position des Ursprungs im Bild in Pixeln.
     * @param y Die vertikale Position des Ursprungs im Bild in Pixeln.
     */
    public void setOrigin(final int x, final int y)
    {
        originX = x;
        originY = y;
    }
    
    /**
     * Setzt, ob dieser Akteur ein Teil des Bodens ist.
     * Teile des Bodens werden vor allen anderen Objekten gezeichnet.
     * Wird diese Methode nicht verwendet, ist ein Akteur Teil des Bodens, wenn der Dateiname
     * des verwendeten Bildes "floor" enthält.
     * 
     * @param isFloor Ist diese Akteur Teil des Bodens?
     */
    public void setIsFloor(boolean isFloor)
    {
        this.isFloor = isFloor;
    }
    
    /**
     * Setzt die Position des Akteurs in Zellenkoordinaten.
     * Zellenkoordinaten werden relativ zu zwei Achsen angegeben: der Nordwest-Südwest-Achse und
     * der Nordost-Südwest-Achse. Die Zelle mit den Koordinaten (0, 0) liegt dort, wo in der Welt 
     * der Ursprung gelegt wurde (mit <code>setOrigin</code> aus <code>PI1GameWorld</code>).
     * 
     * @param x                      Die Koordinate der Zellenposition auf der Nordwest-Südost-Achse
     *                               Negative Koordinaten sind nordwestlicher als der Ursprung in
     *                               der Welt, positive südöstlicher.
     * @param y                      Die Koordinate der Zellenposition auf der Nordost-Südwest-Achse.
     *                               Negative Koordinaten sind nordöstlicher als der Ursprung in der
     *                               Welt, positive südwestlicher.
     * @throws IllegalStateException Falls das Objekt noch nicht in die Welt eingefügt worden ist.
     */
    public void setCellLocation(final int x, final int y)
    {
        final int[] worldLocation = calcWorldLocation(x, y);
        setLocation(worldLocation[0], worldLocation[1]);
    }
    
    /**
     * Testet, ob ein Objekt einer bestimmten Klasse in der gleichen Zelle vorhanden ist,
     * wie der Akteur selbst.
     * 
     * @param cls                    Die Klasse des Objekts, nach dem gesucht wird. Eine leere
     *                               Zeichenkette sucht nach allen Klassen.
     * @return                       Wurde hier ein solches Objekt gefunden?
     * @throws IllegalStateException Falls das Objekt noch nicht in die Welt eingefügt worden ist.
     */
    public boolean isObjectHere(final String cls)
    {
        return isObjectAt(0, 0, cls);
    }
    
    /**
     * Testet, ob ein Objekt einer bestimmten Klasse in einer Zelle relativ zum Akteur vorhanden
     * ist. Dabei wird die Rotation des Akteurs beachtet.
     * 
     * @param cls                    Die Klasse des Objekts, nach dem gesucht wird. Eine leere
     *                               Zeichenkette sucht nach allen Klassen.
     * @param cellsInFront           Wie viele Zellen vor dem Akteur?
     * @param cellsToRight           Wie viele Zellen rechts von ihm?
     * @return                       Wurde hier ein solches Objekt gefunden?
     * @throws IllegalStateException Falls das Objekt noch nicht in die Welt eingefügt worden ist.
     */
    public boolean isObjectAt(final int cellsInFront, final int cellsToRight, final String cls)
    {
        return !getObjectsAt(cellsInFront, cellsToRight, cls).isEmpty();
    }
    
    /**
     * Liefert alle Objekte einer bestimmten Klasse, die in derselben Zelle wie der Akteur 
     * vorhanden sind.
     * 
     * @param cls                    Die Klasse der Objekte, nach denen gesucht wird. Eine leere
     *                               Zeichenkette sucht nach allen Klassen.
     * @return                       Die Liste aller gefundenen Objekte.
     * @throws IllegalStateException Falls das Objekt noch nicht in die Welt eingefügt worden ist.
     */
    public List<Actor> getObjectsHere(final String cls)
    {
        return getObjectsAt(0, 0, cls);
    }    
    
    /**
     * Liefert alle Objekte einer bestimmten Klasse, die in einer Zelle relativ zum Akteur 
     * vorhanden sind.
     * 
     * @param cls                    Die Klasse der Objekte, nach denen gesucht wird. Eine leere
     *                               Zeichenkette sucht nach allen Klassen.
     * @param cellsInFront           Wie viele Zellen vor dem Akteur?
     * @param cellsToRight           Wie viele Zellen rechts von ihm?
     * @return                       Die Liste aller gefundenen Objekte.
     * @throws IllegalStateException Falls das Objekt noch nicht in die Welt eingefügt worden ist.
     */
    @SuppressWarnings("unchecked")
    public List<Actor> getObjectsAt(final int cellsInFront, final int cellsToRight, final String cls)
    {
        if (getWorld() == null) {
            throw new IllegalStateException("isHere/isAt: Methoden können nur verwendet werden, wenn das Objekt Teil einer Welt ist.");
        }
        else {
            final int testX = getX() + STEPS_X[rotation] * cellsInFront - STEPS_Y[rotation] * cellsToRight;
            final int testY = getY() + STEPS_X[rotation] * cellsToRight + STEPS_Y[rotation] * cellsInFront;
            final List<Actor> found = new ArrayList<Actor>();
            try {
                List<Actor> actors = getWorld().getObjects(cls.equals("") ? null : (Class<Actor>) Class.forName(cls));
                for (Actor actor : actors) {
                    if (testX == actor.getX() && testY == actor.getY()) {
                        found.add(actor);
                    }
                }
            }
            catch (ClassNotFoundException e) {
                System.err.println("isAt: Es gibt keine Klasse mit dem Namen '" + cls + "'.");
                getSystem().stop();
            }
            return found;
        }
    }
    
    /**
     * Rundet eine ganze Zahl auf eine bestimmte Schrittweite (interne Methode).
     * 
     * @param value       Die Zahl, die gerundet wird.
     * @param granularity Die Zahl wird auf Vielfaches dieser Zahl gerundet.
     * @return            Die gerundete Zahl.
     */
    private int round(final int value, final int granularity)
    {
        final int offset;
        if (value < 0) {
            offset = -granularity / 2;
        } else {
            offset = granularity / 2;
        }
        
        return (value + offset) / granularity * granularity;
    }
    
    /**
     * Bestimmt aus Weltkoordinaten die dazu gehörigen Zellenkoordinaten (interne Methode).
     * 
     * @param x Die horizontale Koordinate in der Welt in Pixeln.
     * @param y Die vertikale Koordinate in der Welt in Pixeln.
     * @return  Die dazu gehörigen Zellenkoordinaten (x an Index 0, y an Index 1).
     */
    private int[] calcCellLocation(final int x, final int y)
    {
        final int cellWidth = getWorld().getCellWidth();
        final int cellHeight = getWorld().getCellHeight();
        final int cellArea = cellWidth * cellHeight;
        final int worldX = x - getImage().getWidth() / 2 + originX - getWorld().getOriginX();
        final int worldY = y - getImage().getHeight() / 2 + originY - getWorld().getOriginY();
            
        // Auf diagonale Koordinaten umrechnen und auf Kachelabstand runden. 
        // x nach Südost, y nach Südwest.
        final int cellX = round(worldY * cellWidth + worldX * cellHeight, cellArea);
        final int cellY = round(worldY * cellWidth - worldX * cellHeight, cellArea);
        
        return new int[]{cellX / cellArea, cellY / cellArea};
    }
    
    /**
     * Bestimmt aus Zellenkoordinaten die dazu gehörigen Weltkoordinaten (interne Methode).
     * 
     * @param x Die Koordinate der Zellenposition auf der Nordwest-Südost-Achse.
     * @param y Die Koordinate der Zellenposition auf der Nordost-Südwest-Achse.
     * @return  Die dazu gehörigen Weltkoordinaten in Pixeln (x an Index 0, y an Index 1).
     */
    private int[] calcWorldLocation(final int x, final int y)
    {
        return new int[]{
            getWorld().getOriginX() + ((x - y) * getWorld().getCellWidth() + getImage().getWidth()) / 2
            - originX,
            getWorld().getOriginY() + ((x + y) * getWorld().getCellHeight() + getImage().getHeight()) / 2
            - originY};
    }
    
    /** Liefert die horizontale Position des Akteurs in der Welt in Pixeln (interne Methode). */
    private int getWorldX()
    {
        return super.getX();
    }

    /** Liefert die vertikale Position des Akteurs in der Welt in Pixeln (interne Methode). */
    private int getWorldY()
    {
        return super.getY();
    }

    /**
     * Liefert ein Maß für die Tiefe des Akteurs in Pixeln (interne Methode).
     * Die Methode wird benutzt, um die Akteure so zu ordnen, dass sie in der richtigen
     * Reihenfolge gezeichnet werden.
     */
    private int getDepth()
    {
        if (isFloor) {
            return Integer.MIN_VALUE;
        }
        else {
            return getWorldY() - getImage().getHeight() / 2 + originY;
        }
    }
    
    /**
     * Sortiert alle Akteure entsprechend ihrer Tiefe neu ein, so dass Verdeckungen
     * richtig gezeichnet werden. Diese Methode wird von <code>PI1GameWorld</code>
     * verwendet.
     * 
     * @param world Die Welt, deren Akteure sortiert werden.
     */
    static void sortActorsByDepth(final PI1GameWorld world)
    {
        final List<Actor> actors = (List<Actor>) world.getObjects("PI1GameActor");

        Collections.sort(actors, new Comparator<Actor>() {
            public int compare(Actor a1, Actor a2)
            {
                final int depth1 = ((PI1GameActor) a1).getDepth();
                final int depth2 = ((PI1GameActor) a2).getDepth();
                return depth1 < depth2 ? -1 : depth1 > depth2 ? 1 : 0;
            }
        });

        // Koordinaten merken, da sie nur innerhalb der Welt abgefragt werden können.
        final int worldX[] = new int[actors.size()];
        final int worldY[] = new int[actors.size()];
        for (int i = 0; i < actors.size(); ++i) {
            final PI1GameActor actor = (PI1GameActor) actors.get(i);
            worldX[i] = actor.getWorldX();
            worldY[i] = actor.getWorldY();
        }
        
        // Alle löschen
        world.removeObjects(actors);
        
        // Das Einrasten auf feste Zellen wird deaktiviert, damit sich an der Position der Aktoren
        // nichts ändert.
        final boolean snap = snapToGrid;
        snapToGrid = false;
        
        // Wieder einfügen
        for (int i = 0; i < actors.size(); ++i) {
            final PI1GameActor actor = (PI1GameActor) actors.get(i);
            world.addObject(actor, worldX[i], worldY[i]);
        }
        
        // Einrasten wieder auf alten Zustand setzen
        snapToGrid = snap;
    }
}
