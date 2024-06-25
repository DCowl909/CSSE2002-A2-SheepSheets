package sheep.games.tetros;

import java.util.ArrayList;
import java.util.List;

import sheep.sheets.CellLocation;

/**
 * Represents a falling tile in tetros. 
 */
public class FallingTile {
    private List<CellLocation> contents;
    private int type;

    /**
     * Initialises new tile at the top of the sheet based on a given generation value. 
     * Different values correspond to different tiles of different shapes and types.  
     * 
     * @param value the generation value that determines the shape and type of the tile. 
     */
    public FallingTile(int value) {
        contents = new ArrayList<>();

        switch (value) {
            case 0 -> {
                contents.add(new CellLocation(0, 0));
                contents.add(new CellLocation(0, 1));
                contents.add(new CellLocation(1, 1));
                contents.add(new CellLocation(1, 2));
                this.type = 4;
            }
            case 1 -> {
                contents.add(new CellLocation(0, 0));
                contents.add(new CellLocation(1, 0));
                contents.add(new CellLocation(2, 0));
                contents.add(new CellLocation(2, 1));
                this.type = 7;
            }
            case 2 -> {
                contents.add(new CellLocation(0, 1));
                contents.add(new CellLocation(1, 1));
                contents.add(new CellLocation(2, 1));
                contents.add(new CellLocation(2, 0));
                this.type = 5;
            }
            case 3 -> {
                contents.add(new CellLocation(0, 0));
                contents.add(new CellLocation(0, 1));
                contents.add(new CellLocation(0, 2));
                contents.add(new CellLocation(1, 1));
                this.type = 8;
            }
            case 4 -> {
                contents.add(new CellLocation(0, 0));
                contents.add(new CellLocation(0, 1));
                contents.add(new CellLocation(1, 0));
                contents.add(new CellLocation(1, 1));
                this.type = 3;
            }
            case 5 -> {
                contents.add(new CellLocation(0, 0));
                contents.add(new CellLocation(1, 0));
                contents.add(new CellLocation(2, 0));
                contents.add(new CellLocation(3, 0));
                this.type = 6;
            }
            case 6 -> {
                contents.add(new CellLocation(0, 1));
                contents.add(new CellLocation(0, 2));
                contents.add(new CellLocation(1, 1));
                contents.add(new CellLocation(0, 1));
                this.type = 2;
            }
        }
    }

    /**
     * Initialises tile based on a provided list of cells for the tile to occupy.
     * 
     * @param contents the list of cells for the tile to occupy
     * @param type the numeric value the tetros tile will appear as in the sheet
     */
    private FallingTile(List<CellLocation> contents, int type) {
        this.contents = contents;
        this.type = type;
    }


    /**
     * Get the cells taken up by a tile.
     * 
     * @return the contents of the tile.
     */
    public List<CellLocation> getContents() {
        return contents;
    }

    /**
     * Get the number type of the tile. This is the number that the tile is rendered as in
     * a sheet.
     * 
     * @return the number type of the tile.
     */
    public int getType() {
        return type;
    }

    /**
     * Moves a tile left or right.
     * This method will not check for boundaries or illegal tile positions.
     * 
     * @param distance  The direction to move this tile. Use a negative value to move left
    *                   and a positive value to move right. The magnitude determines the
    *                   number of cells to move.
     */
    public FallingTile moveTile(int distance) {
        List<CellLocation> newContents = new ArrayList<>();

        for (CellLocation tile : contents) {
            newContents.add(new CellLocation(tile.getRow(), tile.getColumn() + distance));
        }
        return new FallingTile(newContents, type);
    }

    /**
     * Rotates this tile according to a given direction. The direction of the rotation is determined
     * by the sign of the direction and its magnitude.
     * This method will not check for boundaries or illegal tile positions.
     * 
     * @param direction The direction to move the tile. Use a positive value for clcockwise
     *                  rotation and a negative value for anticlockwise rotation. The magnitude determines the
     *                  scaling of the rotation
     */
    public FallingTile rotateTile(int direction) {
        int x = 0;
        int y = 0;

        for (CellLocation cellLocation : contents) {
            x += cellLocation.getColumn();
            y += cellLocation.getRow();
        }
        x /= contents.size(); 
        y /= contents.size();

        List<CellLocation> newContents = new ArrayList<>();
        
        for (CellLocation location : contents) {
            int lx = x + ((y - location.getRow()) * direction);
            int ly = y + ((x - location.getColumn()) * direction);
            CellLocation replacement = new CellLocation(ly, lx);
            newContents.add(replacement);
        }
        return new FallingTile(newContents, type);
    }

    /**
     * Drops this tile down by one cell.
     * This method will not check for boundaries or illegal tile positions.
     */
    public FallingTile dropTile() {
        List<CellLocation> newContents = new ArrayList<>();
        for (CellLocation tile : contents) {
            newContents.add(new CellLocation(tile.getRow() + 1, tile.getColumn()));
        }
        return new FallingTile(newContents, type);
    }

    /**
     * If a tile contains a certain cell. That is, if a certain cell is part of this tile piece.
     * 
     * @param row the row of the cell
     * @param column the column of the cell
     * @return true if the tile contains the cell, and false otherwise.
     */
    public boolean containsCell(int row, int column) {
        for (CellLocation cell : contents) {
            if (cell.getRow() == row && cell.getColumn() == column) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the tile rendered as a string.
     * 
     * @return the rendered tile as a string.
     */
    public String toString() {
        String renderedTile = "";
        for (CellLocation cell : contents) {
            renderedTile = renderedTile + cell.toString() + ", ";
        }
        return renderedTile;
    }

}