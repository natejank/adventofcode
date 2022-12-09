package net.sloshy.aoc.x22;

import net.sloshy.aoc.common.Utilities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day9 {
    public static void main(String[] args) {
        var day9 = new Day9(Utilities.getContent(args[0], (String s) -> {
            String[] operands = s.split(" ");
            Direction direction = switch (operands[0]) {
                case "U" -> Direction.UP;
                case "D" -> Direction.DOWN;
                case "R" -> Direction.RIGHT;
                case "L" -> Direction.LEFT;
                default -> throw new RuntimeException("Invalid direction");
            };
            int magnitude = Integer.parseInt(operands[1]);
            return new Movement(direction, magnitude);
            })
        );

        Utilities.printResult(day9.part1(), day9.part2());
    }

    private enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT
    }

    private List<Movement> sequence;

    public Day9(List<Movement> sequence) {
        this.sequence = sequence;
    }

    public int part1() {
        Coordinate head = new Coordinate(0, 0);
        Coordinate tail = new Coordinate(0, 0);
        Set<Coordinate> tailLocations = new HashSet<>();

        for (Movement move : sequence) {
            for (int m = move.magnitude(); m > 0; m--) {
                head = moveHead(head, move.direction());
                tail = updateTail(head, tail);
                tailLocations.add(tail);
            }
        }
        return tailLocations.size();
    }

    public int part2() {
        Set<Coordinate> tailLocations = new HashSet<>();
        List<Coordinate> rope = new ArrayList<>(10);
        for (int i = 0; i < 10; i++)
            rope.add(new Coordinate(0, 0));

        for (Movement move : sequence) {
            for (int m = move.magnitude(); m > 0; m--) {
                Coordinate head = moveHead(rope.get(0), move.direction());
                rope.set(0, head);
                for (int r = 1; r < rope.size(); r++) {
                    Coordinate tail = rope.get(r);
                    tail = updateTail(head, tail);
                    rope.set(r, tail);
                    head = tail;
                }
                tailLocations.add(rope.get(rope.size() - 1));
            }
        }
        return tailLocations.size();
    }

    public Coordinate moveHead(Coordinate head, Direction direction) {
        switch (direction) {
            case UP -> head = new Coordinate(head.row() + 1, head.column());
            case DOWN -> head = new Coordinate(head.row() - 1, head.column());
            case RIGHT -> head = new Coordinate(head.row(), head.column() + 1);
            case LEFT -> head = new Coordinate(head.row(), head.column() - 1);
        }
        return head;
    }

    public Coordinate updateTail(Coordinate head, Coordinate tail) {
        int rowDelta = head.row() - tail.row();
        int rowDeltaDirection = Integer.signum(rowDelta);
        int columnDelta = head.column() - tail.column();
        int columnDeltaDirection = Integer.signum(columnDelta);

        if (Math.abs(rowDelta) <= 1 && Math.abs(columnDelta) <= 1)
            return tail;

        if (Math.abs(rowDelta) == 2 && columnDelta == 0)
            return new Coordinate(tail.row() + rowDeltaDirection, tail.column());

        if (Math.abs(columnDelta) == 2 && rowDelta == 0)
            return new Coordinate(tail.row(), tail.column() + columnDeltaDirection);

        return new Coordinate(tail.row() + rowDeltaDirection, tail.column() + columnDeltaDirection);
    }

    public record Coordinate(int row, int column) {
    }

    public record Movement(Direction direction, int magnitude) {
    }
}
