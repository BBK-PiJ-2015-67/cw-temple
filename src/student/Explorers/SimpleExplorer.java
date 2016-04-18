package student.Explorers;

import game.ExplorationState;

import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Set;
import java.util.Stack;

/**
 * Algorithm is a Depth-first search implementation with some prioritisation applied.
 * Try to visit the neighbour of current state that is nearest the orb and that hasn't
 * been visited yet.
 * Since more than one neighbour may be at the same distance, chooses one at random.
 * If no neighbours are unvisited, store each step in a Stack so we can backtrack if
 * necessary.
 *
 * @see CavernExplorer
 *
 * @author lmignot
 */
public class SimpleExplorer implements CavernExplorer {
    private Random rand = new Random();
    private ExplorationState state;

    private Stack<Long> lifeline;
    private Set<Long> visited;

    public SimpleExplorer(ExplorationState state) {
        this.state = state;
        lifeline = new Stack<>();
        visited = new LinkedHashSet<>();
    }

    /**
     * @see CavernExplorer#findTheOrb()
     */
    @Override
    public void findTheOrb() {
        long current;
        long next;

        while(state.getDistanceToTarget() > 0) {
            current = state.getCurrentLocation();
            visited.add(current);
            lifeline.push(current);

            // get the node nearest to orb that hasn't been visited
            Node nextNode = getNextNearest(true);
            next = nextNode != null ? nextNode.getId() : retraceStep();

            // if the nearest node is a neighbour move there
            if (isNeighbour(next, state.getNeighbours())) {
                state.moveTo(next);
            } else {
                // fallback to the nearest node to the orb that may have already
                // been visited.
                Node tmp = getNextNearest(false);
                if (tmp != null) state.moveTo(tmp.getId());
            }

        }
    }

    /**
     * Get the neighbour that is nearest to the goal.
     * Optionally filter out neighbours that have been seen.
     *
     * @param filterVisited Whether or not seen nodes should be skipped
     * @return The neighbour nearest to the goal, or null if there is none.
     */
    private Node getNextNearest(boolean filterVisited) {
        return state.getNeighbours().parallelStream()
            .map(n-> new Node(n.getId(), n.getDistanceToTarget()))
            .filter(n -> filterVisited && !visited.contains(n.getId()))
            .min(Node::compare).orElse(null);
    }

    /**
     * Retrace our steps if necessary.
     * @return The id of a state we can backtrack to.
     */
    private long retraceStep() {
        if (lifeline.size() > 1) lifeline.pop();
        return lifeline.pop();
    }

    /**
     * Private class for neighbour NodeStatus', used to provide a custom comparison method.
     * When 2 neighbours are equidistant from the goal, randomly prioritises one over the other.
     */
    private class Node {
        private long id;
        private int distance;

        Node(long id, int distance) {
            this.id = id;
            this.distance = distance;
        }

        long getId() {
            return id;
        }

        int getDistance() {
            return distance;
        }

        /**
         * Compare method to compare a state's neighbours by distance to target, picks randomly
         * if the distances are equal else returns whichever is closer
         * @param o the other status to compare to
         * @return Whichever of the compared states is closer to the target
         */
        int compare(Node o) {
            if (o.getDistance() == distance) {
                return rand.nextBoolean() ? 1 : -1;
            } else {
                return o.getDistance() > distance ? -1 : 1;
            }
        }
    }
}
