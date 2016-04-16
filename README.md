# cw-temple
PiJ Coursework 4 - George Osborne and the Temple of Gloom

Completed alone.

# Phases

## Exploration
Implemented a depth-first search that uses the distance to Orb as a priority.
If 2 nodes have the same distance to the Orb then randomly picks between them,
as a result the path chosen is not always optimal.
Use a Stack in order to be able to backtrack if necessary.
Tested on 10K+ runs and always returns a path to the Orb so at least it works.

TODO if time permits: constantly add any new neighbours to a map of discovered nodes, 
as we move try and move towards the best node that we know about.

## Escape
### Implementation so far:
A* algorithm using lots of research, notably:
- [Amit Patel - Stanford](http://theory.stanford.edu/~amitp/GameProgramming/AStarComparison.html)
- [Rajiv Eranki - MIT](http://web.mit.edu/eranki/www/tutorials/search/)
- [Kevin Glass](http://www.cokeandcode.com/main/tutorials/path-finding/)

In cases where a tie-breaker is needed ("f" value of 2 nodes is equal) have used the 
amount of gold on a tile to break the tie.

Current implementation gets the next map node with the most gold and if we can reach
that node and the exit in the time remaining, goes to that node. The path is recalculated
at every step as we move to the exit. Uses lots of CPU :)