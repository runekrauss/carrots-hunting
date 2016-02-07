import greenfoot.*;

/**
 * Diese Klasse kapselt die Klassenmethoden der Klasse Greenfoot in einem Objekt.
 *
 * <h3>Tastennamen</h3>
 * 
 * <p> Teil der Funktionalität, die von dieser Klasse bereitgestellt wird,
 * ist die Fähigkeit, Tastatureingaben entgegenzunehmen. Die Methoden
 * getKey(), isKeyDown() und isKeyPressed() werden dazu verwendet und sie geben zurück
 * bzw. verstehen die folgenden Tastennamen:
 * 
 * <ul>
 * <li>"a", "b", .., "z" (Alphabetische Tasten), "0".."9" (Ziffern), die
 *     meisten Satzzeichen. getKey() gibt gegebenenfalls auch Großbuchstaben
 *     zurück.
 * <li>"up", "down", "left", "right" (Die Pfeiltasten)
 * <li>"enter", "space", "tab", "escape", "backspace", "shift", "control"
 * <li>"F1", "F2", .., "F12" (Die Funktionstasten)
 * </ul>
 *
 * @author Thomas Röfer 
 * @version 1.0
 */
public class GreenfootSystem  
{
    private static GreenfootSystem instance = new GreenfootSystem();
    private String keyPressed;
    
    /**
     * Liefert die einzige Instanz dieser Klasse.
     * 
     * @return Ein Hüllobjekt für Aufruf der Klassenmethoden in Greenfoot.
     */
    public static GreenfootSystem getInstance()
    {
        return instance;
    }
    
    /**
     * Legt die angegebene als "die" Welt fest.
     * Diese Welt ist von nun an die Hauptwelt, die Greenfoot vom nächsten Aktionsschritt an ausführt.
     *
     * @param world Die auszuführende Welt, zu der umgeschaltet wird. Darf nicht <code>null</code> sein.
     */
    public void setWorld(World world)
    {
        Greenfoot.setWorld(world);
    }

    /**
     * Liefert die zuletzt gedrückte Taste zurück (seit dem letzten Aufruf).
     * Wenn seit dem letzten Aufruf der Methode keine Taste gedrückt wurde, wird <code>null</code> zurückgegeben.
     * Wenn mehr als eine Taste gedrückt wurde, wird nur die als letztes gedrückte Taste zurückgeliefert.
     *
     * @return Der Name der zuletzt gedrückten Taste
     */
    public String getKey()
    {
        return Greenfoot.getKey();
    }
    
    /**
     * Abfrage, ob eine bestimmte Taste zurzeit gedrückt ist.
     *
     * @param keyName Der Name der zu überprüfenden Taste (siehe Tastennamen) in <code>World<.
     * @return        <code>true</code>, wenn die Taste gedrückt wird, ansonsten <code>false</code>.
     */
    public boolean isKeyDown(String keyName)
    {
        return Greenfoot.isKeyDown(keyName);
    }
    
    /**
     * Abfrage, ob eine bestimmte Taste gedrückt wurde.
     *
     * @param keyName Der Name der zu überprüfenden Taste (siehe Tastennamen).
     * 
     * @return        <code>true</code>, wenn die Taste gedrückt wurde, ansonsten <code>false</code>.
     */
    public boolean wasKeyPressed(String keyName)
    {
        return keyPressed != null && keyPressed.equals(keyName);
    }
    
    /** Aktualisiert die Taste, die gedrückt wurde. */
    void updateKeyPressed()
    {
        keyPressed = getKey();
    }
    
    /**
     * Verzögert die aktuellen Ausführung um die angegebenen Zeitschritte.
     * Die Länge eines Zeitschrittes ist in der Greenfoot-Entwicklungsumgebung definiert (der Geschwindigkeitsschieber).
     * Beachte: Der Befehl kann auch dazu verwendet werden, die Greenfootwelt neu zu zeichnen. (Standardmäßig werden erst die '<code>act</code>' Methoden aller Objekte durchlaufen und anschließend die Welt neu gezeichnet).
     *
     * @see #setSpeed(int)
     */
    public void delay(int time)
    {
        Greenfoot.delay(time);
    }
    
    /**
     * Legt die Ausführungsgeschwindigkeit fest.
     *
     * @param speed Die neue Ausführungsgeschwindigkeit: Der Wert muss im Bereich 1 bis 100 liegen.
     */
    public void setSpeed(int speed)
    {
        Greenfoot.setSpeed(speed);
    }
    
    /**
     * Pausiert die Ausführung des Programms.
     */
    public void stop()
    {
        Greenfoot.stop();
    }
    
