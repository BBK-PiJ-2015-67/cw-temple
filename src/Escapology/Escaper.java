package Escapology;

import game.EscapeState;
import game.Node;

import java.util.*;
import java.util.stream.Collectors;


/**
 * Explore the map and find the optimal route to the exit.
 * Collect as much gold as possible without running out of time.
 *
 * @author lmignot
 */
public class Escaper {

    private EscapeState state;

    public Escaper(EscapeState state) {
        this.state = state;
    }

    /**
     * Find the best route to the exit within the time allowed
     * while collecting the maximum amount of gold possible
     */
    public void getRichAndEscape() {
        Collection<Node> map = state.getVertices();
        RouteFinder routeFinder = new AStarRouteFinder();

        Node exit = state.getExit();
        Route shortestRoute = routeFinder.findRoute(state.getCurrentNode(), exit, map);

        if (state.getTimeRemaining() > shortestRoute.getCost()) {
            // we have enough time to be greedy
            // find a route via the node with the most gold on the map
            while (state.getCurrentNode() != exit) {
                Route greedyRoute = getRouteToFollow(routeFinder, state, map);

                while (state.getTimeRemaining() - greedyRoute.getCost() > 0) {
                    moveAndPickUpGold(state, greedyRoute.getRoute().pop().getNode());
                    greedyRoute = getRouteToFollow(routeFinder, state, map);
                }

                // time has run out so follow the shortest route to the exit
                followRoute(routeFinder.findRoute(state.getCurrentNode(), exit, map));
            }
        } else {
            // we only have time to take the shortest route so off we go
            followRoute(shortestRoute);
        }

        System.out.println("Time remaining: " + state.getTimeRemaining());
    }

    /**
     * Get a route to the exit, possibly via a tile with a lot of gold if there is one
     *
     * @param routeFinder the RouteFinder to use
     * @param state the game's state
     * @param map the nodes on the map
     * @return the route to follow to the exit
     */
    private static Route getRouteToFollow(RouteFinder routeFinder, EscapeState state, Collection<Node> map) {
        Node richestNode = getRichestNode(map);
        if (richestNode != null && state.getCurrentNode() != richestNode) {
            return routeFinder.findRouteVia(state.getCurrentNode(), richestNode, state.getExit(), map);
        } else {
            return routeFinder.findRoute(state.getCurrentNode(), state.getExit(), map);
        }
    }

    /**
     * Follow a given route
     *
     * @param route The Route to follow
     */
    private void followRoute(Route route) {
        Stack<GreedyNode> path = route.getRoute();
        while (!path.empty()) {
            moveAndPickUpGold(state, path.pop().getNode());
        }
    }

    /**
     * Move the state to the next node and pick up gold on the tile if there
     * is any
     *
     * @param state The game's EscapeState
     * @param destination The node to move to
     */
    private void moveAndPickUpGold(EscapeState state, Node destination) {
        state.moveTo(destination);
        if (state.getCurrentNode().getTile().getGold() > 0) state.pickUpGold();
    }

    /**
     * Get the richest node on the map
     *
     * @param nodes The nodes to search through
     * @return The node on the map with the most gold or null if none exists
     */
    private static Node getRichestNode(Collection<Node> nodes) {
        SortedSet<GreedyNode> sorted = getSortedRichNodes(nodes);
        return sorted.size() > 0 ? sorted.last().getNode() : null;
    }

    /**
     * Get a sorted set of the nodes that contain gold
     *
     * @param nodes the nodes to filter and sort
     * @return the set of nodes that have gold, sorted by the amount of gold they have
     */
    private static SortedSet<GreedyNode> getSortedRichNodes(Collection<Node> nodes) {
        Comparator<GreedyNode> greedyOrder = (a,b) -> Integer.compare(a.getGold(), b.getGold());
        return nodes.parallelStream()
                .filter(n -> n.getTile().getGold() > 0)
                .map(GreedyNode::new)
                .collect(Collectors.toCollection(() -> new TreeSet<>(greedyOrder)));
    }
}
