package net.sloshy.aoc.x22;

import net.sloshy.aoc.common.Utilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Day1 {
    public static void main(String[] args) {
        // use null to separate elf groups
        var input = Utilities.getContent(args[0],
                (String line) -> line.equals("") ?
                        null : Integer.parseInt(line));
        var day1 = new Day1(input);
        Utilities.printResult(day1.part1(), day1.part2());
    }

    private final ArrayList<Integer> elves;

    public Day1(List<Integer> input) {
        elves = new ArrayList<>();
        int groupCalories = 0;
        // sum all groups; we only care about aggregate calories
        for (Integer line : input) {
            if (line != null) {
                groupCalories += line;
            } else {
                // this group is over
                elves.add(groupCalories);
                groupCalories = 0;
            }
        }
        // sort by number of calories
        Collections.sort(elves);
    }

    private int part1() {
        // get the last elf group
        return elves.get(elves.size() - 1);
    }

    private int part2() {
        // get the last 3 groups
        return elves.subList(elves.size() - 3, elves.size())
                // (safely) put them in an elf-slurry
                .stream()
                // sum the slurry
                .reduce(Integer::sum)
                // if that somehow didn't work throw an exception
                .orElseThrow();
    }
}
