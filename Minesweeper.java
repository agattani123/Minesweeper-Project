import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

//Class for minesweeper game
public class Minesweeper {
    
    //Minesweeper parameters
    private Tile[][] board;
    private int gameOver;
    private int safeTiles;
    private LinkedList<Tile> flipTrack;
    
    //Constructor
    public Minesweeper() {
        reset();
    }
    
    //Constructor for test, the boolean means nothing
    public Minesweeper(boolean b) {
        resetForTest();
    }
    
    //Flips a tile. Makes sure to end game if a bomb is flipped or the last safe tile
    //is flipped
    public boolean flip(int r, int c) {
        if (board[r][c].isFlipped() || gameOver == -1 || gameOver == 1) {
            return false;
        }
        board[r][c].flipTile();
        flipTrack.add(board[r][c]);
        if (board[r][c].isBomb()) {
            gameOver = -1;
        } else {
            if (board[r][c].getNumBombs() == 0) {
                ArrayList<Tile> neighbors = board[r][c].getNeighbors();
                for (Tile t : neighbors) {
                    int x = t.getXPos();
                    int y = t.getYPos();
                    flip(x, y);
                }
            }
            safeTiles--;
        }
        if (safeTiles == 0) {
            gameOver = 1;
        }
        return true;
    }
    
    //Unflips the most recently flipped tile. Will only unflip if the most recent tile is a bomb.
    //Allows player to keep playing even after loss
    public boolean unflip() {
        Tile tile = flipTrack.getLast();
        if (!tile.isFlipped()) {
            return false;
        }
        if (tile.isBomb()) {
            tile.unflipTile();
            flipTrack.remove(tile);
            gameOver = 0;
            return true;
        }
        return false;
    }
    
    //Resets game
    public void reset() {
        board = new Tile[8][8];
        safeTiles = 0;
        board = generateBombs();
        gameOver = 0;
        flipTrack = new LinkedList<Tile>();
    }
    
  //Resets game
    public void resetForTest() {
        board = new Tile[8][8];
        safeTiles = 0;
        board = generateBombsforTest();
        gameOver = 0;
        flipTrack = new LinkedList<Tile>();
    }
    
    //Generates the tiles for the game. Generates 10 bombs and 54 safe tiles.
    public Tile[][] generateBombs() {
        Random rand = new Random();
        int count = 0;
        while (count < 10) {
            int row = rand.nextInt(8);
            int col = rand.nextInt(8);
            if (board[row][col] == null) {
                Tile tile = new Tile(row, col, board, true);
                board[row][col] = tile;
                count++;
            }
        }
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (board[row][col] == null) {
                    Tile tile = new Tile(row, col, board, false);
                    board[row][col] = tile;
                    safeTiles++;
                }
            }
        }
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col ++) {
                board[row][col].setNeighbors();
                board[row][col].setNumBombs(board[row][col].findNumBombs());
            }
        }
        return board;
    }
    
    //Generating bombs in a way that makes testing easy
    public Tile[][] generateBombsforTest() {
        int row = 0;
        for (int col = 0; col < 8; col++) {
            if (board[row][col] == null) {
                Tile tile = new Tile(row, col, board, true);
                board[row][col] = tile;
            }
        }
        for (int row1 = 0; row1 < 8; row1++) {
            for (int col = 0; col < 8; col++) {
                if (board[row1][col] == null) {
                    Tile tile = new Tile(row1, col, board, false);
                    board[row1][col] = tile;
                    safeTiles++;
                }
            }
        }
        for (int row1 = 0; row1 < 8; row1++) {
            for (int col = 0; col < 8; col ++) {
                board[row1][col].setNeighbors();
                board[row1][col].setNumBombs(board[row1][col].findNumBombs());
            }
        }
        return board;
    }
    
    //Gets a tile given positions
    public Tile getTile(int x, int y) {
        return board[x][y];
    }
    
    //Gets the result of the game currently
    public int gameResult() {
        return gameOver;
    }
    
    //Prints the board with all values shown
    public void printBoard() {

        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[0].length; col++) {
                System.out.print(" " + board[row][col].getNumBombs());
            }
            System.out.println();
        }
    }
    
    //returns the number of remaining safe tiles
    public int getSafeTiles() {
        return safeTiles;
    }
    
    //Prints the outcome of the game
    public void gameOutcome() {
        String s = String.valueOf(gameOver);
        System.out.println(s);
    }
    
    //Prints the number of safeTiles remaining
    public void safeTileSize() {
        System.out.print(" " + safeTiles);
    }
    
    public Tile[][] getBoard() {
        return board;
    }
    
    //Main method
    public static void main(String[] args) {
        Minesweeper t = new Minesweeper();
        t.printBoard();
        t.flip(7, 0);
        t.flip(7, 1);
        t.flip(7, 2);
        t.flip(7, 3);
        t.flip(7, 4);
        t.flip(7, 5);
        t.flip(7, 6);
        t.gameOutcome();
        t.flip(7, 7);
        t.gameOutcome();

    }
}
