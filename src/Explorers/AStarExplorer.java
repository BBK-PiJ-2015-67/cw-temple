package Explorers;

import game.ExplorationState;
import game.NodeStatus;

import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.function.Predicate;

/**
 * @author lmignot
 */
public class AStarExplorer implements CavernExplorer {

    private static final int MOVEMENT_COST = 1;

    private ExplorationState state;
    private Collection<ExploreNode> closed;
    private Collection<ExploreNode> open;

    private int costSoFar = 0;

    public AStarExplorer(ExplorationState state) {
        this.state = state;
        open = new LinkedHashSet<>();
        closed = new LinkedHashSet<>();
    }

    @Override
    public void findTheOrb() {
        ExploreNode start = new ExploreNode(state.getCurrentLocation(), state.getDistanceToTarget(), null);
        start.setF(costSoFar);
        open.add(start);

        while(!open.isEmpty() && state.getDistanceToTarget() != 0) {
//            costSoFar++;
            ExploreNode current = getNodeFrom(state.getCurrentLocation(), open);
            open.remove(current);

            for (NodeStatus n : state.getNeighbours()) {
                ExploreNode tmp = new ExploreNode(n.getId(), n.getDistanceToTarget(), current);
                tmp.setF(current.getF());

                ExploreNode tmpOpen = getNodeFrom(tmp.getId(), open);
                if (tmpOpen != null && tmpOpen.getF() < tmp.getF()) continue;

                ExploreNode tmpClosed = getNodeFrom(tmp.getId(), closed);
                if (tmpClosed != null && tmpClosed.getF() < tmp.getF()) continue;

                open.add(tmp);
            }

            closed.add(current);

            ExploreNode next = getSmallestF(open, state.getNeighbours());
            if (next == null) {
                next = getSmallestF(closed, state.getNeighbours());
                if (next != null) {
                    closed.remove(next);
                    open.add(next);
                    next.setF(10);
                }
            }
            System.out.println("moving from tile " + current.getId() + " to tile " + next.getId());
            state.moveTo(next.getId());
        }
    }

    private ExploreNode getNodeFrom(long id, Collection<ExploreNode> collection) {
        Predicate<ExploreNode> nodeExists = n -> n.getId() == id;
        return collection.parallelStream().filter(nodeExists).findFirst().orElse(null);
    }

    private ExploreNode getSmallestF(Collection<ExploreNode> collection, Collection<NodeStatus> neighbours) {
        Comparator<ExploreNode> minF = (a, b) -> Integer.compare(a.getF(), b.getF());
        return collection.parallelStream().filter(n -> isNeighbour(n.getId(), neighbours)).min(minF).orElse(null);
    }

    /**
     * ExploreNode
     */
    private class ExploreNode implements Comparable<ExploreNode> {
        private ExploreNode parent;
        private long id;
        private int h;
        private int f;
        private int g = 0;

        ExploreNode(long id, int distance, ExploreNode parent) {
            this.id = id;
            h = distance;
            this.parent = parent;
        }

        long getId() {
            return id;
        }

        int getF() {
            return f;
        }

        void setF(int p) {
            g += (p * MOVEMENT_COST);
            f = g + h;
        }

        ExploreNode getParent() {
            return parent;
        }

        @Override
        public int compareTo(ExploreNode o) {
            return Integer.compare(getF(), o.getF());
        }
    }
}
