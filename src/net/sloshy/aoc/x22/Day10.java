package net.sloshy.aoc.x22;

import java.util.LinkedList;
import java.util.List;

import net.sloshy.aoc.common.Utilities;
import net.sloshy.aoc.x22.Day10CPU.CPU;
import net.sloshy.aoc.x22.Day10CPU.CPUDebugger;

public class Day10 {
    public static void main(String[] args) {
        var day10 = new Day10(Utilities.getContent(args[0], (String s) -> s.split(" ")));
        Utilities.printResult(day10.part1(), 0);
    }

    private final List<String[]> instructions;

    public Day10(List<String[]> instructions) {
        this.instructions = instructions;
    }

    public int part1() {
        CPU cpu = new CPU();
        Part1Debugger debugger = new Part1Debugger();
        cpu.attachDebugger(debugger);
        for (String[] instruction : instructions) {
            cpu.execute(instruction);
        }
        return debugger.getSolution();
    }

    private static class Part1Debugger implements CPUDebugger {
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
}