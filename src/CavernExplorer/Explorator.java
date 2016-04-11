package CavernExplorer;

import game.ExplorationState;

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
        long maxDeviation = (long) state.getDistanceToTarget() / 2;

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

            if (nextNode == null && lifeline.size() > 1) {
                lifeline.pop();
                next = lifeline.pop();
            } else {
                if (nextNode.getDistance() - maxDeviation > minDistance && lifeline.size() > 2) {
                    System.out.println("min: " + minDistance);
                    System.out.println("next: " + nextNode.getDistance());
                    System.out.println("backtracking...");
                    lifeline.pop();
                    next = lifeline.pop();
                } else {
                    next = nextNode.getId();
                }
            }

            state.moveTo(next);
        }
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

        int compare(EscapeStatus o) {
            if (o.getDistance() == distance) {
                return Math.random() >= 0.5 ? -1 : 1;
            } else {
                return o.getDistance() > distance ? -1 : 1;
            }
        }
    }
}
