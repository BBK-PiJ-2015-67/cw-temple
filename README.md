# cw-temple
## PiJ Coursework 4 - George Osborne and the Temple of Gloom

## Notes

Completed alone.

Note: I created a couple interfaces as the intent was to try and use other algorithms
to determine a better solution. Was not able to do so in the end but leaving
the interfaces as I may want to experiment with this out of personal interest
in the future.

# Phases

## Exploration
### Files/Classes
-  Package: student.Explorers
-  Interface: CavernExplorer
-  Classes: SimpleExplorer

### Implementation
Implemented some sort of depth-first search prioritising distance to Orb.

-  If 2 nodes have the same distance to the Orb then pick one at random.
-  Uses a Stack in order to be able to backtrack if necessary.
-  Tested on 10K+ runs and always returns a path to the Orb.

## Escape
### Files/Classes
-  Package: student.Escapology
-  Interface: RouteFinder
-  Classes: Escaper, AStarRouteFinder, GreedyNode, Heuristic, Route

### Implementation:
A* algorithm using lots of research, notably:

- [Amit Patel - Stanford](http://theory.stanford.edu/~amitp/GameProgramming/AStarComparison.html)
- [Rajiv Eranki - MIT](http://web.mit.edu/eranki/www/tutorials/search/)
- [Kevin Glass](http://www.cokeandcode.com/main/tutorials/path-finding/)


Note: In cases where a tie-breaker is needed ("f" value of 2 nodes is equal) have used the 
amount of gold on a tile to break the tie.

Escaper class locates the next map node with the most gold and if we can reach
that node and the exit in the time remaining, goes to that node. 

The path is recalculated at every step as we move to the exit.