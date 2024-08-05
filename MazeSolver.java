import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;

public class MazeSolver {
    // Variables for tracking maze solving progress and paths
    boolean foundMazeEnd = false;
    int bfsSteps = 0;
    int bfsBacksteps = 0;
    Stack<Cell> dfsCellPath = new Stack<Cell>();
    Stack<Cell> bfsCellPath = new Stack<Cell>();
    Stack<Cell> dfsSolutionPath = new Stack<Cell>();
    Stack<Cell> bfsSolutionPath = new Stack<Cell>();
    String outputFile;

    public static void main(String[] args) throws IOException {
        // Parse input and output file paths from command line arguments
        String inputFile = args[0];
        String outputFile = args[1];
        MazeSolver solver = new MazeSolver();
        solver.solve(inputFile, outputFile);
    }

    public void solve(String filename, String outputFile) throws IOException {
        // Read maze information from a file
        String[] mazeString = getMazeFromFile(filename);
        int rows = Integer.parseInt(mazeString[0]);
        int columns = Integer.parseInt(mazeString[1]);
        int startCellID = Integer.parseInt(mazeString[2]);
        int endCellID = Integer.parseInt(mazeString[3]);
        Cell startingCell = new Cell();
        Cell endCell = new Cell();
        String[] connections = mazeString[4].split("(?!^)");
        this.outputFile = outputFile;

        // Recreate the maze and mark the start and end cells
        int cellID = 0;
        Cell[][] maze = new Cell[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                Cell newCell = new Cell((i * rows) + j, i, j);
                if (cellID == startCellID) {
                    newCell.markStart();
                    startingCell = newCell;
                } else if (cellID == endCellID) {
                    newCell.markFinish();
                    endCell = newCell;
                }
                newCell.setDirection(Integer.parseInt(connections[(i * rows) + j]));
                maze[i][j] = newCell;
                cellID++;
            }
        }

        // Solve the maze using Depth-First Search (DFS)
        long startTime = System.nanoTime();
        DFS(maze, startingCell, endCell);
        long totalTime = (System.nanoTime() - startTime) / 1000;

        // Print DFS solution details
        System.out.println("DFS");
        System.out.println(stackToString(this.dfsSolutionPath, endCellID));
        System.out.println("Steps: " + this.dfsSolutionPath.size());
        System.out.println("Steps with Backtracks: " + this.dfsCellPath.size());
        System.out.println("Time: " + (totalTime) + " microseconds\n\n");

