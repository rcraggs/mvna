package multiValuedNominalAlpha.model;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.SortedSet;
import java.util.TreeSet;

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
        m.put(l.toSet(), l2.toSet(), 0d);
        assertEquals(m.get(l.toSet(), l2.toSet()), 0d);

        m.put(l.toSet(), l2.toSet(), 0d);
        assertEquals(m.get(l.toSet(), l2.toSet()), 0d);

        m.put(l2.toSet(), l.toSet(), 1d);
        assertEquals(m.get(l2.toSet(), l.toSet()), 1d);
        assertEquals(m.get(l.toSet(), l2.toSet()), 0d);
    }

    @Test
    public void testAdd() throws Exception {

        // Add something that isn't there and check it is
        Double d = 1d;
        m.add(l.toSet(), l2.toSet(), d);

        assertEquals(m.get(l.toSet(), l2.toSet()), d);

        // Add Something that's already there
        m.add(l.toSet(), l2.toSet(), d);
        assertEquals(m.get(l.toSet(), l2.toSet()), 2 * d);

        m.add(l.toSet(), l2.toSet(), 0d);
        assertEquals(m.get(l.toSet(), l2.toSet()), 2 * d);

        // Check that adding something to a matching single label works
        m.add(l.toSet(), l3.toSet(), d);
        m.add(l3.toSet(), l2.toSet(), d);
        assertEquals(m.get(l.toSet(), l2.toSet()), 2 * d);
        assertEquals(m.get(l3.toSet(), l2.toSet()), d);
        assertEquals(m.get(l.toSet(), l3.toSet()), d);
    }

    @Test
    public void testGetLabelsSet() throws Exception {

        m.add(l.toSet(), l3.toSet(), 1);
        assertTrue(m.getLabelsSet().contains(l.toSet()));
        assertFalse(m.getLabelsSet().contains(l3.toSet()));

        m.add(l3.toSet(), l2.toSet(), 2);
        assertTrue(m.getLabelsSet().contains(l.toSet()));
        assertTrue(m.getLabelsSet().contains(l3.toSet()));

    }

    @Test
    public void testHeaderOrderedLabelStrings() throws Exception {

        SortedSet<String> h1 = new TreeSet<String>();
        h1.add("b");
        h1.add("bb");
        h1.add("ba");

        SortedSet<String> h2 = new TreeSet<String>();
        h2.add("a");
        h2.add("ab");
        h2.add("aa");

        SortedSet<String> h3 = new TreeSet<String>();
        h3.add("c");

        SortedSet<String> h4 = new TreeSet<String>();


        m.put(h1, h2, 0d);
        m.put(h2, h1, 0d);
        m.put(h3, h1, 0d);
        m.put(h4, h1, 0d);


        ArrayList al = new ArrayList();
        al.add(h1);
        al.add(h2);
        al.add(h3);

        assertEquals(m.headerOrderedLabelStrings(), new String[]{"{}", "{c}", "{a, aa, ab}", "{b, ba, bb}"});
    }

    @Test
    public void testGetOrderedTableData() throws Exception {
        SortedSet<String> h1 = new TreeSet<String>();
        h1.add("b");
        h1.add("bb");
        h1.add("ba");

        SortedSet<String> h2 = new TreeSet<String>();
        h2.add("a");
        h2.add("ab");
        h2.add("aa");

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