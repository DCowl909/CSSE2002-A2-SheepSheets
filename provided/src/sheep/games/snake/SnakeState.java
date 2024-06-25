package sheep.games.snake;

import java.util.ArrayList;
import java.util.List;

import sheep.sheets.CellLocation;

/**
 * Manages the state of the snake itself in a game of snake. An abstraction of the snake object.
 */
public class SnakeState {

    private List<CellLocation> contents; //the last cell is the head of the snake
    private String direction;

    
    /**
     * Initialises a new snake. By default, the snake is moving downwards.
     */
    public SnakeState(CellLocation startingCell) {
        contents = new ArrayList<>();
        contents.add(startingCell);
        direction = "DOWN";
    }

    /**
     * Initialises a new snake based on a given list of cells for the snake to occupy.
     * 
     * @param contents the list of cells the snake should occupy.
     */
    private SnakeState(List<CellLocation> contents, String direction) {
        setContents(contents);
        this.direction = direction;
    }
    
    /**
     * Gets the cell contents of the snake state. That is, every cell the snake currently 
     * occupies.
     * 
     * @return a list of the cells the snake occupies.
     */
    public List<CellLocation> getContents() {
        return contents;
    }

    /**
     * Set the cell contents of the snake state.
     * 
     * @param contents a list of cells to set the snake to occupy.
     */
    private void setContents(List<CellLocation> contents) {
        this.contents = contents;
    }

    /**
     * Returns the length of the snake. That is, how many cells it occupies.
     * 
     * @return the length of the snake.
     */
    public int getLength() {
        return contents.size();
    }

    /**
     * Returns the direction of the snake.
     * 
     * @return the direction. Either "UP", "DOWN", "LEFT" or "RIGHT"
     */
    public String getDirection() {
        return direction;
    }

    /**
     * Sets the snake direction. A snake cannot move back on itself. 
     * 
     * @param newDirection the direction to set the snake to be moving in.
     *                  Either "UP", "DOWN", "LEFT" or "RIGHT"
     */
    public void changeDirection(String newDirection) {
        if (!areOppositeDirections(direction, newDirection)) {
            direction = newDirection;
        }
    }

    /**
     * Tests if two directions are in opposite directions. 
     * 
     * @param direction1 A direction. Either "UP", "DOWN", "LEFT", "RIGHT"
     * @param direction2 A direction. Either "UP", "DOWN", "LEFT", "RIGHT"
     * @return true if the directions are opposite, false otherwise.
     */
    private boolean areOppositeDirections(String direction1, String direction2) {
        if ((direction1.equals("UP") && direction2.equals("DOWN")) 
            || (direction1.equals("DOWN") && direction2.equals("UP")) 
            || (direction1.equals("LEFT") && direction2.equals("RIGHT"))
            || (direction1.equals("RIGHT") && direction2.equals("LEFT"))) {
            return true;

        } else {
            return false;
        }
        
    }

    /**
     * Returns the cell that is the head of the snake.
     * 
     * @return the cell that is the current head of the snake.
     */
    public CellLocation getHead() {
        return contents.get(getLength() - 1);
    }

    /**
     * Returns the cell that is the tail of the snake.
     * @return the tail of the snake.
     */
    public CellLocation getTail() {
        return contents.get(0);
    }

    /**
     * Tests if the head of the snake is at a specific cell.
     * @param cell the cell to test if the head is at.
     * @return If the head of the snake is at that cell.
     */
    public boolean headAt(CellLocation cell) {
        return (getHead().equals(cell));

    }

    /**
     * Checks if the snake is inside of itself. That is, if the head of the snake is at the same
     * position as any part of its body.
     * @return true if the snake is inside itself, false otherwise.
     */
    public boolean inItself() {
        for (int i = 0; i < getLength() - 1; i++) {
            if (headAt(contents.get(i))) {
                return true;
            }
        }
        return false;
    }

    /**
    * Moves the snake in its current direction.
    * 
    * @param grow Indicates whether the snake should grow after moving
    * @return The new state of the snake after moving
    */
    public SnakeState moveSnake(boolean grow) {

        List<CellLocation> newCells = new ArrayList<>();
        for (CellLocation currentCell : contents) {
            newCells.add(currentCell);
        }
        newCells.add(newHead());
        if (!grow) {
            newCells.remove(0);
        }
        return new SnakeState(newCells, direction);

    }

    /**
     * Returns the cell a new head cell should appear based on the current direction of motion.
     * That is, where the head of the snake will be if it moves one cell in the direction it is 
     * travelling.
     * 
     * @return the cell the new head of the snake should be
     */
    private CellLocation newHead() {
        int headRow = getHead().getRow();
        int headColumn = getHead().getColumn();

        switch (direction) {
            case "UP":
                return new CellLocation(headRow - 1, headColumn);
            case "DOWN":
                return new CellLocation(headRow + 1, headColumn);
            case "LEFT":
                return new CellLocation(headRow, headColumn - 1);
            case "RIGHT":
                return new CellLocation(headRow, headColumn + 1);
            default: 
                throw new RuntimeException("invalid snake direction");
        }

    }

    
}
