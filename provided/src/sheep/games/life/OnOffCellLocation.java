package sheep.games.life;

import sheep.sheets.CellLocation;

/**
 * A cell location with an added attribute of either being "on" or "off". Used for playing
 * Conway's game of life. 
 */
public class OnOffCellLocation extends CellLocation {
    
    private boolean isOn;
    
    /**
     * Construct a new cell location at the given row and column that is either on or off.

     * @requires row and column are greater than or equal to zero.
     * @requires column is less than 26.
     * @param row A number representing the row number.
     * @param column A number representing the column.
     * @param isOn If the Cell Location should be initalised as "on"
     */
    public OnOffCellLocation(int row, char column, boolean isOn) {
        super(row, column);
        this.isOn = isOn;

    }  

    /**
     * Construct a new cell location at the given row and column that is either on or off.
     *
     * In this constructor column chars are converted to column indexes,
     * e.g. column 'A' would become 0 and 'B' would become 1 and so on.
     *
     * @requires row is greater than or equal to zero, column is between 'A' and 'Z' inclusive.
     * @param row A number representing the row number.
     * @param column A character representing the column.
     * @param isOn True if the cell Location should be initalised as "on"
     *             False if the cell location should be initalised as "off"
     */
    public OnOffCellLocation(int row, int column, boolean isOn) {
        super(row, column);
        this.isOn = isOn;

    }

    /**
     * If the cell is on.
     * @return true if the cell is on. False if the cell is off.
     */
    public boolean isOn() {
        return isOn;
    }
    
    /**
     * Turns the cell on.
     */
    public void turnOn() {
        isOn = true;
    }

    /**
     * Turns the cell off.
     */
    public void turnOff() {
        isOn = false;
    }

    /**
     * Determines if a given other cell is a neighbour to this one. That is, if the other cell
     * is either diagonal to this cell or share a boundary. A cell is not its own neighbour.
     * 
     * @param other another cell location to compare to
     * @return True if the cells are neighbours, false otherwise
     */
    public boolean isNeighbour(OnOffCellLocation other) {
        int rowDif = this.getRow() - other.getRow();
        int columnDif = this.getColumn() - other.getColumn();

        if (rowDif <= 1 && rowDif >= -1) {
            if (columnDif <= 1 && columnDif >= -1) {
                if (rowDif != 0 || columnDif != 0) { //cannot be the same cell
                    return true;
                }
            }
        }
        return false;
    }

} 