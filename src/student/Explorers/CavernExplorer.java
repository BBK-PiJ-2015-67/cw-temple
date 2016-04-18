package student.Explorers;

import game.NodeStatus;

import java.util.Collection;

/**
 * An explorer interface. Classes implementing this interface
 * should define a {@code findTheOrb} method which will be called
 * by the Student package in the game.
 *
 * @author lmignot
 */
public interface CavernExplorer {

    /**
     * Explore the map and find the Orb. Exploration is
     * successful if the distance to Orb is 0
     */
    void findTheOrb();

    /**
     * Check that a given state's id is a neighbour of the current state
     * @param id The id to check
     * @param neighbours The neighbours of the current state
     * @return true if the id is a neighbour, false if not
     */
    default boolean isNeighbour(long id, Collection<NodeStatus> neighbours) {
        return neighbours.parallelStream().anyMatch(n -> n.getId() == id);
    }
}
