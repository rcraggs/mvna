package multiValuedNominalAlpha.model;

import java.util.*;

// TODO: refactor this to be a matrix of multi-labels. Some uses will need to other another array type.
public class LabelledFloatMatrix {
    HashMap xLabelMap = new HashMap();

    public void put(Set<String> x, Set<String> y, double value) {
        HashMap yLabelMap;
        if (!this.xLabelMap.containsKey(x)) {
            yLabelMap = new HashMap();
            this.xLabelMap.put(x, yLabelMap);
        } else {
            yLabelMap = (HashMap) this.xLabelMap.get(x);
        }
        yLabelMap.put(y, new Double(value));
    }

    public void add(Set<String> x, Set<String> y, double value) {
        HashMap yLabelMap;
        if (!this.xLabelMap.containsKey(x)) {
            yLabelMap = new HashMap();
            this.xLabelMap.put(x, yLabelMap);
        } else {
            yLabelMap = (HashMap) this.xLabelMap.get(x);
        }
        Double oldValue;
        if (yLabelMap.containsKey(y)) {
            oldValue = (Double) yLabelMap.get(y);
            yLabelMap.remove(y);
        } else {
            oldValue = new Double(0.0D);
        }
        yLabelMap.put(y, new Double(oldValue.doubleValue() + value));
    }

    public double get(Set<String> x, Set<String> y) {
        return ((Double) ((HashMap) this.xLabelMap.get(x)).get(y)).doubleValue();
    }

    //TODO can we remove this and just use the sorted one?
    public Set getLabelsSet() {
        HashSet hs = new HashSet(this.xLabelMap.keySet());
        return hs;
    }

    private Iterator getOrderedHeaders() {
        ArrayList<Collection> a = new ArrayList<Collection>(this.xLabelMap.keySet());
        Collections.sort(a, new Comparator<Collection>() {
            public int compare(Collection o1, Collection o2) {
                if (o1.size() < o2.size())
                    return -1;
                if (o1.size() == o2.size() && isLowerAlphabetically(o1, o2))
                    return -1;
                else
                    return 1;
            }
        });

        return a.iterator();
    }

    private boolean isLowerAlphabetically(Collection a, Collection b) {
        ArrayList orderedA = orderAlphabetically(a);
        ArrayList orderedB = orderAlphabetically(b);

        for (int i = 0; i < a.size(); i++) {
            String sa = (String) orderedA.get(i);
            String sb = (String) orderedB.get(i);

            if (sa.compareToIgnoreCase(sb) < 0) {
                return true;
            }
            if (sa.compareToIgnoreCase(sb) > 0) {
                return false;
            }
        }
        return false;
    }

    private ArrayList orderAlphabetically(Collection s) {
        ArrayList l = new ArrayList(s);
        Collections.sort(l);
        return l;
    }

    public String[] headerOrderedLabelStrings() {
        String[] result = new String[this.xLabelMap.keySet().size()];
        int index = 0;

        Iterator labels = getOrderedHeaders();
        while (labels.hasNext()) {

            // TODO is the ordering here unnecessary?
            ArrayList a = orderAlphabetically((Collection) labels.next());
            String listString = a.toString();
            result[index] = ("{" + listString.substring(1, listString.length() - 1) + "}");
            index++;
        }
        return result;
    }

    public String[][] getOrderedTableData() {
        String[][] result = new String[this.xLabelMap.size()][this.xLabelMap.size()];

        int indexX = 0;

        Iterator labels = getOrderedHeaders();

        while (labels.hasNext()) {
            Set<String> label = (Set<String>) labels.next();
            Iterator labels2 = getOrderedHeaders();
            int indexY = 0;

            while (labels2.hasNext()) {
                Set<String> label2 = (Set<String>) labels2.next();

                String valueStr = "" + ((HashMap) this.xLabelMap.get(label)).get(label2);

                result[indexX][indexY] = getFormattedValue(valueStr);

                indexY++;
            }
            indexX++;
        }

        return result;
    }

    private String getFormattedValue(String v) {
        if (v.equals("null")) {
            return "0";
        }
        int pointIndex = v.indexOf(46);
        if ((pointIndex > -1) && (v.length() > pointIndex + 3 + 1)) {
            return "" + Double.parseDouble(v.substring(0, pointIndex + 3 + 1));
        }
        return v;
    }

    public String getCSVText() {
        String newline = System.getProperty("line.separator");
        String results = "";

        Iterator headerIterator = getOrderedHeaders();
        String colHeaders = "";

        colHeaders = colHeaders + ",";

        while (headerIterator.hasNext()) {
            colHeaders = colHeaders + headerIterator.next().toString().replace(',', '|') + ",";
        }

        colHeaders = colHeaders.substring(0, colHeaders.length());
        results = results + colHeaders;
        results = results + newline;

        Iterator rowHeaders = getOrderedHeaders();
        String[][] data = getOrderedTableData();
        for (int x = 0; x < data.length; x++) {
            String rowString = rowHeaders.next().toString().replace(',', '|') + ",";

            for (int y = 0; y < data[x].length; y++) {
                rowString = rowString + data[x][y];
                if (y < data[x].length + 1) {
                    rowString = rowString + ",";
                }
            }
            results = results + rowString;
            results = results + newline;
        }
        return results;
    }
}