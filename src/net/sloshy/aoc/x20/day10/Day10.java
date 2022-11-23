package net.sloshy.aoc.x20.day10;

import net.sloshy.aoc.common.Utilities;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;

public class Day10 {
    public static void main(String[] args) {
        int part1 = 0;
        int part2 = 0;
        var bag = Utilities.getContent(args[0], Integer::parseInt);
        if (bag == null) {
            System.err.println("File does not exist!");
            return;
        }
        int deviceJoltage = bag.stream().max(Integer::compareTo).get() + 3;
        bag.add(deviceJoltage);  // breaks the model but it's ok for this impl

        var part1Solution = part1Solve(new AdapterConfiguration(bag));
        if (part1Solution != null) {
            var chain = part1Solution.getChain();
            int delta1 = 0;
            int delta3 = 0;
            int last = 0;  //wall has effective joltage of 0
            for (int adapter : chain) {
                int delta = adapter - last;
                last = adapter;
                if (delta == 1)
                    delta1++;
                else if (delta == 3)
                    delta3++;
            }
            part1 = delta1 * delta3;
        } else {
            throw new RuntimeException("Tried all configurations; no solution!");
        }


        System.out.printf("Part 1: %d%n", part1);
        System.out.printf("Part 2: %d%n", part2);
    }

    public static AdapterConfiguration part1Solve(AdapterConfiguration configuration) {
        // DFS to get a solution
            // use all adapters
            if (configuration.isComplete()) {
                return configuration;
            }
            var children = configuration.getChildren();
            for (var child : children) {
                if (child.isValid()) {
                    var solution = part1Solve(child);
                    if (solution != null)
                        return solution;
                }
            }
        // no solution!
        return null;
    }
}
