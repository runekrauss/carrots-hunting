import java.util.*;
import java.io.*;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import javax.swing.JOptionPane;
import javax.swing.JInternalFrame;

/**
 * The level represents information regarding the game.
 * Initializes a level and creates the mover for moving objects.
 * Moreover, saves information like the square.
 * 
 * @author Rune Krauss
 * @author Steffen Gerken
 * @author Niels Arbatschat
 */
public class Level 
{
    /**
     * Square of this game
     */
    private String[][][] square;

    /**
     * Parameters of this game
     */
    private int[] params;

    /**
     * X coordinate regarding origin
     */
    private int originX = 0;

    /**
     * Y coordinate regarding origin
     */
    private int originY = 0;

    /**
     * Initial position for moved objects
     */
    private int initialPosition = 8;

    /**
     * Increment of mover
     */
    private int increment = 1;

    /**
     * Direction of rotation regarding the mover
     */
    private int directionOfRotation = 1;

    /**
     * Moves a NPC to the outside or stops the game.
     */
    private Mover mover;

    /**
     * World of this game
     */
    private Field field;

    /**
     * Internet socket address of the server
     */
    private InetSocketAddress address;

    /**
     * Writer to print outgoing messages
     */
    private PrintWriter out;

    /**
     * Input stream
     */
    private InputStream input;

    /**
     * Output stream
     */
    private OutputStream output;

    /**
     * Reader regarding the console
     */
    private BufferedReader br;

    /**
     * Host of the server
     */
    private final String HOST = "localhost";

    /**
     * Port of the server
     */
    private final int PORT = 55555;

    /**
     * Constructor for objects of class Level.
     * 
     * @param square
     *      Square of this game
     * @param originX
     *      X coordinate regarding origin
     * @param originY
     *      Y coordinate regarding origin
     * @param initialPosition
     *      Square of this game.
     * @param increment
     *      Increment of mover
     * @param directionOfRotation
     *      Direction of rotation regarding the mover
     * @throws FileNotFoundException
     *      If a file was not found, this exception will be expected.
     */
    public Level(String fileName) throws FileNotFoundException
    {
        readFromFile(fileName);
        originX = params[0];
        originY = params[1];
        initialPosition = params[2];
        increment = params[3];
        directionOfRotation = params[4];
    }

    /**
     * Reads and formats a file.
     * @param fileName 
     *      Name of the file
     * @throws FileNotFoundException
     *      If a file was not found, this exception will be expected.
     */
    private void readFromFile(String fileName) throws FileNotFoundException {
        String extension = "";
        int e = fileName.lastIndexOf('.');
        if (e > 0) {
            extension = fileName.substring(e+1);
        }
        if ( !extension.equals("txt") ) {
            throw new IllegalArgumentException("Invalid file extension. There are only text files (.txt) allowed.");
        }
        Scanner scan = new Scanner( getClass().getResourceAsStream("level.txt") );
        List<List<List<String>>> d3 = new ArrayList<>();
        params = new int[5];
        int counter = 0;
        int curlyBracesLeft = 0;
        int curlyBracesRight = 0;
        int quotationMarks = 0;
        while ( scan.hasNextLine() ) {
            if ( scan.hasNextInt() ) {
                if (d3.size() == 0) {
                    throw new InputMismatchException("The list must be in front of the parameters.");
                }
                int param = scan.nextInt();
                if (param < 1 || param > 10) {
                    throw new IllegalArgumentException("The integer values must be between 1 and 10.");
                }
                params[counter] = param;
                counter++;
            } else {
                String line = scan.nextLine();
                if ( !line.matches(".*\\{.*") ) {
                    throw new InputMismatchException("The list must be in front of the parameters.");
                }
                curlyBracesLeft = line.length() - line.replace("{", "").length();
                curlyBracesRight = line.length() - line.replace("}", "").length();
                if (curlyBracesLeft != curlyBracesRight) {
                    throw new InputMismatchException("Number of curly braces per line must be the same.");
                }
                quotationMarks = line.length() - line.replace("\"", "").length();
                if ( quotationMarks % 2 != 0) {
                    throw new InputMismatchException("Number of quotation marks per line must be straight.");
                }
                String[] splitted = line.split("(?<!\\\\)\\\"");
                List<List<String>> d2 = new ArrayList<>();
                d3.add(d2);
                List<String> d1 = new ArrayList<>();
                d2.add(d1);
                for (int i = 1; i < splitted.length - 1; i++) {
                    if ( (i & 1) != 0 ) {
                        d1.add(splitted[i].replace("\\\"", "\"").replace("\\\\", "\\"));
                    } else {
                        if ( splitted[i].matches(".*\\{.*") ) {
                            d1 = new ArrayList<>();
                            d2.add(d1);
                        }
                    }
                }
            }
        }
        if (counter != 5) {
            throw new InputMismatchException("There must exist integer values for 5 parameters.");
        }
        scan.close();
        square = convertListToArray(d3);
        if ( !checkSquare(square) ) {
            throw new IllegalArgumentException("An invalid grid name was used.");
        }
    }

    /**
     * Converts a list to an array.
     * @param d3 
     *      Specific list
     */
    private String[][][] convertListToArray(List<List<List<String>>> square) {
        String[][][] result = new String[square.size()][][];
        for (int i = 0; i < result.length; i++) {
            result[i] = new String[square.get(i).size()][];
            for (int j = 0; j < result[i].length; j++) {
                result[i][j] = new String[square.get(i).get(j).size()];
                for (int k = 0; k < result[i][j].length; k++) {
                    result[i][j][k] = square.get(i).get(j).get(k);
                }
            }
        }
        //System.out.println( Arrays.deepToString(result) );
        return result;
    }

