package net.sloshy.aoc.x20;

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
        // use a data structure; there are far too many configurations to use
        // an implicit stack
        // TODO use ConcurrentLinkedDeque for threading

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

    private static class AdapterConfiguration {
        // array for fast random access
        private final ArrayList<Integer> bag;
        // linkedlist because it is used as a queue
        private final LinkedList<Integer> chain;

        public AdapterConfiguration(List<Integer> bag) {
            this.bag = new ArrayList<>(bag);
            this.chain = new LinkedList<>();
        }

        /**
         * Copy constructor
         * @param bag previous bag
         * @param chain previous chain
         * @param index index of adapter in bag to add to the end of the chain
         */
        private AdapterConfiguration(ArrayList<Integer> bag, LinkedList<Integer> chain, int index) {
            this.bag = new ArrayList<>(bag);
            this.chain = new LinkedList<>(chain);
            this.chain.add(this.bag.remove(index));
        }

        public ArrayList<Integer> getBag() {
            return new ArrayList<>(bag);
        }

        public LinkedList<Integer> getChain() {
            return chain;
        }

        public boolean isValid() {
            int prev = 0;
//            for (int adapter : chain) {
//                if (!meetsRequirements(prev, adapter))
//                    return false;
//                prev = adapter;
//            }
            if (chain.size() > 1)
                prev = chain.get(chain.size()-2);
            // we assume all previous elements have been validated
            return meetsRequirements(prev, chain.getLast());
        }

        /**
         * This configuration is solved if the bag is empty
         * @return if the bag is empty
         */
        public boolean isComplete() {
            return bag.size() == 0;
        }

        private boolean meetsRequirements(int last, int required) {
            var delta = required - last;
            // the difference is between 0 and 3 jolts
            return delta <= 3 && delta >= 0;
        }

        public boolean meetsRequirements(int required) {
            return meetsRequirements(chain.getLast(), required);
        }


        public List<AdapterConfiguration> getChildren() {
            var children = new LinkedList<AdapterConfiguration>();
            for (int i = 0; i < bag.size(); i++) {
                children.add(new AdapterConfiguration(bag, chain, i));
            }
            return children;
        }
    }
}
