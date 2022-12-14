package net.sloshy.aoc.common.search;

import java.util.List;

/**
 * Generic search state interface.  Most searches are similar enough
 * that this works for bfs/dfs/etc.
 * <br>
 * Each state is intended to be immutable, so the solution becomes
 * a chain of states from start to finish.
 */
public interface SearchState {
    /**
     * @return all children of current state
     */
    List<SearchState> getChildren();

    /**
     * @return Checks if the current state is a solution
     */
    boolean isGoal();

    // ensure implementations override these
    @Override
    int hashCode();

    @Override
    boolean equals(Object other);
}
