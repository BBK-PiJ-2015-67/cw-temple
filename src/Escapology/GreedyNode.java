package Escapology;

import game.Node;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;

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

    public Collection<GreedyNode> getNeighbours() {
        return node.getNeighbours()
                .parallelStream()
                .map(GreedyNode::new)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    /**
     * Comparison method, uses the "f" value
     * of GreedyNodes for comparison.
     *
     * @param o the node to compare this node against
     * @return -1, 0 or 1 depending on if this node is less
     * than, equal to, or greater than the node compared against
     */
    @Override
    public int compareTo(GreedyNode o) {
        if (getF() == o.getF()) return 0;
        return getF() > o.getF() ? 1 : -1;
    }
}
