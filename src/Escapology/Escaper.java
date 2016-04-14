package Escapology;

import game.EscapeState;

import java.util.Stack;

/**
 * @author lmignot
 */
public class Escaper {

    private EscapeState state;

    public Escaper(EscapeState state) {
        this.state = state;
    }

    public void getRichAndEscape() {
        RouteFinder routeFinder = new AStarRouteFinder();
        Stack<GreedyNode> route =
                routeFinder.findRoute(state.getCurrentNode(), state.getExit(), state.getVertices());
        int goldCollected = 0;

        System.out.println("Route length: " + route.size());
        while (!route.empty()) {
            GreedyNode current = route.pop();
            goldCollected += current.countGold();
            System.out.println("Moving from: " + state.getCurrentNode().getId() + " to: " + current.getNode().getId());
            state.moveTo(current.getNode());
            if (current.hasGold()) state.pickUpGold();
        }
//        System.out.println("Gold collected: " + goldCollected);
        return;
    }
}
