package Explorers;

import game.NodeStatus;

import java.util.Collection;

/**
 * @author lmignot
 */
public interface CavernExplorer {

    /**
     * Main method required by any classes implementing this interface.<br>
     * This method will be called from the Student Explorer class.
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
