package student.Escapology;

import game.Node;

import java.util.Collection;

/**
 * RouteFinder finds a route and return it as a Queue
 * @author lmignot
 */
/* package */ interface RouteFinder {

    /**
     * Finds the shortest route from a start Node to an End node
     *
     * @param start the Node to start the search from
     * @param end the Node to end the search at
     * @param nodes the set of nodes to search for the route
     * @return A (possibly empty) Queue containing the route to follow
     */
    Route findRoute(Node start, Node end, Collection<Node> nodes);

    /**
     * Find the shortest route from start to end that passes a given waypoint
     *
     * @param start The start node
     * @param waypoint The interim destination node
     * @param end The final node
     * @param nodes The nodes to search for a route
     * @return The shortest route from start to end that passes by the waypoint
     */
    Route findRouteVia(Node start, Node waypoint, Node end, Collection<Node> nodes);
}
