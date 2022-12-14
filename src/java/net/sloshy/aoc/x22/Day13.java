package net.sloshy.aoc.x22;

import net.sloshy.aoc.common.Utilities;

import java.util.*;

public class Day13 {
    public static void main(String[] args) {
        var day13 = new Day13(Utilities.getContent(args[0]));

        Utilities.printResult(day13.part1(), 0);
    }

    private enum Comparison {
        VALID,
        INVALID,
        INCONCLUSIVE,
    }

    private List<String> transmission;

    public Day13(List<String> transmission) {
        this.transmission = transmission;
    }

    public int part1() {
        int indices = 0;
        int pairs = 0;
        for (int i = 0; i < transmission.size() - 3; i += 3) {
            // if we start with a blank line, do nothing
            if (transmission.get(i).equals(""))
                continue;
            pairs++;
            System.out.printf("== Pair %d ==%n", pairs);
            String left = transmission.get(i);
            String right = transmission.get(i + 1);
            Comparison result = comparePackets(left, right);
            if (result == Comparison.VALID)
                indices += pairs;
            else
                indices = indices;
        }
        return indices;
    }

    private Comparison comparePackets(String left, String right) {
        System.out.printf("Compare %s vs %s%n", left, right);

        int leftLength = getPacketLength(left);
        int rightLength = getPacketLength(right);

        Comparison result;
        for (int i = 0; i < Math.min(leftLength, rightLength); i++) {
            String leftValue = getPacketIndex(left, i);
            String rightValue = getPacketIndex(right, i);
            boolean leftPacket = valueIsPacket(leftValue);
            boolean rightPacket = valueIsPacket(rightValue);

            if (leftPacket && rightPacket) {
                // if both values are packets, compare the inner packet
                result = comparePackets(leftValue, rightValue);
            } else if (rightPacket) {
                // if the right value is a packet, convert the left value to a packet and compare
                result = comparePackets(valueToPacket(leftValue), rightValue);
            } else if (leftPacket) {
                // if the left value is a packet, same as above
                result = comparePackets(leftValue, valueToPacket(rightValue));
            } else {
                // both values are integers
                int leftInt = Integer.parseInt(leftValue);
                int rightInt = Integer.parseInt(rightValue);
                System.out.printf("Compare %d vs %d%n", leftInt, rightInt);
                result = comparePacketValues(leftInt, rightInt);
                if (result != Comparison.INCONCLUSIVE)
                    if (result == Comparison.VALID)
                        System.out.println("Left side is smaller, so inputs are in the right order");
                    else
                        System.out.println("Right side is smaller, so inputs are not in the right order");
            }
            if (result != Comparison.INCONCLUSIVE)
                return result;
        }
        // we reached the end of one packet; true if left is shorter)
        if (leftLength < rightLength)
            System.out.println("Left side ran out of items, so inputs are in the right order");
        else if (rightLength < leftLength)
            System.out.println("Right side ran out of items, so inputs are not in the right order");
        return comparePacketValues(leftLength, rightLength);

    }

    private int getPacketLength(String packet) {
        int length = 1;
        int packetLayer = -1;  // outer layer will make 0
        boolean empty = true;
        for (char c : packet.toCharArray()) {
            if (c == '[') {
                packetLayer++;
                if (packetLayer > 0)
                    empty = false;
            } else if (c == ']') {
                packetLayer--;
            } else {
                empty = false;
                // count commas on outer layer
                if (packetLayer == 0 && c == ',')
                    length++;
            }
        }
        return empty ? 0 : length;
    }

    private String getPacketIndex(String packet, int index) {
        List<Integer> packetIndices = new ArrayList<>();
        packetIndices.add(0);  // the first index starts at 0
        int packetLayer = -1;
        for (int i = 0; i < packet.length(); i++) {
            char c = packet.charAt(i);
            if (c == '[') {
                packetLayer++;
            } else if (c == ']') {
                packetLayer--;
            } else {
                if (packetLayer == 0 && c == ',') {
                    packetIndices.add(i);
                    if (packetIndices.size() == index + 2)
                        break;
                }
            }
        }
        // add the last index for an ending point
        packetIndices.add(packet.length() - 1);

        int from = packetIndices.get(index) + 1;
        int to = packetIndices.get(index + 1);

        return packet.substring(from, to);
    }

    private boolean valueIsPacket(String value) {
        return value.charAt(0) == '[';
    }

    private String valueToPacket(String value) {
        return "[%s]".formatted(value);
    }

    private Comparison comparePacketValues(int left, int right) {
        if (left < right)
            return Comparison.VALID;
        else if (left > right)
            return Comparison.INVALID;
        else
            return Comparison.INCONCLUSIVE;
    }
}
