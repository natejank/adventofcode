package net.sloshy.aoc.x22;

import net.sloshy.aoc.common.Utilities;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

public class Day11 {
    public static void main(String[] args) {
        var day11 = new Day11(Utilities.getContent(args[0]));

        Utilities.printResult(day11.part1(), 0);
    }

    private final List<String> shenanigans;

    public Day11(List<String> happenings) {
        shenanigans = happenings;
    }

    public int part1() {
        List<Monkey> monkys = getMonky();

        for (int i = 0; i < 20; i++)
            turn(monkys);

        monkys.sort(Comparator.comparingInt(Monkey::getInspections));
        return monkys.get(monkys.size()-1).getInspections() * monkys.get(monkys.size() - 2).getInspections();
    }


    private List<Monkey> getMonky() {
        List<Monkey> monkys = new LinkedList<>();

        int monkyNumber = -1;
        List<Integer> startingItems = null;
        Function<Integer, Integer> operation = null;
        Function<Integer, Integer> test = null;

        int lineNo = 0;
        String line;
        while (lineNo < shenanigans.size()) {
            line = shenanigans.get(lineNo++);
            switch (lineNo % 7) {
                case 0 -> {
                    // newline; reset
                    monkyNumber = -1;
                    startingItems = null;
                    operation = null;
                    test = null;
                }
                case 1 -> monkyNumber = Integer.parseInt(
                        line.split(" ")[1]
                                .replaceAll(":", "")
                );

                case 2 -> {
                    startingItems = new ArrayList<>();
                    String[] items = line.split(":")[1].strip().split(",");
                    for (String item : items)
                        startingItems.add(Integer.parseInt(item.strip()));
                }
                case 3 -> {
                    String[] operands = line.split(" ");
                    if (operands[7].equals("old")) {
                        // hardcode the square case
                        operation = (Integer x) -> x * x;
                    } else {
                        int operand = Integer.parseInt(operands[7]);
                        operation = switch (operands[6]) {
                            case "+" -> (Integer x) -> x + operand;
                            case "-" -> (Integer x) -> x - operand;
                            case "*" -> (Integer x) -> x * operand;
                            case "/" -> (Integer x) -> x / operand;
                            default -> throw new RuntimeException("Invalid operand!");
                        };
                    }
                    }
                    case 4 -> {
                        int operand = Integer.parseInt(line.strip().split(" ")[3]);
                        int ifTrue = Integer.parseInt(shenanigans.get(lineNo++).strip().split(" ")[5]);
                        int ifFalse = Integer.parseInt(shenanigans.get(lineNo++).strip().split(" ")[5]);
                        test = (Integer x) -> x % operand == 0 ? ifTrue : ifFalse;

                        monkys.add(new Monkey(monkyNumber, startingItems, operation, test));
                    }
                    default -> throw new RuntimeException("Reached invalid state!");
                }
            }
        return monkys;
    }

    private void turn(List<Monkey> monkys) {
        for (Monkey monky : monkys) {
            var turn = monky.inspectAll();
            for (Throw t : turn) {
                monkys.get(t.monkey()).catchItem(t.worry());
            }
        }
    }


    private record Throw(int monkey, int worry){};

    private static class Monkey {
        private final int number;
        private final List<Integer> items;
        private final Function<Integer, Integer> operation;
        private final Function<Integer, Integer> test;
        private int inspections;


        public Monkey(int number, List<Integer> items, Function<Integer, Integer> operation, Function<Integer, Integer> test) {
            this.number = number;
            this.items = items;
            this.operation = operation;
            this.test = test;
            inspections = 0;
        }

        private List<Throw> inspectAll() {
            List<Throw> turn = new LinkedList<>();
            items.replaceAll(operation::apply);
            inspections += items.size();
            while(!items.isEmpty()) {
                int worry = items.remove(0) / 3;
                int monkey = test.apply(worry);
                turn.add(new Throw(monkey, worry));
            }
            return turn;
        }

        private void catchItem(int worry) {
            items.add(worry);
        }

        public int getInspections() {
            return inspections;
        }

        @Override
        public String toString() {
            return "Monkey %d: %s".formatted(number, items.toString());
        }
    }
}