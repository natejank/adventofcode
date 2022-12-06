package net.sloshy.aoc.x22;

import net.sloshy.aoc.common.Utilities;

import java.util.Comparator;
import java.util.List;

public class Day3 {
    public static void main(String[] args) {
        var day3 = new Day3(Utilities.getContent(args[0]));
        Utilities.printResult(day3.part1(), day3.part2());
    }

    public static final int LOWER_CASE = 97;
    public static final int UPPER_CASE = 65;

    private final List<String> input;

    public Day3(List<String> input) {
        this.input = input;
    }

    public int part1() {
        int priorities = 0;
        for (var line : input) {
            var pocket1 = line.substring(0, line.length() / 2);
            var pocket2 = line.substring(line.length() / 2);
            for (var item : pocket1.toCharArray()) {
                // if pocket2 contains the char
                if (pocket2.indexOf(item) >= 0) {
                    priorities += getPriority(item);
                    break;
                }
            }
        }
        return priorities;
    }

    public int part2() {
        int priorities = 0;
        for (int i = 0; i <= input.size() - 3; i += 3) {
            var group = input.subList(i, i + 3);
            // sort to get the bag with the least items
            group.sort(Comparator.comparingInt(String::length));
            for (char item : group.get(0).toCharArray()) {
                if (group.get(1).indexOf(item) >= 0 && group.get(2).indexOf(item) >= 0) {
                    priorities += getPriority(item);
                    break;
                }
            }
        }
        return priorities;
    }

    public int getPriority(char item) {
        if (item >= UPPER_CASE && item <= UPPER_CASE + 25) {
            return (item - UPPER_CASE) + 27;
        } else if (item >= LOWER_CASE && item <= LOWER_CASE + 25) {
            return (item - LOWER_CASE) + 1;
        } else {
            throw new RuntimeException("Invalid character!");
        }
    }
}
