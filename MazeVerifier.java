import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.Stack;

public class MazeVerifier {

    public static void main(String[] args) throws IOException {
        // Get the maze file and solve file paths from command line arguments
        String mazeFile = args[0];
        String solveFile = args[0]; // Assuming this should be args[1]
        MazeVerifier verifier = new MazeVerifier();
        verifier.run(mazeFile, solveFile);
    }

    private void run(String mazeFile, String solveFile) {
        // Harvest information from the maze file
        String[] mazeString = getMazeFromFile(mazeFile);
        int rows = Integer.parseInt(mazeString[0]);
        int columns = Integer.parseInt(mazeString[1]);
        int startCellID = Integer.parseInt(mazeString[2]);
        int endCellID = Integer.parseInt(mazeString[3]);
        Cell startingCell = new Cell();
        String[] connections = mazeString[4].split("(?!^)");

        // Recreate the maze and mark the start and end cells
        Cell[][] maze = new Cell[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                int cellID = (i * rows) + j;
                Cell newCell = new Cell((i * rows) + j, i, j);
                if (cellID == startCellID) {
                    newCell.markStart();
                    startingCell = newCell;
                } else if (cellID == endCellID) {
                    newCell.markFinish();
                }
                newCell.setDirection(Integer.parseInt(connections[(i * rows) + j]));
                maze[i][j] = newCell;
            }
        }

        // ==== NO FOUR WALLS TEST ====
        int numOfFourWallCells = 0;
        int numOfNoWalls = 0;
        String circularPaths = "";
        String allNodesVisitable = "";

        // Perform tests on the maze
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                numOfFourWallCells += verifyNoFourWalls(maze, maze[i][j]);
                numOfNoWalls += verifyNoWalls(maze, maze[i][j]);
            }
        }

        // Detect circular paths using DFS
        circularPaths = dfsCircular(maze, startingCell);

        // Reset visited states of cells
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                maze[i][j].markUnvisited();
            }
        }

        // Check if all nodes are visitable from the start node using DFS
        allNodesVisitable = dfsVisitable(maze, startingCell);

        // Print test results
        System.out.println("Cells with Four Walls: " + numOfFourWallCells);
        System.out.println("Cells with No Walls: " + numOfNoWalls);
        System.out.println("Circular Paths: " + circularPaths);
        System.out.println("All nodes visitable via Start Node: " + allNodesVisitable);
    }

    private String[] getMazeFromFile(String filename) {
        // Read maze information from a file and split into an array
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

    private int verifyNoFourWalls(Cell[][] maze, Cell cell) {
        // Check if a cell has four walls
        boolean left = false;
        boolean right = false;
        boolean up = false;
        boolean down = false;

        int row = cell.getRow();
        int col = cell.getCol();

        if (row != 0 && ((maze[row - 1][col].getDirection() == 2 || maze[row - 1][col].getDirection() == 3))) {
            up = true;
        }
        if (col != 0 && (maze[row][col - 1].getDirection() == 1 || maze[row][col - 1].getDirection() == 3)) {
            left = true;
        }
        if (col < maze[0].length - 1 && (maze[row][col].getDirection() == 1 || maze[row][col].getDirection() == 3))

        {
            right = true;
        }
        if (row < maze.length - 1 && (maze[row][col].getDirection() == 2 || maze[row][col].getDirection() == 3)) {
            down = true;
        }
        if (left && right && up && down) {
            return 1;
        } else {
            return 0;
        }
    }

    private int verifyNoWalls(Cell[][] maze, Cell cell) {
        // Check if a cell has no walls
        boolean left = false;
        boolean right = false;
        boolean up = false;
        boolean down = false;

        int row = cell.getRow();
        int col = cell.getCol();

        if (row != 0 && ((maze[row - 1][col].getDirection() == 2 || maze[row - 1][col].getDirection() == 3))) {
            up = true;
        }
        if (col != 0 && (maze[row][col - 1].getDirection() == 1 || maze[row][col - 1].getDirection() == 3)) {
            left = true;
        }
        if (col < maze[0].length - 1 && (maze[row][col].getDirection() == 1 || maze[row][col].getDirection() == 3))

        {
            right = true;
        }
        if (row < maze.length - 1 && (maze[row][col].getDirection() == 2 || maze[row][col].getDirection() == 3)) {
            down = true;
        }

        if (!left && !right && !up && !down) {
            System.out.println(maze[row][col].getID() + ", " + maze[row][col].getDirection());
            return 1;
        } else {
            return 0;
        }
    }

    private String dfsCircular(Cell[][] maze, Cell startingCell) {
        // Perform DFS to detect circular paths
        String circularPaths = "No";
        Stack<Cell[]> stack = new Stack<Cell[]>();
        Cell[] pair = { null, startingCell };
        stack.push(pair);
        while (!stack.isEmpty()) {
            Cell[] currentPair = stack.pop();
            Cell currentCell = currentPair[1];
            Cell parentCell = currentPair[0];

            if (!currentCell.isVisited()) {
                currentCell.markVisited();
                Stack<Cell> neighbours = getNeighbours(maze, currentCell);
                for (Cell neighbour : neighbours) {
                    if (!neighbour.isVisited()) {
                        Cell[] newPair = { currentCell, neighbour };
                        stack.push(newPair);
                    } else {
                        if (neighbour.getID() != parentCell.getID()) {
                            circularPaths = "Yes";
                        }
                    }
                }

            }
        }

        return circularPaths;
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

    private String dfsVisitable(Cell[][] maze, Cell startingCell) {
        // Perform DFS to check if all nodes are visitable from the start node
        String allNodesVisitable = "No";
        Stack<Cell> stack = new Stack<Cell>();
        int visitedNodes = 0;
        startingCell.markVisited();
        stack.push(startingCell);

        while (!stack.isEmpty()) {
            Cell currentCell = stack.pop();
            currentCell.markVisited();
            visitedNodes++;
            Stack<Cell> neighbours = getNeighbours(maze, currentCell);
            for (Cell neighbour : neighbours) {
                if (!neighbour.isVisited()) {
                    stack.push(neighbour);
                }
            }
        }

        int mazeSize = maze.length * maze[0].length;
        if (visitedNodes == mazeSize) {
            allNodesVisitable = "Yes";
        }

        return allNodesVisitable;
    }
}