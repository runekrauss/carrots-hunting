/**
 * Die Klasse repräsentiert eine Zellenposition.
 * Eine Zellenposition besteht aus einer x- und einer y-Koordinate.
 * Beide müssen dem Konstruktor übergeben werden und können über
 * Zugriffsmethoden wieder abgefragt werden. Es ist nicht möglich,
 * die gespeicherten Koordinaten wieder zu ändern. Objekte dieser
 * Klasse können als Schlüssel in Hash-Tabellen verwendet werden.
 */
public class CellLocation
{
    /** Die x-Koordinate der Zelle. */
    private final int x;

    /** Die y-Koordinate der Zelle. */
    private final int y;

    /**
     * Der Konstruktor initialisiert die Zellenposition.
     * @param x Die x-Koordinate der Zelle.
     * @param y Die y-Koordinate der Zelle.
     */
    public CellLocation(final int x, final int y)
    {
        this.x = x;
        this.y = y;
    }

    /**
     * Liefert die x-Koordinate der Zelle.
     * @return Die x-Koordinate der Zelle.
     */
    public int getX()
    {
        return x;
    }

    /**
     * Liefert die y-Koordinate der Zelle.
     * @return Die y-Koordinate der Zelle.
     */
    public int getY()
    {
        return y;
    }

    /**
     * Vergleicht, ob diese Zellenposition gleich einer anderen ist.
     * Dies wird für den Einsatz in HashSet und HashMap benötigt.
     * @param other Das Objekt, mit dem verglichen wird.
     * @return      Ist das andere Objekt gleich zu diesem?
     */
    public boolean equals(final Object other)
    {
        return other != null
                && other instanceof CellLocation
                && ((CellLocation) other).x == x
                && ((CellLocation) other).y == y;
    }

    /**
     * Liefert einen Hash-Code für dieses Objekt.
     * Dies wird für den Einsatz in HashSet und HashMap benötigt.
     * @return Ein Hash-Code, der durch exklusiv-oder-Verknüpfung von x
     *         und y bestimmt wird, wobei von y die unteren und oberen
     *         16 Bit vertauscht werden.
     */
    public int hashCode()
    {
        return x ^ y << 16 ^ y >> 16;
    }
}
