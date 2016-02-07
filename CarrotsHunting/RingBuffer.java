/**
 * Die Klasse implementiert einen Ringpuffer, d.h. einen Puffer, der eine
 * bestimmte Menge von Werten zwischenspeichern kann. Man kann mit
 * {@link push(int)} Werte hinzufügen und mit {@link pop()} wieder entnehmen.
 * Dabei liefert {@link pop()} die Werte in derselben Reihenfolge zurück, in der
 * sie mit {@link push(int)} eingefügt wurden, d.h. {@link pop()} liefert immer
 * das Element zurück, das schon am längsten im Puffer gespeichert ist. Wird ein
 * neues Element in den Puffer eingefügt, wenn seine Kapazität bereits erreicht
 * ist, geht das älteste Element verloren.
 */
public class RingBuffer {
    /**
     * Ringpuffer, der die jeweiligen Elemente enthält.
     */
    private int[] buffer;
    /**
     * Repräsentiert die Kapazität der Warteschlage, also die maximale Anzahl
     * von Elementen.
     */
    private int capacity;
    /**
     * Gibt an, wie viele Elemente es in der Warteschlange gibt.
     */
    private int length;
    /**
     * Repräsentiert das aktuelle Element, womit man auch das älteste Element
     * finden kann.
     */
    private int elem;

    /**
     * Erzeugt einen Ringpuffer.
     * 
     * @param capacity
     *            Die maximale Anzahl von Einträgen, die gepuffert werden
     *            können.
     */
    public RingBuffer(final int capacity) {
        buffer = new int[capacity];
        this.capacity = capacity;
        length = 0;
        elem = 0;
    }

    /**
     * Fügt ein neues Element in den Ringpuffer ein.
     * 
     * @param value
     *            Der Wert, der eingefügt werden soll.
     */
    public void push(final int value) {
        if(capacity != 0) {
            buffer[elem] = value;
            elem++;
            if (length != capacity) {
                length++;
            }
            if (elem >= capacity) {
                elem = 0;
            }
            if (elem < 0) {
                elem = capacity - 1;
            }
        }
    }

    /**
     * Entnimmt das älteste Element aus dem Ringpuffer.
     * 
     * @return Das Element, das entnommen wurde.
     */
    public int pop() {
        int tmp;
        if (length != 0){
            if (size() <= capacity) {
                tmp = buffer[0];
                for (int i = 0; i < length - 1; i++) {
                    buffer[i] = buffer[i + 1];
                }
            } else {
                tmp = buffer[elem];
                for (int j = elem; j < length - 1; j++) {
                    buffer[j] = buffer[j + 1];
                }
                buffer[length - 1] = buffer[0];
                for (int k = 0; k < elem; k++) {
                    buffer[k] = buffer[k + 1];
                }
            }
            elem--;
            if (elem >= capacity) {
                elem = 0;
            }
            if (elem < 0) {
                elem = capacity - 1;
            }
            length--;
            return tmp;
        }
        return 0;
    }

    /**
     * Liefert das älteste Element aus dem Ringpuffer zurück, ohne es zu
     * entnehmen.
     * 
     * @return Das älteste Element im Ringpuffer.
     */
    public int peek() {
        if (length != 0){
            if (size() <= capacity) {
                return buffer[0];
            } else {
                return buffer[elem];
            }
        }
        return 0;
    }

    /**
     * Liefert die Anzahl der Elemente zurück, die sich im Puffer befinden, d.h.
     * die mit {@link pop()} entnommen werden könnten.
     * 
     * @return Die Anzahl der belegten Einträge im Puffer.
     */
    public int size() {
        return length;
    }
    
    /**
     * Liefert alle Elemente von dem Ringpuffer.
     * 
     * @return Liste aller Elemente von dem Ringpuffer.
     */
    public String toString() {
        String rs = "{";
        for (int i = 0; i < size(); i++) {
            rs = rs + buffer[i] + ", ";
        }
        rs = rs.substring(0, rs.length() - 2) + "}";
        return rs;
    }
    
    /**
     * Leert den Ringpuffer.
     */
    public void clear() {
        for (int i = 0; i <= size(); i++) {
            pop();
        }
    }
}