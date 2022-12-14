package net.sloshy.aoc.x22;

import java.util.*;

import net.sloshy.aoc.common.Coordinate;
import net.sloshy.aoc.common.Utilities;

public class Day14 {
    public static void main(String[] args) {
        var day14 = new Day14(Utilities.getContent(args[0], (String s) -> {
            List<Coordinate> vertex = new ArrayList<>();
            var coords = s.split(" -> ");
            for (var coord : coords) {
                var coordinate = coord.split(",");
                vertex.add(new Coordinate(Integer.parseInt(coordinate[0]), Integer.parseInt(coordinate[1])));
            }
            return vertex;
        }));

        Utilities.printResult(day14.part1(), day14.part2());
    }

    private List<List<Coordinate>> input;
    private static final Coordinate origin = new Coordinate(500, 0);


    public Day14(List<List<Coordinate>> input) {
        this.input = input;
    }

    public int part1() {
        Cave cave = new Cave(input);
        int sandCount = 0;
        while (placePart1(cave, origin)) {
            sandCount++;
        }
        return sandCount;
    }

    public int part2() {
        Cave cave = new Cave(input);
        int sandCount = 0;
        do {
            sandCount++;
        } while (!placePart2(cave, origin).equals(origin));
        return sandCount;
    }

    public boolean placePart1(Cave cave, Coordinate sand) {
        Coordinate down = new Coordinate(sand.x(), sand.y() + 1);
        Coordinate left = new Coordinate(sand.x() - 1, sand.y() + 1);
        Coordinate right = new Coordinate(sand.x() + 1, sand.y() + 1);

        if (!cave.withinBounds(sand)) {
            return false;
        } else if (!cave.occupied(down)) {
            return placePart1(cave, down);
        } else {
            if (!cave.occupied(left)) {
                return placePart1(cave, left);
            } else if (!cave.occupied(right)) {
                return placePart1(cave, right);
            } else {
                cave.putSand(sand);
                return true;
            }
        }
    }

    public Coordinate placePart2(Cave cave, Coordinate sand) {
        Coordinate down = new Coordinate(sand.x(), sand.y() + 1);
        Coordinate left = new Coordinate(sand.x() - 1, sand.y() + 1);
        Coordinate right = new Coordinate(sand.x() + 1, sand.y() + 1);

        if (!cave.occupied(down)) {
            return placePart2(cave, down);
        } else {
            if (!cave.occupied(left)) {
                return placePart2(cave, left);
            } else if (!cave.occupied(right)) {
                return placePart2(cave, right);
            } else if (!cave.occupied(sand)) {
                cave.putSand(sand);
                return sand;
            } else {
                throw new RuntimeException("There's nowhere to put this sand 😭");
            }
        }
    }

    public class Cave {
        private Set<Coordinate> rocks;
        private Set<Coordinate> sand;
        private Coordinate leftmost;
        private Coordinate rightmost;
        private Coordinate lowest;

        public Cave(List<List<Coordinate>> rockVertices) {
            rocks = new HashSet<>();
            sand = new HashSet<>();
            for (var vertex : rockVertices) {
                for (int i = 0; i < vertex.size() - 1; i++) {
                    // initialize with something reasonable
                    if (leftmost == null)
                        leftmost = vertex.get(i);
                    if (rightmost == null)
                        rightmost = vertex.get(i);
                    if (lowest == null)
                        lowest = vertex.get(i);

                    Coordinate start = vertex.get(i);
                    Coordinate end = vertex.get(i + 1);

                    // ----- BEGIN SPAGHETTI -----
                    if (start.x() < leftmost.x())
                        leftmost = start;
                    if (end.x() > rightmost.x())
                        rightmost = end;
                    if (start.x() > rightmost.x())
                        rightmost = start;
                    if (end.x() < leftmost.x())
                        leftmost = end;
                    if (start.y() > lowest.y())
                        lowest = start;
                    if (end.y() > lowest.y())
                        lowest = end;
                    // ----- END SPAGHETTI -----

                    if (start.y() - end.y() == 0) {
                        // horizontal
                        boolean increasing = end.x() - start.x() > 0;
                        for (int x = start.x(); increasing ? x <= end.x() : x >= end.x(); ) {
                            rocks.add(new Coordinate(x, start.y()));

                            if (increasing) {
                                x++;
                            } else {
                                x--;
                            }
                        }
                    } else {
                        // vertical
                        boolean increasing = end.y() - start.y() > 0;
                        for (int y = start.y(); increasing ? y <= end.y() : y >= end.y(); ) {
                            rocks.add(new Coordinate(start.x(), y));
                            if (increasing) {
                                y++;
                            } else {
                                y--;
                            }
                        }
                    }
                }
            }
        }

        /**
         * FILL YOUR PC WITH SAND
         *
         * @param position position to fill with sand
         */
        public void putSand(Coordinate position) {
            sand.add(position);
        }

        public boolean rockAt(Coordinate position) {
            return rocks.contains(position);
        }

        public boolean sandAt(Coordinate position) {
            return sand.contains(position);
        }

        public boolean withinBounds(Coordinate position) {
            if (position.x() < leftmost.x() || position.x() > rightmost.x())
                return false;
            if (position.y() > lowest.y())
                return false;
            return true;
        }

        public boolean occupied(Coordinate position) {
            // if there's something there, or it's on the floor
            return rockAt(position) || sandAt(position) || position.y() >= lowest.y() + 2;
        }
    }
}
