package Escapology;

import game.EscapeState;

import java.util.Queue;

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
        Queue<GreedyNode> route = routeFinder.findRoute(state.getCurrentNode(), state.getExit(), state.getVertices());
        int goldCollected = 0;

        System.out.println("Route length: " + route.size());
        while (!route.isEmpty()) {
            GreedyNode current = route.poll();
            goldCollected += current.collectGold();
            state.moveTo(route.peek().getNode());
        }
        System.out.println("Gold collected: " + goldCollected);
    }
}
