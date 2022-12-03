package net.sloshy.aoc.x22;

import net.sloshy.aoc.common.Utilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Day1 {
    public static void main(String[] args) {
        var input = Utilities.getContent(args[0],
                (String line) -> line.equals("") ?
                        null : Integer.parseInt(line));
        var day1 = new Day1(input);
        Utilities.printResult(day1.part1(), day1.part2());
    }

    private final ArrayList<Integer> elves;

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
