package multiValuedNominalAlpha.mvnaCalculator;

import com.opencsv.CSVReader;
import multiValuedNominalAlpha.ui.UISettings;
import multiValuedNominalAlpha.mvnaCalculator.model.ReliabilityDataMatrix;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;

public class ReliabilityDataFactory {

    /**
     * Load from a path
     *
     * @param filename the path to the file
     * @param ui       the settings used to parse the file
     * @return parsed data
     * @throws Exception
     */
    public static ReliabilityDataMatrix loadReliabilityDataFromCSV(String filename, UISettings ui)
            throws Exception {

        String[][] reliabilityData = readDataFromCSV(filename, ui.getUnitSeperator());
        return processReliabilityData(ui, reliabilityData);
    }


    /**
     * Process a data matrix based on the expected format
     *
     * @param ui settings required to parse the data
     * @param reliabilityData the data matrix
     * @return A reliability data matrix
     * @throws Exception
     */
    public static ReliabilityDataMatrix processReliabilityData(UISettings ui, String[][] reliabilityData) throws Exception {

        int baseRowIndex;
        int baseColIndex;

        if (!ui.isRowForCoders()) {
            reliabilityData = switchOrientation(reliabilityData);
            baseRowIndex = ui.getFromCol() - 1;
            baseColIndex = ui.getFromRow() - 1;
        } else {
            baseRowIndex = ui.getFromRow() - 1;
            baseColIndex = ui.getFromCol() - 1;
        }

        // reliability[0] has all of the observations for the first unit

        int numberOfCoders = reliabilityData.length - baseRowIndex;
        int numberOfUnits = reliabilityData[0].length - baseColIndex;

        if (numberOfUnits < 1) {
            throw new Exception("Data does not contain at least one unit.");
        }

        if (numberOfCoders < 2) {
            throw new Exception("Data does not contain at least two coders.");
        }

        ReliabilityDataMatrix rdm = new ReliabilityDataMatrix(numberOfUnits, numberOfCoders);

        //
        for (int coder = baseRowIndex; coder < reliabilityData.length; coder++) {
            for (int unit = baseColIndex; unit < reliabilityData[coder].length; unit++) {

                if (reliabilityData[coder].length - baseColIndex != numberOfUnits) {
                    throw new Exception("Reliability data does not have equal number of units on each line");
                }

                String[] labels = parseMultiLabel(reliabilityData[coder][unit], ui.getValueSeperator());
                try {
                    rdm.setLabels(unit - baseColIndex, coder - baseRowIndex, labels);
                } catch (ArrayIndexOutOfBoundsException boundsE) {
                    throw new Exception("Reliability data does not have equal number of units on each line");
                }
            }
        }
        return rdm; // This matrix is rdm[0] is the first unit.
    }


    private static String[][] readDataFromCSV(String filename, char delimeter) throws Exception {
        String[][] reliabilityData;
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            CSVReader reader = new CSVReader(br, delimeter);
            reliabilityData = reader.readAll().toArray(new String[0][0]);
        } catch (IOException e) {
            throw new Exception(e.getMessage());
        }
        return reliabilityData;
    }

    /**
     * Transpose the array
     * @param data
     * @return a new array, transposed
     */
    private static String[][] switchOrientation(String[][] data) {
        String[][] result = new String[data[0].length][data.length];
        for (int x = 0; x < data.length; x++) {
            for (int y = 0; y < data[x].length; y++) {
                result[y][x] = data[x][y];
            }
        }

        return result;
    }

    private static String[] parseMultiLabel(String label, char seperator)
            throws Exception {
        String[] multiLabels = null;

        if (label.equals(Character.toString(seperator))) {
            return new String[0];
        }
        try {
            CSVReader reader = new CSVReader(new StringReader(label), seperator);
            multiLabels = reader.readNext();
        } catch (IOException e) {
            throw new Exception(e.getMessage());
        } catch (NullPointerException ignored) {
        }

        if (multiLabels == null) {
            return null;
        }

        if ((multiLabels.length == 1) && (multiLabels[0].equals("{}"))) {
            return new String[0];
        }

        return multiLabels;
    }

}