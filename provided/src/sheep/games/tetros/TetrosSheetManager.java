package sheep.games.tetros;

import sheep.expression.TypeError;
import sheep.expression.basic.Constant;
import sheep.expression.basic.Nothing;
import sheep.sheets.CellLocation;
import sheep.sheets.Sheet;

/**
 * An object that manages interaction with the sheet for a game of tetros. It handles both reading
 * the sheet when needed and rendering the sheet based on the tetros game state.
 */
public class TetrosSheetManager {

    private Sheet gameSheet;

    /**
     * Initalises a a TetrosSheetManager object with the sheet it will manage.
     * 
     * @param sheet the sheet that the manager will manage.
     */
    public TetrosSheetManager(Sheet sheet) {
        gameSheet = sheet;
    }

    /**
     * Checks if this tile is in the bounds of a provided sheet. This means that no cell
     * the tile occupies is outside the cells of the sheet.
     * 
     * @param tile the state of the falling tile
     * 
     * @return True if the tile is in the bounds of the sheet, false otherwise.
     */
    public boolean inBounds(FallingTile tile) {
        for (CellLocation location : tile.getContents()) {
            if (!gameSheet.contains(location)) {
                return false;
            }
        }
        return true;
    }

    /** Checks if this tile is inside the tile pile of a given sheet. The tile pile is the pile of
     * tile pieces that are sitting stacked from the bottom of the sheet. 
     * This means that no cell this tile occupies is the same as a cell occupied by a tile piece in the
     * pile.
     * 
     * @param fallingTile the state of the falling tile
     * 
     * @return true if this tile is touching the pile of the given sheet, false otherwise.
     */
    public boolean touchingPile(FallingTile fallingTile) {
        for (CellLocation cell : fallingTile.getContents()) {
            if (!gameSheet.valueAt(cell).render().equals("")) { //stops on non empty squares
                return true;
            }
        }   
        return false;
    }

    /**
     * Unrenders the falling tile from the sheet. That is, all cells where the falling tile used
     * to occupy will now be blank.
     * 
     * @param fallingTile the state of the falling tile.
     */
    public void unrender(FallingTile fallingTile) {
        for (CellLocation cell : fallingTile.getContents()) {
            try {
                gameSheet.update(cell, new Nothing());
            } catch (TypeError e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
    * Renders a falling tile onto the sheet.
    **
    * @param tile the falling tile to render onto the sheet.
    */ 
    public void render(FallingTile tile) {
        for (CellLocation cell : tile.getContents()) {
            try {
                gameSheet.update(cell, new Constant(tile.getType()));
            } catch (TypeError e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * If the tiles have fallen such that an entire row is filled, this method clears that row
     * and drops the tiles above down one cell.
     * 
     * @param fallingTile the current state of the fallingTile
     */
    public void clearFullRows(FallingTile fallingTile) {
        for (int row = gameSheet.getRows() - 1; row >= 0; row--) {
            if (isRowFull(row)) {
                for (int rowX = row; rowX > 0; rowX--) { //drops every row beneath the erased row
                    for (int col = 0; col < gameSheet.getColumns(); col++) {
                        if (fallingTile.containsCell(rowX - 1, col)) {
                            continue;
                        }
                        String value = gameSheet.valueAt(rowX - 1, col).getContent();
                        gameSheet.update(rowX, col, value);
                    }
                }
                row = row + 1;
            }
        }
    }

    /**
     * Helper method for clearFullRows. Checks if a given row is full. That is, every cell along
     * that row is occupied by a tile.
     * 
     * @param row the row to check
     * @return True if the row is full, false otherwise.
     */
    private boolean isRowFull(int row) {
        for (int col = 0; col < gameSheet.getColumns(); col++) {
            if (gameSheet.valueAt(row, col).getContent().equals("")) {
                return false;
            }
        }
        return true;
    }
    


    
}
