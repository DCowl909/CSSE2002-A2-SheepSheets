package sheep.games.life;

import sheep.features.Feature;
import sheep.sheets.Sheet;
import sheep.ui.Prompt;
import sheep.ui.Tick;
import sheep.ui.UI;

/**
 * Manages and runs Conway's Game of Life in a sheet. On cells are represented by a 1 and off
 * cells are represented by a blank cell.
 * To run the game, a user turns on as many cells as they want by entering a 1, and then selecting
 * "Start Game of Life" in the features menu.
 * Note cells CAN be manually adjusted halfway through the game.
 * 
 */
public class Life implements Feature, Tick {

    private Sheet liveSheet;
    private LifeState gameState; //game of life state
    private boolean started = false; //if the game has started


    /**
     * Initalises a new life object with the sheet the game of life will be run in.
     * 
     * @param sheet the sheet the game of life will be run in.
     */
    public Life(Sheet sheet) {
        liveSheet = sheet;
        gameState = new LifeState(sheet);
    }

    @Override
    public void register(UI ui) {
        ui.onTick(this);
        ui.addFeature("gol-start", "Start Game of Life", (row, column, prompt) -> started = true);
        ui.addFeature("gol-end", "End Game of Life", (row, column, prompt) -> started = false);

    }
    
    /**
     * Advances the state of Conway's Game of Life by one iteration.
     * 
     * This method is invoked in each tick of the game loop to progress the game state 
     * according to the rules of Conway's Game of Life. It reads the current state of 
     * the game from the provided live sheet, applies the rules of the game to update 
     * the state, and then writes the new state back to the live sheet.
     * 
     * @param prompt The prompt object for the user input and events.
     * @return true if the game state was successfully progressed, 
     * false if the game has not started yet and no action was taken.
    */
    public boolean onTick(Prompt prompt) {
        if (!started) {
            return false;
        }

        gameState.readState(liveSheet);

        gameState.progressState();

        gameState.writeState(liveSheet);

        return true;
        
    }
}
