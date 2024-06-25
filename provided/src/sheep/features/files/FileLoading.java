package sheep.features.files;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import sheep.features.Feature;
import sheep.sheets.Sheet;
import sheep.ui.Prompt;
import sheep.ui.UI;

/**
 * Object that manages loading of files.
 */
public class FileLoading implements Feature {

    private Sheet activeSheet;
    
    /**
     * Initialises a FileLoading object with a sheet to load to.
     * 
     * @param sheet the sheet to load to
     */
    public FileLoading(Sheet sheet) {

        activeSheet = sheet;

    }

    /**
     * Registers file loading as another feature in the feature menu.
     * 
     * @param ui the UI object to register the feature to.
     */
    @Override
    public void register(UI ui) {
        ui.addFeature("load-file", "Load a file", 
            (row, column, prompt) -> this.loadSheet(prompt));
    }

    /**
     * Loads a sheet from a file specified by user input. If an issue occurs while loading
     * the file, an appropriate error message popup will be delivered to the user. 
     * Additionally, if an error occurs during loading, the sheet will revert to its state 
     * before the user tried to load anything.
     * 
     * @param prompt the prompt object the user enters input into and through which error
     *               messages pop up.
     */
    public void loadSheet(Prompt prompt) {

        try {
            //obtain Path object from user input
            Optional<String> pathInput = prompt.ask("Sheet path:");
            if (pathInput.isEmpty()) { //if save attempt is cancelled
                return;
            }
            Path sheetPath = Paths.get(pathInput.get()); 

            //read in and verify that sheet file is encoded properly
            List<String> encodedLines = Files.readAllLines(sheetPath);
            int[] dimensions = SheetFileFormat.readDimensions(encodedLines);
            String[][] cellValues = SheetFileFormat.readCells(dimensions, encodedLines);
            
            //load pre-read sheet information into sheet
            activeSheet.clear();
            activeSheet.updateDimensions(dimensions[0], dimensions[1]);
            loadCellValues(activeSheet, cellValues, dimensions); 

        } catch (InvalidPathException e) {
            prompt.message("Invalid path entered.");

        } catch (SheetFileReadingException e) {
            prompt.message("File cannot be read \nReason:" + e.getMessage());

        } catch (IOException e) {
            prompt.message("An IOException was thrown. Type:" + e.getClass().getName());
        }
        
    }
       
    /**
     * Load cell values into a given sheet based on pre-read cell values.
     * 
     * @requires the dimensions of the parameter cellValues to agree with the parameter
     *           dimensions
     * @param cellValues the value of every cell in the sheet to load.
     * @param dimensions the dimensions of the sheet to load.
     */
    public void loadCellValues(Sheet sheet, String[][] cellValues, int[] dimensions) {

        for (int i = 0; i < dimensions[0]; i++) {
            for (int j = 0; j < dimensions[1]; j++) {
                sheet.update(i, j, cellValues[i][j]);
            }
        }

    }

}


