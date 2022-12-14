package net.sloshy.aoc.x22.Day10CPU;

import java.util.LinkedList;
import java.util.List;

public class CPU {
    private int register;
    private int cycles;
    private final List<CPUObserver> observers;

    public CPU() {
        register = 1;
        cycles = 0;
        observers = new LinkedList<>();
    }

    public void attachObserver(CPUObserver observer) {
        observers.add(observer);
    }

    public int getRegister() {
        return register;
    }

    private void cycle() {
        cycles++;
        for (var observer : observers)
            observer.onCycle(this, cycles);
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