    /**
     * Startet die Ausführung des Programms bzw. nimmt deren Ausführung wieder auf.
     */
    public void start()
    {
        Greenfoot.start();
    }
    
    /**
     * Liefert eine Zufallszahl zwischen 0 (inklusive) und limit (exklusiv) zurück.
     */
    public int getRandomNumber(int limit)
    {
        return Greenfoot.getRandomNumber(limit);
    }

    /**
     * Spielt eine Sounddatei ab.
     * Die folgenden Formate werden unterstützt: AIFF, AU und WAV. Groß- und Kleinschreibung werden unterschieden.
     *
     * Der Dateiname kann angegeben werden
     * <ul>
     * <li> mit absoluter Pfadangabe</li>
     * <li> als Name für eine Datei, die im Projektverzeichnis liegt</li>
     * <li> oder als Name für eine Datei, die im Soundverzeichnis des Projektverzeichnisses liegt</li>
     * </ul>
     *
     * @param  soundFile                Normalerweise (siehe oben) der Name der Datei im Ordner Sound 
     *                                  des Projektordners.
     * @throws IllegalArgumentException Wenn die Datei nicht geladen werden konnte.
     */
    public void playSound(final String soundFile)
    {
        Greenfoot.playSound(soundFile);
    }


    /**
     * Ist wahr, wenn die Maus auf dem angegebene Objekt gedrückt wurde (Veränderung vom nicht gedrücktem Zustand in den gedrückten Zustand).
     * Wenn der Parameter ein <code>Actor</code> ist, wird nur dann <code>true</code> zurückgeliefert, wenn mit der Maus auf das übergebenen Objekt gedrückt wurde.
     * Wenn sich mehrere Objekte auf derselben Position befinden, wird nur das Objekt, welches sich am weitesten oben befindet, den Mausklick erreichen.
     * Wenn das Objekt <code>World</code> als Parameter übergeben wird, wird nur dann <code>true</code> zurückgeliefert, wenn mit der Maus auf den Hintergrund der Welt gedrückt wurde.
     * Wenn der Parameter <code>null</code> ist, wird immer dann <code>true</code> zurückgeliefert, wenn die Maustaste gedrückt wurde, unabhängig davon, ob ein Objekt getroffen wurde oder nicht.
     *
     * @param obj Normalerweise ein <code>Actor</code>, die <code>World</code> oder <code>null</code>.
     * @return    <code>true</code>, wenn die Maustaste gedrückt wurde (siehe Beschreibung oben), anderenfalls <code>false</code>.
     */
    public boolean mousePressed(Object obj)
    {
        return Greenfoot.mousePressed(obj);
    }

    /**
     * Ist <code>true</code>, wenn mit der Maus auf ein im Parameter übergebenes Objekt geklickt (heißt: gedrückt und losgelassen) wurde.
     * Wenn der Parameter vom Typ <code>Actor</code> ist, wird nur dann <code>true</code> zurückgeliefert, wenn mit der Maus auf das übergebene Objekt geklickt wurde.
     * Wenn sich mehrere Objekte auf derselben Position befinden, wird nur das Objekt, welches sich am weitesten oben befindet, den Mausklick erreichen.
     * Wenn das Objekt <code>World</code> als Parameter übergeben wird, wird nur dann <code>true</code> zurückgeliefert, wenn mit der Maus auf den Hintergrund der Welt geklickt wurde.
     * Wenn der Parameter <code>null</code> ist, wird immer dann <code>true</code> zurückgeliefert, wenn die Maustaste geklickt wurde, unabhängig davon, ob ein Objekt getroffen wurde oder nicht.
     *
     * @param obj Normalerweise ein <code>Actor</code>, die <code>World</code> oder <code>null</code>.
     * @return    <code>true</code>, wenn die Maustaste geklickt (gedrückt und losgelassen) wurde, anderenfalls <code>false</code>.
     */
    public boolean mouseClicked(Object obj)
    {
        return Greenfoot.mouseClicked(obj);
    }

