package multiValuedNominalAlpha.reliabilityDataFactory;

import multiValuedNominalAlpha.gui.UISettings;
import multiValuedNominalAlpha.mvnaCalculator.ReliabilityDataFactory;
import multiValuedNominalAlpha.mvnaCalculator.model.Labels;
import multiValuedNominalAlpha.mvnaCalculator.model.ReliabilityDataMatrix;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class ReliabilityDataFactoryTest {

    @Test
    public void testLoadReliabilityDataFromCSV() throws Exception {

    }

    @Test
    public void testProcessReliabilityData() throws Exception {

        String[][] exReal = new String[][]{{"b|x", "a|b|x", "{}", "a"}, {"x", "b", "a", ""}, {"", "b|x", "a", ""}, {"p", "p", "p", "p"}};
        ReliabilityDataMatrix rReal = ReliabilityDataFactory.processReliabilityData(UISettings.DEFAULT, exReal);
        assertEquals(rReal.getLabels(1, 1), new Labels(new String[]{"b"}));
        assertEquals(rReal.getLabels(0, 0), new Labels(new String[]{"b", "x"}));

        String[][] ex1 = new String[][]{{"", "u1", "u2"}, {"c1", "A", "B"}, {"c2", "", "{}"}};
        UISettings u1 = new UISettings("", "", ',', '|', 2, 2, false, true);
        ReliabilityDataMatrix r1 = ReliabilityDataFactory.processReliabilityData(u1, ex1);
        assertEquals(r1.getLabels(1, 1), new Labels(new String[]{}));
        assertEquals(r1.getLabels(0, 0), new Labels(new String[]{"A"}));
        assertEquals(r1.getLabels(0, 1), null);

        String[][] ex2 = new String[][]{{"c1", "c2", "c3"}, {"c1a1;c1a2", "c2a", "c3a"}, {"c1b", "c2b", "c3b"}};
        UISettings u2 = new UISettings("", "", ',', ';', 2, 1, false, false);
        ReliabilityDataMatrix r2 = ReliabilityDataFactory.processReliabilityData(u2, ex2);
        assertEquals(r2.getLabels(0, 0), new Labels(new String[]{"c1a1", "c1a2"}));
        assertEquals(r2.getLabels(1, 2), new Labels(new String[]{"c3b"}));
    }
}