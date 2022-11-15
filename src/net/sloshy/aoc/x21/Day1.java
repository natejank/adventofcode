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

        part1count = slidingWindowSum(content, 1);
        part2count = slidingWindowSum(content, 3);

        System.out.printf("Part 1: %d%n", part1count);
        System.out.printf("Part 2: %d%n", part2count);
    }

    static int sum(List<Integer> list) {
        int sum = 0;
        for (int n : list)
            sum += n;
        return sum;
    }

    static int slidingWindowSum(List<Integer> values, int windowSize) {
        int count = 0;
        for (int i = 0; i < values.size() - windowSize; i++) {
            int last = sum(values.subList(i, i + windowSize));
            int current = sum(values.subList(i + 1, i + windowSize + 1));
            if (current > last)
                count++;
        }
        return count;
    }
}
