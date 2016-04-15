package Escapology;

import game.Node;

import java.util.Collection;

/**
 * RouteFinder finds a route and return it as a Queue
 * @author lmignot
 */
/* package */ interface RouteFinder {

    /**
     * Finds a route from a start Node to an End node
     *
     * @param start the Node to start the search from
     * @param end the Node to end the search at
     * @param nodes the set of nodes to search for the route
     * @return A (possibly empty) Queue containing the route to follow
     */
    Route findRoute(Node start, Node end, Collection<Node> nodes);
}
