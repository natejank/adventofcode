package net.sloshy.aoc.x20.day10;

import java.util.*;

class AdapterConfiguration {
    public static HashMap<LinkedList<Integer>, List<AdapterConfiguration>> knownChildren = new HashMap<>();

    private ArrayList<Integer> bag;
    private LinkedList<Integer> config;

    public AdapterConfiguration(List<Integer> bag) {
        this.bag = new ArrayList<>(bag);
        this.config = new LinkedList<>();
        this.config.add(this.bag.remove(0));
    }

    private AdapterConfiguration(AdapterConfiguration other, int index) {
        this.bag = new ArrayList<>(other.bag);
        this.config = new LinkedList<>(other.config);
        for (int i = 0; i < index && i < this.bag.size(); i++)
            this.bag.remove(i); //prune all earlier elements
        this.config.add(this.bag.remove(0));
    }

    public boolean isSolution(){
        return bag.isEmpty();
    }

    public List<AdapterConfiguration> getChildren() {
        var children = new LinkedList<AdapterConfiguration>();
        // if we've already computed this, it's somewhere in the queue already
        if (!knownChildren.containsKey(config)) {
            // only the next three could possibly be!
            for (int i = 0; i < 3 && i < bag.size(); i++) {
                int next = bag.get(i);
                if (next - config.getLast() <= 3)
                    children.add(new AdapterConfiguration(this, i));
            }
            // now that we know, let's never find out ever again
            knownChildren.put(config, children);
        }
        return children;
    }

}