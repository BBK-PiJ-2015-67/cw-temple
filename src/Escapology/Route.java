package Escapology;

import java.util.Stack;
import java.util.stream.Collectors;

/**
 * Wrapper class for a route
 * containing the route to follow as a Stack
 * and the length of the route as a sum of the
 * movement cost of traversing all the nodes in the route.
 * @author lmignot
 */
/* package */ class Route {

    private Stack<GreedyNode> route;

    /* package */ Route() {
        this.route = new Stack<>();
    }

    /* package */ Route(Stack<GreedyNode> route) {
        if (route == null) this.route = new Stack<>();

        this.route = route;
    }

    /* package */ int size() {
        return route.size();
    }

    /* package */ Stack<GreedyNode> getRoute() {
        return route.parallelStream().collect(Collectors.toCollection(Stack::new));
    }

    /**
     * Retrieve the total movement cost for traversing this route
     *
     * @return The cost of traversing this route, 0 if the route is empty
     */
    /* package */ int getCost() {
        return route.parallelStream().map(GreedyNode::getMovementCost).reduce(Integer::sum).orElse(0);
    }

    /**
     * Retrieve the total amount of gold available on this route
     *
     * @return The amount of gold on the route
     */
    /* package */ int getValue() {
        return route.parallelStream().map(GreedyNode::getGold).reduce(Integer::sum).orElse(0);
    }

    /**
     * Combines two sequential routes and returns them as one
     * @param a The first route
     * @param b The route to add to the first
     * @return The union of route a and b
     */
    /* package */ static Route combineRoutes(Route a, Route b) {
        Stack<GreedyNode> routeB = b.getRoute();
        routeB.addAll(a.getRoute());
        return new Route(routeB);
    }
}