        // Reset cells and variables for BFS
        this.foundMazeEnd = false;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                maze[i][j].markUnvisited();
            }
        }

        // Solve the maze using Breadth-First Search (BFS)
        startTime = System.nanoTime();
        BFS(maze, startingCell, endCellID);
        totalTime = (System.nanoTime() - startTime) / 1000;

        // Print BFS solution details
        System.out.println("BFS");
        System.out.println(stackToString(this.bfsSolutionPath, endCellID));
        System.out.println("Steps: " + this.bfsSolutionPath.size());
        System.out.println("Steps with Backtracks: "
                + (this.bfsSolutionPath.size() + (this.bfsCellPath.size() - this.bfsSolutionPath.size()) * 2));
        System.out.println("Time: " + (totalTime) + " microseconds\n\n");

        // Write the solution to the output file
        writeToFile(this.bfsSolutionPath.size() + ":" + stackToString(this.dfsSolutionPath, endCellID) + "\n"
                + this.bfsSolutionPath.size() + ":" + stackToString(this.bfsSolutionPath, endCellID));
    }

    private void DFS(Cell[][] maze, Cell cell, Cell endingCell) {
        // Depth-First Search algorithm to find a solution path
        Stack<Cell[]> stack = new Stack<Cell[]>();
        Stack<Cell[]> visitedCellPath = new Stack<Cell[]>();
        int endingCellID = endingCell.getID();

        Cell[] cellPair = { cell, cell };
        cellPair[1].markVisited();
        stack.push(cellPair);
        visitedCellPath.push(cellPair);
        while (!stack.isEmpty()) {
            Cell[] currentCellPair = stack.pop();
            Cell currentCell = currentCellPair[1];
            currentCell.markVisited();
            if (currentCell.getID() != endingCellID) {
                Stack<Cell> neighbours = getNeighbours(maze, currentCell);
                for (Cell neighbour : neighbours) {
                    if (!neighbour.isVisited()) {
                        Cell[] newPair = { currentCell, neighbour };
                        stack.push(newPair);
                        visitedCellPath.push(newPair);
                    }
                }
            } else {
                stack.clear();
                visitedCellPath.push(currentCellPair);
            }
        }
        for (Cell[] cellPairjoe : visitedCellPath) {
            if (cellPairjoe[1].getID() == endingCellID) {
            }
            if (cellPairjoe[0].getID() == endingCellID) {
            }
        }
        // Extract the solution path and count steps and backsteps
        int currentCellID = endingCellID;
        while (currentCellID != cell.getID()) {
            for (int i = 0; i < visitedCellPath.size(); i++) {
                if (visitedCellPath.get(i)[1].getID() == currentCellID) {
                    currentCellID = visitedCellPath.get(i)[0].getID();
                    this.dfsSolutionPath.push(visitedCellPath.get(i)[0]);
                }
                if (!this.dfsCellPath.contains(visitedCellPath.get(i)[1])) {
                    this.dfsCellPath.push(visitedCellPath.get(i)[1]);
                }
            }
        }

    }

    private void BFS(Cell[][] maze, Cell cell, int endingCellID) {
        // Breadth-First Search algorithm to find a solution path
        Queue<Cell[]> queue = new LinkedList<Cell[]>();
        Stack<Cell[]> visitedCellPath = new Stack<Cell[]>();
        Cell[] cellPair = { null, cell };
        cellPair[1].markVisited();
        queue.add(cellPair);
        visitedCellPath.push(cellPair);
        while (!queue.isEmpty()) {
            Cell[] currentCellPair = queue.poll();
            Cell currentCell = currentCellPair[1];
            if (currentCell.getID() != endingCellID) {
                currentCell.markVisited();
                Stack<Cell> neighbours = getNeighbours(maze, currentCell);
                for (Cell neighbour : neighbours) {
                    if (!neighbour.isVisited()) {
                        Cell[] newPair = { currentCell, neighbour };
                        queue.add(newPair);
                        visitedCellPath.push(newPair);
                    }
                }
            } else {
                queue.clear();
                visitedCellPath.push(currentCellPair);
            }

        }
        // Extract the solution path and count steps and backsteps
        int currentCellID = endingCellID;
        while (currentCellID != cell.getID()) {
            for (int i = 0; i < visitedCellPath.size(); i++) {
                if (visitedCellPath.get(i)[1].getID() == currentCellID) {
                    currentCellID = visitedCellPath.get(i)[0].getID();
                    this.bfsSolutionPath.push(visitedCellPath.get(i)[0]);
                }
                if (!this.bfsCellPath.contains(visitedCellPath.get(i)[1])) {
                    this.bfsCellPath.push(visitedCellPath.get(i)[1]);
                }
            }
        }
    }

    private Stack<Cell> getNeighbours(Cell[][] maze, Cell cell) {
        // Get neighboring cells for a given cell
        int dir = cell.getDirection();
        Stack<Cell> neighbours = new Stack<Cell>();

        int row = cell.getRow();
        int col = cell.getCol();

        // Always add up and left neighbors if valid.
        if (row != 0) { // up is a valid move
            if (maze[row - 1][col].getDirection() == 2 || maze[row - 1][col].getDirection() == 3) {
                neighbours.push(maze[row - 1][col]);
            }
        }
        if (col != 0) { // left is a valid move
            if (maze[row][col - 1].getDirection() == 1 || maze[row][col - 1].getDirection() == 3) {
                neighbours.push(maze[row][col - 1]);
            }
        }

        switch (dir) {
            case 1: // right is possible
                if (col < maze[0].length - 1) { // right is a valid move
                    neighbours.push(maze[row][col + 1]);
                }
                break;
            case 2: // down is possible
                if (row < maze.length - 1) { // down is a valid move
                    neighbours.push(maze[row + 1][col]);
                }
                break;
            case 3: // all four directions are TECHNICALLY possible.
                if (row < maze.length - 1) { // down is a valid move
                    neighbours.push(maze[row + 1][col]);
                }
                if (col < maze[0].length - 1) { // right is a valid move
                    neighbours.push(maze[row][col + 1]);
                }
                break;
        }
        return neighbours;
    }

    private String[] getMazeFromFile(String filename) {
        // Read maze information from a file
        String mazeString = "";
        try {
            File file = new File(filename);
            Scanner scanner = new Scanner(file);
            mazeString = scanner.nextLine();
            scanner.close();
        } catch (IOException err) {
            err.printStackTrace();
        }
        return mazeString.split(":");
    }

    private void writeToFile(String text) throws IOException {
        // Write text to the output file
        try {
            File file = new File(this.outputFile);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter writer = new FileWriter(this.outputFile);
            writer.write(text);
            writer.close();
        } catch (IOException err) {
            err.printStackTrace();
        }
    }

    private String stackToString(Stack<Cell> stack, int endingCellID) {
        // Convert a stack of cells to a string for printing
        String outputString = "(";
        for (int i = 0; i < stack.size(); i++) {
            outputString += stack.get(stack.size() - 1 - i).getID() + ", ";
        }
        outputString += endingCellID + ")";
        return outputString;
    }
}
