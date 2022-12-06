package net.sloshy.aoc.x22;

import net.sloshy.aoc.common.Utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class Day6 {
    public static void main(String[] args) {
        var day6 = new Day6(Utilities.getContent(args[0]).get(0));
        Utilities.printResult(day6.part1(), day6.part2());
    }

    private final List<Character> transmission;

    public Day6(String transmission) {
        this.transmission = new ArrayList<>(transmission.length());
        for (var c : transmission.toCharArray())
            this.transmission.add(c);
    }

    public int part1() {
        return getStartOfMessage(4);
    }

    public int part2() {
        return getStartOfMessage(14);
    }

    /**
     * Gets the first index of a unique message
     *
     * @param messageLength length of message
     * @return index of start of message
     * @throws NoSuchElementException if message does not exist in transmission
     */
    public int getStartOfMessage(int messageLength) {
        for (int i = messageLength; i < transmission.size(); i++) {
            var message = transmission.subList(i - messageLength, i);
            if (message.stream().distinct().count() == message.size())
                return i;
        }
        throw new NoSuchElementException("Packet does not exist in transmission!");
    }
}
