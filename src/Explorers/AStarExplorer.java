package Explorers;

import game.ExplorationState;
import student.PriorityQueue;
import student.PriorityQueueImpl;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Predicate;

/**
 * @author lmignot
 */
public class AStarExplorer implements CavernExplorer {
    private static final double COST = 1.0;
    private ExplorationState state;

    private Set<ExploreNode> closed;
    private PriorityQueue<ExploreNode> open;

    public AStarExplorer(ExplorationState state) {
        this.state = state;
        open = new PriorityQueueImpl<>();
        closed = new LinkedHashSet<>();
    }

    @Override
    public void findTheOrb() {
        ExploreNode start = new ExploreNode(state.getCurrentLocation(), state.getDistanceToTarget(), 0);
        open.add(start, 0);

        while(open.size() > 0 && state.getDistanceToTarget() != 0) {
            ExploreNode current = open.poll();
            closed.add(current);

            state.getNeighbours()
                    .stream()
                    .filter(s -> !closed.parallelStream().anyMatch(n -> n.getId() == s.getId()))
                    .forEach(s -> {
                        ExploreNode n = new ExploreNode(s.getId(), s.getDistanceToTarget(), current.getPriority());
                        open.add(n, n.getPriority());
                    });

            System.out.println(open.toString());
            System.out.println("moving from tile " + current.getId() + " to tile " + open.peek().getId());
            state.moveTo(open.peek().getId());
        }
    }

    private static boolean nodeExistsIn(long id, Collection<ExploreNode> collection) {
        Predicate<ExploreNode> nodeExists = n -> n.getId() == id;
        return collection.parallelStream().anyMatch(nodeExists);
    }

    /**
     * ExploreNode
     */
    private class ExploreNode implements Comparable<ExploreNode> {
        private long id;
        private int h;
        private double g;
        private double f;

        ExploreNode(long id, int distance, double p) {
            this.id = id;
            h = distance;
            g = p * COST;
            f = g + h;
        }

        long getId() {
            return id;
        }

        double getPriority() {
            return f;
        }

        void setPriority(int p) {
            g += p;
            f = g + h;
        }

        @Override
        public int compareTo(ExploreNode o) {
            if (getPriority() == o.getPriority()) {
                return 0;
            } else {
                return getPriority() < o.getPriority() ? 1 : -1;
            }
        }
    }
}
