package net.sloshy.aoc.common;

public record Coordinate(int x, int y) {
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
