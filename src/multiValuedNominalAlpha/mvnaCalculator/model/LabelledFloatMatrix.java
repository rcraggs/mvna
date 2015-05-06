package multiValuedNominalAlpha.mvnaCalculator.model;

import java.util.*;

// TODO: refactor this to be a matrix of multi-labels. Some uses will need to other another array type.
public class LabelledFloatMatrix {

    HashMap xLabelMap = new HashMap();

    public void put(Labels x, Labels y, double value) {
        HashMap yLabelMap;
        if (!this.xLabelMap.containsKey(x)) {
            yLabelMap = new HashMap();
            this.xLabelMap.put(x, yLabelMap);
        } else {
            yLabelMap = (HashMap) this.xLabelMap.get(x);
        }
        yLabelMap.put(y, value);
    }

    public void add(Labels x, Labels y, double value) {
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
            oldValue = 0.0D;
        }
        yLabelMap.put(y, oldValue + value);
    }

    public double get(Labels x, Labels y) {
        return (Double) ((HashMap) this.xLabelMap.get(x)).get(y);
    }

    //TODO can we remove this and just use the sorted one?
    public Set<Labels> getLabelsSet() {
        return new HashSet(this.xLabelMap.keySet());
    }

    private Iterator getOrderedHeaders() {
        ArrayList a = new ArrayList<>(this.xLabelMap.keySet());
        Collections.sort(a);
        return a.iterator();
    }

    public String[] headerOrderedLabelStrings() {
        String[] result = new String[this.xLabelMap.keySet().size()];
        int index = 0;

        Iterator labels = getOrderedHeaders();
        while (labels.hasNext()) {
            // TODO is the ordering here unnecessary?
            //ArrayList a = orderAlphabetically((Collection) labels.next());
            String listString = labels.next().toString();
            result[index] = ("{" + listString.substring(1, listString.length() - 1) + "}");
            index++;
        }
        return result;
    }

    public String[][] getOrderedTableData() {
        String[][] result = new String[this.xLabelMap.size()][this.xLabelMap.size()];

        int indexX = 0;

        // todo can we not deal with iterators here - justa  list?
        Iterator labels = getOrderedHeaders();

        while (labels.hasNext()) {
            Labels label = (Labels) labels.next();
            Iterator labels2 = getOrderedHeaders();
            int indexY = 0;

            while (labels2.hasNext()) {
                Labels label2 = (Labels) labels2.next();
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
        for (String[] aData : data) {
            String rowString = rowHeaders.next().toString().replace(',', '|') + ",";

            for (int y = 0; y < aData.length; y++) {
                rowString = rowString + aData[y];
                if (y < aData.length + 1) {
                    rowString = rowString + ",";
                }
            }
            results = results + rowString;
            results = results + newline;
        }
        return results;
    }
}