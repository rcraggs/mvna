package multiValuedNominalAlpha.reliabilityDataFactory;

import multiValuedNominalAlpha.ReliabilityDataMatrix;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class ReliabilityDataFactoryTest {

    @Test
    public void testLoadReliabilityDataFromCSV() throws Exception {

    }

    @Test
    public void testProcessReliabilityData() throws Exception {

        String[][] testData = new String[1][2];
        testData[0][0] = "a";
        testData[0][1] = "b";
        ReliabilityDataMatrix r = ReliabilityDataFactory.processReliabilityData('|', 1, 1, true, testData);
        assertEquals(r.getNumberOfUnits(), 1);
        assertEquals(r.getNumberOfCoders(), 2);
        assertEquals(r.getLabels()[0][0].size(), 1);
        assertTrue(r.getLabels()[0][0].contains("a"));
        assertEquals(r.getLabels()[0][1].size(), 1);
        assertTrue(r.getLabels()[0][1].contains("b"));

        testData = new String[1][2];
        testData[0][0] = "a|b";
        testData[0][1] = "{}";
        r = ReliabilityDataFactory.processReliabilityData('|', 1, 1, true, testData);
        assertEquals(r.getNumberOfUnits(), 1);
        assertEquals(r.getNumberOfCoders(), 2);
        assertEquals(r.getLabels()[0][0].size(), 2);
        assertTrue(r.getLabels()[0][0].contains("a"));
        assertTrue(r.getLabels()[0][0].contains("b"));
        assertEquals(r.getLabels()[0][1].size(), 0);
    }
}