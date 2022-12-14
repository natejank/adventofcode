package net.sloshy.aoc.x22;

import java.util.LinkedList;
import java.util.List;

import net.sloshy.aoc.common.Utilities;
import net.sloshy.aoc.x22.Day10CPU.CPU;
import net.sloshy.aoc.x22.Day10CPU.CPUObserver;

public class Day10 {
    public static void main(String[] args) {
        var day10 = new Day10(Utilities.getContent(args[0], (String s) -> s.split(" ")));
        System.out.println("Part 2:");
        int part1 = day10.execute();
        System.out.println();
        System.out.println("Part 1: " + part1);
    }

    private final List<String[]> instructions;

    public Day10(List<String[]> instructions) {
        this.instructions = instructions;
    }

    public int execute() {
        CPU cpu = new CPU();
        CRT crt = new CRT();
        cpu.attachObserver(crt);
        for (String[] instruction : instructions) {
            cpu.execute(instruction);
        }
        return crt.getPart1();
    }

    private static class CRT implements CPUObserver {
        private final List<Integer> signalStrength = new LinkedList<>();

        @Override
        public void onCycle(CPU caller, int cycle) {
            if ((cycle - 20) % 40 == 0) {
                signalStrength.add(cycle * caller.getRegister());
            }

            int cursor = (cycle - 1) % 40;
            int sprite = caller.getRegister();
            boolean visible = Math.abs(sprite - cursor) <= 1;
            if (cursor == 0) {
                System.out.println();
            }
            // emoji makes the output actually legible 💀
            System.out.print(visible ? "🎄": "  ");
        }

        public int getPart1() {
            return signalStrength.stream().mapToInt(Integer::intValue).sum();
        }
    }
}