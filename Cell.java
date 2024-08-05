public class Cell {
    private int id;
    private int row;
    private int column;

    boolean visited = false;
    boolean startCell;
    boolean finishCell;
    boolean rightOpen;
    boolean downOpen;

    int direction;
    int stepsFromCenter;

    String[][] contents = { { "#", "#", "#" },
            { "#", " ", "#" },
            { "#", "#", "#" } }; // top row, middle row, bottom row

    // Constructor to initialize Cell object with ID, row, and column
    Cell(int inputID, int row, int column) {
        this.id = inputID;
        this.row = row;
        this.column = column;
    }
    
    // Default constructor with no parameters
    Cell() {};

    // Set the ID of the cell
    public void setID(int inputID) {
        this.id = inputID;
    }

    // Get the ID of the cell
    public int getID() {
        return this.id;
    }

    // Mark the cell as visited
    public void markVisited() {
        this.visited = true;
    }

    // Mark the cell as unvisited
    public void markUnvisited() {
        this.visited = false;
    }

    // Mark the cell as the starting cell and update its content
    public void markStart() {
        this.startCell = true;
        this.contents[1][1] = "S";
    }

    // Mark the cell as the finishing cell and update its content
    public void markFinish() {
        this.finishCell = true;
        this.contents[1][1] = "F";
    }

    // Check if the cell has been visited
    public boolean isVisited() {
        return this.visited;
    }

    // Get the row of the cell
    public int getRow() {
        return this.row;
    }

    // Get the column of the cell
    public int getCol() {
        return this.column;
    }

    // Increment the number of steps from the center of the cell
    public void setSteps(int num) {
        this.stepsFromCenter += num;
    }

    // Get the number of steps from the center of the cell
    public int getSteps() {
        return this.stepsFromCenter;
    }

    // Mark the cell as open upward by updating its content
    public void up() {
        this.contents[0][1] = " ";
    }

    // Mark the cell as open downward and set the downOpen flag
    public void down() {
        this.contents[2][1] = " ";
        this.downOpen = true;
    }

    // Mark the cell as open to the right and set the rightOpen flag
    public void right() {
        this.contents[1][2] = " ";
        this.rightOpen = true;
    }

    // Mark the cell as open to the left
    public void left() {
        this.contents[1][0] = " ";
    }

    // Determine the direction of the cell based on openness
    public void generateDirection() {
        this.direction = (this.rightOpen ? 1 : 0) + (this.downOpen ? 2 : 0);
        //this.contents[1][1] = String.valueOf(direction);
    }

    // Get the direction of the cell
    public int getDirection() {
        return this.direction;
    }

    // Set the direction of the cell
    public void setDirection(int dir) {
        this.direction = dir;
    }

    // Get the contents of a specific row as a concatenated string
    public String getCellContents(int index) {
        return contents[index][0] + contents[index][1] + contents[index][2];
    }
}
