package sheep.games.snake;

import sheep.sheets.CellLocation;
import sheep.sheets.Sheet;
import sheep.ui.Prompt;
import sheep.ui.Tick;
import sheep.ui.UI;

import java.util.ArrayList;
import java.util.List;


import sheep.features.Feature;
import sheep.games.random.RandomCell;

/**
 * Manages and runs the classic snake game on the sheet. For this, apples are represented by
 * cells containing a 2 and the snake represented by cells containing a 1.
 * To play the game, a user places as many apples as they want on the sheet, and then selects
 * "Start Snake" in the features menu.
 * Please note apples CANNOT be added after the game has begun. Furthermore, a user cannot change
 * the direction of the snake more than once per tick.
 * 
 */
public class Snake implements Feature, Tick {

    private boolean started = false;
    private boolean growNextTick;
    private boolean changedDirectionThisTick = false; 
    //ensures user can't change direction more than once per tick

    private RandomCell randomCell;
    private SnakeState snake; //the snake in the game
    private List<CellLocation> food; //all the foods in the game
    private SnakeSheetManager sheetManager; //manages interaction with the sheet

    /**
     * Initalises a Snake object using the sheet the snake game will be played on, and a
     * randomCell generator to allow random cells to be selected. 
     * @param sheet the sheet this game of snake will be played on 
     * @param randomCell a randomCell generator to get random cells from
     */
    public Snake(Sheet sheet, RandomCell randomCell) {
        this.randomCell = randomCell;
        sheetManager = new SnakeSheetManager(sheet);
    }

    /**
     * Gets the random cell.
     * 
     * @return the random cell.
     */
    public RandomCell getRandom() {
        return randomCell;
    }

    /**
     * Gets the sheet manager.
     * 
     * @return the sheet manager.
     */
    public SnakeSheetManager getManager() {
        return sheetManager;
    }

    /**
    * Registers the UI components and event handlers for controlling the Snake game.
    * This method binds specific actions to UI events, such as ticking, starting the game,
    * and changing the direction of the Snake.

    @param ui UI instance to register components and event handlers with.
     */
    @Override
    public void register(UI ui) {
        ui.onTick(this);
        ui.addFeature("snake", "Start Snake", 
            (row, column, prompt) -> startSnake(row, column, prompt));
        ui.onKey("w", "Turn Up", (row, column, prompt) -> changeSnakeDirection("UP"));
        ui.onKey("s", "Turn Down", (row, column, prompt) -> changeSnakeDirection("DOWN"));
        ui.onKey("a", "Turn Left", (row, column, prompt) -> changeSnakeDirection("LEFT"));
        ui.onKey("d", "Move Right", (row, column, prompt) -> changeSnakeDirection("RIGHT"));
        
    }

    /**
     * Starts a game of snake. The snake will spawn in on the cell the user has currently
     * selected. If the user has not selected a cell, a prompt will appear to the user that says
     * "Mr snake kind reminds you to select a starting cell."
     * 
     * @param row the row the user has currently selected
     * @param column the column the user has currently selected
     * @param prompt the prompt through which the user is notified if they do not select a 
     *               starting cell.
     */
    public void startSnake(int row, int column, Prompt prompt) {
        if (row == -2 && column == -2) { //if a cell is not selected
            prompt.message("Mr snake kind reminds you to select a starting cell.");
        } else {
            started = true;
            snake = new SnakeState(new CellLocation(row, column));
            food = sheetManager.readFood();
            sheetManager.renderSnake(snake);
            sheetManager.renderFood(food);
        }
    }

    /**
     * Changes the direction of the snake. If the snake has already changed direction on 
     * this tick, then it will not change direction. 
     * 
     * @param direction the direction the snake should move in. Either "UP", "DOWN", "LEFT" or
     *                  "RIGHT".
     */
    public void changeSnakeDirection(String direction) {
        if (!started) {
            return;
        }

        if (!changedDirectionThisTick && started) { 
            snake.changeDirection(direction);
            changedDirectionThisTick = true;
        }

    }
    
    /**
     * Updates the game every tick. This method checks for loss or win conditions, and updates
     * the food and snake appropriately.
     * 
     * @param prompt the prompt object that is used for the tick.
     * @returns if the sheet should be updated after the tick.
     */
    @Override
    public boolean onTick(Prompt prompt) {

        if (!started) {
            return false;
        }


        boolean growThisTick = growNextTick;
        growNextTick = false;
        
        SnakeState movedSnake = snake.moveSnake(growThisTick);

        //lose condition
        if (!sheetManager.inBounds(movedSnake) || movedSnake.inItself()) {
            prompt.message("Game Over!");
            started = false;
            return true;
        }
        
        //win condition
        if (checkWin()) {
            prompt.message("You win!");
            started = false;
        }

        updateSnake(movedSnake);
        growNextTick = updateFood();

        changedDirectionThisTick = false;
        return true;

    }


    /**
     * Checks for a win by checking if the entire sheet is covered by the snake.
     * 
     * @return true if the entire sheet is covered by the snake (thus a win), and false otherwise
     */
    public boolean checkWin() {
        int noOfCells = sheetManager.getSheetSize();
        if (snake.getLength() == noOfCells) {
            return true;
        }
        return false;
    }

    /**
     * Updates both the internal snake state and the sheet.
     * 
     * @param movedSnake the next snake object to update the snake to
     */
    private void updateSnake(SnakeState movedSnake) {
        sheetManager.renderSnake(snake);
        sheetManager.unrender();
        snake = movedSnake;
        sheetManager.renderSnake(snake);
    }

    /**
    * Updates the position of the food in the game.
    * 
    * This method checks if the Snake's head is positioned on a food cell.
    * If the Snake consumes the food, a new food cell is generated at a random location and the
    * sheet is updated accordingly.
    * 
    * @return True if the food has been updated (i.e., consumed by the Snake and replaced), false otherwise.
    */
    public boolean updateFood() {
        boolean foodUpdated = false;

        List<CellLocation> newFood = new ArrayList<>();
        for (CellLocation foodCell : food) {
            if (snake.headAt(foodCell)) { //checks if next move will hit food
                foodUpdated = true;
                CellLocation newFoodCell = randomCell.pick();

                if (!newFoodCell.equals(foodCell)) {
                    newFood.add(newFoodCell);
                }

            } else {
                newFood.add(foodCell);
            }
        }
        food = newFood;
        sheetManager.renderFood(food);
        return foodUpdated;
    }

    
}
