package multiValuedNominalAlpha.mvnaCalculator.model;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;

import static org.testng.Assert.*;

public class LabelledFloatMatrixTest {

    LabelledFloatMatrix m;

    Labels l;
    Labels l2;
    Labels l3;
//    Labels l4;

    @BeforeMethod
    public void setUp() throws Exception {
        this.m = new LabelledFloatMatrix();

        l = new Labels(new String[]{"apple"});
        l2 = new Labels(new String[]{"Jane"});
        l3 = new Labels(new String[]{"Ashby"});
    }

    @Test
    public void testPut() throws Exception {
        m.put(l, l2, 0d);
        assertEquals(m.get(l, l2), 0d);

        m.put(l, l2, 0d);
        assertEquals(m.get(l, l2), 0d);

        m.put(l2, l, 1d);
        assertEquals(m.get(l2, l), 1d);
        assertEquals(m.get(l, l2), 0d);
    }

    @Test
    public void testAdd() throws Exception {

        // Add something that isn't there and check it is
        Double d = 1d;
        m.add(l, l2, d);

        assertEquals(m.get(l, l2), d);

        // Add Something that's already there
        m.add(l, l2, d);
        assertEquals(m.get(l, l2), 2 * d);

        m.add(l, l2, 0d);
        assertEquals(m.get(l, l2), 2 * d);

        // Check that adding something to a matching single label works
        m.add(l, l3, d);
        m.add(l3, l2, d);
        assertEquals(m.get(l, l2), 2 * d);
        assertEquals(m.get(l3, l2), d);
        assertEquals(m.get(l, l3), d);
    }

    @Test
    public void testGetLabelsSet() throws Exception {

        m.add(l, l3, 1);
        assertTrue(m.getLabelsSet().contains(l));
        assertFalse(m.getLabelsSet().contains(l3));

        m.add(l3, l2, 2);
        assertTrue(m.getLabelsSet().contains(l));
        assertTrue(m.getLabelsSet().contains(l3));

    }

    @Test
    public void testHeaderOrderedLabelStrings() throws Exception {

        Labels h1 = new Labels(new String[]{"b", "bb", "ba"});
        Labels h2 = new Labels(new String[]{"a", "ab", "aa"});
        Labels h3 = new Labels(new String[]{"c"});


        m.put(h1, h2, 0d);
        m.put(h2, h1, 0d);
        m.put(h3, h1, 0d);
        m.put(new Labels(new String [0]), h1, 0d);


        ArrayList al = new ArrayList();
        al.add(h1);
        al.add(h2);
        al.add(h3);

        assertEquals(m.headerOrderedLabelStrings(), new String[]{"{}", "{c}", "{a, aa, ab}", "{b, ba, bb}"});
    }

    @Test
    public void testGetOrderedTableData() throws Exception {

        Labels h1 = new Labels(new String[]{"b", "bb", "ba"});
        Labels h2 = new Labels(new String[]{"a", "ab", "aa"});

        m.put(h1, h2, 1d);
        m.put(h2, h1, 2d);

        ArrayList al = new ArrayList();
        al.add(h1);
        al.add(h2);

        String result[][] = new String[2][2];
        result[0] = new String[]{"0", "2.0"};
        result[1] = new String[]{"1.0", "0"};

        String[][] expected = m.getOrderedTableData();

        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[i].length; j++) {
                assertEquals(result[i][j], expected[i][j]);
            }
        }
    }
}