package Escapology;

import game.Node;

import java.util.*;

/**
 * Implementation of RouteFinder using A* algorithm for finding routes
 *
 * @author lmignot
 */
public class AStarRouteFinder implements RouteFinder {

    /**
     * @see RouteFinder#findRoute(Node, Node, Collection)
     */
    @Override
    public Queue<GreedyNode> findRoute(Node start, Node end, Collection<Node> nodes) {
        if (start == null || end == null || nodes == null || nodes.isEmpty()) return new ArrayDeque<>();

        Queue<GreedyNode> route = new ArrayDeque<>();
        SortedSet<GreedyNode> open = new TreeSet<>();
        Set<GreedyNode> closed = new LinkedHashSet<>();



        return route;
    }
}
