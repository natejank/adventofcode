package net.sloshy.aoc.x22;

import net.sloshy.aoc.common.Coordinate;
import net.sloshy.aoc.common.Utilities;

import java.util.*;

public class Day12 {
    public static void main(String[] args) {
        List<char[]> input = Utilities.getContent(args[0], String::toCharArray);
        char[][] map = new char[input.size()][input.get(0).length];
        Coordinate start = null;
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                map[y][x] = input.get(y)[x];
                if (map[y][x] == Position.STARTING_CHAR)
                    start = new Coordinate(x, y);
            }
        }
        
        var day12 = new Day12(map, start);

        Utilities.printResult(day12.part1(), 0);
    }

    private Coordinate start;
    private char[][] map;
    public Day12(char[][] map, Coordinate start) {
        this.map = map;
        this.start = start;
    }

    public int part1() {
        Solver solver = new Solver();
        List<Position> solution = solver.solve(new Position(map, start));
        if (solution != null)
            // don't include the final step in the count
            return solution.size() - 1;
        else
            throw new RuntimeException("No solution!");
    }

    private static class Solver {
        private final Map<Position, Position> adjacencyMap;

        public Solver() {
            adjacencyMap = new HashMap<>();
        }

        public List<Position> solve(Position initial) {
            Queue<Position> queue = new LinkedList<>();
            Position current;

            queue.add(initial);
            adjacencyMap.put(initial, null);

            while(!queue.isEmpty()) {
                current = queue.remove();
                if (current.isGoal()) {
                    return resolveParentChain(current);
                } else {
                    List<Position> neighbors = current.getChildren();
                    for (Position neighbor : neighbors) {
                        // if we haven't seen this one already
                        if (!adjacencyMap.containsKey(neighbor)) {
                            queue.add(neighbor);
                            adjacencyMap.put(neighbor, current);
                        }
                    }
                }
            }
            return null;
        }


        /**
         * Create a linked list of configurations to go from start to child
         *
         * @param end the end of the chain to be resolved
         * @return The chain of succession to get the end configuration
         */
        private List<Position> resolveParentChain(Position end) {
            List<Position> chain = new LinkedList<>();
            Position next = end;
            while (next != null) {  // the first item has a parent of null
                chain.add(0, next);
                next = adjacencyMap.get(next);  // get the parent
            }
            return chain;
        }
    }

    private static class Position {
        private final char[][] map;
        private final Coordinate position;
        private final int X_BOUND;
        private final int Y_BOUND;

        public static final char STARTING_CHAR = 'S';
        public static final char GOAL_CHAR = 'E';

        /**
         * Initial constructor; sets coordinate to 0
         * @param map the map
         */
        public Position(char[][] map, Coordinate start) {
            this.map = map;
            this.X_BOUND = map[0].length;
            this.Y_BOUND = map.length;

            position = start;
        }

        /**
         * Child copy constructor
         * @param parent the parent position
         * @param position the new coordinate
         */
        private Position(Position parent, Coordinate position) {
            this.map = parent.map;
            this.X_BOUND = parent.X_BOUND;
            this.Y_BOUND = parent.Y_BOUND;
            this.position = position;
        }

        /**
         * Gets all valid children
         * @return
         */
        public List<Position> getChildren() {
            List<Position> children = new LinkedList<>();

            // we can move 1 position in any direction
            children.add(new Position(
                    this,
                    new Coordinate(position.x() + 1, position.y())
            ));
            children.add(new Position(
                    this,
                    new Coordinate(position.x() - 1, position.y())
            ));
            children.add(new Position(
                    this,
                    new Coordinate(position.x(), position.y() + 1)
            ));
            children.add(new Position(
                    this,
                    new Coordinate(position.x(), position.y() - 1)
            ));

            // validation and pruning
            for (int i = 0; i < children.size();) {
                Position child = children.get(i);
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
        private char getCurrentPosition() {
            return getTile(position);
        }

        /**
         * @return height of the current position
         */
        private int getCurrentHeight() {
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
         * @param coordinate coordinate to get
         * @return char at that coordinate
         */
        private char getTile(Coordinate coordinate) {
            return map[coordinate.y()][coordinate.x()];
        }

        /**
         * Gets coordinate as an integer
         * @param coordinate coordinate to get
         * @return height of the coordinate from 0-25
         */
        private int getHeight(Coordinate coordinate) {
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
            if (other instanceof Position otherPosition) {
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