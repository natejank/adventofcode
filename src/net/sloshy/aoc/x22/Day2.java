package net.sloshy.aoc.x22;

import net.sloshy.aoc.common.Utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Day2 {
    public static void main(String[] args) {
        var input = Utilities.getContent(
                args[0],
                (String line) -> new char[]{
                        line.charAt(0),  // 0: input
                        line.charAt(2)   // 1: response
                });
        if (input == null) {
            System.err.println("Input file does not exist!");
            return;
        }

        var day2 = new Day2(input);
        int part1 = day2.part1();
        int part2 = day2.part2();

        System.out.printf("Part 1: %d%n", part1);
        System.out.printf("Part 2: %d%n", part2);

    }

    public enum Result {
        WIN(6),
        LOSS(0),
        TIE(3),
        ;
        public final int points;

        Result(int points) {
            this.points = points;
        }

        @Override
        public String toString() {
            return "%s (%d)".formatted(super.toString(), points);
        }
    }

    // the moves you can make in RPS
    public enum Move {
        ROCK(1),
        PAPER(2),
        SCISSORS(3),
        ;
        public final int points;

        Move(int points) {
            this.points = points;
        }

        @Override
        public String toString() {
            return "%s (%d)".formatted(super.toString(), points);
        }
    }


    private List<char[]> guide;

    public Day2(List<char[]> input) {
        guide = new ArrayList<>(input);
    }

    public int part1() {
        int points = 0;
        for (var match : guide) {
            var theirs = decryptMove(match[0]);
            var yours = decryptMove(match[1]);
            var result = getMatchResult(yours, theirs);
            var matchPoints = yours.points + result.points;
            points += matchPoints;
        }
        return points;
    }

    public int part2() {
        int points = 0;
        for (var match : guide) {
            var theirs = decryptMove(match[0]);
            var result = decryptResult(match[1]);
            var yours = getResultPairing(theirs).get(result);
            var matchPoints = yours.points + result.points;
            points += matchPoints;
        }
        return points;
    }

    /**
     * Gets the match result of your move and an opponents move
     *
     * @param yours  your move
     * @param theirs your opponents move
     * @return the match result
     */
    public static Result getMatchResult(Move yours, Move theirs) {
        if (yours == theirs)
            return Result.TIE;
        else if (yours == getResultPairing(theirs).get(Result.WIN))
            return Result.WIN;
        else
            return Result.LOSS;
    }

    /**
     * Gets the move required for any result of an encounter
     *
     * @param move the opponent's move
     * @return results mapped to their required moves
     */
    public static Map<Result, Move> getResultPairing(Move move) {
        return switch (move) {
            case ROCK -> Map.of(
                    Result.WIN, Move.PAPER,
                    Result.LOSS, Move.SCISSORS,
                    Result.TIE, Move.ROCK
            );
            case PAPER -> Map.of(
                    Result.WIN, Move.SCISSORS,
                    Result.LOSS, Move.ROCK,
                    Result.TIE, Move.PAPER
            );
            case SCISSORS -> Map.of(
                    Result.WIN, Move.ROCK,
                    Result.LOSS, Move.PAPER,
                    Result.TIE, Move.SCISSORS
            );
        };
    }

    /**
     * Decodes the book values
     *
     * @param move the encoded move value
     * @return the pertaining move
     */
    public static Move decryptMove(char move) {
        return switch (move) {
            case 'A', 'X' -> Move.ROCK;
            case 'B', 'Y' -> Move.PAPER;
            case 'C', 'Z' -> Move.SCISSORS;
            default -> throw new RuntimeException("Invalid Move!");
        };
    }

    /**
     * Decodes a result
     *
     * @param result the encoded result
     * @return the decoded result
     */
    public static Result decryptResult(char result) {
        return switch (result) {
            case 'X' -> Result.LOSS;
            case 'Y' -> Result.TIE;
            case 'Z' -> Result.WIN;
            default -> throw new RuntimeException("Invalid result!");
        };
    }
}
