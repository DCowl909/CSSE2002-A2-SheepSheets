package sheep.features.files;

import sheep.features.Feature;
import sheep.sheets.Sheet;
import sheep.ui.Prompt;
import sheep.ui.UI;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Optional;

/**
 * Object for saving.
 */
public class FileSaving implements Feature {

    private Sheet activeSheet;


    /**
     * Initialises a FileSaving object for a particular sheet.
     * @param sheet the sheet to save to a file.
     */
    public FileSaving(Sheet sheet) {
        this.activeSheet = sheet;
    }

    @Override
    public void register(UI ui) {

        ui.addFeature("save-file", "Save sheet", (row, column, prompt) -> this.saveSheet(prompt));
    }

    /**
     * Saves the current sheet to a file specified by the user. If an issue occurs while saving
     * the file, an appropriate error message popup will be delivered to the user. The file will
     * saved with a format dictated by the SheetFileFormat class. 
     * If a file already exists with that name, it will be overwritten by the newly saved sheet
     * file.
     * 
     * @param prompt the prompt object the user enters input into and through which error messages
     *               pop up.
     */
    public void saveSheet(Prompt prompt) {
        try {
            Optional<Path> sheetPathOptional = getSheetPathFromUser(prompt);
            
            if (sheetPathOptional.isEmpty()) { //if save attempt is cancelled
                return;
            }
            Path sheetPath = sheetPathOptional.get();

            List<String> encodedLines = SheetFileFormat.encodeSheet(activeSheet);
            //create and load encoding into file.
            Files.write(sheetPath, encodedLines);

        } catch (IOException e) {
            prompt.message("An error occurred. Are you sure that directory exists?");
        }
    }

    /**
     * Prompts the user to enter a valid path for saving the sheet and returns 
     * it as a Path object.
     * 
     * @param prompt The prompt object used for communicating with the user.
     * @return An Optional containing the Path object representing the sheet path 
     * entered by the user, or an empty Optional if the user cancels the operation.
     */
    private Optional<Path> getSheetPathFromUser(Prompt prompt) {
        try {
            Optional<String> pathInput = prompt.ask("Sheet path:");
            
            if (pathInput.isEmpty()) { //if save attempt is cancelled
                return Optional.empty();
            }
            Path sheetPath = Paths.get(pathInput.get()); 
            return Optional.of(sheetPath);

        } catch (InvalidPathException e) {
            prompt.message("Invalid path entered.");
            return Optional.empty();
        }
    }
        
        
}
