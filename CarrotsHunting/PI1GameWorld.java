import greenfoot.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Die graphische Darstellung des Programms und alle Akteure (Actors) finden 
 * in der Welt statt. Sie stellt die "Bühne" für alle Aktionen dar. Dies ist
 * die Basisklasse für die Level des PI-1-Spiels. Sie bietet Methoden für die
 * Erstellung eines Spiels in isometrischer Ansicht an, die von der Klasse
 * PI1GameActor genutzt werden. Insbesondere liefert sie Informationen über
 * die Breite und Höhe der verwendeten Zellen und wo der Ursprung des
 * Spielfeldes ist.
 * 
 * <p>Es gibt auch Alternativ-Implementierungen für einige der Methoden der
 * Klasse World. Dies sind aber viel weniger, als in den Beispielen zur
 * Vorlesung, weil viele der ursprünglichen Methoden aus World für eine
 * isometrische Perspektive keine sinnvolle Anwendung haben und insofern
 * ohnehin nicht benutzt werden können. Letzteres bezieht sich insbesondere
 * auf Methoden zum Finden von benachbarten Objekten und zum Setzen der 
 * Zeichenreihenfolge.</p>
 * 
 * @see PI1GameActor
 * @author Thomas Röfer
 * @version 1.0
 */
public abstract class PI1GameWorld extends World
{
    /** Die Breite einer Zelle in Pixeln. */
    private final int cellWidth;

    /** Die Höhe einer Zelle in Pixeln. */
    private final int cellHeight;

    /** Die horizontale Position des Ursprungs in der Welt in Pixeln. */
    private int originX;

    /** Die vertikale Position des Ursprungs in der Welt in Pixeln. */
    private int originY;
    
    /** Sollen Objekte entsprechend ihrer Entfernung vom Betrachter eingefügt werden? */
    private boolean maintainPaintOrder = true;

    /**
     * Erzeugt eine neue Welt für eine isometrische Perspektive.
     *
     * @param worldWidth  Die Breite der Welt in Pixeln.
     * @param worldHeigth Die Höhe der Welt in Pixeln.
     * @param cellWidth   Die Breite einer Zelle in Pixeln.
     * @param cellHeight  Die Höhe einer Zelle in Pixeln.
     */
    public PI1GameWorld(final int worldWidth, final int worldHeight, 
            final int cellWidth, final int cellHeight)
    {
        super(worldWidth, worldHeight, 1, false);
        
        this.cellWidth = cellWidth;
        this.cellHeight = cellHeight;
        originX = worldWidth / 2;
        originY = worldHeight / 2;
        
        // Erzwinge, dass für die Ausführungsreihenfolge der Aktoren eine neue Liste
        // erzeugt wird. Dies passiert, wenn die Standardliste für die Zeichenreihenfolge
        // genutzt wird.
        setPaintOrder(Actor.class);
    }
    
    /**
     * Diese Methode wird in jedem Simulationsschritt einmal ausgeführt.
     * Wenn sie überschrieben wird, muss die Methode der Basisklasse aufgerufen werden.
     */
    public void act()
    {
        getSystem().updateKeyPressed();
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
    
    /** Liefert die Breite einer Zelle in Pixeln. */
    public int getCellWidth()
    {
        return cellWidth;
    }

    /** Liefert die Höhe einer Zelle in Pixeln. */
    public int getCellHeight()
    {
        return cellHeight;
    }
    
    /**
     * Setzt den Ursprung der Welt.
     * Dies ist der Punkt, wo der Ursprung der Zelle mit den Zellenkoordinaten
     * (0, 0) liegt.
     * Wird diese Methode nicht verwendet, liegt der Ursprung der Welt genau in
     * der Mitte.
     * @param x Die horizontale Position des Ursprungs in der Welt in Pixeln.
     * @param y Die vertikale Position des Ursprungs in der Welt in Pixeln.
     */
    public void setOrigin(final int x, final int y)
    {
        originX = x;
        originY = y;
    }
    
    
    /** Die horizontale Position des Ursprungs in der Welt in Pixeln. */
    public int getOriginX()
    {
        return originX;
    }
    
    /** Die vertikale Position des Ursprungs in der Welt in Pixeln. */
    public int getOriginY()
    {
        return originY;
    }
    
    /**
     * Aktivieren oder Deaktivieren des Einsortierens von Objekten entsprechend
     * ihrer Entfernung vom Betrachter. Das Abschalten kann das Einfügen neuer Objekte
     * deutlich beschleunigen. Allerdings sollte es auf jeden Fall wieder aktiviert
     * werden, bevor Objekte wirklich gezeichnet werden.
     * 
     * @param maintainPaintOrder Sollen Objekte entsprechend ihrer Entfernung einsortiert werden?
     */
    public void setMaintainPaintOrder(boolean maintainPaintOrder)
    {
        if (!this.maintainPaintOrder && maintainPaintOrder) {
            PI1GameActor.sortActorsByDepth(this);
        }
        this.maintainPaintOrder = maintainPaintOrder;
    }
    
    /** Liefert, ob Objekte entsprechend ihrer Entfernung einsortiert werden sollen. */
    public boolean getMaintainPaintOrder()
    {
        return maintainPaintOrder;
    }
    
    /**
     * Setzt die Reihenfolge, in der die <code>act</code> Methode der Objekte aufgerufen werden.
     * Die Reihenfolge wird in Klassen angegeben. Objekte einer Klasse agieren immer vor Objekte 
     * einer anderen Klasse. Die Reihenfolge der Objekte einer gleichen Klasse kann nicht beeinflusst
     * werden. Objekte von Klassen, die als erstes in der Parameterliste aufgelistet werden, agieren
     * eher als Objekte der Klassen, die später aufgelistet werden.
     * Objekte der Klassen, die nicht ausdrücklich genannt werden, erben die Reihenfolge ihrer Oberklassen.
     * Objekte der Klassen, die nicht aufgelistet sind, agieren nach den Objekten der angegebenen Klassen.
     *
     * @param classes Die Klassen in gewünschter Reihenfolge. in der die Methode <code>act</code> 
     * aufgerufen werden soll.
     */
    @SuppressWarnings("unchecked")
    public void setActOrder(final String... classes)
    {
        Class[] realClasses = new Class[classes.length];
        for (int i = 0; i < classes.length; ++i) {
            try {
                realClasses[i] = Class.forName(classes[i]);
            }
            catch (ClassNotFoundException e) {
                System.err.println("setActOrder: Es gibt keine Klasse mit dem Namen '" + classes[i] + "'.");
                getSystem().stop();
                return;
            }    
        }
        setActOrder(realClasses);
    }

    /**
     * Gibt alle Objekte der Welt oder alle Objekte einer angegebenen Klasse zurück.
     *
     * Wenn eine Klasse im Parameter angeben ist, werden nur Objekte dieser Klasse (oder deren
     * Unterklassen) zurückgegeben.
     *
     * @param cls Klasse von Objekten, nach denen gesucht werden soll. Eine leere
     *            Zeichenkette sucht nach allen Klassen.
     * @return    Eine Liste von gefundenen Objekten.
     */
    @SuppressWarnings("unchecked")
    public List<Actor> getObjects(final String cls)
    {
        try {
            return super.getObjects(cls.equals("") ? null : (Class<Actor>) Class.forName(cls));
        }
        catch (ClassNotFoundException e) {
            System.err.println("getObjects: Es gibt keine Klasse mit dem Namen '" + cls + "'.");
            getSystem().stop();
            return new ArrayList<Actor>();
        }
    }
}
