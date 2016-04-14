# cw-temple
PiJ Coursework 4 - George Osborne and the Temple of Gloom

# Phases

## Exploration
Implemented a depth-first search that uses the distance to Orb as a priority.
If 2 nodes have the same distance to the Orb then randomly picks between them,
as a result the path chosen is not always optimal.
Use a Stack in order to be able to backtrack if necessary.
Tested on 10K+ runs and always returns a path to the Orb so at least it works.

## Escape
### Implementation so far:
A* algorithm using lots of research, notably:
- [Amit Patel - Stanford](http://theory.stanford.edu/~amitp/GameProgramming/AStarComparison.html)
- [Rajiv Eranki - MIT](http://web.mit.edu/eranki/www/tutorials/search/)
- [Kevin Glass](http://www.cokeandcode.com/main/tutorials/path-finding/)

In cases where a tie-breaker is needed ("f" value of 2 nodes is equal) have used the 
amount of gold on a tile to break the tie.

### Known issues
A* returns the single shortest path between nodes, it does it so well that the amount of Gold 
collected is not ideal.
Need to look at adjusting the algorithm to search neighbouring nodes with more Gold that are
within the allowed time-slot.
Might add some waypoints with lots of Gold and try to route via those points if the time permits. 

### TODO if time permits
- Use threading?
    - Thread safe data structures
- Some sort of heuristics needed to keep track of best score