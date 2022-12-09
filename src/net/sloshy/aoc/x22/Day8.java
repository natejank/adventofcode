package net.sloshy.aoc.x22;

import net.sloshy.aoc.common.Utilities;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

public class Day8 {
    public static void main(String[] args) {
        Day8 day8 = new Day8(Utilities.getContent(args[0], (String line) -> {
            var row = new ArrayList<Integer>(line.length());
            for (char c : line.toCharArray())
                row.add(Character.getNumericValue(c));
            return row;
        }));

        Utilities.printResult(day8.part1(), day8.part2());
    }

    private enum Direction {
        LEFT,
        RIGHT,
        UP,
        DOWN
    }

    private final List<List<Integer>> map;
    private final int ROWS;
    private final int COLUMNS;

    public Day8(List<List<Integer>> map) {
        this.map = map;
        ROWS = map.size();
        COLUMNS = map.get(0).size();
    }

    public int part1() {
        int visible = 0;
        for (int row = 0; row < map.size(); row++) {
            for (int column = 0; column < map.get(row).size(); column++) {
                if (row == 2 && column == 2)
                    row = row;
                if (isVisible(row, column))
                    visible++;
            }
        }
        return visible;
    }

    public int part2() {
        return 0;
    }

    public boolean isVisible(int row, int column) {
        if (isEdge(row, column))
            return true;

        boolean   left = isShortest(row, column, Direction.LEFT);
        boolean  right = isShortest(row, column, Direction.RIGHT);
        boolean     up = isShortest(row, column, Direction.UP);
        boolean   down = isShortest(row, column, Direction.DOWN);

        return left | right | up | down;
    }

    public boolean isEdge(int row, int column) {
        return (row == 0 || row == ROWS || column == 0 || column == COLUMNS);
    }

    public List<int[]> getNeighbors(int row, int column) {
        List<int[]> neighbors = new LinkedList<>();
        if (row != 0)
            neighbors.add(getCoord(row - 1, column));
        if (row != ROWS)
            neighbors.add(getCoord(row + 1, column));
        if (column != 0)
            neighbors.add(getCoord(row, column - 1));
        if (column != COLUMNS)
            neighbors.add(getCoord(row, column + 1));
        return neighbors;
    }

    public boolean isShortest(int row, int column, Direction direction) {
        int value = map.get(row).get(column);
        boolean horizontal = direction == Direction.LEFT || direction == Direction.RIGHT;
        boolean toEnd = direction == Direction.DOWN || direction == Direction.RIGHT;
        int dimension = horizontal ? column : row;

        // welcome to lambda hell bby ;)
        Function<Integer, Boolean> bounds = toEnd ?
                (Integer i) -> i < (horizontal ? COLUMNS - 1 : ROWS - 1) :
                (Integer i) -> i > 0;
        Function<Integer, Integer> cell = (Integer i) -> horizontal ? map.get(row).get(i) : map.get(i).get(column);

        for (int i = dimension; bounds.apply(i);) {
            if (cell.apply((toEnd ? ++i : --i)) >= value) {
                return false;
            }
        }
        return true;
    }

    private static int[] getCoord(int row, int column) {
        // the int constructor is just really annoying to type :)
        return new int[]{row, column};
    }
}
