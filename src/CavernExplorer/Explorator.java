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

    private NodeStatus nextPos;

    public Explorator(ExplorationState state) {
        this.state = state;
        lifeline = new Stack<>();
        visited = new LinkedHashSet<>();
    }

    public void findTheOrb() {
        while(state.getDistanceToTarget() > 0) {
            lifeline.push(state.getCurrentLocation());
            nextPos = getNextPos(state.getNeighbours());
            System.out.println("Next distance: " + state.getDistanceToTarget());
            if (nextPos != null) {
                visited.add(nextPos.getId());
                System.out.println("Moving from position " + state.getCurrentLocation() + " to " + nextPos.getId());
                state.moveTo(nextPos.getId());
            } else {
                if (!lifeline.isEmpty()) {
                    lifeline.pop();
                    long next = lifeline.peek();
                    System.out.println("Backtracking...");
                    System.out.println("Moving from position " + state.getCurrentLocation() + " to " + next);
                    state.moveTo(next);
                }
            }
        }
    }

    NodeStatus getNextPos(Collection<NodeStatus> nodes) {
        return nodes.stream()
                .filter(n -> !visited.contains(n.getId()))
                .sorted(NodeStatus::compareTo)
                .findFirst()
                .orElse(null);
    }
}
