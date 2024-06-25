//State an abstract class?

package sheep.games.life;

import java.util.ArrayList;
import java.util.List;

import sheep.expression.TypeError;
import sheep.expression.basic.Constant;
import sheep.sheets.Sheet;

/**
 * Represents the state of a Game of Life. 
 * Responsible for handling the game logic and applying the rules of Game of Life to progress
 * to the next state. 
 * This class can run independently of the sheet playing life. 
 */
public class LifeState {
    
    private List<OnOffCellLocation> state = new ArrayList<>(); 

    /**
     * Initalises a new LifeState based off a sheet.
     * 
     * @param sheet the sheet to read to obtain the starting state
     */
    public LifeState(Sheet sheet) {
        readState(sheet);
    }

    /**
     * Initalises a new LifeState based off a list of cell states.
     * 
     * @param cellStates the list of starting cell states
     */
    public LifeState(List<OnOffCellLocation> cellStates) {
        state = cellStates;
    }

    /**
     * Clears the LifeState to a blank state that contains no OnOffCellLocations.
     */
    private void clearState() {
        state = new ArrayList<>();
    }

    /**
     * Reads a sheet and updates the life state accordingly.
     * @param sheet the sheet to read
     */
    public void readState(Sheet sheet) {
        clearState();
        for (int i = 0; i <  sheet.getRows(); i++) {
            for (int j = 0; j < sheet.getColumns(); j++) {

                OnOffCellLocation cell = new OnOffCellLocation(i, j, false);
                String value = sheet.valueAt(cell).render();
                if (value.equals("1")) {
                    cell.turnOn();
                }
                state.add(cell);
            }
        }
    }

    /**
     * Writes the life state to a given sheet by updating all the cell values accordingly.
     * 
     * @param sheet the sheet to write to
     */
    public void writeState(Sheet sheet) { //maybe these should be in Life
        sheet.clear();
        try {
            for (OnOffCellLocation cell : state) {
                if (cell.isOn()) {
                    sheet.update(cell, new Constant(1));
                }
            }
        } catch (TypeError e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Progresses the state. This is done by checking each cell in the state and determining
     * if it should turn on or off depending on the game of life rules (see determineOnOffCell).
     */
    public void progressState() {   
        List<OnOffCellLocation> newState = new ArrayList<>();

        for (OnOffCellLocation cell : state) {
            int neighbours = countNeighbourOnCells(cell);

            if (determineCellOnOff(cell.isOn(), neighbours)) {
                newState.add(new OnOffCellLocation(cell.getRow(), cell.getColumn(), true));
            } else {
                newState.add(new OnOffCellLocation(cell.getRow(), cell.getColumn(), false));
            }
        }
        state = newState;
    }
    
    /**
     * Counts the neighbouring on cells of a given cell.
     * 
     * @param countingCell the cell to count the on neighbours of.
     * @return the number of neighbour cells that are on
     */
    private int countNeighbourOnCells(OnOffCellLocation countingCell) {
        int count = 0;

        for (OnOffCellLocation cell : state) {
            if (countingCell.isNeighbour(cell) && cell.isOn()) {
                count++;
            }
        }
        return count;
    }

    /**
    * Determines whether a cell should be turned on or off based on Conway's Game of Life rules.
    * 
    * The rules are:
    * Rule 1: Any on cell with fewer than two on neighbours dies.
    * Rule 2: Any ln cell with two or three on neighbours lives on.
    * Rule 3: Any on cell with more than three on neighbours turns off.
    * Rule 4: Any off cell with exactly three on neighbours becomes an on cell.
    * 
    * @param isOn true if the given cell is on, false if it is off
    * @param neighbours The number of on neighbours the cell has.
    * @return true if the cell should be turned on, false if it should be turned off.
    */
    private boolean determineCellOnOff(boolean isOn, int neighbours) {
        if (isOn) { 
            // Rule 1: Any on cell with fewer than two on neighbours turns off.
            if (neighbours < 2) {
                return false;
            // Rule 2: Any on cell with two or three on neighbours stays on.
            } else if (neighbours == 2 || neighbours == 3) {
                return true;
            // Rule 3: Any on cell with more than three on neighbours turns off.
            } else {
                return false;
            }
        } else {
            // Rule 4: Any cell with three on neighbours turns on, otherwise it stays off.
            return neighbours == 3;
        }
    }

    /**
     * Renders the state into a string.
     */
    public String toString() {
        String renderedState = "";
        for (OnOffCellLocation cell : state) {
            if (cell.isOn()) {
                renderedState += cell.toString() + ":ON, ";
            }
        }
        return renderedState;
    }

}
