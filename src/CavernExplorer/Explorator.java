package CavernExplorer;

import game.ExplorationState;
import game.NodeStatus;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Stack;

/**
 * @author lmignot
 */
public class Explorator {
    private ExplorationState state;

    private Stack<Long> lifeline;
    private Set<Long> visited;

    public Explorator(ExplorationState state) {
        this.state = state;
        lifeline = new Stack<>();
        visited = new LinkedHashSet<>();
    }

    public void findTheOrb() {
        long current;
        long next;
        long minDistance = Long.MAX_VALUE;
        long maxDeviation = (long) state.getDistanceToTarget();

        while(state.getDistanceToTarget() > 0) {
            current = state.getCurrentLocation();
            minDistance = state.getDistanceToTarget() < minDistance ? state.getDistanceToTarget() : minDistance;

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
                if (nextNode.getDistance() - maxDeviation > minDistance) {
                    next = retraceStep();
                } else {
                    next = nextNode.getId();
                }
            } else {
                next = retraceStep();
            }

            if (isNeighbour(next, state.getNeighbours())) {
                state.moveTo(next);
            } else {
                EscapeStatus tmp = state
                    .getNeighbours()
                    .parallelStream()
                    .map(n-> new EscapeStatus(n.getId(), n.getDistanceToTarget()))
                    .min(EscapeStatus::compare)
                    .orElse(null);

                if (tmp != null) {
                    state.moveTo(tmp.getId());
                }
            }
        }
    }

    private boolean isNeighbour(long id, Collection<NodeStatus> neighbours) {
        return neighbours.parallelStream().anyMatch(n -> n.getId() == id);
    }

    private long retraceStep() {
        if (lifeline.size() > 1) {
            lifeline.pop();
        }
        return lifeline.pop();
    }

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
                return Math.random() > 0.5 ? -1 : 1;
            } else {
                return o.getDistance() > distance ? -1 : 1;
            }
        }
    }
}
