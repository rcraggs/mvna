package multiValuedNominalAlpha;

import multiValuedNominalAlpha.ui.UISettings;
import multiValuedNominalAlpha.mvnaCalculator.model.ReliabilityDataMatrix;
import multiValuedNominalAlpha.mvnaCalculator.MultiValuedAlphaCalculator;
import multiValuedNominalAlpha.mvnaCalculator.ReliabilityDataFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class MultiValuedAlphaCalculatorTest {

    String[][] ex0;
    ReliabilityDataMatrix r0;
    String[][] ex1;
    ReliabilityDataMatrix r1;
    String[][] ex2;
    ReliabilityDataMatrix r2;
    String[][] ex3;
    ReliabilityDataMatrix r3;

    @BeforeMethod
    public void setUp() throws Exception {

        ex0 = new String[][]{{"b|x", "a|b|x", "{}", "a"}, {"x", "b", "a", ""}, {"", "b|x", "a", ""}};
        r0 = ReliabilityDataFactory.processReliabilityData(UISettings.DEFAULT, ex0);

        ex1 = new String[3][3];
        ex1[0][0] = "a";
        ex1[1][0] = "a";
        ex1[2][0] = "a";
        ex1[0][1] = "a|b|c";
        ex1[1][1] = "a|b|c";
        ex1[2][1] = "a|b|c|d";
        ex1[0][2] = "c";
        ex1[1][2] = "c";
        ex1[2][2] = "c";

        r1 = ReliabilityDataFactory.processReliabilityData(UISettings.DEFAULT, ex1);

        ex2 = new String[3][3];
        ex2[0][0] = "a";
        ex2[1][0] = "a";
        ex2[2][0] = "a";
        ex2[0][1] = "a|b";
        ex2[1][1] = "a|b";
        ex2[2][1] = "a|b|c";
        ex2[0][2] = "c";
        ex2[1][2] = "c";
        ex2[2][2] = "c";

        r2 = ReliabilityDataFactory.processReliabilityData(UISettings.DEFAULT, ex2);

        ex3 = new String[3][3];
        ex3[0][0] = "a";
        ex3[1][0] = "a";
        ex3[2][0] = "a";
        ex3[0][1] = "b";
        ex3[1][1] = "b";
        ex3[2][1] = "b|c";
        ex3[0][2] = "c";
        ex3[1][2] = "c";
        ex3[2][2] = "c";

        r3 = ReliabilityDataFactory.processReliabilityData(UISettings.DEFAULT, ex3);
    }

    @Test
    public void testCalculateDo() throws Exception {

        MultiValuedAlphaCalculator m = new MultiValuedAlphaCalculator(r1, false);
        m.calculateAlpha();
        double deo = m.getDo();
        assertEquals(deo, 0.0317, 0.001);

        m = new MultiValuedAlphaCalculator(r2, false);
        m.calculateAlpha();
        deo = m.getDo();
        assertEquals(deo, 0.044, 0.001);

        m = new MultiValuedAlphaCalculator(r3, false);
        m.calculateAlpha();
        deo = m.getDo();
        assertEquals(deo, 0.074, 0.001);


    }


    @Test
    public void testCalculateAlpha() throws Exception {

        MultiValuedAlphaCalculator m0 = new MultiValuedAlphaCalculator(r0, true);
        m0.calculateAlpha();
        System.out.println(m0.getCalculations());

        double alpha = m0.getAlpha();
        System.out.println("Do: " + m0.getDo());
        assertEquals(alpha, 0.2866, 0.001);

        MultiValuedAlphaCalculator m = new MultiValuedAlphaCalculator(r1, false);
        m.calculateAlpha();
        alpha = m.getAlpha();
        System.out.println("Do: " + m.getDo());

        assertEquals(alpha, 0.947, 0.001);

        m = new MultiValuedAlphaCalculator(r2, false);
        m.calculateAlpha();
        alpha = m.getAlpha();
        System.out.println("Do: " + m.getDo());
        assertEquals(alpha, 0.923, 0.001);

        m = new MultiValuedAlphaCalculator(r3, false);
        m.calculateAlpha();
        alpha = m.getAlpha();
        System.out.println("Do: " + m.getDo());
        assertEquals(alpha, 0.895, 0.001);
    }


}