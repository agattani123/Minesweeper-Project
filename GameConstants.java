/**
 * Constants class for the Minesweeper game.
 * Contains all game configuration values in one centralized location.
 */
public final class GameConstants {
    
    public static final int BOARD_ROWS = 8;
    public static final int BOMB_COUNT = 10;

    
    // Board dimensions
    public static final int BOARD_ROWS = 8;
    public static final int BOARD_COLS = 8;
    public static final int TOTAL_TILES = BOARD_ROWS * BOARD_COLS;
    
    // Game configuration
    public static final int BOMB_COUNT = 10;
    public static final int SAFE_TILE_COUNT = TOTAL_TILES - BOMB_COUNT;
    
    // UI dimensions
    public static final int BOARD_WIDTH = 400;
    public static final int BOARD_HEIGHT = 400;
    public static final int CELL_SIZE = 50;
    
    // Timer configuration
    public static final int TIMER_DELAY_MS = 1000;
    
    // Game states
    public static final int GAME_IN_PROGRESS = 0;
    public static final int GAME_WON = 1;
    public static final int GAME_LOST = -1;
    
    // File paths
    public static final String SCORES_FILE_PATH = "Files/FastestTime.txt";
    public static final String SCORES_DIRECTORY = "Files";
    
    // Leaderboard configuration
    public static final int MAX_HIGH_SCORES = 5;
    
    // Private constructor to prevent instantiation
    private GameConstants() {
        throw new AssertionError("Utility class should not be instantiated");
    }
}