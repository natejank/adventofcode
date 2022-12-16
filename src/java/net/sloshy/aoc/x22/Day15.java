package net.sloshy.aoc.x22;

import java.util.*;

import net.sloshy.aoc.common.Coordinate;
import net.sloshy.aoc.common.Utilities;

public class Day15 {
    public static void main(String[] args) {
        var day15 = new Day15(Utilities.getContent(args[0]));

        Utilities.printResult(day15.part1(), day15.part2());
    }

    private static final int MAX_X = 4000000;
    private static final int MIN_X = 0;
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

    public int part2() {
        Set<Coordinate> radii = new HashSet<>();
        int freq = 0;
        for (var bs : pairs) {
            System.out.println("bs = " + bs);
            var radius = bs.getAroundRadius();
            radii.addAll(radius);
            for (var r : radius) {
                var valid = true;
                for (var bs1 : pairs) {
                    if (bs1.inArea(r)) {
                        valid = false;
                        break;
                    } if (bs1.beacon().equals(r)) {
                        valid = false;
                        break;
                    }
                }
                if (valid) {
                    System.out.println("HOOOOLY SHIT");
                    radii.add(r);
                }
            }
        }
        System.out.println(radii.size());
        return freq;
    }

    private int getTuningFrequency(Coordinate coordinate) {
        return coordinate.x() * 4000000 + coordinate.y();
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

        private Set<Coordinate> getAroundRadius() {
            Set<Coordinate> radius = new HashSet<>();
            for (int md = 0; md < getManhattanDistance(); md++) {
                int x = getManhattanDistance() - md;
                radius.add(new Coordinate(sensor.x() + x, sensor.y() + md));
                radius.add(new Coordinate(sensor.x() - x, sensor.y() + md));
                radius.add(new Coordinate(sensor.x() + x, sensor.y() - md));
                radius.add(new Coordinate(sensor.x() - x, sensor.y() - md));
            }
            radius.add(new Coordinate(sensor.x(), sensor.y() + getManhattanDistance()));
            radius.add(new Coordinate(sensor.x(), sensor.y() - getManhattanDistance()));

            return radius;
        }

        private boolean inArea(Coordinate coordinate) {
            if (coordinate.x() < sensor.x() + getManhattanDistance() ||
                    coordinate.x() > sensor.x() - getManhattanDistance())
                return true;
            else if (coordinate.y() < sensor.y() + getManhattanDistance() ||
                    coordinate.y() > sensor.y() - getManhattanDistance())
                return true;
            return false;

        }

    }


}
