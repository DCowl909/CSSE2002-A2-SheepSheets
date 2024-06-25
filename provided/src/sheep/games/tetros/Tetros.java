package sheep.games.tetros;


import sheep.features.Feature;
import sheep.games.random.RandomTile;
import sheep.sheets.Sheet;
import sheep.ui.*;

/**
 * Controls the logic of a game of tetros onto a provided sheet. Is response for deciding when to
 * update the board and how to change the state.
 */
public class Tetros implements Tick, Feature {

    private boolean started = false;
    private FallingTile fallingTile;
    private RandomTile randomTile;
    private TetrosSheetManager sheetManager;

    /**
     * Initalises a Tetros object using the sheet the snake game will be played on, and a
     * randomCell generator to allow random cells to be selected. 
     * 
     * @param sheet the sheet this game of tetros will be played on 
     * @param randomTile a randomTile generator to get random cells from
     */
    public Tetros(Sheet sheet, RandomTile randomTile) {
       
        this.randomTile = randomTile;
        sheetManager = new TetrosSheetManager(sheet);
    }

    /**
     * Gets the random tile.
     * 
     * @return the random tile.
     */
    public RandomTile getRandomTile() {
        return randomTile;
    } 

    /**
     * Gets the manager.
     * 
     * @return the the manager
     */
    public TetrosSheetManager getManager() {
        return sheetManager;
    } 

    /**
    * Registers the UI components and event handlers for controlling the Tetros game.
    * This method binds specific actions to UI events, such as ticking, starting the game,
    * and changing the direction of Tetros.
    * Binds keystrokes to their corresponding method calls.

    * @param ui UI instance to register components and event handlers with.
    */
    @Override
    public void register(UI ui) {
        ui.onTick(this);
        ui.addFeature("tetros", "Start Tetros", (row, column, prompt) -> startTetros());
        ui.onKey("a", "Move Left", (row, column, prompt) -> move(-1));
        ui.onKey("d", "Move Right", (row, column, prompt) -> move(1));
        ui.onKey("q", "Rotate Left", (row, column, prompt) -> rotate(-1));
        ui.onKey("e", "Rotate Right", (row, column, prompt) -> rotate(1));
        ui.onKey("s", "Drop", (row, column, prompt) -> fullDrop());
    }

    /**
     * Starts the Tetros game. 
     */
    public void startTetros() {
        started = true;
        dropNewTile();

    }

    /**
    * Moves the falling tile left or right on the game sheet and then re-renders the sheet.
    * If the tile is touching the edge of the sheet, it will not move.
    *  
    * @param direction -The direction to move the tile. Use a negative value to move left
    *                   and a positive value to move right. The magnitude determines the
    *                   number of cells to move.
    */
    public void move(int direction) {
        if (!started) {
            return;
        }
        FallingTile movedTile = fallingTile.moveTile(direction);
        if (sheetManager.inBounds(movedTile)) {
            sheetManager.unrender(fallingTile);
            fallingTile = movedTile;
        }
        sheetManager.render(fallingTile);

    }

    /**
     * Rotates the falling tile either clockwise or anticlockwise on the game sheet and then
     * re-renders the sheet. If the tile is touching the edge and the rotation would place it
     * out of bounds, it will not rotate.
     * 
     * @param direction The direction to move the tile. Use a positive value for clcockwise
     * rotation and a negative value for anticlockwise rotation. The magnitude determines the
     * scaling of the rotation
     */
    public void rotate(int direction) {
        if (!started) {
            return;
        }
        FallingTile rotatedTile = fallingTile.rotateTile(direction);
        if (sheetManager.inBounds(rotatedTile)) {
            sheetManager.unrender(fallingTile);
            fallingTile = rotatedTile;
        }
        sheetManager.render(fallingTile);
    }


    /**
    * Drops the falling tile vertically by one cell on the game sheet.
    * If the tile successfully drops without colliding with existing tiles or the edge of the 
    * game sheet, it updates the game sheet and returns true; otherwise, it retains
    * the previous position of the falling tile and returns false.
    *
    * @return true if the tile successfully lands without collision, false otherwise.
    */
    public boolean drop() {
        if (!started) {
            return false;
        }
        FallingTile droppedTile = fallingTile.dropTile();

        sheetManager.unrender(fallingTile);

        if (sheetManager.inBounds(droppedTile) && !sheetManager.touchingPile(droppedTile)) {
            fallingTile = droppedTile;
            sheetManager.render(fallingTile);
            return true;
        } else {
            sheetManager.render(fallingTile);
            return false;
        }
    }

    /**
     * Drops the tile to the bottom of the gamesheet. This means it will either drop to the bottom
     * of the sheet, or land on top of another tile in the pile.
     */
    public void fullDrop() {
        while (drop()) {

        }
    }

    /**
     * Drops a new tile from the top of the gamesheet. This tile will begin falling and can be
     * moved by the player. 
     * If the tile is successfully dropped, it updates the game and returns true. Otherwise, 
     * returns false.
     * 
     * @return If the tile generates in a location where a tile already exists, then
     *         returns false. Otherwise, if the generation is successful, returns true.
     */
    private boolean dropNewTile() {
        int value = randomTile.pick();
        fallingTile =  new FallingTile(value);

        if (sheetManager.touchingPile(fallingTile)) {
            return false;

        } else { 
            sheetManager.render(fallingTile);
            return true;
        }
    }

    /**
    * Updates the game sheet every tick. 
    * For Tetris, this involves either dropping the current falling tile by one cell or 
    * introducing a new tile, and subsequently checking for complete rows to erase.
    * 
    * @param prompt the prompt the tick interacts with.
    * @returns if the sheet should be updated after the tick.
    */
    @Override
    public boolean onTick(Prompt prompt) {
        if (!started) {
            return false;
        }

        if (!drop()) {
            if (!dropNewTile()) {
                prompt.message("Game Over!");
                started = false;
            }
        }
        sheetManager.clearFullRows(fallingTile);
        return true;
    }

}