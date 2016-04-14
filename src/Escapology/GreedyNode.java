package Escapology;

import game.Node;

/**
 * @author lmignot
 */
public class GreedyNode {

    private Node node;
    private GreedyNode parent;
    private int h;
    private int g;
    private int f;

    public GreedyNode(Node n, GreedyNode p) {
        node = n;
        parent = p;
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

    public int collectGold() {
        return hasGold() ? node.getTile().takeGold() : 0;
    }

    public int getH() {
        return h;
    }

    public int getG() {
        return g;
    }

    public void setF(int f) {
        this.f = f;
    }

    public int getF() {
        return f;
    }

    public GreedyNode getParent() {
        return parent;
    }

}
