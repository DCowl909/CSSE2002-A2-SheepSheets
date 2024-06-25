package sheep.games.snake;

import java.util.ArrayList;
import java.util.List;


import sheep.expression.TypeError;
import sheep.expression.basic.Constant;
import sheep.sheets.CellLocation;
import sheep.sheets.Sheet;

/**
 * An object that manages interaction with the sheet for a game of snake. It handles both reading
 * the sheet when needed and rendering the sheet based on the snake game state.
 */
public class SnakeSheetManager {

    private Sheet gameSheet;

    /**
     * Initalises a a SnakeSheetManager object with the sheet it will manage.
     * 
     * @param sheet the sheet that the manager will manage.
     */
    public SnakeSheetManager(Sheet sheet) {
        gameSheet = sheet;

    }
    
    /**
     * Returns the game sheet
     * 
     * @returns the sheet
     */
    public Sheet getSheet() {
        return gameSheet;
    }



    /**
     * Checks if a snake is in the bounds of a provided sheet. This means that no cell
     * the snake occupies is outside the cells of the sheet.
     * 
     * @param snake the current snake state
     * @return if the snake is in the bounds of the sheet.
     */
    public boolean inBounds(SnakeState snake) {
        for (CellLocation cell : snake.getContents()) {
            if (!gameSheet.contains(cell)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Gives the locations of food from given sheet. A cell in a sheet is food if it 
     * contains a 2.
     * 
     * @return a list of all the CellLocations the food is at.
     */
    public List<CellLocation> readFood() {
        List<CellLocation> foodCells = new ArrayList<>();

        for (int i = 0; i < gameSheet.getRows(); i++) {
            for (int j = 0; j < gameSheet.getColumns(); j++) {

                CellLocation cell = new CellLocation(i, j);
                String value = gameSheet.valueAt(cell).render();
                if (value.equals("2")) {
                    foodCells.add(cell);
                }
            }
        }
        return foodCells;
    }

    /**
     * Renders a given SnakeState snake object onto the game sheet.
     * 
     * @param snake the snake to render.
     */
    public void renderSnake(SnakeState snake) {
        
        try { 
            for (CellLocation cell : snake.getContents()) {
                gameSheet.update(cell, new Constant(1)); //renders the snake
            }
        } catch (TypeError e) {
            throw new RuntimeException("Issue rendering Snake game.");
        } 
    }  

    /**
     * Renders snake food onto the game sheet.
     * 
     * @param food the list of cells where food is at. 
     */
    public void renderFood(List<CellLocation> food) {
        
        try { 
            for (CellLocation foodCell : food) {
                gameSheet.update(foodCell, new Constant(2)); //renders the food
            }
        } catch (TypeError e) {
            throw new RuntimeException("Issue rendering Snake game.");
        } 
        //render snake on top of apple
    }

    /**
     * Unrenders the entire game of snake from the sheet.
     */
    public void unrender() {
        gameSheet.clear();
    }

    /**
     * Gets the size of the sheet. That is, how many cells the sheet contains.
     * 
     * @return how many cells the sheet contains.
     */
    public int getSheetSize() {
        return gameSheet.getRows() * gameSheet.getColumns();
    }

    
}
