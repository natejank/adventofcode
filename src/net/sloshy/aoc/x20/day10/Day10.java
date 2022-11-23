package net.sloshy.aoc.x20.day10;

import net.sloshy.aoc.common.Utilities;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;

public class Day10 {
    static HashMap<LinkedList<Integer>, List<AdapterConfiguration>> memo = new HashMap<>();
    static int configurations = 0;
    static int required = 0;
    public static void main(String[] args) {
        int part1 = 0;
        int part2 = 0;
        var bag = Utilities.getContent(args[0], Integer::parseInt);
        if (bag == null) {
            System.err.println("File does not exist!");
            return;
        }
        int deviceJoltage = bag.stream().max(Integer::compareTo).get() + 3;
        required = deviceJoltage;
//        bag.add(deviceJoltage);  // breaks the model but it's ok for this impl
//
//        var part1Solution = part1Solve(new AdapterConfiguration(bag));
//        if (part1Solution != null) {
//            var chain = part1Solution.getChain();
//            int delta1 = 0;
//            int delta3 = 0;
//            int last = 0;  //wall has effective joltage of 0
//            for (int adapter : chain) {
//                int delta = adapter - last;
//                last = adapter;
//                if (delta == 1)
//                    delta1++;
//                else if (delta == 3)
//                    delta3++;
//            }
//            part1 = delta1 * delta3;
//        } else {
//            throw new RuntimeException("Tried all configurations; no solution!");
//        }


        part2Solve(new AdapterConfiguration(bag));
        System.out.printf("Part 1: %d%n", part1);
        System.out.printf("Part 2: %d%n", configurations);
    }

    public static AdapterConfiguration part1Solve(AdapterConfiguration configuration) {
            // DFS to get a solution
            if (memo.containsKey(configuration.getChain())) {
                return null;  // we've already computed this!
            }
            // use all adapters
            if (configuration.isComplete()) {
                return configuration;
            }
            var children = configuration.getChildren();
            memo.put(configuration.getChain(), children);
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

    public static void part2Solve(AdapterConfiguration configuration) {
        // DFS to get a solution
        if (memo.containsKey(configuration.getChain())) {
            return;  // we've already computed this!
        }
        // use all adapters
        if (configuration.meetsRequirements(required)) {
            configurations++;
        }
        var children = configuration.getChildren();
        memo.put(configuration.getChain(), children);
        for (var child : children) {
            if (child.isValid() && !memo.containsKey(child.getChain())) {
                part2Solve(child);
            }
        }
    }
}
