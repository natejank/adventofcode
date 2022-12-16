package net.sloshy.aoc.x22;

import java.util.*;

import net.sloshy.aoc.common.Coordinate;
import net.sloshy.aoc.common.Utilities;

public class Day15 {
    public static void main(String[] args) {
        var day15 = new Day15(Utilities.getContent(args[0]));

        Utilities.printResult(day15.part1(), 0);
    }

    private List<BeaconSensor> pairs;

    public Day15(List<String> input) {
        pairs = new LinkedList<>();

        for (String line : input) {
            String[] parts = line.split(":");
            String[] sensorInput = parts[0].split(" ");
            Coordinate sensor = new Coordinate(
                    Integer.parseInt(sensorInput[2].split("=")[1].replace(",", "")),
                    Integer.parseInt(sensorInput[3].split("=")[1])
            );
            String[] beaconInput = parts[1].split(" ");
            Coordinate beacon = new Coordinate(
                    Integer.parseInt(beaconInput[5].split("=")[1].replace(",", "")),
                    Integer.parseInt(beaconInput[6].split("=")[1])
            );

            pairs.add(new BeaconSensor(beacon, sensor));
        }
    }

    public int part1() {
        Set<Coordinate> area = new HashSet<>();
        for (var bs : pairs)
//            area.addAll(bs.getAreaAtY(10));
            area.addAll(bs.getAreaAtY(2000000));
        return area.size() - 1;
    }

    private record BeaconSensor(Coordinate beacon, Coordinate sensor) {
        private int getManhattanDistance() {
            int deltaX = beacon.x() - sensor.x();
            int deltaY = beacon.y() - sensor.y();
            return Math.abs(deltaX) + Math.abs(deltaY);
        }

        private Set<Coordinate> getAreaAtY(int y) {
            int quantity = getManhattanDistance() - Math.abs(sensor.y() - y);
            Set<Coordinate> coordinates = new HashSet<>();

            if (quantity > 0) {
                for (int x = sensor.x() - quantity; x <= sensor.x() + quantity; x++) {
                    coordinates.add(new Coordinate(x, y));
                }
            }
            return coordinates;
        }

    }


}
