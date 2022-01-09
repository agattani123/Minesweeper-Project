/**
 * CIS 120 HW09 - TicTacToe Demo
 * (c) University of Pennsylvania
 * Created by Bayley Tuch, Sabrina Green, and Nicolas Corona in Fall 2020.
 */

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

/**
 * This class sets up the top-level frame and widgets for the GUI.
 * 
 * This game adheres to a Model-View-Controller design framework.  This
 * framework is very effective for turn-based games.  We STRONGLY 
 * recommend you review these lecture slides, starting at slide 8, 
 * for more details on Model-View-Controller:  
 * https://www.seas.upenn.edu/~cis120/current/files/slides/lec37.pdf
 * 
 * In a Model-View-Controller framework, Game initializes the view,
 * implements a bit of controller functionality through the reset
 * button, and then instantiates a GameBoard.  The GameBoard will
 * handle the rest of the game's view and controller functionality, and
 * it will instantiate a TicTacToe object to serve as the game's model.
 */
public class Game implements Runnable {
    public void run() {
        // NOTE: the 'final' keyword denotes immutability even for local variables.

        // Top-level frame in which game components live
        final JFrame frame = new JFrame("Minesweeper");
        frame.setLocation(400, 400);

        // Status panel
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel status = new JLabel("Setting up...");
        status_panel.add(status);
        
        //Instructions panel
        final JPanel instruction_panel = new JPanel();
        frame.add(instruction_panel, BorderLayout.EAST);
        final JLabel instructions_label = new JLabel("<html>How to play:<br/>This game is similar "
                + "to"
                + " "
                + "the classic minesweeper game.<br/> There are exactly 10 bombs on the board.<br/>"
                + " Flip all the tiles that aren't bombs to win! <br/>"
                + "The reset button will restart the game. <br/>The undo "
                + "button will undo a move if you ever make a mistake. <br/>The timer at the bottom"
                + " indicates how much time you are taking to win the game.<br/> The numMoves "
                + "tells "
                + "you how many moves you have used to win the game.<br/> Try to win in the "
                + "fastest time possible with the least amount of moves!<html>");
        instruction_panel.add(instructions_label);
        
        //High scores panel
        final JPanel score_panel = new JPanel();
        frame.add(score_panel, BorderLayout.NORTH);
        final JLabel leaderboard = new JLabel("leaderboard");
        score_panel.add(leaderboard);

        // Game board
        final GameBoard board = new GameBoard(status, leaderboard);
        frame.add(board, BorderLayout.CENTER);
        

        // Reset button
        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.WEST);

        // Note here that when we add an action listener to the reset button, we define it as an
        // anonymous inner class that is an instance of ActionListener with its actionPerformed()
        // method overridden. When the button is pressed, actionPerformed() will be called.
        final JButton reset = new JButton("Reset");
        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                board.reset();
            }
        });
        control_panel.add(reset);

        //Undo button
        final JButton undo = new JButton("Undo");
        undo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                board.undo();
            }
        });
        control_panel.add(undo);
        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Start the game
        board.reset();
    }

    /**
     * Main method run to start and run the game. Initializes the GUI elements specified in Game and
     * runs it. IMPORTANT: Do NOT delete! You MUST include this in your final submission.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Game());
    }
}
