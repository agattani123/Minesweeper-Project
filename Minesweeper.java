import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Minesweeper game logic class.
 * Handles the core game mechanics including board generation, tile flipping,
 * game state management, and win/loss conditions.
 */
public class Minesweeper {
    
    private static final Logger LOGGER = Logger.getLogger(Minesweeper.class.getName());
    
    private Tile[][] board;
    private int gameState;
    private int remainingSafeTiles;
    private LinkedList<Tile> moveHistory;
    
    /**
     * Default constructor.
     * Initializes a new game with random bomb placement.
     */
    public Minesweeper() {
        reset();
    }
    
    /**
     * Constructor for testing purposes.
     * Creates a game with predictable bomb placement for unit tests.
     * @param isTestMode flag to indicate test mode (value doesn't matter)
     */
    public Minesweeper(boolean isTestMode) {
        resetForTest();
    }
    
    /**
     * Flips a tile at the specified coordinates.
     * Handles game state changes and auto-reveals adjacent tiles with zero bombs.
     * @param row the row coordinate
     * @param col the column coordinate
     * @return true if the flip was successful, false if invalid or game is over
     */
    public boolean flip(int row, int col) {
        if (!isValidMove(row, col)) {
            return false;
        }
        
        Tile tile = board[row][col];
        tile.flipTile();
        moveHistory.add(tile);
        
        if (tile.isBomb()) {
            gameState = GameConstants.GAME_LOST;
            LOGGER.log(Level.INFO, "Game lost - bomb flipped at ({0}, {1})", new Object[]{row, col});
        } else {
            remainingSafeTiles--;
            
            // Auto-reveal adjacent tiles if this tile has no neighboring bombs
            if (tile.getNumBombs() == 0) {
                revealAdjacentTiles(tile);
            }
            
            // Check for win condition
            if (remainingSafeTiles == 0) {
                gameState = GameConstants.GAME_WON;
                LOGGER.log(Level.INFO, "Game won - all safe tiles revealed");
            }
        }
        
        return true;
    }
    
    /**
     * Checks if a move is valid.
     * @param row the row coordinate
     * @param col the column coordinate
     * @return true if the move is valid
     */
    private boolean isValidMove(int row, int col) {
        return isValidCoordinate(row, col) && 
               !board[row][col].isFlipped() && 
               gameState == GameConstants.GAME_IN_PROGRESS;
    }
    
    /**
     * Checks if coordinates are within board bounds.
     * @param row the row coordinate
     * @param col the column coordinate
     * @return true if coordinates are valid
     */
    private boolean isValidCoordinate(int row, int col) {
        return row >= 0 && row < GameConstants.BOARD_ROWS && 
               col >= 0 && col < GameConstants.BOARD_COLS;
    }
    
    /**
     * Reveals all adjacent tiles recursively.
     * @param tile the tile whose neighbors should be revealed
     */
    private void revealAdjacentTiles(Tile tile) {
        ArrayList<Tile> neighbors = tile.getNeighbors();
        for (Tile neighbor : neighbors) {
            int x = neighbor.getXPos();
            int y = neighbor.getYPos();
            flip(x, y);
        }
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
