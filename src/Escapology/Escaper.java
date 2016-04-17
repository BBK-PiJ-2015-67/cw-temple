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
        Set<Long> seen = new HashSet<>();
        RouteFinder routeFinder = new AStarRouteFinder();

        // if the first node contains gold, pick it up straight away
        // so it doesn't affect route planning
        if (state.getCurrentNode().getTile().getGold() > 0) state.pickUpGold();

        Node exit = state.getExit();

        // while we can find a route via the node with the most gold on the map
        while (state.getCurrentNode() != exit) {
            Route greedyRoute = getRouteToFollow(routeFinder, state, seen);

            while (state.getTimeRemaining() - greedyRoute.getCost() > 0) {
                if (greedyRoute.size() == 0) break;
                moveAndPickUpGold(state, greedyRoute.getRoute().pop().getNode());
                greedyRoute = getRouteToFollow(routeFinder, state, seen);
            }

            // time has run out so follow the shortest route to the exit
            followRoute(routeFinder.findRoute(state.getCurrentNode(), exit, state.getVertices()));
        }
    }

    /**
     * Get a route to the exit, possibly via a tile with a lot of gold if there is one
     *
     * @param routeFinder the RouteFinder to use
     * @param state the game's state
     * @param seen a set containing previously analysed "rich" nodes
     * @return the route to follow to the exit
     */
    private static Route getRouteToFollow(RouteFinder routeFinder, EscapeState state, Set<Long> seen) {
        Node richestNode = getRichestNode(state.getVertices(), seen);
        if (richestNode != null) {
            return routeFinder.findRouteVia(state.getCurrentNode(), richestNode, state.getExit(), state.getVertices());
        } else {
            return routeFinder.findRoute(state.getCurrentNode(), state.getExit(), state.getVertices());
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
        if (state.getCurrentNode().getTile().getGold() > 0) state.pickUpGold();
        state.moveTo(destination);
    }

    /**
     * Get the richest node on the map
     *
     * @param nodes The nodes to search through
     * @param seen a set containing previously analysed "rich" nodes
     * @return The node on the map with the most gold or null if none exists
     */
    private static Node getRichestNode(Collection<Node> nodes, Set<Long> seen) {
        SortedSet<GreedyNode> sorted = getSortedRichNodes(nodes, seen);
        return sorted.size() > 0 ? sorted.last().getNode() : null;
    }

    /**
     * Get a sorted set of the nodes that contain gold
     *
     * @param nodes the nodes to filter and sort
     * @param seen a set containing previously analysed "rich" nodes to filter out
     * @return the set of nodes that have gold, sorted by the amount of gold they have
     */
    private static SortedSet<GreedyNode> getSortedRichNodes(Collection<Node> nodes, Set<Long> seen) {
        Comparator<GreedyNode> greedyOrder = (a,b) -> Integer.compare(a.getGold(), b.getGold());
        return nodes.parallelStream()
                .filter(n -> n.getTile().getGold() > 0 && !seen.contains(n.getId()))
                .map(GreedyNode::new)
                .collect(Collectors.toCollection(() -> new TreeSet<>(greedyOrder)));
    }
}
