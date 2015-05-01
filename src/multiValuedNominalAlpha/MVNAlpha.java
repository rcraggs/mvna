 package multiValuedNominalAlpha;
 
/*import gnu.getopt.Getopt;
import gnu.getopt.LongOpt;
*/
import multiValuedNominalAlpha.reliabilityDataFactory.ReliabilityDataFactory;
 
 public class MVNAlpha
 {
   public static void main(String[] args)
   {
       /*
     boolean noErrors = true;
 
     char unitSeperator = ',';
     char valueSeperator = '|';
     boolean rowsForUnits = false;
     int fromRow = 1;
     int fromCol = 1;
     boolean showCalcText = false;
     boolean showData = false;
 
     String unitSepText = ""; String valueSepText = ""; String fromRowText = ""; String fromColText = ""; String filename = "";
 
     LongOpt[] longOpts = new LongOpt[8];
     LongOpt helpOpt = new LongOpt("Help", 0, null, 104);
     LongOpt unitSepOpt = new LongOpt("unit-sep", 1, null, 117);
 
     LongOpt valueSepOpt = new LongOpt("value-sep", 1, null, 118);
 
     LongOpt rowForUnit = new LongOpt("rows-for-units", 0, null, 116);
 
     LongOpt logCalcOpt = new LongOpt("print-calc", 0, null, 112);
 
     LongOpt fromRowOpt = new LongOpt("from-row", 1, null, 114);
 
     LongOpt fromColOpt = new LongOpt("from-col", 1, null, 99);
 
     LongOpt showDataOpt = new LongOpt("show-data", 0, null, 115);
 
     Getopt g = new Getopt("MVNAlpha", args, ":hu:v:tpsr:c:", longOpts);
     g.setOpterr(false);
     int cInt;
     while ((cInt = g.getopt()) != -1)
     {
       char c = (char)cInt;
 
       switch (cInt) {
       case 117:
         unitSepText = g.getOptarg();
         break;
       case 118:
         valueSepText = g.getOptarg();
         break;
       case 116:
         rowsForUnits = true;
         break;
       case 112:
         showCalcText = true;
         break;
       case 114:
         fromRowText = g.getOptarg();
         break;
       case 115:
         showData = true;
         break;
       case 99:
         fromColText = g.getOptarg();
         break;
       case 63:
         System.err.println("Error: " + (char)g.getOptopt() + " is an invalid option.");
 
         noErrors = false;
         break;
       case 58:
         System.err.println("Error: " + (char)g.getOptopt() + " requires an argument.");
 
         noErrors = false;
       }
 
     }
 
     if (args.length - g.getOptind() != 1) {
       System.err.println("Error: You must specify a single filename.");
       noErrors = false;
     } else {
       filename = args[g.getOptind()];
     }
 
     if (!isOptionsValid(unitSepText, valueSepText, fromRowText, fromColText)) {
       noErrors = false;
     }
 
     if (noErrors) {
       if (!unitSepText.equals("")) unitSeperator = unitSepText.charAt(0);
       if (!valueSepText.equals(""))
         valueSeperator = valueSepText.charAt(0);
       if (!fromRowText.equals(""))
         fromRow = Integer.parseInt(fromRowText);
       if (!fromColText.equals("")) {
         fromCol = Integer.parseInt(fromColText);
       }
 
       ReliabilityDataMatrix rdm = null;
       try
       {
         rdm = ReliabilityDataFactory.loadReliabilityDataFromCSV(filename, unitSeperator, valueSeperator, fromRow, fromCol, rowsForUnits);
 
         MultiValuedAlphaCalculator alpha = new MultiValuedAlphaCalculator(rdm, showCalcText);
 
         alpha.calculateAlpha();
 
         if (showData) {
           System.out.println(rdm + "\n");
           System.out.println(alpha.getValues());
         }
 
         if (showCalcText) {
           System.out.println(alpha.getCalculations() + "\n");
         }
         System.out.println("Alpha = " + alpha.getAlpha());
       }
       catch (Exception e) {
         System.err.println("Error: Failed to parse reliability data file\nthe following error message was returned:\n\n" + e.getMessage());
       }
 
     }
 
     System.exit(0);
   }
 
   public static boolean isOptionsValid(String unitSep, String valueSep, String fromRow, String fromCol)
   {
     boolean noErrors = true;
 
     if ((!unitSep.equals("")) && (unitSep.length() > 1)) {
       System.err.println("Error: Unit seperator must be a single character.");
 
       noErrors = false;
     }
     if ((!valueSep.equals("")) && (valueSep.length() > 1)) {
       System.err.println("Error: Value seperator must be a single character.");
 
       noErrors = false;
     }
     if ((!unitSep.equals("")) && (!valueSep.equals("")) && (valueSep.equals(unitSep)))
     {
       System.err.println("Error: Unit and value seperators must be different.");
 
       noErrors = false;
     }
 
     if (!fromRow.equals("")) {
       try {
         int rowInt = Integer.parseInt(fromRow);
         if (rowInt < 1) {
           System.err.println("Error: From row argument must be greater than -1.");
 
           noErrors = false;
         }
       } catch (NumberFormatException e) {
         System.err.println("Error: From row argument is not a valid integer.");
 
         noErrors = false;
       }
     }
     if (!fromCol.equals("")) {
       try {
         int colInt = Integer.parseInt(fromCol);
         if (colInt < 1) {
           System.err.println("Error: From column argument must be greater than -1.");
 
           noErrors = false;
         }
       } catch (NumberFormatException e) {
         System.err.println("Error: From column argument is not a valid integer.");
 
         noErrors = false;
       }
     }
     return noErrors;
     */
   }
 }