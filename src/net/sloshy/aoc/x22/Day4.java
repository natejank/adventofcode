package net.sloshy.aoc.x22;

import net.sloshy.aoc.common.Utilities;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Day4 {
    public static void main(String[] args) {
        var day4 = new Day4(Utilities.getContent(args[0],
                (String line) -> {
                    var elfValues = new int[2][2];
                    var elves = line.split(",");
                    // I'm tired 😭
                    elfValues[0] = new int[] {
                            Integer.parseInt(elves[0].split("-")[0]),
                            Integer.parseInt(elves[0].split("-")[1])
                    };
                    elfValues[1] = new int[] {
                            Integer.parseInt(elves[1].split("-")[0]),
                            Integer.parseInt(elves[1].split("-")[1])
                    };
                    return elfValues;
                }));
        Utilities.printResult(day4.part1(), day4.part2());
    }

    List<int[][]> input;
    public Day4(List<int[][]> input) {
        this.input = input;
    }

    public int part1() {
        int overlaps = 0;
        for (var line : input) {
            // lowest start first
            Arrays.sort(line, Comparator.comparingInt((int[] a) -> a[0]));
            // if the higher range ends before the lower range, it is fully enclosed
            if (line[0][1] - line[1][1] >= 0 || line[0][0] == line[1][0]) {
                overlaps++;
            }
        }
        return overlaps;
    }

    public int part2() {
        return 0;
    }
}
