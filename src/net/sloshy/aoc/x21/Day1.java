package net.sloshy.aoc.x21;

import net.sloshy.aoc.common.Utilities;

import java.util.List;

public class Day1 {
    public static void main(String[] args) {
        var filename = args[0];
        var content = Utilities.getContent(filename, Integer::parseInt);
        int part1count = 0;
        int part2count = 0;

        if (content == null) {
            System.err.println("Invalid file!");
            System.exit(1);
        }

        for (int i = 0; i < content.size()-1; i++) {
            if (content.get(i+1) > content.get(i)) {
                part1count++;
            }
        }

        for (int i = 0; i < content.size()-3; i++) {
            int last = sum(content.subList(i, i+3));
            int current = sum(content.subList(i+1, i+4));
            if (current > last)
                part2count++;
        }

        System.out.printf("Part 1: %d%n", part1count);
        System.out.printf("Part 2: %d%n", part2count);
    }

    static int sum(List<Integer> list) {
        int sum = 0;
        for (int n : list)
            sum += n;
        return sum;
    }
}
