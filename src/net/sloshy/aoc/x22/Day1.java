package net.sloshy.aoc.x22;

import net.sloshy.aoc.common.Utilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Day1 {
    private ArrayList<Integer> elves;

    public Day1(List<Integer> input) {
        elves = new ArrayList<>();
        int current = 0;
        for (var line : input) {
            if (line != null) {
                current += line;
            } else {
                elves.add(current);
                current = 0;
            }
        }
        Collections.sort(elves);
    }

    public static void main(String[] args) {
        var input = Utilities.getContent(args[0],
                (String line) -> line.equals("") ?
                        null : Integer.parseInt(line));
        if (input == null) {
            System.err.println("Input file does not exist!");
            System.exit(1);
        }
        var day1 = new Day1(input);

        System.out.printf("Part 1: %d%n".formatted(day1.part1()));
        System.out.printf("Part 2: %d%n".formatted(day1.part2()));
    }

    private int part1() {
        // get the last elf
        return elves.get(elves.size() - 1);
    }

    private int part2() {
        // get the last 3 elves
        return elves.subList(elves.size() - 3, elves.size())
                // (safely) put them in an elf-slurry
                .stream()
                // sum the slurry
                .reduce(Integer::sum)
                // if that somehow didn't work throw an exception
                .orElseThrow();
    }
}
