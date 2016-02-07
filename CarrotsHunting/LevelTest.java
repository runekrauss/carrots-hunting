import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.*;
import java.io.*;

/**
 * Test class for creation of levels.
 * 
 * @author Rune Krauss
 * @author Steffen Gerken
 * @author Niels Arbatschat
 */
public class LevelTest
{
    /** Name of the file */
    private String fileName;

    /** Level of the game */
    private Level level;
    
    /** Square of this game */
    private String[][][] square = {
        {{""},{""},{""},{""},{""},{""},{""},{""},{""}},
        {{""},{""},{""},{""},{""},{""},{""},{""},{""}},
        {{""},{""},{""},{""},{""},{""},{""},{""},{""}},
        {{""},{""},{""},{""},{""},{""},{""},{""},{""}},
        {{""},{""},{"GSE"},{""},{""},{""},{""},{""},{""}},
        {{""},{""},{"GNWSE"},{""},{""},{""},{""},{""},{""}},
        {{""},{""},{"GNW","GSE"},{""},{""},{""},{""},{""},{""}},
        {{""},{""},{"GNWSE"},{""},{""},{""},{""},{""},{""}},
        {{"GSW","GSE"},{"GSWNE"},{"GNW","GNE","GSW"},{"GSWNE"},{"GNE","GSE"},{""},{""},{""},{""}},
        {{"GNWSE"},{""},{""},{""},{"GNWSE"},{""},{""},{""},{""}},
        {{"GSE","GNW"},{""},{""},{""},{"GSW","GSE","GNW"},{"GSWNE"},{"GSW","GNE"},{"GSWNE"},{"GNE"}},
        {{"GNWSE"},{""},{""},{""},{"GNWSE"},{""},{""},{""},{""}},
        {{"GSW","GNW"},{"GSWNE"},{"GSE","GNE","GSW"},{"GSWNE"},{"GNE","GNW"},{""},{""},{""},{""}},
        {{""},{""},{"GNWSE"},{""},{""},{""},{""},{""},{""}},
        {{""},{""},{"GNW","GSE"},{""},{""},{""},{""},{""},{""}},
        {{""},{""},{"GNWSE"},{""},{""},{""},{""},{""},{""}},
        {{""},{""},{"GNW"},{""},{""},{""},{""},{""},{""}}
        };

    /**
     * Default constructor for test class LevelTest
     */
    public LevelTest()
    {
    }

    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
    @Before
    public void setUp() throws FileNotFoundException
    {
        fileName = "level.txt";
        level = new Level(fileName);
    }

    /**
     * Tears down the test fixture.
     *
     * Called after every test case method.
     */
    @After
    public void tearDown()
    {
    }

    /**
     * Reads in the file and checks the expected values like 
     * the square and game parameters.
     */
    @Test
    public void normalLevelTest() throws FileNotFoundException
    {
        assertArrayEquals( square, level.getSquare() );
        int[] params = level.getParams();
        assertEquals(1, params[0]);
        assertEquals(1, params[1]);
        assertEquals(8, params[2]);
        assertEquals(1, params[3]);
        assertEquals(1, params[4]);
    }

    /**
    * Reads in the file and checks the expected 
    * input mismatch exception because of 
    * missed parameters.
    */
    @Test (expected = InputMismatchException.class)
    public void faultLevelTest() throws FileNotFoundException
    {
        fileName = "level2.txt";
        level = new Level(fileName);
    }

    /**
    * Reads in the file and checks the expected 
    * input mismatch exception because of 
    * invalid curly braces.
    */
    @Test (expected = InputMismatchException.class)
    public void faultLevelTest2() throws FileNotFoundException
    {
        fileName = "level3.txt";
        level = new Level(fileName);
    }

    /**
    * Reads in the file and checks the expected 
    * illegal argument exception because of an
    * invalid file format.
    */
    @Test (expected = IllegalArgumentException.class)
    public void faultLevelTest3() throws FileNotFoundException
    {
        fileName = "level4.rtf";
        level = new Level(fileName);
    }

    /**
    * Reads in the file and checks the expected 
    * input mismatch exception because of 
    * invalid quotation marks.
    */
    @Test (expected = InputMismatchException.class)
    public void faultLevelTest4() throws FileNotFoundException
    {
        fileName = "level5.txt";
        level = new Level(fileName);
    }

    /**
    * Reads in the file and checks the expected 
    * input mismatch exception because of 
    * another sequence.
    */
    @Test (expected = InputMismatchException.class)
    public void faultLevelTest5() throws FileNotFoundException
    {
        fileName = "level6.txt";
        level = new Level(fileName);
    }

    /**
    * Reads in the file and checks the expected 
    * illegal argument exception because of 
    * invalid used grids.
    */
    @Test (expected = IllegalArgumentException.class)
    public void faultLevelTest6() throws FileNotFoundException
    {
        fileName = "level7.txt";
        level = new Level(fileName);
    }

    /**
    * Reads in the file and checks the expected 
    * illegal argument exception because of an  
    * incorrect value range.
    */
    @Test (expected = IllegalArgumentException.class)
    public void faultLevelTest7() throws FileNotFoundException
    {
        fileName = "level8.txt";
        level = new Level(fileName);
    }

    /**
     * Reads in the file and checks the expected values like 
     * the square and game parameters based on 
     * allowed values.
     */
    @Test
    public void limitLevelTest() throws FileNotFoundException
    {
        fileName = "level9.txt";
        level = new Level(fileName);
        assertArrayEquals( square, level.getSquare() );
        int[] params = level.getParams();
        assertEquals(10, params[0]);
        assertEquals(1, params[1]);
        assertEquals(8, params[2]);
        assertEquals(1, params[3]);
        assertEquals(1, params[4]);
    }

    /**
     * Reads in the file and checks the expected values like 
     * the square and game parameters based on 
     * missed commas.
     */
    @Test
    public void normalLevelTest2() throws FileNotFoundException
    {
        fileName = "level10.txt";
        level = new Level(fileName);
        assertArrayEquals( square, level.getSquare() );
        int[] params = level.getParams();
        assertEquals(1, params[0]);
        assertEquals(1, params[1]);
        assertEquals(8, params[2]);
        assertEquals(1, params[3]);
        assertEquals(1, params[4]);
    }

    /**
     * Reads in the file and checks the expected values like 
     * the square and game parameters based on 
     * standing parameters back to back.
     */
    @Test
    public void normalLevelTest3() throws FileNotFoundException
    {
        fileName = "level11.txt";
        level = new Level(fileName);
        assertArrayEquals( square, level.getSquare() );
        int[] params = level.getParams();
        assertEquals(1, params[0]);
        assertEquals(1, params[1]);
        assertEquals(8, params[2]);
        assertEquals(1, params[3]);
        assertEquals(1, params[4]);
    }
}

