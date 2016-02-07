import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Die Test-Klasse RingBufferTest, die Tests bez. der Klasse 
 * RingBuffer haelt.
 *
 * @author Rune Krauss
 * @author Steffen Gerken
 * @author Niels Arbatschat
 */
public class RingBufferTest
{
    private int length;
    private RingBuffer buffer;

    /**
     * Konstruktor fuer die Test-Klasse RingBufferTest
     */
    public RingBufferTest()
    {
    }

    /**
     * Setzt das Testgeruest fuer den Test.
     *
     * Wird vor jeder Testfall-Methode aufgerufen.
     */
    @Before
    public void setUp()
    {
        length = 1;
        buffer = new RingBuffer(length);
    }

    /**
     * Gibt das Testgeruest wieder frei.
     *
     * Wird nach jeder Testfall-Methode aufgerufen.
     */
    @After
    public void tearDown()
    {
    }

    /**
     * Test des Normalfalls für den Push mit einem Wert und der anschliessenden
     * Kontrolle der Groesse des Buffers.
     */
    @Test
    public void pushTest()
    {
        buffer.push(1);
        assertEquals( 1, buffer.size() );
    }

    /**
     * Test für den ueberschreitenen Grenzfall. Es werden zwei Elemente in den Ringpuffer
     * gepusht, obwohl dieser nur Platz für einen Wert hat.
     */
    @Test
    public void crossPushTest()
    {
        buffer.push(1);
        buffer.push(2);
        assertEquals( 1, buffer.size() );
    }

    /**
     * Test für den überschreitenen Grenzfall. Es wird ein Element in den Ringpuffer
     * gepusht, obwohl dieser die Kapazitaet von 0 hat und somit keinen Wert speichern kann.
     */
    @Test
    public void crossPushTest2()
    {
        buffer = new RingBuffer(0);
        buffer.push(1);
        assertEquals( 0, buffer.size() );
    }

    /**
     * Test für den Normalfall der pop-Methode. Es wird ein Element in den Buffer geladen.
     * Danach wird dieses wieder rausgeholt. Hierbei wird der Wert des Elements sowie die
     * Groesse des Buffers nach dem Rausholen des Elements geprueft.
     */
    @Test
    public void popTest()
    {
        buffer.push(1);
        assertEquals( 1, buffer.pop() );
        assertEquals( 0, buffer.size() );
    }

    /**
     * Test für den ueberschreitenen Grenzfall. Es wird ein Element in den Ringpuffer
     * gepusht, welcher aufgrund der Kapazitaet kein Element aufnehmen kann. Anschliessend
     * wird versucht, das Element auszulesen und es wird ebenfalls im Anschluss die Groesse 
     * der Buffers geprueft.
     */
    @Test
    public void crossPopTest()
    {
        buffer = new RingBuffer(0);
        buffer.push(1);
        assertEquals( 0, buffer.pop() );
        assertEquals( 0, buffer.size() );
    }

    /**
     * Test für den Grenzfall, dass ein Element in den Buffer geladen wird und anschliessend
     * versucht wird, dieses eine Element zwei Mal wieder rauszunehmen.
     */
    @Test
    public void crossPopTest2()
    {
        buffer.push(1);
        buffer.pop();
        assertEquals( 0, buffer.pop() );
        assertEquals( 0, buffer.size() );
    }

    /**
     * Test des Normalfalls der peek-Methode. Es wird ein Element in den Buffer geladen
     * und anschließend ausgeleses. Das Element bleibt im Gegensazt zur pop-Methode in
     * dem Buffer weiterhin enthalten.
     */    
    @Test
    public void peekTest()
    {
        buffer.push(1);
        assertEquals( 1, buffer.peek() );
    }

    /**
     * Test für den überschreitenden Grenztest. Es wird ein Buffer mit der Kapazität von 0
     * erzeugt. Anschließed wird versucht ein Element in den Buffer zu laden, welches mittels
     * der peek-Methode ausgelesen wird. Dies wird nicht funktionieren.
     */ 
    @Test
    public void crossPeekTest()
    {
        buffer = new RingBuffer(0);
        buffer.push(1);
        assertEquals( 0, buffer.peek() );
    }

    /**
     * Test des Normalfalls für die Groesse des Buffers. Es wird ein Element in den Buffer geladen.
     * Anschliessend wird der Buffer auf seine Groesse überprueft.
     */ 
    @Test
    public void sizeTest()
    {
        buffer.push(1);
        assertEquals( 1, buffer.size() );
    }

    /**
     * Test für den ueberschreitenen Grenztest des Buffers. Es wird ein Buffer mit der Kapazitaet 
     * von 0 erzeugt, welcher somit ein Element speichern kann. Anschliessend wird versucht, ein 
     * Element in den Buffer zu laden. Es wird die Groesse der Buffers, nach dem push-Versuch, geprüft. 
     */ 
    @Test
    public void crossSizeTest()
    {
        buffer = new RingBuffer(0);
        buffer.push(1);
        assertEquals( 0, buffer.size() );
    }
}

