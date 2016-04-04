package CavernExplorer;

import game.ExplorationState;
import game.NodeStatus;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author lmignot
 */
public class Explorator {

    private ExplorationState state;


    public Explorator(ExplorationState state) {
        this.state = state;
    }

    public void findTheOrb () {
        Set<Long> seen = new LinkedHashSet<>();

        while(state.getDistanceToTarget() > 0) {
            Collection<NodeStatus> neighbours = state.getNeighbours();
            // add current to seen items
            seen.add(state.getCurrentLocation());
            int distance = Integer.MAX_VALUE;
            long next = -1L;
            NodeStatus closest = getClosestNeighbour(neighbours, seen);

            if (closest != null && closest.getDistanceToTarget() < distance) {
                distance = closest.getDistanceToTarget();
                next = closest.getId();
            }

            System.out.println("Moving to tile with id: " + next);
            System.out.print("Moving from position: " + state.getCurrentLocation());
            state.moveTo(next);
            System.out.println("\tto: " + state.getCurrentLocation());
        }
    }

    private NodeStatus getClosestNeighbour(Collection<NodeStatus> neighbours, Set<Long> visited) {
        return neighbours.parallelStream()
                .sorted(NodeStatus::compareTo)
                .filter(n -> !visited.contains(n.getId()))
                .findFirst().orElse(null);
    }
}
