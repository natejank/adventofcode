package net.sloshy.aoc.x22;

import java.util.LinkedList;
import java.util.List;

import net.sloshy.aoc.common.Utilities;
import net.sloshy.aoc.x22.Day10CPU.CPU;
import net.sloshy.aoc.x22.Day10CPU.CPUObserver;

public class Day10 {
    public static void main(String[] args) {
        var day10 = new Day10(Utilities.getContent(args[0], (String s) -> s.split(" ")));
        Utilities.printPart(day10.part1(), 1);
        System.out.println("Part 2:");
        day10.part2();
    }

    private final List<String[]> instructions;

    public Day10(List<String[]> instructions) {
        this.instructions = instructions;
    }

    public int part1() {
        CPU cpu = new CPU();
        Part1Observer observer = new Part1Observer();
        cpu.attachObserver(observer);
        for (String[] instruction : instructions) {
            cpu.execute(instruction);
        }
        return observer.getSolution();
    }

    public void part2() {
        CPU cpu = new CPU();
        CRT crt = new CRT();
        cpu.attachObserver(crt);
        for (String[] instruction : instructions) {
            cpu.execute(instruction);
        }
    }

    private static class Part1Observer implements CPUObserver {
        private final List<Integer> signalStrength = new LinkedList<>();

        @Override
        public void onCycle(CPU caller, int cycle) {
            if ((cycle - 20) % 40 == 0) {
                signalStrength.add(cycle * caller.getRegister());
            }
        }

        public int getSolution() {
            return signalStrength.stream().mapToInt(Integer::intValue).sum();
        }
    }

    private static class CRT implements CPUObserver {
        @Override
        public void onCycle(CPU caller, int cycle) {
            int cursor = (cycle - 1) % 40;
            int sprite = caller.getRegister();
            boolean visible = Math.abs(sprite - cursor) <= 1;
            if (cursor == 0) {
                System.out.println();
            }
            // emoji makes the output actually legible 💀
            System.out.print(visible ? "🎄": "  ");
        }
    }
}