package net.sloshy.aoc.x22;

import net.sloshy.aoc.common.Utilities;

import java.util.LinkedList;
import java.util.List;

public class Day5 {
    public static void main(String[] args) {
        Utilities.printResult(getDay(args[0]).part1(), getDay(args[0]).part2());
    }

    List<LinkedList<Character>> crates;
    List<String> moves;
    public Day5(List<LinkedList<Character>> crates, List<String> moves) {
        this.crates = crates;
        this.moves = moves;
    }

    public String part1() {
        for (String move : moves) {
            String[] operands = move.split(" ");
            int quantity = Integer.parseInt(operands[1]);
            var from = crates.get(Integer.parseInt(operands[3]) - 1);
            var to = crates.get(Integer.parseInt(operands[5]) - 1);
            for (int i = 0; i < quantity; i++) {
                to.addFirst(from.pop());
            }
        }
        StringBuilder output = new StringBuilder();
        for (var crate : crates) {
            output.append(crate.peek());
        }
        return output.toString();
    }

    public String part2() {
        for (String move : moves) {
            String[] operands = move.split(" ");
            int quantity = Integer.parseInt(operands[1]);
            var from = crates.get(Integer.parseInt(operands[3]) - 1);
            var to = crates.get(Integer.parseInt(operands[5]) - 1);
            for (int i = quantity-1; i >= 0; i--) {
                // yes I'm aware LinkedLists have O(N) random access leave me alone
                to.addFirst(from.remove(i));
            }

        }
        StringBuilder output = new StringBuilder();
        for (var crate : crates) {
            output.append(crate.peek());
        }
        return output.toString();
    }

    public static Day5 getDay(String fileName) {
        List<String> moves = new LinkedList<>();
        List<LinkedList<Character>> containers = new LinkedList<>();
        boolean atStack = true;
        for (String line : Utilities.getContent(fileName)) {
            if (line.length() == 0) {
                atStack = false;
                continue;
            }

            if (containers.isEmpty()) {
                for (int i = 0; i < (line.length() + 1) / 4; i++) {
                    containers.add(new LinkedList<>());
                }
            }

            if (atStack) {
                for (int c = 0; c < containers.size(); c++) {
                    char chr = line.charAt(1 + 4*c);
                    if (chr != ' ' && !Character.isDigit(chr)) {
                        var container = containers.get(c);
                        container.addLast(chr);
                    }
                }
            } else {
                moves.add(line);
            }
        }
        return new Day5(containers, moves);
    }
}
