package Explorers;

import game.ExplorationState;

import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Set;
import java.util.Stack;

/**
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

    @Override
    public void findTheOrb() {
        long current;
        long next;

        while(state.getDistanceToTarget() > 0) {
            current = state.getCurrentLocation();

            lifeline.push(current);
            visited.add(current);

            EscapeStatus nextNode = state
                    .getNeighbours()
                    .parallelStream()
                    .map(n-> new EscapeStatus(n.getId(), n.getDistanceToTarget()))
                    .filter(n -> !visited.contains(n.getId()))
                    .min(EscapeStatus::compare)
                    .orElse(null);

            if (nextNode != null) {
                next = nextNode.getId();
            } else {
                next = retraceStep();
            }

            if (isNeighbour(next, state.getNeighbours())) {
                state.moveTo(next);
            } else {
                EscapeStatus tmp = state
                    .getNeighbours()
                    .parallelStream()
                    .map(n -> new EscapeStatus(n.getId(), n.getDistanceToTarget()))
                    .min(EscapeStatus::compare)
                    .orElse(null);

                if (tmp != null) {
                    state.moveTo(tmp.getId());
                }
            }
        }
    }

    /**
     * Retrace our steps if necessary, if there is no parent state
     * revisits the current state.
     * @return The id of the previous visited state if available,
     * or the id of the current state if not.
     */
    private long retraceStep() {
        if (lifeline.size() > 1) {
            lifeline.pop();
        }
        return lifeline.pop();
    }

    /**
     * Private class for Neighbour NodeStatus', used to provide a custom comparison method.
     * When 2 neighbours are equidistant from the goal, "flip a coin" to determine which
     * one to visit.
     */
    private class EscapeStatus {
        private long id;
        private int distance;

        EscapeStatus(long id, int distance) {
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
        int compare(EscapeStatus o) {
            if (o.getDistance() == distance) {
                return rand.nextBoolean() ? 1 : -1;
            } else {
                return o.getDistance() > distance ? -1 : 1;
            }
        }
    }
}
