package net.sloshy.aoc.common;

/**
 * Generic record for row/column like data storage.
 * <br>
 * Includes some helpful methods
 *
 * @param x x coordinate
 * @param y y coordinate
 */
public record Coordinate(int x, int y) {
    /**
     * Checks if the coordinate is within set bounds
     * @param xBound x boundary
     * @param yBound y boundary
     * @return true if object is in bounds
     */
    public boolean inBounds(int xBound, int yBound) {
        if (x < 0)
            return false;
        if (y < 0)
            return false;
        if (x >= xBound)
            return false;
        if (y >= yBound)
            return false;

        return true;
    }
}
