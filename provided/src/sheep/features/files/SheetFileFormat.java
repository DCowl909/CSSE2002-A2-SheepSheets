package sheep.features.files;

import java.util.ArrayList;
import java.util.List;

import sheep.sheets.CellLocation;
import sheep.sheets.Sheet;

/**
 * Static class that encodes and decodes files based on a format. This allows for a user
 * to easily change the sheet file format without breaking FileReading and FileWriting. Note
 * this format encodes and decodes in reference to lists of strings which are assumed to be the
 * lines of the sheet file.
 */
public class SheetFileFormat {

    /**
     * Encodes the sheet to a list of strings. Each string is assumed to be a line of the
     * sheet file that this sheet will be saved to. 
     * In this format, the dimensions are encoded in the first string as "Dimensions:x,y".
     * Each following string encodes a line of the sheet, with each value seperated by commas.
     * 
     * @param sheetToEncode Sheet to encode
     * @requires Path and file to be existing
     */
    public static List<String> encodeSheet(Sheet sheetToEncode) {
        List<String> encodedLines = new ArrayList<>();

        int rows = sheetToEncode.getRows();
        int columns = sheetToEncode.getColumns();

        encodedLines.add("Dimensions:" + rows + "," + columns);
        for (int i = 0; i < rows; i++) {
            String encodedRow = "";
            for (int j = 0; j < columns; j++) {

                String cellValue = sheetToEncode.valueAt(new CellLocation(i, j)).render();
                encodedRow = encodedRow + ", " + cellValue.strip(); //avoid whitespace
            }
            encodedLines.add(encodedRow.substring(1));
        }
        return encodedLines;
    }

    /**
     * Reads the dimensions of a sheet to be loaded based on a list of strings. This list of
     * strings is assumed to be the lines of a saved sheet file to be loaded.
     * 
     * @param encodedLines the encoded lines of the sheet file.
     * @returns the dimensions of the new sheet being loaded
     * @throws SheetFileReadingException if the dimensions cannot be read due to incorrect file
     *                                   formatting.
     */
    public static int[] readDimensions(List<String> encodedLines) 
            throws SheetFileReadingException {

        String line1 = encodedLines.get(0);
        try {
            String[]splitLine1 = line1.split(":");
            String[] splitDimensions = splitLine1[1].split(",");

            int rows = Integer.parseInt(splitDimensions[0]);
            int columns = Integer.parseInt(splitDimensions[1]);

            return new int[]{rows, columns};

        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            throw new SheetFileReadingException("Cannot read dimensions on first line of file.");
        }
    }

    /**
     * Reads the cell values from a list of strings. This list of
     * strings is assumed to be the lines of a saved sheet file to be loaded.
     * 
     * @param dimensions the dimensions of the sheet file to be loaded
     * @param encodedLines the lines read from the sheet file
     * @throws SheetFileReadingException if the cells cannot be read due to incorrect file
     *                                   formatting.
     */
    public static String[][] readCells(int[] dimensions, List<String> encodedLines) 
        throws SheetFileReadingException {
        String[][] cellValues = new String[dimensions[0]][dimensions[1]];
        int rows = encodedLines.size();

        try {
            for (int i = 1; i < rows; i++) {
                String[] lineValues = encodedLines.get(i).split(",");
                for (int j = 0; j < dimensions[1]; j++) {
                    cellValues[i - 1][j] = lineValues[j]; 

                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new SheetFileReadingException("Incorrect encoding of sheet values in file");
        }
        return cellValues;
    }
    
}
