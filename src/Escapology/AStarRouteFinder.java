package Escapology;

import game.Node;
import game.Tile;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation of a RouteFinder using A* algorithm for finding routes
 *
 * Researched a number of articles regarding A* theory, notably
 * http://theory.stanford.edu/~amitp/GameProgramming/AStarComparison.html
 * http://web.mit.edu/eranki/www/tutorials/search/
 * http://www.cokeandcode.com/main/tutorials/path-finding/
 *
 * @author lmignot
 */
/* package */ class AStarRouteFinder implements RouteFinder {

    /**
     * @see RouteFinder#findRoute(Node, Node, Collection)
     */
    @Override
    public Route findRoute(Node start, Node end, Collection<Node> nodes) {
        Route route = new Route();
        if (start == null || end == null || nodes == null || nodes.isEmpty()) return route;

        // put all the nodes into a graph as GreedyNodes
        Collection<GreedyNode> graph = nodes.parallelStream().map(GreedyNode::new)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        Queue<GreedyNode> open = new PriorityQueue<>();
        Collection<GreedyNode> closed = new LinkedHashSet<>();

        GreedyNode current = null;

        // add start node to open list
        open.add(graph.parallelStream().filter(n -> n.getId() == start.getId()).findFirst().orElse(null));

        while (!open.isEmpty()) {
            current = open.poll();
            if (current.getNode().equals(end)) break;

            closed.add(current);

            int costSoFar = current.getG();
            Collection<GreedyNode> neighbours = current.getNeighbours(graph);

            for(GreedyNode g : neighbours) {
                int costToNext = costSoFar + current.getNode().getEdge(g.getNode()).length();

                // if the cost for this neighbour is now less than previously calculated
                // remove it from the open and closed lists so it can be re-evaluated
                if (costToNext < g.getG()) {
                    if (open.contains(g)) open.remove(g);
                    if (closed.contains(g)) closed.remove(g);
                }

                if (!open.contains(g) && !closed.contains(g)) {
                    g.setG(costToNext);
                    g.setH(getH(end.getTile(), g.getNode().getTile()));
                    g.setParent(current);
                    open.add(g);
                }
            }
        }

        if (current != null) route = getRoute(current, start);

        return route;
    }

    /**
     * Get a route for the Escaper to follow
     * @param end The GreedyNode containing the exit node
     * @param start The state's start node
     * @return A stack containing the route
     */
    private Route getRoute(GreedyNode end, Node start) {
        Stack<GreedyNode> route = new Stack<>();
        GreedyNode previous = end;
        do {
            route.push(previous);
            previous = previous.getParent();
        } while(previous.getId() != start.getId());
        return new Route(route);
    }

    /**
     * Calculate the heuristic "H" for the A* algorithm
     * using the <a href="http://theory.stanford.edu/~amitp/GameProgramming/Heuristics.html#manhattan-distance">Manhattan</a>
     * distance.
     * @param a the starting Tile
     * @param b the destination Tile
     * @return the (possibly adjusted) heuristic
     */
    private static int getH(Tile a, Tile b) {
        // @TODO: incorporate Gold into this calculation
        // for now just using the Manhattan distance
        int D = 1;

        return D * (Math.abs(a.getColumn() - b.getColumn()) + Math.abs(a.getRow() - b.getRow()));
    }
}
