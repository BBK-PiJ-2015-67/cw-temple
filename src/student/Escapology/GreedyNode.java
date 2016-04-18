package student.Escapology;

import game.Node;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;

/**
 * @author lmignot
 */
/* package */  class GreedyNode implements Comparable<GreedyNode> {

    private Node node;
    private GreedyNode parent = null;
    private int h = 0;
    private int g = 0;
    private int f = 0;

    /* package */ GreedyNode(Node n) {
        node = n;
    }

    public Node getNode() {
        return node;
    }

    /* package */ long getId() {
        return node.getId();
    }

    /* package */ boolean hasGold() {
        return node.getTile().getGold() > 0;
    }

    /* package */ void setH(int h) {
        this.h = h;
        setF(this.h + g);
    }

    /* package */ int getG() {
        return g;
    }

    /* package */ void setG(int g) {
        this.g = g;
        setF(h + this.g);
    }

    private int getF() {
        return f;
    }

    private void setF(int f) {
        this.f = f;
    }

    /* package */ void setParent(GreedyNode p) {
        parent = p;
    }

    /* package */ GreedyNode getParent() {
        return parent;
    }

    /**
     * Get the cost of moving from this node to its parent
     *
     * @return The movement cost between this node and its parent
     */
    int getMovementCost() {
        return parent != null ? node.getEdge(parent.getNode()).length() : 0;
    }

    /**
     * Get the amount of gold available on this node's tile
     *
     * @return the amount of gold on the tile
     */
    int getGold() {
        return node.getTile().getGold();
    }

    /**
     * Retrieve the GreedyNode neighbours for this particular node
     * from the graph containing all nodes in the map.
     * @param graph The graph of all nodes in the map to search through
     * @return A (possibly empty) set of this node's neighbours as GreedyNodes
     */
    /* package */ Collection<GreedyNode> getNeighbours(Collection<GreedyNode> graph) {
        Collection<Node> neighbours = node.getNeighbours();
        return graph.parallelStream()
                .filter(e -> neighbours.contains(e.getNode()))
                .collect(Collectors.toCollection(LinkedHashSet::new));
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
