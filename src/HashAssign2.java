/*
HashAssign2.java
Sat Arora
Question 2 on HashTable assignment - different spots on a map on Windsor have different emotions.
This program allows a user to click on the map and displays the emotions at the spots within a radius of
10 units from the spots clicked, converting each type of emotion to an R,G,B value and displaying that color.
 */
//importing necessary packages
import java.awt.geom.Ellipse2D;
import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.Timer;

//class that loads all data and holds the frame
public class HashAssign2 extends JFrame  {
    static HashTable<creep> h = new HashTable<creep>(); //storing hashtable of all creepers
    GamePanel game; //holding the panel used
    Timer myTimer; //timer kept from template, not actually used
    public HashAssign2() {
        super("HashAssign2.java");
        setSize(800,600); //window size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myTimer = new Timer(10, new TickListener());
        game = new GamePanel(this,h); //initiating the panel
        add(game);
        setResizable(false);
        setVisible(true);
    }
    public void start() {myTimer.start();} //template start method which starts the timer, not actually used
    class TickListener implements ActionListener{ //class inside HashAssign2 holds the actionPerformed method
        public void actionPerformed(ActionEvent evt){ //checks if something happened
            if(game!= null){
                game.repaint();
            }
        }
    }
    public static void main(String[] args) throws IOException {
        load(); //loads all creepers from the "creeper.txt" file
        new HashAssign2(); //initiating the frame
    }
    public static void load() throws IOException { //method that loads all creepers from the "creeper.txt" file
        Scanner inFile = new Scanner(new BufferedReader(new FileReader("creeper.txt"))); //getting the scanner from the file
        int x,y,love,happiness,excitement; //variables that store all the different data per line in creeper.txt
        while (inFile.hasNext()) { //runs while there is still data in the file
            //next 5 lines get the data
            x = inFile.nextInt();
            y = inFile.nextInt();
            love = inFile.nextInt();
            happiness = inFile.nextInt();
            excitement = inFile.nextInt();
            creep possiblyACreep = h.get(x*1000+y); //retrieving the creep that has the same x and y coordinate (may not exist)
            if (possiblyACreep == null) { //if there is no object with the same x&y
                h.add(new creep(x,y,love,happiness,excitement)); //adding the new creep to the hashtable with the data
            }
            else {
                possiblyACreep.averageRGB(love,happiness,excitement); //averaging the values for all the creeps at that same position
            }
        }
    }
}
//object class that holds the data of each creeper
class creep {
    private final int x,y,code; //x&y are position coordinates, code is the hashCode of the object
    private double r,g,b; //holding the exact r,g,b color values that are used at the specific spot
    private int count; //count holds the number of creepers at that same spot
    public creep(int x, int y, int love, int happiness, int excitement) { //constructor
        this.x = x;
        this.y = y;
        r = color(love); //retrieving the r value with the conversion of love
        g = color(happiness); //retrieving the g value with the conversion of happiness
        b = color(excitement); //retrieving the b value with the conversion of excitement
        code = x*1000+y; //setting the hashCode to a unique value that is scaled by a factor larger than the width of the screen
        count = 1; //only one creeper at the same spot when uniquely initialized
    }
    public double color(int emoValue) { //conversion from emotion value (-100 --> 100) to color value (0 --> 255)
        return (double)((emoValue+100)*255)/200;
    }
    public void averageRGB(int love, int happiness, int excitement) { //averaging the color at the spot because there is another creeper at the same spot
        //next 3 lines all do weighted averages for each color value
        r = (double)(r*count+color(love))/(count+1);
        g = (double)(g*count+color(happiness))/(count+1);
        b = (double)(b*count+color(excitement))/(count+1);
        ++count; //one more creeper at the same spot
    }
    @Override
    public int hashCode() { return code; } //override hashCode method allows for easy retrieval given coordinates
    public int getR() {return (int)r;} //getting the integer red value
    public int getG() {return (int)g;} //getting the integer green value
    public int getB() {return (int)b;} //getting the integer blue value
    public int getX() {return x;} //getting the x coordinate of object
    public int getY() {return y;} //getting the y coordinate of object
}
//class that holds the panel
class GamePanel extends JPanel {
    private HashAssign2 mainFrame; //storing the panel
    private Image map; //image of the windsor map
    private HashTable<creep> table; //hashtable that was initialized in the HashAssign2 class
    private int cx,cy; //location of mouse pressed, stored as clicked x and y
    public static final int r=10; //radius clicked, set to 10
    public GamePanel(HashAssign2 m, HashTable<creep> h) { //constructor
        map = new ImageIcon("windsor.PNG").getImage();
        table = h;
        mainFrame = m;
        addMouseListener(new clickListener());
        cx = -10; //dummy x value that prevent displaying anything initially
        cy = -10; //dummy y value that prevent displaying anything initially
    }
    public void addNotify() { //template addNotify method
        super.addNotify();
        requestFocus();
        mainFrame.start();
    }
    public void paintComponent(Graphics g) {
        g.drawImage(map,0,0,null); //drawing the windsor map
        if (cx != -10 && cy != -10) { //checks if there was a click by testing the inequality to the dummy values
            Shape emotionCircle = new Ellipse2D.Double(cx-r,cy-r,2*r,2*r); //storing the ellipse that checks if every creep is within 10px of the clicked spot
            for (int x = cx-r; x <= cx+r; x++) { //nested for loop checking the 20*20 square around cx,cy , this is tangential to the circle so it encloses every point in the circle
                for (int y = cy - r; y <= cy + r; y++) {
                    if (emotionCircle.contains(x, y)) { //checking if the current x,y is within 10px of the cx,cy
                        creep possibleCreeper = table.get(x * 1000 + y); //getting the creep at x,y by using its hashCode, however it may not exist
                        if (possibleCreeper != null) { //checking if there is a creep at the spot
                            g.setColor(new Color(possibleCreeper.getR(), possibleCreeper.getG(), possibleCreeper.getB())); //setting the color to the creep's color
                            g.fillRect(x, y, 1, 1); //drawing the 1*1 px at the spot
                        }
                    }
                }
            }
            //next 5 lines draw a good oval around the circle, used for convenience
            g.setColor(Color.black);
            g.drawOval(cx-r-1,cy-r-1,r*2+2,r*2+2);
            g.drawOval(cx-r,cy-r,r*2+2,r*2+2);
            g.drawOval(cx-r,cy-r-1,r*2+2,r*2+2);
            g.drawOval(cx-r-1,cy-r,r*2+2,r*2+2);
        }
    }
    class clickListener implements MouseListener{
        // ------------ MouseListener ------------------------------------------
        public void mouseEntered(MouseEvent e) {}
        public void mouseExited(MouseEvent e) {}
        public void mouseReleased(MouseEvent e) {}
        public void mouseClicked(MouseEvent e){}
        public void mousePressed(MouseEvent e){
            cx = e.getX(); //storing the x coordinate of the location pressed
            cy = e.getY(); //storing the y coordinate of the location pressed
        }
    }
}