package net.sloshy.aoc.x20.day10;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

class AdapterConfiguration {
    // array for fast random access
    private final ArrayList<Integer> bag;
    // linkedlist because it is used as a queue
    private final LinkedList<Integer> chain;

    public AdapterConfiguration(List<Integer> bag) {
        this.bag = new ArrayList<>(bag);
        this.chain = new LinkedList<>();
    }

    /**
     * Copy constructor
     * @param bag previous bag
     * @param chain previous chain
     * @param index index of adapter in bag to add to the end of the chain
     */
    private AdapterConfiguration(ArrayList<Integer> bag, LinkedList<Integer> chain, int index) {
        this.bag = new ArrayList<>(bag);
        this.chain = new LinkedList<>(chain);
        this.chain.add(this.bag.remove(index));
    }

    public ArrayList<Integer> getBag() {
        return new ArrayList<>(bag);
    }

    public LinkedList<Integer> getChain() {
        return chain;
    }

    public boolean isValid() {
        int prev = 0;
//            for (int adapter : chain) {
//                if (!meetsRequirements(prev, adapter))
//                    return false;
//                prev = adapter;
//            }
        if (chain.size() > 1)
            prev = chain.get(chain.size()-2);
        // we assume all previous elements have been validated
        return meetsRequirements(prev, chain.getLast());
    }

    /**
     * This configuration is solved if the bag is empty
     * @return if the bag is empty
     */
    public boolean isComplete() {
        return bag.size() == 0;
    }

    private boolean meetsRequirements(int last, int required) {
        var delta = required - last;
        // the difference is between 0 and 3 jolts
        return delta <= 3 && delta >= 0;
    }

    public boolean meetsRequirements(int required) {
        if (chain.size() > 1)
            return meetsRequirements(chain.getLast(), required);
        return false;
    }


    public List<AdapterConfiguration> getChildren() {
        var children = new LinkedList<AdapterConfiguration>();
        for (int i = 0; i < bag.size(); i++) {
            children.add(new AdapterConfiguration(bag, chain, i));
        }
        return children;
    }
}