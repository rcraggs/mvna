 package multiValuedNominalAlpha;
 
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
 
 public class LabelledFloatMatrix
 {
   HashMap xLabelMap;
 
   public LabelledFloatMatrix()
   {
     this.xLabelMap = new HashMap();
   }
 
   public void put(Object x, Object y, double value)
   {
     HashMap yLabelMap;
     if (!this.xLabelMap.containsKey(x)) {
       yLabelMap = new HashMap();
       this.xLabelMap.put(x, yLabelMap);
     } else {
       yLabelMap = (HashMap)this.xLabelMap.get(x);
     }
     yLabelMap.put(y, new Double(value));
   }
 
   public void add(Object x, Object y, double value)
   {
     HashMap yLabelMap;
     if (!this.xLabelMap.containsKey(x)) {
       yLabelMap = new HashMap();
       this.xLabelMap.put(x, yLabelMap);
     } else {
       yLabelMap = (HashMap)this.xLabelMap.get(x);
     }
     Double oldValue;
     if (yLabelMap.containsKey(y)) {
       oldValue = (Double)yLabelMap.get(y);
       yLabelMap.remove(y);
     } else {
       oldValue = new Double(0.0D);
     }
     yLabelMap.put(y, new Double(oldValue.doubleValue() + value));
   }
 
   public double get(Object x, Object y)
   {
     return ((Double)((HashMap)this.xLabelMap.get(x)).get(y)).doubleValue();
   }
 
   public Iterator getLabels()
   {
     return this.xLabelMap.keySet().iterator();
   }
 
   public Set getLabelsSet()
   {
     HashSet hs = new HashSet(this.xLabelMap.keySet());
     return hs;
   }
 
   private Iterator getOrderedHeaders()
   {
     Set allLabels = new HashSet(this.xLabelMap.keySet());
     ArrayList orderedLabels = new ArrayList(allLabels.size());
 
     while (!allLabels.isEmpty())
     {
       Collection currentBestLabel = null;
       Object[] labelArray = allLabels.toArray();
 
       for (int i = 0; i < labelArray.length; i++)
       {
         Collection nextLabel = (Collection)labelArray[i];
         if (currentBestLabel == null) {
           currentBestLabel = nextLabel;
         }
         else if (nextLabel.size() < currentBestLabel.size()) {
           currentBestLabel = nextLabel;
         }
         else if ((nextLabel.size() == currentBestLabel.size()) && (isLowerAlphabetically(nextLabel, currentBestLabel)))
         {
           currentBestLabel = nextLabel;
         }
       }
       orderedLabels.add(currentBestLabel);
       allLabels.remove(currentBestLabel);
     }
 
     return orderedLabels.iterator();
   }
 
   private boolean isLowerAlphabetically(Collection a, Collection b)
   {
     ArrayList orderedA = orderAlphabetically(a);
     ArrayList orderedB = orderAlphabetically(b);
 
     for (int i = 0; i < a.size(); i++)
     {
       String sa = (String)orderedA.get(i);
       String sb = (String)orderedB.get(i);
 
       if (sa.compareToIgnoreCase(sb) < 0){
         return true;
                }
       if (sa.compareToIgnoreCase(sb) > 0) {
         return false;
       }
     }
     return false;
   }
 
   private ArrayList orderAlphabetically(Collection s)
   {
     Set c = new HashSet(s);
 
     ArrayList result = new ArrayList(c.size());
     while (!c.isEmpty())
     {
       Object[] strings = c.toArray();
       String nextBest = (String)strings[0];
       for (int i = 1; i < strings.length; i++)
       {
         String next = (String)strings[i];
         if (next.compareToIgnoreCase(nextBest) < 0) {
           nextBest = next;
         }
       }
       result.add(nextBest);
       c.remove(nextBest);
     }
 
     return result;
   }
 
   public String[] headerOrderedLabelStrings()
   {
     String[] result = new String[this.xLabelMap.keySet().size()];
     int index = 0;
 
     Iterator labels = getOrderedHeaders();
     while (labels.hasNext()) {
       String listString = orderAlphabetically((Collection)labels.next()).toString();
       result[index] = ("{" + listString.substring(1, listString.length() - 1) + "}");
       index++;
     }
     return result;
   }
 
   public String[][] getOrderedTableData()
   {
     String[][] result = new String[this.xLabelMap.size()][this.xLabelMap.size()];
 
     int indexX = 0;
 
     Iterator labels = getOrderedHeaders();
 
     while (labels.hasNext())
     {
       Object label = labels.next();
       Iterator labels2 = getOrderedHeaders();
       int indexY = 0;
 
       while (labels2.hasNext())
       {
         Object label2 = labels2.next();
 
         String valueStr = "" + ((HashMap)this.xLabelMap.get(label)).get(label2);
 
         result[indexX][indexY] = getFormattedValue(valueStr);
 
         indexY++;
       }
       indexX++;
     }
 
     return result;
   }
 
   private String getFormattedValue(String v)
   {
     if (v.equals("null")) {
       return "0";
     }
     int pointIndex = v.indexOf(46);
     if ((pointIndex > -1) && (v.length() > pointIndex + 3 + 1))
     {
       return "" + Double.parseDouble(v.substring(0, pointIndex + 3 + 1));
     }
     return v;
   }
 
   public String getCSVText()
   {
     String newline = System.getProperty("line.separator");
     String results = "";
 
     Iterator headerIterator = getOrderedHeaders();
     String colHeaders = "";
 
     colHeaders = colHeaders + ",";
 
     while (headerIterator.hasNext())
     {
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