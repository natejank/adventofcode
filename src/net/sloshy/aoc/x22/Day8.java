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
        LEFT(true, false),
        RIGHT(true, true),
        UP(false, false),
        DOWN(false, true),
        ;
        public final boolean horizontal;
        public final boolean endFacing;
        Direction(boolean horizontal, boolean endFacing) {
            this.horizontal = horizontal;
            this.endFacing = endFacing;
        }
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
                if (isVisible(row, column))
                    visible++;
            }
        }
        return visible;
    }

    public int part2() {
        int scenicScore = 0;
        for (int row = 0; row < map.size(); row++) {
            for (int column = 0; column < map.get(row).size(); column++) {
                int up = scenicScore(row, column, Direction.UP);
                int down = scenicScore(row, column, Direction.DOWN);
                int left = scenicScore(row, column, Direction.LEFT);
                int right = scenicScore(row, column, Direction.RIGHT);

                scenicScore = Math.max(scenicScore, up * down * left * right);
            }
        }
        return scenicScore;
    }

    private boolean isVisible(int row, int column) {
        if (isEdge(row, column))
            return true;

        boolean   left = isShortest(row, column, Direction.LEFT);
        boolean  right = isShortest(row, column, Direction.RIGHT);
        boolean     up = isShortest(row, column, Direction.UP);
        boolean   down = isShortest(row, column, Direction.DOWN);

        return left | right | up | down;
    }

    private boolean isEdge(int row, int column) {
        return (row == 0 || row == ROWS || column == 0 || column == COLUMNS);
    }

    private boolean isShortest(int row, int column, Direction direction) {
        int value = map.get(row).get(column);
        int dimension = direction.horizontal ? column : row;

        // welcome to lambda hell bby ;)
        Function<Integer, Integer> cell = (Integer i) -> direction.horizontal ? map.get(row).get(i) : map.get(i).get(column);

        for (int i = dimension; inBounds(i, direction);) {
            if (cell.apply((direction.endFacing ? ++i : --i)) >= value) {
                return false;
            }
        }
        return true;
    }

    private int scenicScore(int row, int column, Direction direction) {
        int score = 0;
        int dimension = direction.horizontal ? column : row;
        int value = map.get(row).get(column);

        if (isEdge(row, column))
            return 0;

        // welcome to lambda hell bby ;)
        Function<Integer, Integer> cell = (Integer i) -> direction.horizontal ? map.get(row).get(i) : map.get(i).get(column);

        // while in bounds
        for (int i = dimension; inBounds(i, direction);) {
            score++;
            if (cell.apply((direction.endFacing ? ++i : --i)) >= value) {
                // if the tree we just counted is taller, stop!
                break;
            }
        }
        return score;

    }

    private boolean inBounds(int position, Direction direction) {
        if (direction.endFacing) {
            return position < (direction.horizontal ? COLUMNS - 1 : ROWS - 1);
        } else {
            return position > 0;
        }
    }

    private static int[] getCoord(int row, int column) {
        // the int constructor is just really annoying to type :)
        return new int[]{row, column};
    }
}
