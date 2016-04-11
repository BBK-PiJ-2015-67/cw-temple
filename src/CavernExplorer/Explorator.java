package CavernExplorer;

import game.ExplorationState;
import student.PriorityQueue;
import student.PriorityQueueImpl;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

/**
 * @author lmignot
 */
public class Explorator {
    private ExplorationState state;

    private Stack<ExplorerState> lifeline;
    private Set<ExplorerState> visited;
    private PriorityQueue<ExplorerState> queue;

    private ExplorerState current;
    private ExplorerState next;

    /*
    PriorityQueue of states
    Add neighbours to queue
    next = pick the first off the queue - check that it's a neighbour
    decrement priority of neighbours
    stack current, move to next
    if no next, unstack, move up, decrement priority of non-neighbours
     */
    public Explorator(ExplorationState state) {
        this.state = state;
        lifeline = new Stack<>();
        visited = new HashSet<>();
        queue = new PriorityQueueImpl<>();
    }

    public void findTheOrb() {
        while(state.getDistanceToTarget() > 0) {
            current = new ExplorerState(state.getCurrentLocation(), state.getDistanceToTarget());
            lifeline.push(current);

            // add neighbours to PQ
            state.getNeighbours()
                    .stream()
                    .map(n -> new ExplorerState(n.getId(), n.getDistanceToTarget()))
                    .forEach(n -> queue.add(n, n.getDistance()));

            // if PQ is not empty, get the next node off the queue
            ExplorerState tmp = queue.size() > 0 ? queue.peek() : null;
            if (tmp != null && state.getNeighbours().stream().anyMatch(n -> n.getId() == tmp.getId())) {
                next = queue.poll();
            } else {
                // backtrack
                lifeline.pop();
                next = lifeline.peek();
            }
            // this is rubbish - ideally we'd lower the priority of the queue members but
            // the provided PQ has no iterators and no streams, ugh!
            emptyQueue(queue);
            state.moveTo(next.getId());
        }
    }

    private void emptyQueue(PriorityQueue<ExplorerState> q) {
        for(int i = 0; i < q.size(); i++) {
            q.poll();
        }
    }

    private class ExplorerState {

        private long id;
        private int distance;
        private boolean didVisit = false;

        ExplorerState(long id, int distance) {
            this.id = id;
            this.distance = distance;
        }

        long getId() {
            return id;
        }

        int getDistance() {
            return distance;
        }

        boolean didVisit() {
            return didVisit;
        }

        void visit() {
            didVisit = true;
        }
    }
}
