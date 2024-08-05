del output.txt
javac *.java
java MazeGenerator 10 10 maze.dat
java MazeSolver maze.dat mazesolve.dat
java MazeVerifier maze.dat mazesolve.dat
pause