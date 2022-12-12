package net.sloshy.aoc.common.search.bfs;

import net.sloshy.aoc.common.search.SearchState;

import java.util.*;

/**
 * Generic breadth-first search solver
 */
public class Solver {
    /**
     * Store all configs and their parents
     */
    private Map<SearchState, SearchState> adjacencyMap;

    /**
     * Construct a solver
     */
    public Solver() {
        adjacencyMap = new HashMap<>();
    }

    /**
     * Run bfs from the current state to hopefully reach a solution
     *
     * @param initial the initial state to search from
     * @return a list of states from start to end; null if no solution exists
     */
    public List<SearchState> solve(SearchState initial) {
        Queue<SearchState> queue = new LinkedList<>();
        SearchState current;

        // seed initial queue
        queue.add(initial);
        adjacencyMap.put(initial, null);

        while (!queue.isEmpty()) {
            current = queue.remove();
            if (current.isGoal()) {
                return resolveParentChain(current);
            } else {
                List<SearchState> neighbors = current.getChildren();
                for (SearchState neighbor : neighbors) {
                    if (!adjacencyMap.containsKey(neighbor)) {
                        queue.add(neighbor);
                        adjacencyMap.put(neighbor, current);
                    }
                }
            }
        }
        return null;
    }

    /**
     * Create a linked list of configurations to go from start to child
     *
     * @param end the end of the chain to be resolved
     * @return The chain of succession to get the end configuration
     */
    private List<SearchState> resolveParentChain(SearchState end) {
        List<SearchState> chain = new LinkedList<>();
        SearchState next = end;
        while (next != null) {  // the first item has a parent of null
            chain.add(0, next);
            next = adjacencyMap.get(next);  // get the parent
        }
        return chain;
    }
}
