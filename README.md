# cw-temple
PiJ Coursework 4 - George Osborne and the Temple of Gloom

# Phases

## Exploration
After a fair bit of research looking at both depth-first and breadth-first algorithms online,
have decided to try and implement an "Iterative Deepening Depth-first Search" or IDDFS for the
Exploration phase of this coursework.

### General Idea

1. For each state, add a new class "Node" representing the current state, containing the parent if
there is one (ie. not the first node) the number of times this node has been visited, and the neighbours for the 
node.
2. Store the visited Nodes as we proceed in a PriorityQueue based on distance to orb.
3. Move to the neighbour closest to the orb and repeat. 
4. If we have to backtrack (no neighbours left and not reached the orb), then backtrack up to the parent 
(with the least number of visits and that is closest to the orb) and proceed from there.

## Escape
- Use threading?
    - Thread safe data structures
- Some sort of heuristics needed to keep track of best score