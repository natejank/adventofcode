package net.sloshy.aoc.x22;

import java.util.*;

import net.sloshy.aoc.common.Coordinate;
import net.sloshy.aoc.common.Utilities;

public class Day15 {
    public static void main(String[] args) {
        var day15 = new Day15(Utilities.getContent(args[0]));

        Utilities.printResult(day15.part1(), 0);
    }

    private Set<Coordinate> beacons;
    private Set<Coordinate> sensors;

    public Day15(List<String> input) {
        beacons = new HashSet<>();
        sensors = new HashSet<>();

        for (String line : input) {
            String[] parts = line.split(":");
            String[] sensorInput = parts[0].split(" ");
            Coordinate sensor = new Coordinate(
                    Integer.parseInt(sensorInput[2].split("=")[1].replace(",", "")),
                    Integer.parseInt(sensorInput[3].split("=")[1])
            );
            sensors.add(sensor);
            String[] beaconInput = parts[1].split(" ");
            Coordinate beacon = new Coordinate(
                    Integer.parseInt(beaconInput[5].split("=")[1].replace(",", "")),
                    Integer.parseInt(beaconInput[6].split("=")[1])
            );
            beacons.add(beacon);
        }
    }

    public int part1() {
        int count = 0;
        Set<Coordinate> area = new HashSet<>();
        for (Coordinate sensor : sensors) {
            area.addAll(computeArea(sensor));
        }
        for (Coordinate a : area)
//            if (a.y() == 10)
            if (a.y() == 2000000)
                count++;
        return count;
    }

    private Set<Coordinate> computeArea(Coordinate sensor) {
        Set<Coordinate> area = new HashSet<>();
        Queue<Coordinate> toScan = new LinkedList<>();

        toScan.add(sensor);
        while (!toScan.isEmpty()) {
            Coordinate scan = toScan.remove();
            if (beacons.contains(scan)) {
                area.remove(scan);
                break;
            }
            for (Coordinate neighbor : getNeighbors(scan)) {
                if (!area.contains(neighbor)) {
                    area.add(neighbor);
                    toScan.add(neighbor);
                }
            }
        }
        return area;
    }

    private List<Coordinate> getNeighbors(Coordinate coord) {
        List<Coordinate> neighbors = new LinkedList<>();

        neighbors.add(coord);
        neighbors.add(new Coordinate(coord.x() + 1, coord.y()));
        neighbors.add(new Coordinate(coord.x() - 1, coord.y()));
        neighbors.add(new Coordinate(coord.x(), coord.y() + 1));
        neighbors.add(new Coordinate(coord.x() , coord.y() - 1));

        return neighbors;
    }


}
