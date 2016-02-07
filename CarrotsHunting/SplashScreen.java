import greenfoot.*;
import java.io.*;
import javax.swing.JOptionPane;
import javax.swing.JInternalFrame;
/**
 * Creates a Splashscreen
 * 
 * @author Steffen Gerken
 * @author Rune Krauß
 * @author Niels Arbatschata
 */
public class SplashScreen extends PI1GameWorld
{

    /**
     * Constructor for objects of class SplashScreen.
     * 
     */
    public SplashScreen()
    {
        super(800,600,1,1);
    }

    public void act() 
    {
        try{
            if(getSystem().isKeyDown("Enter")){
                Greenfoot.setWorld(new Field());
            }
        }
        catch(FileNotFoundException e){
        }

        if(getSystem().isKeyDown("h")){
            JOptionPane.showMessageDialog(new JInternalFrame(),
                "1. Benutze die Pfeiltasten, um dich zu bewegen. \n" 
                + "2. Versuche alle Karotten zu sammeln, die sich auf dem Feld befinden. Dabei musst du dich jedoch vor den gefährlichen Jägern in Acht nehmen! \n"
                + "3. Sobald diese auf dein Feld gehen, ist das Spiel vorbei! Du kannst die Jäger jedoch austricksen, indem du auf ihr Feld gehst und sie damit aus dem Spiel beförderst. \n"
                + "4. Ab dem Moment, in dem einer der Jäger dich gesehen hat, verfolgt er dich. Also sei vorsichtig! \n"
                + "5. Der Wolf kann dir behilflich sein, denn er lenkt die Aufmerksamkeit der Jäger auf sich, sodass du Zeit hast, die Karotten zu sammeln. \n"
                + "6. Es gibt auch ein Rating-System, also fühl dich frei, den Like Button zu nutzen, wenn dir ein Level gefällt. \n"
            );

        }
    }
}
