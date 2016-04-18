package student.Escapology;

import game.Tile;

/**
 * Heuristic calculations used in escape algorithms
 */

/* package */ abstract class Heuristic {

    private Heuristic() {}

    /**
     * Get the
     * <a href="http://theory.stanford.edu/~amitp/GameProgramming/Heuristics.html#manhattan-distance">Manhattan</a>
     * distance between 2 Tiles
     *
     * @param a the starting Tile
     * @param b the destination Tile
     * @return the Manhattan distance between 2 Tiles
     */
    /* package */ static int manhattanDistance(Tile a, Tile b) {
        return (Math.abs(a.getColumn() - b.getColumn()) + Math.abs(a.getRow() - b.getRow()));
    }
}
