import java.util.Random;
import java.util.Stack;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;

public class MazeGenerator {
    Cell[][] maze;
    String output;

    public static void main(String[] args) throws IOException {
        // Parse command line arguments
        int rows = Integer.parseInt(args[0]);
        int columns = Integer.parseInt(args[1]);
        String filename = args[2];
        MazeGenerator gen = new MazeGenerator();
        gen.createMaze(rows, columns, filename);
    }

    // Initialize and generate the maze
    public void createMaze(int rows, int columns, String filename) throws IOException {
        Random rand = new Random();
        this.maze = new Cell[rows][columns];
        String connections = "";
        int startCellID = 0;
        int endCellID = 0;
        int id = 0;

        // Create the grid of cells
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                Cell newCell = new Cell(id, i, j);
                id++;
                this.maze[i][j] = newCell;
            }
        }

        // Select a random starting cell and mark it as the start
        Cell startingCell = this.maze[rand.nextInt(rows)][rand.nextInt(columns)];
        startingCell.markStart();
        startCellID = startingCell.getID();

        // Generate the maze using randomised depth-first search
        randomisedDFS(startingCell);

        // Find the furthest cell and mark it as the finish
        int[] endCoordinates = getFurthestCell();
        this.maze[endCoordinates[0]][endCoordinates[1]].markFinish();
        endCellID = this.maze[endCoordinates[0]][endCoordinates[1]].getID();

        // Generate the connections between cells
        connections = generateConnections();

        // Print maze information
        System.out.println("Rows: " + rows);
        System.out.println("Columns: " + columns);
        System.out.println("Starting Cell: " + startCellID);
        System.out.println("Ending Cell: " + endCellID);
        System.out.println("Connection List: " + connections);
        System.out.println();

        // Print the maze to the console
        printMaze();

        // Store maze information in the 'output' variable
        this.output = rows + ":" +
                columns + ":" +
                startCellID + ":" +
                endCellID + ":" +
                connections;

        // Write maze information to a file
        writeToFile(filename);
    }

    // Generate the maze using randomised depth-first search
    private void randomisedDFS(Cell cell) {
        Stack<Cell[]> stack = new Stack<Cell[]>();
        Cell[] startingCell = { cell, cell };
        stack.push(startingCell);

        while (!stack.isEmpty()) {
            Cell[] cellPairs = stack.pop();
            Cell currentCell = cellPairs[1];
            Cell previousCell = cellPairs[0];

            if (!currentCell.isVisited()) {
                currentCell.markVisited();
                currentCell.setSteps(previousCell.getSteps() + 1);

                // Generate connections between cells
                if (currentCell.getID() != previousCell.getID()) {
                    int rowDifference = previousCell.getRow() - currentCell.getRow();
                    int colDifference = previousCell.getCol() - currentCell.getCol();
                    if (rowDifference == 0) { // Same row
                        if (colDifference == 1) { // Left
                            currentCell.right();
                            previousCell.left();
                        } else if (colDifference == -1) { // Right
                            currentCell.left();
                            previousCell.right();
                        }
                    } else { // Different rows
                        if (rowDifference == 1) { // Up
                            currentCell.down();
                            previousCell.up();
                        } else if (rowDifference == -1) { // Down
                            currentCell.up();
                            previousCell.down();
                        }
                    }
                }

                // Get randomised neighboring cells
                Cell[][] neighbours = randomisedNeighbours(currentCell);
                for (int i = 0; i < neighbours.length; i++) {
                    stack.push(neighbours[i]);
                }
            }
        }
    }

    // Get randomised neighboring cells
    private Cell[][] randomisedNeighbours(Cell cell) {
        int[][] directions = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } }; // Down, up, right, left
        int currentRow = cell.getRow();
        int currentColumn = cell.getCol();
        Stack<Cell[]> tempStack = new Stack<Cell[]>();
        Random rand = new Random();

        for (int[] direction : directions) {
            int newRow = currentRow + direction[0];
            int newCol = currentColumn + direction[1];

            if (newRow >= 0 && newRow < this.maze.length && newCol >= 0 && newCol < this.maze[0].length) {
                if (!this.maze[newRow][newCol].isVisited()) {
                    Cell[] neighbouringPair = { this.maze[currentRow][currentColumn], this.maze[newRow][newCol] };
                    tempStack.push(neighbouringPair);
                }
            }
        }

        Cell[][] neighbours = new Cell[tempStack.size()][2];
        for (int i = 0; i < neighbours.length; i++) {
            neighbours[i] = tempStack.remove(rand.nextInt(tempStack.size()));
        }

        return neighbours;
    }

    // Find the furthest cell from the starting point
    private int[] getFurthestCell() {
        int[] currentFarthestIndex = { 0, 0 };
        int currentFarthestDistance = 0;
        for (int i = 0; i < this.maze.length; i++) {
            for (int j = 0; j < this.maze[0].length; j++) {
                if (this.maze[i][j].getSteps() > currentFarthestDistance) {
                    currentFarthestIndex[0] = i;
                    currentFarthestIndex[1] = j;
                    currentFarthestDistance = this.maze[i][j].getSteps();
                }
            }
        }
        return currentFarthestIndex;
    }

    // Print the maze to the console
    private void printMaze() {
        for (int row = 0; row < this.maze.length; row++) {
            for (int cellType = 0; cellType < 3; cellType++) { // Loop through cell types 0, 1, and 2
                for (int column = 0; column < this.maze[row].length; column++) {
                    System.out.print(maze[row][column].getCellContents(cellType));
                }
                System.out.println();
            }
        }
    }

    // Generate the connections between cells
    private String generateConnections() {
        String output = "";
        for (int i = 0; i < this.maze.length; i++) {
            for (int j = 0; j < this.maze[0].length; j++) {
                this.maze[i][j].generateDirection();
                output += String.valueOf(this.maze[i][j].getDirection());
            }
        }
        return output;
    }

    // Write maze information to a file
    public void writeToFile(String filename) throws IOException {
        try {
            File file = new File(filename);
            file.createNewFile();
            FileWriter writer = new FileWriter(filename);
            writer.write(this.output);
            writer.close();
        } catch (IOException err) {
            err.printStackTrace();
        }
    }
}