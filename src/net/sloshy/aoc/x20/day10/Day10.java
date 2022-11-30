package net.sloshy.aoc.x20.day10;

import net.sloshy.aoc.common.Utilities;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;

public class Day10 {
    static int part2Configs = 0;
    public static void main(String[] args) {
        int part1 = 0;
        int part2 = 0;
        var bag = Utilities.getContent(args[0], Integer::parseInt);
        if (bag == null) {
            System.err.println("File does not exist!");
            return;
        }
        Collections.sort(bag);
        bag.add(bag.get(bag.size()-1) + 3);  // our device!
        System.out.println(bag);

        part1 = part1(bag);
        part2(new AdapterConfiguration(bag));
        part2 = part2Configs;

        System.out.printf("Part 1: %d%n", part1);
        System.out.printf("Part 2: %d%n", part2);
    }

    public static int part1(List<Integer> bag) {
        // we assume there is a way if we're using all possible adapters
        int one = 0;
        int three = 0;
        int previous = 0;
        for (int adapter : bag) {
            switch (adapter - previous) {
                case 1 -> one++;
                case 3 -> three++;
            }
            previous = adapter;
        }
        return one * three;
    }

    public static void part2(AdapterConfiguration adapter) {
        if (adapter.isSolution()) {
            part2Configs++;
        }
        var children = adapter.getChildren();
        for (var child : children) {
            part2(child);
        }
    }
}

