import java.util.ArrayList;
//Tile class
public class Tile {
    
    //Tile parameters
    private int numBombs;
    private boolean flipped;
    private boolean bomb;
    private Tile[][] board;
    private int xPos;
    private int yPos;
    private ArrayList<Tile> neighbors;
    private int displayValue;
    
    //Constructor
    public Tile(int x, int y, Tile[][] boardInstance, boolean isBomb) {
        this.numBombs = -1;
        this.xPos = x;
        this.yPos = y;
        this.board = boardInstance;
        this.bomb = isBomb;
        this.flipped = false;
        this.neighbors = new ArrayList<Tile>();
    }
    
    //Finds all neighbors of tile on the board
    public void setNeighbors() {
        if (!this.isBomb()) {
            for (int row = -1; row <= 1; row++) {
                for (int col = -1; col <= 1; col++) {
                    int x = row + yPos;
                    int y = col + xPos;
                    if (!((x == xPos) && (y == yPos))) {
                        int width = board[0].length;
                        int height = board.length;
                        if (y >= 0 && y < height && x >= 0 && x < width) {
                            neighbors.add(board[y][x]);
                        }
                    }
                }
            }
        }
    }
    
    //Finds the number of bombs around the tile
    public int findNumBombs() {
        if (!this.isBomb()) {
            int count = 0;
            for (Tile t : neighbors) {
                if (t.isBomb()) {
                    count++;
                }
            }
            return count;
        }
        return -1;
    }
    
    //Flips the tile
    public void flipTile() {
        this.flipped = true;
    }
    
    //Unflips tile
    public void unflipTile() {
        this.flipped = false;
    }
    
    //Returns number of bombs around tile
    public int getNumBombs() {
        return numBombs;
    }
    
    //sets the number of bombs around the tile
    public void setNumBombs(int a) {
        numBombs = a;
    }
    
    //checks if tile is flipped
    public boolean isFlipped() {
        return flipped;
    }
    
    //checks if tile is a bomb
    public boolean isBomb() {
        return bomb;
    }
    
    public void setBomb(boolean bombOrNot) {
        bomb = bombOrNot;
    }
    
    //gets x position of the tile
    public int getXPos() {
        return xPos;
    }
    
    //gets y position of the tile
    public int getYPos() {
        return yPos;
    }
    
    //gets the neighbors of the tile
    public ArrayList<Tile> getNeighbors() {
        return neighbors;
    }
    
    //gets the displayvalue of the tile
    public int getDisplayValue() {
        return displayValue;
    }

}
