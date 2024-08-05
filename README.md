# Custom Maze Algorithms - Generation, Solving and Verification
This project was sourced from a university project completed during my enrollment. The original repository has been kept private as per the university code of conduct, so this repository has all references to the university removed.

### Key Information

Running the run.bat file will execute the generator, solver and verifier in a single batch, so you can do it all simulaneously without executing multiple lines.

The Maze Solving does not appear to work with non-square mazes, and due to upcoming events, primarily my Grandpa's 100th Birthday, I did not have time to properly identify and fix the potential issue. As compromise, I've replaced the "20x50" maze with a "35x35" maze.

The COMPARING AND CONTRASTING task, Task 4 in the assignment spec, can be found below:

### Task 4 - Comparing and Contrasting

The BFS and DFS algorithms have a notable amount of differences, with some of the major ones as follows:

###### Time to complete (Execution Time)

Whilst DFS proved to be more efficient in terms of space compelxity, BFS proved to be significantly more efficient in terms of time complexity for execution time. This likely due to the fact that BFS will search every neighbour of every neighbour, rather than just fully committing itself to a specific path, as DFS does. As a result, it is a much less riskier algorithm to find the maze end as opposed to DFS. However, this is only present in smaller mazes where fewer neighbour options are possible, being nearly 50% more efficient in 20x20 mazes.

Once the mazes starting significiantly increasing in size, especially in the 35x35 and 100x100 mazes, the methodical nature of BFS really began to significantly hinder the speed in which BFS can solve, being 20%  and nearly 50% less efficient for their respective maze.

###### Nodes Visits

As viewable by the results, the differences between the two algorithms becomes more prominent the larger the maze gets. More often than not, however, DFS will beat out BFS in terms of finding the end of the maze in fewer nodes visited. This is likely due to the fact DFS prioritises depth over breadth, meaning that dedicating itself to a specific path means that it's more likely to come across the maze finish (considering the fact that the maze finish is at the furthest point from the start). However, this is a risky approach, as DFS can choose the completely wrong direction to search, resulting in nearly 10x more node traversals, having to go back and forth many times.

Whilst BFS had a smaller search time, BFS was vastly more inefficient in terms of space complexity. BFS was 30% less efficient in the 20x20 mazes, 50% less efficient in the 35x35 mazes, and  60% less efficient in the 100x100 mazes.

### Conclusions

DFS, in terms of path-finding, is more efficient than BFS is, sometimes by a significant margin. It appears to harbour a notably smaller node-count, visiting fewer nodes overall than BFS. This isn't a huge surprise as the assessment required the finish node to be farthest node from the start node as possible, making DFS arguably more suitable to this kind of application under these conditions, as DFS goes as deep as possible into the tree, before checking alternative paths. However, DFS is also a riskier path as shooting in the wrong direction will significantly increase the amount of time needed. Alternatively, there is also a chance that DFS can randomly shoot in the desired direction, making it faster in some cases, although this is purely determined by chance and shouldn't be relied on as a proper argument point for DFS.

However, BFS shows promising results in mass-coverage, as it exhaustively searches every possible neighbour instead of blindly dedicating to a path. As a result it is able to locate the finishing cell significantly faster than DFS is able to. However, as a result, BFS will typically search more nodes, and whilst this does mean BFS is able to search more nodes in a smaller amount of time, BFS is vastly more inefficient with storage space, due to it's exhaustive coverage. This also means that larger mazes will significantly impact BFS, making it more detrimental to use than DFS for larger mazes.

As a result, an algorithm such as BFS would be more suited to applications requiring thorough location-searching and ensuring as many locations as possible are visited and noted, such as situations where there are multiple locations to be visited, whereas DFS is far more suited to applications requiring efficient path finding to locate a singular goal.

### Tables

##### Solution Steps

| Maze Size | Solution Steps - DFS | Solution Steps - BFS | Actual Steps - DFS | Actual Steps - BFS | Time Millises - DFS | Time Millises - BFS |
| --------- | -------------------- | -------------------- | ------------------ | ------------------ | ------------------- | ------------------- |
| 20 x 20   | 244                  | 244                  | 326                | 556                | 27.101              | 16.253              |
| 20 x 20   | 217                  | 217                  | 346                | 583                | 29.283              | 15.880              |
| 20 x 20   | 225                  | 225                  | 259                | 575                | 25.033              | 21.601              |
| 20 x 20   | 216                  | 216                  | 312                | 584                | 23.86               | 18.371              |
| 20 x 20   | 195                  | 195                  | 246                | 605                | 20.79               | 14.636              |
| 35 x 35   | 243                  | 243                  | 362                | 557                | 26.962              | 22.219              |
| 35 x 50   | 186                  | 186                  | 340                | 614                | 24.788              | 14.147              |
| 35 x 35   | 232                  | 232                  | 263                | 568                | 37.127              | 45.269              |
| 35 x 35   | 246                  | 246                  | 275                | 554                | 23.417              | 18.616              |
| 35 x 35   | 162                  | 162                  | 227                | 638                | 21.525              | 16.137              |
| 100 x 100 | 3143                 | 3143                 | 5522               | 16857              | 9970.304            | 31827.22            |
| 100 x 100 | 3705                 | 3705                 | 7025               | 16295              | 17934.581           | 43788.352           |
| 100 x 100 | 3504                 | 3504                 | 7063               | 16496              | 18575.076           | 36323.927           |
| 100 x 100 | 2701                 | 2701                 | 3625               | 17299              | 27456.491           | 27456.491           |
| 100 x 100 | 3528                 | 3528                 | 8938               | 16472              | 30919.795           | 37537.388           |