    /**
     * Ist wahr, wenn die Maus auf dem angegebenen Objekt verschoben wurde.
     * Dazu muss das Verschieben der Maus bei gedrückter Maustaste auf einem Objekt beginnen. Die Maus kann bei gedrückter Maustaste auf außerhalb des Objektes bewegt werden. Das Verschieben endet erst beim Loslassen der Maustaste.
     *
     * Wenn der Übergabewert ein <code>Actor</code> ist, wird nur dann <code>true</code> zurückgeliefert, wenn das Verschieben auf dem angegebenen Objekt auch gestartet wurde.
     * Wenn sich mehrere Objekte auf derselben Position befinden, wird nur das Objekt, welches sich am weitesten oben befindet, den Mausdruck erreichen.
     * Wenn das Objekt <code>World</code> als Parameter übergeben wird, wird nur dann <code>true</code> zurückgeliefert, wenn das Verschieben der Maus auf den Hintergrund der Welt gestartet wurde.
     * Wenn der Parameter <code>null</code> ist, wird immer dann <code>true</code> zurückgeliefert, wenn die Maustaste gedrückt wurde, unabhängig davon, ob ein Objekt getroffen wurde oder nicht.
     *
     * @param obj Normalerweise ein <code>Actor</code>, die <code>World</code> oder <code>null</code>.
     * @return    <code>true</code>, wenn die Maus mit gedrückter Maustaste verschoben wurde (siehe Beschreibung oben), anderenfalls <code>false</code>.
     */
    public boolean mouseDragged(Object obj)
    {
        return Greenfoot.mouseDragged(obj);
    }

    /**
     * Ist wahr, wenn ein Verschieben eines Objektes beendet wurde.
     * Dies ist dann der Fall, wenn die Maus mit gedrückter Maustaste verschoben wurde und anschließend die Maustaste losgelassen wurde.
     *
     * Wenn der Übergabewert ein <code>Actor</code> ist, wird nur dann <code>true</code> zurückgeliefert, wenn das Verschieben auf dem angegebenen Objekt auch gestartet wurde.
     * Wenn sind mehrere Objekte auf derselben Position befinden, wird nur das Objekt, welches sich am weitesten oben befindet, den Mausdruck erreichen.
     * Wenn das Objekt <code>World</code> als Parameter übergeben wird, wird nur dann <code>true</code> zurückgeliefert, wenn das Verschieben der Maus auf dem Hintergrund der Welt gestartet wurde.
     * Wenn der Parameter <code>null</code> ist, wird immer dann <code>true</code> zurückgeliefert, wenn die Maustaste gedrückt wurde, unabhängig davon, ob ein Objekt getroffen wurde oder nicht.
     *
     * @param obj Normalerweise ein <code>Actor</code>, die <code>World</code> oder <code>null</code>.
     * @return    <code>true</code>, wenn die Maus mit gedrückter Maustaste verschoben wurde (siehe Beschreibung oben), anderenfalls <code>false</code>.
     */
    public boolean mouseDragEnded(Object obj)
    {
        return Greenfoot.mouseDragEnded(obj);
    }

    /**
     * Ist wahr, wenn die Maus auf dem angegebenen Objekt bewegt wurde.
     *
     * Wenn der Übergabewert ein <code>Actor</code> ist, wird nur dann <code>true</code> zurückgeliefert, wenn die Maus auf dem angegebenen Objekt bewegt wird.
     * Wenn sich mehrere Objekte auf derselben Position befinden, wird nur das Objekt, welches sich am weitesten oben befindet, die Mausbewegung erreichen.
     * Wenn das Objekt <code>World</code> als Parameter übergeben wird, wird nur dann <code>true</code> zurückgeliefert, wenn die Maus auf den Hintergrund der Welt bewegt wurde.
     * Wenn der Parameter <code>null</code> ist, wird immer dann <code>true</code> zurückgeliefert, wenn die Maus bewegt wurde, unabhängig davon, ob sich ein Objekt unter Maus befand oder nicht.
     *
     * @param obj Normalerweise ein <code>Actor</code>, die <code>World</code> oder <code>null</code>.
     * @return    <code>true</code>, wenn die Maus bewegt wurde (siehe Beschreibung oben), anderenfalls <code>false</code>.
     */
    public boolean mouseMoved(Object obj)
    {
        return Greenfoot.mouseMoved(obj);
    }

    /**
     * Liefert ein Objekt Mouseinfo zurück, welches Informationen über den Status der Maus enthält.
     *
     * @return Information über den aktuellen Status der Maus. Oder <code>null</code>, wenn nichts seit der letzten Überprüfung mit der Maus passiert ist.
     */
    public MouseInfo getMouseInfo()
    {
        return Greenfoot.getMouseInfo();
    }
    
    /**
     * Ermittelt den Eingangspegel des Mikrofons. Der Pegel ist eine Schätzung der Lautstärke 
     * aller Geräusche, die momentan von dem Mikrofon empfangen werden.
     * 
     * @return Der Mikrofoneingangspegel (Zwischen 0 und 100, inklusive).
     */
    public int getMicLevel()
    {
        return Greenfoot.getMicLevel();
    }
}