    /**
     * Checks grid names regarding the square.
     * @param square 
     *      Square of this game
     */
    private boolean checkSquare(String[][][] square) {
        for (int x = 0; x < square.length; x++) {
            for (int y = 0; y < square[x].length; y++) {
                for (int z = 0; z < square[x][y].length; z++) {
                    switch(square[x][y][z]) {
                        case "GSE":
                        break;
                        case "GNE":
                        break;
                        case "GSW":
                        break;
                        case "GNW":
                        break;
                        case "GSWNE":
                        break;
                        case "GNWSE":
                        break;
                        case "":
                        break;
                        default:
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Initializes a level based on 3d array.
     * Furthermore, sets the origin and creates the mover for moving objects.
     * 
     * @param field
     *      Specific field to do operations like setting the origin.
     */
    public void init(Field field) throws IllegalArgumentException {
        this.field = field;
        field.setOrigin(originX, originY);
        for (int x = 0; x < square.length; x++) {
            for (int y = 0; y < square[x].length; y++) {
                for (int z = 0; z < square[x][y].length; z++) {
                    switch(square[x][y][z]) {
                        case "GSE":
                        Grid grid = new Grid();
                        field.addObject(grid, 0, 0);
                        grid.setRotation(0);
                        grid.setCellLocation(x, y);
                        break;
                        case "GNE":
                        grid = new Grid();
                        field.addObject(grid, 0, 0);
                        grid.setRotation(3); 
                        grid.setCellLocation(x, y);
                        break;
                        case "GSW":
                        grid = new Grid();
                        field.addObject(grid, 0, 0);
                        grid.setRotation(1);
                        grid.setCellLocation(x, y);
                        break;
                        case "GNW":
                        grid = new Grid();
                        field.addObject(grid, 0, 0);
                        grid.setRotation(2);
                        grid.setCellLocation(x, y);
                        break;
                        case "GSWNE":
                        GridSWNE gridswne = new GridSWNE();
                        field.addObject(gridswne, 0, 0);
                        gridswne.setCellLocation(x, y);
                        break;
                        case "GNWSE":
                        GridNWSE gridnwse = new GridNWSE();
                        field.addObject(gridnwse, 0, 0);
                        gridnwse.setCellLocation(x, y);
                        break;
                        case "":
                        break;
                        default:
                        throw new IllegalArgumentException("Invalid grid: " + square[x][y][z]);
                    }
                }
            }
        }
        mover = new Mover(initialPosition, increment, directionOfRotation);
        CarrotCounter carrotCounter = new CarrotCounter(new Color(0, 0, 0));
        field.addObject(carrotCounter, 350, 50);
        LikeCounter likeCounter = new LikeCounter(new Color(63, 81, 181));
        field.addObject(likeCounter, 681, 31);
        LikeButton likeButton = new LikeButton();
        field.addObject(likeButton, 631, 31);
        address = new InetSocketAddress(HOST, PORT);
    }

    /**
     * Gets mover for moving a NPC or stopping the game
     */
    public Mover getMover() {
        return mover;
    }

    /**
     * Gets the square.
     */
    public String[][][] getSquare() {
        return square;
    }

    /**
     * Gets the parameters.
     * @return Parameters
     */
    public int[] getParams() {
        return params;
    }

    /**
     * Connects and sends a like to the server.
     */
    public void like() {
        try {
            Socket socket = new Socket();
            socket.connect(address);
            out = new PrintWriter(socket.getOutputStream(), true);
            String userName = System.getProperty("user.name");
            System.out.println(Field.getLevelName());
            out.println("add " + Field.getLevelName() + " " + userName);
            input = socket.getInputStream();
            br = new BufferedReader(new InputStreamReader(input));
            String msg = br.readLine();
            if (msg.equals("The user name was accepted.")) {
                checkLikes(true);
            } else if (msg.equals("The user name already exists.")) {
                JOptionPane.showMessageDialog(new JInternalFrame(), "The user name already exists.","Duplicate name", JOptionPane.INFORMATION_MESSAGE);
            } else if (msg.equals("The regular expression did not match with the given commands.")) {
                JOptionPane.showMessageDialog(new JInternalFrame(), "The regular expression did not match with the given commands.","Incorrect regular expression", JOptionPane.INFORMATION_MESSAGE);
            }
            socket.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(new JInternalFrame(), "The server could not be reached. Try again later.","Connection refused", JOptionPane.INFORMATION_MESSAGE);
            //e.printStackTrace();
        }
    }

    /**
     * Connects to the server for checking all likes regarding a level.
     */
    public void checkLikes(boolean popup) {
        try {
            Socket socket = new Socket();
            socket.connect(address);
            out = new PrintWriter(socket.getOutputStream(), true);
            out.println("get " + Field.getLevelName());
            input = socket.getInputStream();
            br = new BufferedReader(new InputStreamReader(input));
            int likes = Integer.parseInt(br.readLine());
            socket.close();
            List<LikeCounter> likeCounter = field.getObjects(LikeCounter.class);
            likeCounter.get(0).add(likes);
        } catch (IOException ioe) {
            if (popup) {
                JOptionPane.showMessageDialog(new JInternalFrame(), "The server could not be reached. Try again later.","Connection refused", JOptionPane.INFORMATION_MESSAGE);
            }
            //ioe.printStackTrace();
        } catch (NumberFormatException nfe) {
            if (popup) {
                JOptionPane.showMessageDialog(new JInternalFrame(), "The regular expression did not match with the given commands.","Incorrect regular expression", JOptionPane.INFORMATION_MESSAGE);
                //nfe.printStackTrace();
            }
        }
    }
}
