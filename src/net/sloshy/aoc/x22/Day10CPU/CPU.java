package net.sloshy.aoc.x22.Day10CPU;

import java.util.LinkedList;
import java.util.List;

public class CPU {
    private int register;
    private int cycles;
    private final List<CPUDebugger> debuggers;

    public CPU() {
        register = 1;
        cycles = 0;
        debuggers = new LinkedList<>();
    }

    public void attachDebugger(CPUDebugger debugger) {
        debuggers.add(debugger);
    }

    public int getRegister() {
        return register;
    }

    private void cycle() {
        cycles++;
        for (var debugger : debuggers)
            debugger.onCycle(this, cycles);
    }

    private void incrementRegister(int value) {
        cycle(); // one cycle to increment (happens before the register changes)
        register += value;
    }

    public void execute(String[] operands) {
        cycle();  // one cycle to process the instruction
        switch (operands[0]) {
            case "noop" -> {}
            case "addx" -> incrementRegister(Integer.parseInt(operands[1]));
            default -> throw new RuntimeException("Illegal instruction! Halting execution.");
        }
    }
}
