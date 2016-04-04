# cw-temple
PiJ Coursework 4 - George Osborne and the Temple of Gloom

# Phases

## Exploration
- Need to keep track of visited squares and avoid them unless we have to backtrack
    - Use PriorityQueue..?
    - Track how many times we've visited a square..?
- Need to be able to backtrack if we get stuck
    - Some sort of collection of reachable squares "before" current square
- Look at neighbours and evaluate closest to target - move there first..?    

## Escape
- Use threading?
    - Thread safe data structures
- Some sort of heuristics needed to keep track of best score