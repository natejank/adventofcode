package net.sloshy.aoc.x22;

import net.sloshy.aoc.common.Coordinate;
import net.sloshy.aoc.common.Utilities;
import net.sloshy.aoc.common.search.SearchState;
import net.sloshy.aoc.common.search.bfs.Solver;

import java.util.*;

public class Day12 {
    public static void main(String[] args) {
        List<char[]> input = Utilities.getContent(args[0], String::toCharArray);
        char[][] map = new char[input.size()][input.get(0).length];
        Coordinate start = null;
        Coordinate goal = null;
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                map[y][x] = input.get(y)[x];
                if (map[y][x] == Part1State.STARTING_CHAR)
                    start = new Coordinate(x, y);
                else if (map[y][x] == Part1State.GOAL_CHAR)
                    goal = new Coordinate(x, y);
            }
        }

        var day12 = new Day12(map, start, goal);

        Utilities.printResult(day12.part1(), day12.part2());
    }

    private Coordinate start;
    private Coordinate goal;
    private char[][] map;

    public Day12(char[][] map, Coordinate start, Coordinate goal) {
        this.map = map;
        this.start = start;
        this.goal = goal;
    }

    public int part1() {
        Solver solver = new Solver();
        List<SearchState> solution = solver.solve(new Part1State(map, start));
        if (solution != null)
            // don't include the final step in the count
            return solution.size() - 1;
        else
            throw new RuntimeException("No solution!");
    }

    public int part2() {
        Solver solver = new Solver();
        List<SearchState> solution = solver.solve(new Part2State(map, goal));
        if (solution != null)
            // don't include the final step in the count
            return solution.size() - 1;
        else
            throw new RuntimeException("No solution!");
    }


    private static class Part2State extends Part1State {

        public static char GOAL_CHAR = 'a';

        public Part2State(char[][] map, Coordinate start) {
            super(map, start);
        }

        protected Part2State(Part1State parent, Coordinate position) {
            super(parent, position);
        }

        /**
         * @return all valid children
         */
        @Override
        public List<SearchState> getChildren() {
            List<SearchState> children = new LinkedList<>();

            // we can move 1 position in any direction
            children.add(new Part2State(
                    this,
                    new Coordinate(getPosition().x() + 1, getPosition().y())
            ));
            children.add(new Part2State(
                    this,
                    new Coordinate(getPosition().x() - 1, getPosition().y())
            ));
            children.add(new Part2State(
                    this,
                    new Coordinate(getPosition().x(), getPosition().y() + 1)
            ));
            children.add(new Part2State(
                    this,
                    new Coordinate(getPosition().x(), getPosition().y() - 1)
            ));

            // validation and pruning
            for (int i = 0; i < children.size(); ) {
                Part1State child = (Part1State) children.get(i);
                // we can't go out of bounds!
                if (!child.getPosition().inBounds(X_BOUND, Y_BOUND))
                    children.remove(i);
                    // we can't go down more than 1 position (we're going backwards)
                else if (getCurrentHeight() - child.getCurrentHeight() > 1)
                    children.remove(i);
                else
                    i++;
            }
            return children;
        }

        @Override
        public boolean isGoal() {
            return getCurrentPosition() == GOAL_CHAR;
        }
    }


    private static class Part1State implements SearchState {
        private final char[][] map;
        private final Coordinate position;
        protected final int X_BOUND;
        protected final int Y_BOUND;

        public static final char STARTING_CHAR = 'S';
        public static final char GOAL_CHAR = 'E';

        /**
         * Initial constructor; sets coordinate to 0
         *
         * @param map the map
         */
        public Part1State(char[][] map, Coordinate start) {
            this.map = map;
            this.X_BOUND = map[0].length;
            this.Y_BOUND = map.length;

            position = start;
        }

        /**
         * Child copy constructor
         *
         * @param parent   the parent position
         * @param position the new coordinate
         */
        protected Part1State(Part1State parent, Coordinate position) {
            this.map = parent.map;
            this.X_BOUND = parent.X_BOUND;
            this.Y_BOUND = parent.Y_BOUND;
            this.position = position;
        }

        /**
         * Gets all valid children
         *
         * @return
         */
        public List<SearchState> getChildren() {
            List<SearchState> children = new LinkedList<>();

            // we can move 1 position in any direction
            children.add(new Part1State(
                    this,
                    new Coordinate(position.x() + 1, position.y())
            ));
            children.add(new Part1State(
                    this,
                    new Coordinate(position.x() - 1, position.y())
            ));
            children.add(new Part1State(
                    this,
                    new Coordinate(position.x(), position.y() + 1)
            ));
            children.add(new Part1State(
                    this,
                    new Coordinate(position.x(), position.y() - 1)
            ));

            // validation and pruning
            for (int i = 0; i < children.size(); ) {
                Part1State child = (Part1State) children.get(i);
                // we can't go out of bounds!
                if (!child.getPosition().inBounds(X_BOUND, Y_BOUND))
                    children.remove(i);
                    // we can't go up more than 1 position
                else if (child.getCurrentHeight() - getCurrentHeight() > 1)
                    children.remove(i);
                else
                    i++;
            }
            return children;
        }

        /**
         * @return the current position as a char
         */
        protected char getCurrentPosition() {
            return getTile(position);
        }

        /**
         * @return height of the current position
         */
        protected int getCurrentHeight() {
            return getHeight(position);
        }

        /**
         * @return the current position as a coordinate
         */
        public Coordinate getPosition() {
            return position;
        }

        /**
         * @return true if the current position is the goal character
         */
        public boolean isGoal() {
            return getCurrentPosition() == GOAL_CHAR;
        }

        /**
         * Gets an arbitrary tile on the board
         * (I mostly made this so I wouldn't mix up x and y lol)
         *
         * @param coordinate coordinate to get
         * @return char at that coordinate
         */
        protected char getTile(Coordinate coordinate) {
            return map[coordinate.y()][coordinate.x()];
        }

        /**
         * Gets coordinate as an integer
         *
         * @param coordinate coordinate to get
         * @return height of the coordinate from 0-25
         */
        protected int getHeight(Coordinate coordinate) {
            char val = getTile(coordinate);
            if (val == STARTING_CHAR)
                val = 'a';
            else if (val == GOAL_CHAR)
                val = 'z';
            return (int) val - (int) 'a';
        }

        @Override
        public boolean equals(Object other) {
            boolean result;
            if (this == other)
                return true;
            if (other instanceof Part1State otherPosition) {
                result = position.equals(otherPosition.position);
                // compare map
                if (map.length == otherPosition.map.length) {
                    for (int y = 0; y < map.length; y++) {
                        if (map[y].length == otherPosition.map[y].length) {
                            for (int x = 0; x < map[y].length; x++) {
                                result &= map[y][x] == otherPosition.map[y][x];
                            }
                        } else result = false;
                    }
                } else result = false;
            } else result = false;
            return result;
        }

        @Override
        public int hashCode() {
            return Arrays.deepHashCode(map)
                    + position.hashCode();
        }
    }
}