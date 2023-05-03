package net.sloshy.aoc.x22;

import java.util.*;

import net.sloshy.aoc.common.Utilities;
import net.sloshy.aoc.common.search.SearchState;

public class Day16 {
    public static void main(String[] args) {

    }

    private class Solver {
        private int solve() {
            Queue<CaveState> priority = new PriorityQueue<>(Collections.reverseOrder());

            return 0;
        }
    }

    private class CaveState implements Comparable<CaveState> {
        private Map<String, Valve> valves;
        private final String position;
        private final int pressure;
        private final int time;

        public CaveState(Map<String, Valve> valves) {
            this.valves = new HashMap<>(valves);
            this.position = "AA";
            this.pressure = 0;
            this.time = 0;
        }

        private CaveState(CaveState parent, String position, boolean open) {
            this(parent.valves);
            // this.valves = parent.valves;
            // this.position = position;
            // if (open) {
            //     // this.valves.get(position).
            // }
        }

        public List<CaveState> getNeighbors() {
            for (String neighbor : valves.keySet()) {

            }
            return null;
        }

        public int getWeight() {
            return pressure - time;
        }

        @Override
        public int compareTo(CaveState o) {
            return this.getWeight() - o.getWeight();
        }
    }
    private record Valve(String name, int flow, List<String> tunnels, boolean opened) {}
}
