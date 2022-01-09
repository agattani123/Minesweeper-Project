import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

public class Tests {

    @Test
    public void testBombTiles() {
        Minesweeper m = new Minesweeper(true);
        Tile bombTile = m.getTile(0, 1);
        assertTrue(bombTile.isBomb());
        assertFalse(bombTile.isFlipped());
        bombTile.flipTile();
        assertTrue(bombTile.isFlipped());
        bombTile.unflipTile();
        assertFalse(bombTile.isFlipped());
        bombTile.setNeighbors();
        assertEquals(-1, bombTile.findNumBombs());
        assertEquals(0, bombTile.getXPos());
        assertEquals(1, bombTile.getYPos());
    }
    
    @Test
    public void testSafeTiles() {
        Minesweeper m = new Minesweeper(true);
        Tile[][] board1 = m.getBoard();
        Tile safeTile = m.getTile(1, 0);
        Tile bombTile1 = board1[0][0];
        Tile bombTile2 = board1[0][1];
        assertFalse(safeTile.isBomb());
        assertFalse(safeTile.isFlipped());
        safeTile.flipTile();
        assertTrue(safeTile.isFlipped());
        safeTile.unflipTile();
        assertFalse(safeTile.isFlipped());
        safeTile.setNeighbors();
        ArrayList<Tile> neighbors = safeTile.getNeighbors();
        neighbors.contains(bombTile1);
        neighbors.contains(bombTile2);
        assertEquals(2, safeTile.findNumBombs());
        assertEquals(1, safeTile.getXPos());
        assertEquals(0, safeTile.getYPos());
    }
    
    @Test
    public void testSafeTilesWithNoBombs() {
        Minesweeper m = new Minesweeper(true);
        Tile[][] board1 = m.getBoard();
        Tile safeTile = board1[7][7];
        assertFalse(safeTile.isBomb());
        safeTile.setNeighbors();
        assertEquals(0, safeTile.findNumBombs());
    }
    
    @Test
    public void testMinesweeperExplosion() {
        Minesweeper m = new Minesweeper(true);
        Tile[][] board1 = m.getBoard();
        m.flip(2, 0);
        assertTrue(board1[1][0].isFlipped());
        assertTrue(board1[1][1].isFlipped());
        assertTrue(board1[2][0].isFlipped());
        assertTrue(board1[2][1].isFlipped());
        assertTrue(board1[3][0].isFlipped());
        assertTrue(board1[3][1].isFlipped());
    }
    
    @Test
    public void testMinesweeperGameOverLoss() {
        Minesweeper m = new Minesweeper(true);
        m.flip(0, 0);
        assertEquals(-1,  m.gameResult());
        m.reset();
        assertEquals(0,  m.gameResult());
    }
    
    @Test
    public void testMinesweeperGameOverWin() {
        Minesweeper m = new Minesweeper(true);
        assertEquals(m.getSafeTiles(), 56);
        m.flip(1, 0);
        assertEquals(m.getSafeTiles(), 55);
        assertEquals(0,  m.gameResult());
        for (int i = 1; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                m.flip(i, j);
            }
        }
        assertEquals(1,  m.gameResult());
        m.reset();
        assertEquals(0,  m.gameResult());
    }
    
    @Test
    public void testMinesweeperUnflip() {
        Minesweeper m = new Minesweeper(true);
        Tile[][] board1 = m.getBoard();
        m.flip(1, 0);
        assertEquals(0,  m.gameResult());
        assertTrue(board1[1][0].isFlipped());
        assertFalse(m.unflip());
        assertEquals(0,  m.gameResult());
        m.flip(0, 0);
        assertEquals(-1,  m.gameResult());
        assertTrue(board1[0][0].isFlipped());
        assertTrue(m.unflip());
        assertEquals(0,  m.gameResult());
        assertFalse(board1[0][0].isFlipped());
    }
    
}
