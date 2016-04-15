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
}
