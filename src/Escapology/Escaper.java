package Escapology;

import game.EscapeState;

import java.util.Stack;

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

    public void getRichAndEscape() {
        RouteFinder routeFinder = new AStarRouteFinder();
        Route route = routeFinder.findRoute(state.getCurrentNode(), state.getExit(), state.getVertices());
        Stack<GreedyNode> path = route.getRoute();
        int routeCost = route.getCost();

        while (!path.empty()) {
            GreedyNode current = path.pop();
            state.moveTo(current.getNode());
            if (current.hasGold()) state.pickUpGold();
        }

        System.out.println("Time remaining: " + state.getTimeRemaining());
        System.out.println(routeCost + state.getTimeRemaining());
    }
}
