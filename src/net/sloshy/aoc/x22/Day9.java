package net.sloshy.aoc.x22;

import net.sloshy.aoc.common.Utilities;

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

        Utilities.printResult(day9.part1(), 0);
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
                tail = updateTail(head, tail, move.direction());
                tailLocations.add(tail);
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

    public Coordinate updateTail(Coordinate head, Coordinate tail, Direction headMovement) {
        int rowDelta = Math.abs(head.row() - tail.row());
        int columnDelta = Math.abs(head.column() - tail.column());

        if (rowDelta <= 1 && columnDelta <= 1)
            return tail;

        return switch (headMovement) {
            case UP -> new Coordinate(tail.row() + 1, head.column());
            case DOWN -> new Coordinate(tail.row() - 1, head.column());
            case RIGHT -> new Coordinate(head.row(), tail.column() + 1);
            case LEFT -> new Coordinate(head.row(), tail.column() - 1);
        };
    }

    public record Coordinate(int row, int column) {
    }

    public record Movement(Direction direction, int magnitude) {
    }
}
