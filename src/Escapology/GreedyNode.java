package Escapology;

import game.Node;

import java.util.Collection;
import java.util.LinkedHashSet;

/**
 * @author lmignot
 */
public class GreedyNode implements Comparable<GreedyNode> {

    private Node node;
    private GreedyNode parent = null;
    private int h = 0;
    private int g = 0;
    private int f = 0;

    public GreedyNode(Node n) {
        node = n;
    }

    public Node getNode() {
        return node;
    }

    public long getId() {
        return node.getId();
    }

    public boolean hasGold() {
        return node.getTile().getGold() > 0;
    }

    public int countGold() {
        return node.getTile().getGold();
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
        setF(this.h + g);
    }

    public int getG() {
        return g;
    }

    public void setG(int g) {
        this.g = g;
        setF(h + this.g);
    }

    public int getF() {
        return f;
    }

    private void setF(int f) {
        this.f = f;
    }

    public void setParent(GreedyNode p) {
        parent = p;
    }

    public GreedyNode getParent() {
        return parent;
    }

    public Collection<GreedyNode> getNeighbours(Collection<GreedyNode> graph) {
        Collection<GreedyNode> result = new LinkedHashSet<>();
        node.getNeighbours()
                .parallelStream()
                .forEach(n -> result.add(graph.parallelStream().filter(e -> e.getId() == n.getId()).findFirst().get()));
        return result;
    }

    /**
     * Comparison method, uses the "f" value
     * of GreedyNodes for comparison.<br>
     * Uses the amount of gold contained
     * on a tile as a tie-breaker
     *
     * @param o the node to compare this node against
     * @return -1, 0 or 1 depending on if this node is less
     * than, equal to, or greater than the node compared against
     */
    @Override
    public int compareTo(GreedyNode o) {
        int myGold = node.getTile().getGold();
        int theirGold = o.getNode().getTile().getGold();
        if (getF() == o.getF()) {
            if (myGold == theirGold) return 0;
            return myGold > theirGold ? -1 : 1;
        }
        return getF() > o.getF() ? 1 : -1;
    }
}
