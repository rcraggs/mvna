package multiValuedNominalAlpha;
 
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

 public class ReliabilityDataMatrix
 {
   private Set<String>[][] labels;
   private final String CODER_LABEL = "C";
   private final String UNIT_LABEL = "U";
   
   public ReliabilityDataMatrix(int units, int coders)
   {
     this.labels = new Set[units][coders];
   }
 
   public Set getLabels(int unit, int coder)
   {
     return this.labels[unit][coder];
   }
 
   public Object[][] getLabels()
   {
     return this.labels;
   }
 
   public String[] getCoderHeaders()
   {
     String[] headers = new String[getNumberOfCoders()];
     for (int i = 0; i < headers.length; i++) {
       headers[i] = ("C" + (i + 1));
     }
 
     return headers;
   }
 
   public String[] getUnitHeaders()
   {
     String[] headers = new String[getNumberOfUnits()];
     for (int i = 0; i < headers.length; i++) {
       headers[i] = ("U" + (i + 1));
     }
     return headers;
   }
 
//   public void setLabels(int unit, int coder, MathSet label)
//   {
//     this.labels[unit][coder] = label;
//   }
 
   public void setLabels(int unit, int coder, ArrayList label)
   {
     if (label == null) {
           this.labels[unit][coder] = null;
       }
     else {
           this.labels[unit][coder] = new HashSet(label);
       }
   }
 
   public int getNumberOfUnits() {
     return this.labels.length;
   }
 
   public int getNumberOfCoders() {
     return this.labels[0].length;
   }
 
   public int getNumberOfLabelsForUnit(int unit)
   {
     int count = 0;
     for (int i = 0; i < this.labels[unit].length; i++) {
       if (this.labels[unit][i] != null) {
             count++;
         }
     }
     return count;
   }

   public String toCSV(){

    String result = "";

    for (int i = 0; i < labels.length; i++) {

      result += "\n U" + i + " ";

      for (int j = 0; j < labels[i].length; j++) {
        if (labels[i][j] != null){
          result += labels[i][j];  
        }
        
        if (j < labels[i].length - 1){
          result += ",";
        }
      }
    }

    return result;
   }

 }