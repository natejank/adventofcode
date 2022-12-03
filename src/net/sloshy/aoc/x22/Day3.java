package net.sloshy.aoc.x22;

import net.sloshy.aoc.common.Utilities;

import java.util.List;

public class Day3 {
    public static final int LOWER_CASE = 97;
    public static final int UPPER_CASE = 65;

    public static void main(String[] args) {
        var input = Utilities.getContent(args[0]);
        if (input == null) {
            System.err.println("Input file does not exist!");
            return;
        }
        var day3 = new Day3(input);
        int part1 = day3.part1();
        int part2 = 0;

        System.out.printf("Part 1: %d%n", part1);
        System.out.printf("Part 2: %d%n", part2);
    }

    private final List<String> input;

    public Day3(List<String> input) {
        this.input = input;
    }

    public int part1() {
        int priorities = 0;
        for (var line : input) {
            var pocket1 = line.substring(0, line.length()/2);
            var pocket2 = line.substring(line.length()/2);
            for (int i = 0; i < pocket1.length(); i++) {
                var item = pocket1.charAt(i);
                // if pocket2 contains the char
                if (pocket2.indexOf(item) >= 0) {
                    int priority = 0;
                    if (item >= UPPER_CASE && item <= UPPER_CASE + 25) {
                        priority = (item - UPPER_CASE) + 27;
                    } else if (item >= LOWER_CASE && item <= LOWER_CASE + 25) {
                        priority = (item - LOWER_CASE) + 1;
                    } else {
                        throw new RuntimeException("Invalid character!");
                    }
                    priorities += priority;
                    break;
                }
            }
        }
        return priorities;
    }
}
