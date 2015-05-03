package multiValuedNominalAlpha.reliabilityDataFactory;

import com.opencsv.CSVReader;
import multiValuedNominalAlpha.ReliabilityDataMatrix;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

public class ReliabilityDataFactory {


    public static ReliabilityDataMatrix loadReliabilityDataFromCSV(String filename, char labelSeperator, char valueSeperator, int firstDataRow, int firstDataCol, boolean rowsForUnits)
            throws Exception {

        String[][] reliabilityData;
        reliabilityData = readDataFromCSV(filename, labelSeperator);
        ReliabilityDataMatrix rdm = processReliabilityData(valueSeperator, firstDataRow, firstDataCol, rowsForUnits, reliabilityData);

        return rdm;
    }

    /**
     * Process a data matrix based on the expected format
     *
     * @param valueSeperator  char used to seperate values within a cell
     * @param firstDataRow    which row to start interpreting as reliability data
     * @param firstDataCol    which col to start interpreting as reliability data
     * @param rowsForUnits    if true, use a row per unit, otherwise a row per coder with units in columns
     * @param reliabilityData the data matrix
     * @return A reliability data matrix
     * @throws Exception
     */
    public static ReliabilityDataMatrix processReliabilityData(char valueSeperator, int firstDataRow, int firstDataCol, boolean rowsForUnits, String[][] reliabilityData) throws Exception {
        int numOfUnits;
        int numOfCoders;
        if (!rowsForUnits) {
            numOfUnits = reliabilityData[0].length - (firstDataCol - 1);
            numOfCoders = reliabilityData.length - (firstDataRow - 1);
        } else {
            numOfUnits = reliabilityData.length - (firstDataCol - 1);
            numOfCoders = reliabilityData[0].length - (firstDataRow - 1);
        }

        if (numOfUnits < 1) {
            throw new Exception("Data does not contain at least one unit.");
        }

        if (numOfCoders < 2) {
            throw new Exception("Data does not contain at least two coders.");
        }

        if (rowsForUnits) {
            reliabilityData = switchOrientation(reliabilityData);
        }

        int numberOfCoders = reliabilityData.length - (firstDataRow - 1);
        int numberOfUnits = reliabilityData[0].length - (firstDataCol - 1);

        int baseRowIndex = firstDataRow - 1;
        int baseColIndex = firstDataCol - 1;

        ReliabilityDataMatrix rdm = new ReliabilityDataMatrix(numberOfUnits, numberOfCoders);

        for (int coder = baseRowIndex; coder < reliabilityData.length; coder++) {
            for (int unit = baseColIndex; unit < reliabilityData[coder].length; unit++) {
                if (reliabilityData[coder].length - (firstDataCol - 1) != numberOfUnits) {
                    throw new Exception("Reliability data does not have equal number of units on each line");
                }

                ArrayList labels = parseMultiLabel(reliabilityData[coder][unit], valueSeperator);
                try {
                    rdm.setLabels(unit - baseColIndex, coder - baseRowIndex, labels);
                } catch (ArrayIndexOutOfBoundsException boundsE) {
                    throw new Exception("Reliability data does not have equal number of units on each line");
                }
            }
        }
        return rdm;
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

    private static String[][] switchOrientation(String[][] data) {
        String[][] result = new String[data[0].length][data.length];
        for (int x = 0; x < data.length; x++) {
            for (int y = 0; y < data[x].length; y++) {
                result[y][x] = data[x][y];
            }
        }

        return result;
    }

    private static ArrayList parseMultiLabel(String label, char seperator)
            throws Exception {
        String[] multiLabels = null;

        if (label.equals(Character.toString(seperator))) {
            return new ArrayList();
        }
        try {
            CSVReader reader = new CSVReader(new StringReader(label), seperator);
            multiLabels = reader.readNext();
        } catch (IOException e) {
            throw new Exception(e.getMessage());
        } catch (NullPointerException e) {
        }

        if (multiLabels == null) {
            return null;
        }

        if ((multiLabels.length == 1) && (multiLabels[0].equals("{}"))) {
            return new ArrayList();
        }

        ArrayList labels = new ArrayList();
        for (int i = 0; i < multiLabels.length; i++) {
            if (!labels.contains(multiLabels[i])) {
                labels.add(multiLabels[i]);
            }
        }

        return labels;
    }

}