package Escapology;

import game.Node;

import java.util.Collection;
import java.util.Stack;

/**
 * RouteFinder finds a route and return it as a Queue
 * @author lmignot
 */
public interface RouteFinder {

    /**
     * Finds a route from a start Node to an End node
     *
     * @param start the Node to start the search from
     * @param end the Node to end the search at
     * @param nodes the set of nodes to search for the route
     * @return A (possibly empty) Queue containing the route to follow
     */
    Stack<GreedyNode> findRoute(Node start, Node end, Collection<Node> nodes);

    /**
     * Determine if a GreedyNode is in a collection
     * @param node The node to search for
     * @param collection The collection to search
     * @return True if the GreedyNode is in the collection
     */
    default boolean isNodeIn(GreedyNode node, Collection<GreedyNode> collection) {
        return collection.parallelStream().anyMatch(n -> n.getId() == node.getId());
    }

    /**
     * Determine if a GreedyNode is in either of a pair of collections
     * @param node The node to search for
     * @param a The first collection to search
     * @param b The second collection to search
     * @return True if the GreedyNode is in either collection
     */
    default boolean isNodeIn(GreedyNode node, Collection<GreedyNode> a, Collection<GreedyNode> b) {
        return isNodeIn(node, a) || isNodeIn(node, b);
    }
}
