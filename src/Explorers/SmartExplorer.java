package Explorers;

import game.ExplorationState;
import game.NodeStatus;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @see CavernExplorer
 * @author lmignot
 */
public class SmartExplorer implements CavernExplorer {

    private ExplorationState state;

    public SmartExplorer(ExplorationState state) {
        this.state = state;
    }

    /**
     * @see CavernExplorer#findTheOrb()
     */
    @Override
    public void findTheOrb() {

        Collection<Node> graph = new LinkedHashSet<>();

        // add the start node
        Node start = new Node(state.getCurrentLocation(), state.getDistanceToTarget());
        graph.add(start);

        while (state.getDistanceToTarget() != 0) {
            Node current = getCurrent(graph);
            current.visit();
            current.setNeighbours(state.getNeighbours());
            addNeighboursToGraph(graph);
            Node nearest = getNearest(graph);
            Node next = getNextStep(current, nearest, graph);
            state.moveTo(next.getId());
        }

    }

    private void addNeighboursToGraph(Collection<Node> graph) {
        state.getNeighbours().parallelStream()
                .filter(n -> !graph.parallelStream().anyMatch(ns -> ns.getId() == n.getId()))
                .map(n -> new Node(n.getId(), n.getDistanceToTarget()))
                .forEach(graph::add);
    }

    private Node getNextStep(Node start, Node end, Collection<Node> graph) {
        Queue<Node> open = new PriorityQueue<>(sortByDistance);
        Collection<Node> closed = new LinkedHashSet<>();
        open.add(start);
        Node current = null;

        while(!open.isEmpty()) {
            current = open.poll();
            if (current.getId() == end.getId()) break;

            closed.add(current);

            Collection<Node> neighbours = current.getNeighboursFrom(graph);

            neighbours.stream().filter(n -> !open.contains(n) && !closed.contains(n)).forEach(open::add);
        }

        return current;
    }

    private Node getCurrent(Collection<Node> graph) {
        return graph.parallelStream()
                .filter(n -> n.getId() == state.getCurrentLocation())
                .findFirst()
                .orElse(null);
    }

    private Comparator<Node> sortByDistance = (a,b) -> {
        if (a.getDistance() == b.getDistance()) {
            if (a.getVisits() == b.getVisits()) return 0;
            return a.getVisits() > b.getVisits() ? 1 : -1;
        }
        return a.getDistance() > b.getDistance() ? 1 : -1;
    };

    private Node getNearest(Collection<Node> graph) {
        return graph.parallelStream().min(sortByDistance).orElse(null);
    }

    private class Node {

        private Node parent;
        private long id;
        private int distance;
        private int visitCount = 0;
        private Collection<NodeStatus> neighbours;

        public Node(long id, int distance) {
            this.id = id;
            this.distance = distance;
        }

        /* package */ long getId() {
            return id;
        }

        /* package */ int getDistance() {
            return distance;
        }

        /* package */ int getVisits() {
            return visitCount;
        }

        /* package */ void visit() {
            visitCount++;
        }

        /* package */ void setNeighbours(Collection<NodeStatus> neighbours) {
            this.neighbours = neighbours;
        }

        /* package */ Collection<Node> getNeighboursFrom(Collection<Node> graph) {
            return graph.parallelStream()
                .filter(n -> neighbours.parallelStream().anyMatch(ns -> ns.getId() == n.getId()))
                .collect(Collectors.toCollection(LinkedHashSet::new));
        }
    }
}
