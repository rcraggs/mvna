 package multiValuedNominalAlpha;
 
 import java.io.BufferedReader;
 import java.io.BufferedWriter;
 import java.io.FileReader;
 import java.io.FileWriter;
 import java.io.IOException;
 import java.io.PrintWriter;
 
 public class ReliabilityDataFileAndFormat
 {
   String filename;
   String filepath;
   boolean logCalculations;
   int fromRow;
   int fromCol;
   char valueSeperator;
   char unitSeperator;
   boolean rowForCoders;
 
   public ReliabilityDataFileAndFormat(String path) throws IOException
   {
     BufferedReader br = new BufferedReader(new FileReader(path));
     try
     {
       this.filename = br.readLine();
 
       this.filepath = br.readLine();
 
       this.fromRow = Integer.parseInt(br.readLine());
       this.fromCol = Integer.parseInt(br.readLine());
       this.valueSeperator = br.readLine().charAt(0);
       this.unitSeperator = br.readLine().charAt(0);
       this.logCalculations = br.readLine().trim().toLowerCase().equals("true");
       this.rowForCoders = br.readLine().trim().toLowerCase().equals("true");
     }
     catch (Exception e)
     {
     }
   }
 
   public ReliabilityDataFileAndFormat(String name, String path, char unitSep, char valSep, int row, int col, boolean log, boolean rowCoders)
   {
     this.filename = name;
     this.filepath = path;
     this.fromRow = row;
     this.fromCol = col;
     this.valueSeperator = valSep;
     this.unitSeperator = unitSep;
     this.logCalculations = log;
     this.rowForCoders = rowCoders;
   }
 
   void setDefaultValues()
   {
     this.valueSeperator = ',';
     this.unitSeperator = '|';
     this.logCalculations = false;
     this.rowForCoders = true;
   }
 
   public void writeFormatFile(String f)
     throws IOException
   {
     PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(f)), true);
 
     out.println(this.filename);
     out.println(this.filepath);
     out.println(this.fromRow);
     out.println(this.fromCol);
     out.println(this.valueSeperator);
     out.println(this.unitSeperator);
     out.println(this.logCalculations);
     out.println(this.rowForCoders);
 
     out.flush();
     out.close();
   }
 
   public String getFilename()
   {
     return this.filename;
   }
 
   public String getFilepath()
   {
     return this.filepath;
   }
 
   public int getFromCol()
   {
     return this.fromCol;
   }
 
   public int getFromRow()
   {
     return this.fromRow;
   }
 
   public boolean isLogCalculations()
   {
     return this.logCalculations;
   }
 
   public boolean isRowForCoders()
   {
     return this.rowForCoders;
   }
 
   public char getUnitSeperator()
   {
     return this.unitSeperator;
   }
 
   public char getValueSeperator()
   {
     return this.valueSeperator;
   }
 
   public String toString()
   {
     String result = "";
     result = result + "FILENAME " + this.filename + "\n";
     result = result + "FILEPATH " + this.filepath + "\n";
     result = result + "FIRST_ROW " + this.fromRow + "\n";
     result = result + "FIRST_COL " + this.fromCol + "\n";
     result = result + "VALUE_SEP " + this.valueSeperator + "\n";
     result = result + "UNIT_SEP " + this.unitSeperator + "\n";
     result = result + "LOG_CALCULATIONS " + this.logCalculations + "\n";
     result = result + "ROW_FOR_CODERS " + this.rowForCoders + "\n";
 
     return result;
   }
 }
