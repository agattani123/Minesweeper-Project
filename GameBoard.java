import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.TreeMap;


@SuppressWarnings("serial")
public class GameBoard extends JPanel {

    private Minesweeper t; // model for the game
    private JLabel status; // current status text
    private JLabel leaderBoard;

    // Game constants
    public static final int BOARD_WIDTH = 400;
    public static final int BOARD_HEIGHT = 400;
    
    private int timerDelay;
    private final Timer myTimer;
    private long startTime;
    private long gameTime;
    private int numMoves;
    
    private TreeMap<Integer, Integer> scores;
    private LinkedList<Integer> highscores = new LinkedList<Integer>();

    

    /**
     * Initializes the game board.
     * @param leaderboard 
     */
    public GameBoard(JLabel statusInit, JLabel leaderboardInit) {
        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Enable keyboard focus on the court area.
        // When this component has the keyboard focus, key events are handled by its key listener.
        setFocusable(true);
        
        t = new Minesweeper(); // initializes model for the game
        status = statusInit; // initializes the status JLabel
        leaderBoard = leaderboardInit;
        /*
         * Listens for mouseclicks.  Updates the model, then updates the game board
         * based off of the updated model.
         */
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point p = e.getPoint();
                
                // updates the model given the coordinates of the mouseclick
                t.flip(p.y / 50, p.x / 50);
                if (!(t.gameResult() == 1 || t.gameResult() == -1)) {
                    numMoves++;
                }
                updateStatus(); // updates the status JLabel
                repaint(); // repaints the game board
            }
        });
        
        timerDelay = 1000;
        myTimer = new Timer(timerDelay, gameTimer);
        startTime = System.currentTimeMillis();
        gameTime = 0;
        myTimer.start();
        numMoves = 0;
    }
    
    ActionListener gameTimer = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            gameTime = (System.currentTimeMillis() - startTime) / 1000; 
            status.setText("Keep going!   Time: " + gameTime + "  NumMoves: " + numMoves);
        }
    };
    

    /**
     * (Re-)sets the game to its initial state.
     */
    public void reset() {
        if (t.gameResult() == 1) {
            write();
        }
        updateScores();
        updateHighScores();
        leaderBoard.setText(toStringHighScores());
        t.reset();
        startTime = System.currentTimeMillis();
        gameTime = 0;
        myTimer.restart();
        numMoves = 0;
        repaint();
        
        // Makes sure this component has keyboard/mouse focus
        requestFocusInWindow();
    }
    
    public void undo() {
        if (t.unflip()) {
            numMoves += 2;
            myTimer.start();
        }
        repaint();
        requestFocusInWindow();
    }

    /**
     * Updates the JLabel to reflect the current state of the game.
     */
    private void updateStatus() {
        
        int gameStatus = t.gameResult();
        if (gameStatus == 1) {
            myTimer.stop();
            status.setText("You won!!!  Time: " + gameTime + "  NumMoves: " + (numMoves + 1));
        } else if (gameStatus == -1) {
            myTimer.stop();
            status.setText("You lost   Time: " + gameTime + "  NumMoves: " + (numMoves + 1));
        } 
    }

    /**
     * Draws the game board.
     * 
     * There are many ways to draw a game board.  This approach
     * will not be sufficient for most games, because it is not 
     * modular.  All of the logic for drawing the game board is
     * in this method, and it does not take advantage of helper 
     * methods.  Consider breaking up your paintComponent logic
     * into multiple methods or classes, like Mushroom of Doom.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Draws board grid
        g.drawLine(50, 0, 50, 400);
        g.drawLine(100, 0, 100, 400);
        g.drawLine(150, 0, 150, 400);
        g.drawLine(200, 0, 200, 400);
        g.drawLine(250, 0, 250, 400);
        g.drawLine(300, 0, 300, 400);
        g.drawLine(350, 0, 350, 400);
        g.drawLine(0, 50, 400, 50);
        g.drawLine(0, 100, 400, 100);
        g.drawLine(0, 150, 400, 150);
        g.drawLine(0, 200, 400, 200);
        g.drawLine(0, 250, 400, 250);
        g.drawLine(0, 300, 400, 300);
        g.drawLine(0, 350, 400, 350);
        
        // Draws X's and O's
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Tile tile = t.getTile(row, col);
                if (tile.isFlipped() && !tile.isBomb()) {
                    String s = String.valueOf(tile.getNumBombs());
                    g.drawString(s, 50 * col + 25, 50 * row + 25);
                }
                if (tile.isFlipped() && tile.isBomb()) {
                    g.drawLine(50 * col, 50 * row, 50 * col + 50, 50 * row + 50);
                    g.drawLine(50 * col, 50 * row + 50, 50 * col + 50, 50 * row);
                } 
            }
        }
    }
    
    //Writes the score of the current game into the FastesTime.txt file
    public void write() {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("Files/FastestTime.txt", true));
            bw.write("" + gameTime + " " + (numMoves + 1));
            bw.newLine();
            bw.flush();
            bw.close();
        } catch (IOException e) {
            System.out.println("Could not write");
        }
    }
    
    //Reads the FastestTime.txt file and places all the game outcomes into a TreeMap called scores
    //, which has the time to win as the key and the number of moves as the value.
    public void updateScores() {
        BufferedReader br = null;
        boolean hasValue = true;
        TreeMap<Integer, Integer> temp = new TreeMap<Integer, Integer>();
        try {
            br = new BufferedReader(new FileReader("Files/FastestTime.txt"));
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            hasValue = false;
        }
        if (hasValue) {
            try {
                String line = br.readLine();
                line = br.readLine();
                while (line != null) {
                    String[] split = line.split(" ");
                    int dataTime = 0;
                    int dataMoves = 0;
                    try {
                        dataTime = Integer.parseInt(split[0]);
                        dataMoves = Integer.parseInt(split[1]);
                    } catch (NumberFormatException e) {
                        System.out.println("No such data");
                    }
                    if (dataTime != 0 && dataMoves != 0) {
                        temp.put(dataTime, dataMoves);
                    }
                    line = br.readLine();
                }
            } catch (IOException e) {
                System.out.println("Could not update");
            }
            try {
                br.close();
            } catch (IOException e) {
                System.out.println("Could not close");
            }
        }
        scores = temp;
    }
    
    //Orders the values stored in TreeMap scores from least to greatest then picks out the 5
    //smallest values to be the high scores.
    public void updateHighScores() {
        Object[] temp = scores.keySet().toArray();
        LinkedList<Integer> tempscores = new LinkedList<Integer>();
        Arrays.sort(temp);
        int i = 0;
        while (i < 5 && i < temp.length) {
            int x = (int) temp[i];
            tempscores.add(x);
            i++;
        }
        highscores = tempscores;
    }
    
    //Returns the number of moves for a particular game given the time it took to finish that
    //game
    public int getDataMoves(int dataTime) {
        return scores.get(dataTime);
    }
    
    //Converts the values in the high scores list to a string
    public String toStringHighScores() {
        String s = "Top 5 Scores: ";
        for (int i = 0; i < highscores.size(); i++) {
            s += highscores.get(i) + " " + scores.get(highscores.get(i)) + ", ";
        }
        return s;
    }
    

    /**
     * Returns the size of the game board.
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
    }
}


