 package multiValuedNominalAlpha.gui;
 
 import java.awt.Dimension;
 import java.awt.Font;
 import java.awt.GridBagConstraints;
 import java.awt.GridBagLayout;
 import java.awt.Insets;
 import java.awt.event.ActionEvent;
 import java.awt.event.ActionListener;
 import java.io.File;
 import javax.swing.ButtonGroup;
 import javax.swing.JButton;
 import javax.swing.JCheckBox;
 import javax.swing.JComboBox;
 import javax.swing.JDialog;
 import javax.swing.JFileChooser;
 import javax.swing.JFrame;
 import javax.swing.JLabel;
 import javax.swing.JRadioButton;
 import javax.swing.JSeparator;
 import javax.swing.JSpinner;
 import javax.swing.JTextField;
 import javax.swing.SpinnerNumberModel;
 import javax.swing.event.ChangeEvent;
 import javax.swing.event.ChangeListener;
 import multiValuedNominalAlpha.ReliabilityDataFileAndFormat;
 import multiValuedNominalAlpha.mvnaCalculator.model.ReliabilityDataMatrix;
 import multiValuedNominalAlpha.mvnaCalculator.ReliabilityDataFactory;
 
 public class OpenFileDialog extends JDialog
 {
   private JTextField filenameTextField;
   private JButton fileBrowseButton;
   private JButton okButton;
   private JButton cancelButton;
   private JCheckBox logCalculationsBox;
   private JTextField valueOtherField;
   private JTextField unitOtherField;
   private JComboBox valueSeparatorCombo;
   private JComboBox unitSeparatorCombo;
   private JRadioButton rowsForUnitsButton;
   private JRadioButton columnsForUnitsButton;
   private ButtonGroup dataOrientationGroup;
   private JSeparator sep1;
   private JLabel valueSeperatorLabel;
   private JLabel unitSepLabel;
   private JSpinner fromRowSpinner;
   private JSpinner fromColumnSpinner;
   private JLabel fromColumnLabel;
   private JLabel fromRowLabel;
   private String errorMessages;
   private boolean okPressed;
   private ReliabilityDataMatrix dataMatrix;
   private String prevFilepath;
   private final String OTHER_TEXT = "Other";
 
   public OpenFileDialog(JFrame parent)
   {
     super(parent, "Select Data Format", true);
     initGUI();
   }
 
   public void initGUI()
   {
     try
     {
       preInitGUI();
       this.dataOrientationGroup = new ButtonGroup();
 
       this.fromRowLabel = new JLabel();
       this.fromColumnLabel = new JLabel();
       this.fromColumnSpinner = new JSpinner();
       this.fromRowSpinner = new JSpinner();
       this.cancelButton = new JButton();
       this.okButton = new JButton();
       this.unitSepLabel = new JLabel();
       this.valueSeperatorLabel = new JLabel();
       this.sep1 = new JSeparator();
       this.filenameTextField = new JTextField();
       this.fileBrowseButton = new JButton();
       this.columnsForUnitsButton = new JRadioButton();
       this.rowsForUnitsButton = new JRadioButton();
       this.unitSeparatorCombo = new JComboBox();
       this.valueSeparatorCombo = new JComboBox();
       this.unitOtherField = new JTextField();
       this.valueOtherField = new JTextField();
       this.logCalculationsBox = new JCheckBox();
 
       GridBagLayout thisLayout = new GridBagLayout();
       getContentPane().setLayout(thisLayout);
       thisLayout.columnWidths = new int[] { 1, 1, 1, 1, 1 };
       thisLayout.rowHeights = new int[] { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };
       thisLayout.columnWeights = new double[] { 0.1D, 0.1D, 0.1D, 0.1D, 0.1D };
       thisLayout.rowWeights = new double[] { 0.1D, 0.1D, 0.1D, 0.1D, 0.1D, 0.1D, 0.1D, 0.1D, 0.1D, 0.1D, 0.1D, 0.1D };
       setResizable(false);
       setSize(new Dimension(408, 270));
 
       this.fromRowLabel.setText("From row:");
       this.fromRowLabel.setFont(new Font("Dialog", 0, 12));
       getContentPane().add(this.fromRowLabel, new GridBagConstraints(3, 4, 1, 1, 0.0D, 0.0D, 21, 0, new Insets(0, 0, 0, 0), 0, 0));
 
       this.fromColumnLabel.setText("From column:");
       this.fromColumnLabel.setFont(new Font("Dialog", 0, 12));
       getContentPane().add(this.fromColumnLabel, new GridBagConstraints(1, 4, 2, 1, 0.0D, 0.0D, 21, 0, new Insets(0, 0, 0, 0), 0, 0));
 
       getContentPane().add(this.fromColumnSpinner, new GridBagConstraints(2, 4, 1, 1, 0.0D, 0.0D, 21, 0, new Insets(0, 0, 0, 0), 0, 0));
       this.fromColumnSpinner.addChangeListener(new ChangeListener() {
         public void stateChanged(ChangeEvent evt) {
           OpenFileDialog.this.fromColumnSpinnerStateChanged(evt);
         }
       });
       getContentPane().add(this.fromRowSpinner, new GridBagConstraints(4, 4, 1, 1, 0.0D, 0.0D, 21, 0, new Insets(0, 0, 0, 0), 0, 0));
       this.fromRowSpinner.addChangeListener(new ChangeListener() {
         public void stateChanged(ChangeEvent evt) {
           OpenFileDialog.this.fromRowSpinnerStateChanged(evt);
         }
       });
       this.cancelButton.setText("Cancel");
       this.cancelButton.setFont(new Font("Dialog", 0, 12));
       this.cancelButton.setSize(new Dimension(72, 25));
       getContentPane().add(this.cancelButton, new GridBagConstraints(4, 11, 1, 1, 0.0D, 0.0D, 10, 0, new Insets(0, 0, 0, 0), 0, 0));
       this.cancelButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent evt) {
           OpenFileDialog.this.cancelButtonActionPerformed(evt);
         }
       });
       this.okButton.setText("Ok");
       this.okButton.setFont(new Font("Dialog", 0, 12));
       this.okButton.setMinimumSize(new Dimension(81, 26));
       this.okButton.setMaximumSize(new Dimension(81, 26));
       this.okButton.setSize(new Dimension(50, 25));
       getContentPane().add(this.okButton, new GridBagConstraints(3, 11, 1, 1, 0.0D, 0.0D, 10, 0, new Insets(0, 0, 0, 0), 0, 0));
       this.okButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent evt) {
           OpenFileDialog.this.okButtonActionPerformed(evt);
         }
       });
       this.unitSepLabel.setText("Unit Separator:");
       this.unitSepLabel.setFont(new Font("Dialog", 0, 12));
       getContentPane().add(this.unitSepLabel, new GridBagConstraints(1, 6, 2, 1, 0.0D, 0.0D, 21, 0, new Insets(0, 0, 0, 0), 0, 0));
 
       this.valueSeperatorLabel.setText("Value Separator:");
       this.valueSeperatorLabel.setFont(new Font("Dialog", 0, 12));
       getContentPane().add(this.valueSeperatorLabel, new GridBagConstraints(3, 6, 2, 1, 0.0D, 0.0D, 21, 0, new Insets(0, 0, 0, 0), 0, 0));
 
       getContentPane().add(this.sep1, new GridBagConstraints(2, 2, 1, 1, 0.0D, 0.0D, 10, 0, new Insets(0, 0, 0, 0), 0, 0));
 
       getContentPane().add(this.filenameTextField, new GridBagConstraints(1, 0, 3, 1, 0.0D, 0.0D, 16, 2, new Insets(0, 0, 0, 0), 0, 0));
 
       this.fileBrowseButton.setText("Browse");
       this.fileBrowseButton.setSize(new Dimension(80, 25));
       getContentPane().add(this.fileBrowseButton, new GridBagConstraints(4, 0, 1, 1, 0.0D, 0.0D, 15, 0, new Insets(0, 0, 0, 0), 0, 0));
       this.fileBrowseButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent evt) {
           OpenFileDialog.this.fileBrowseButtonActionPerformed(evt);
         }
       });
       this.columnsForUnitsButton.setText("Rows of units with a column for each coder");
       this.columnsForUnitsButton.setFont(new Font("Dialog", 0, 12));
       getContentPane().add(this.columnsForUnitsButton, new GridBagConstraints(1, 2, 4, 1, 0.0D, 0.0D, 21, 0, new Insets(0, 0, 0, 0), 0, 0));
 
       this.rowsForUnitsButton.setText("Rows of coders with a column for each unit");
       this.rowsForUnitsButton.setFont(new Font("Dialog", 0, 12));
       getContentPane().add(this.rowsForUnitsButton, new GridBagConstraints(1, 1, 4, 1, 0.0D, 0.0D, 21, 0, new Insets(0, 0, 0, 0), 0, 0));
 
       this.unitSeparatorCombo.setEditable(false);
       this.unitSeparatorCombo.setFont(new Font("Dialog", 0, 12));
       getContentPane().add(this.unitSeparatorCombo, new GridBagConstraints(1, 7, 1, 1, 0.3D, 0.0D, 21, 0, new Insets(0, 0, 0, 0), 0, 0));
       this.unitSeparatorCombo.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent evt) {
           OpenFileDialog.this.unitSeparatorComboActionPerformed(evt);
         }
       });
       this.valueSeparatorCombo.setEditable(false);
       this.valueSeparatorCombo.setFont(new Font("Dialog", 0, 12));
       getContentPane().add(this.valueSeparatorCombo, new GridBagConstraints(3, 7, 1, 1, 0.3D, 0.0D, 21, 0, new Insets(0, 0, 0, 0), 0, 0));
       this.valueSeparatorCombo.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent evt) {
           OpenFileDialog.this.valueSeparatorComboActionPerformed(evt);
         }
       });
       this.unitOtherField.setColumns(2);
       this.unitOtherField.setEnabled(false);
       getContentPane().add(this.unitOtherField, new GridBagConstraints(2, 7, 1, 1, 0.0D, 0.0D, 21, 0, new Insets(0, 0, 0, 0), 0, 0));
 
       this.valueOtherField.setColumns(2);
       this.valueOtherField.setEnabled(false);
       getContentPane().add(this.valueOtherField, new GridBagConstraints(4, 7, 1, 1, 0.0D, 0.0D, 21, 0, new Insets(0, 0, 0, 0), 0, 0));
 
       this.logCalculationsBox.setText("Log Calculations (Slower)");
       this.logCalculationsBox.setFont(new Font("Dialog", 0, 12));
       getContentPane().add(this.logCalculationsBox, new GridBagConstraints(1, 9, 3, 1, 0.0D, 0.0D, 21, 0, new Insets(0, 0, 0, 0), 0, 0));
 
       postInitGUI();
     } catch (Exception e) {
       e.printStackTrace();
     }
   }
 
   public void preInitGUI() {
     this.okPressed = false;
   }
 
   public void postInitGUI()
   {
     this.unitSeparatorCombo.addItem("Comma");
     this.unitSeparatorCombo.addItem("Tab");
     this.unitSeparatorCombo.addItem("Space");
     this.unitSeparatorCombo.addItem("Other");
 
     this.valueSeparatorCombo.addItem("None");
     this.valueSeparatorCombo.addItem("Pipe");
     this.valueSeparatorCombo.addItem("Comma");
     this.valueSeparatorCombo.addItem("Other");
 
     this.dataOrientationGroup.add(this.columnsForUnitsButton);
     this.dataOrientationGroup.add(this.rowsForUnitsButton);
 
     setDefaultValues();
   }
 
   private void setDefaultValues()
   {
     this.fromColumnSpinner.getModel().setValue(new Integer(1));
     this.fromRowSpinner.getModel().setValue(new Integer(1));
 
     this.unitSeparatorCombo.setSelectedIndex(0);
     this.valueSeparatorCombo.setSelectedIndex(0);
 
     this.rowsForUnitsButton.setSelected(true);
   }
 
   public char getUnitSeperator()
   {
     switch (this.unitSeparatorCombo.getSelectedIndex()) {
     case 0:
       return ',';
     case 1:
       return '\t';
     case 2:
       return ' ';
     }
 
     return this.unitOtherField.getText().charAt(0);
   }
 
   public char getValueSeperator()
   {
     switch (this.valueSeparatorCombo.getSelectedIndex())
     {
     case 0:
       return '\b';
     case 1:
       return '|';
     case 2:
       return ',';
     case 3:
       return this.valueOtherField.getText().charAt(0);
     }
 
     return '-';
   }
 
   public int getFromRow()
   {
     int value = ((SpinnerNumberModel)this.fromRowSpinner.getModel()).getNumber().intValue();
 
     return value;
   }
 
   public int getFromColumn()
   {
     int value = ((SpinnerNumberModel)this.fromColumnSpinner.getModel()).getNumber().intValue();
 
     return value;
   }
 
   private boolean isOptionsValid()
   {
     this.errorMessages = "";
     boolean noErrors = true;
 
     if ((this.unitSeparatorCombo.getSelectedItem() == "Other") && 
       (this.unitOtherField.getText().length() != 1)) {
       this.errorMessages += "\nUnit separator must be a single char.";
       noErrors = false;
     }
     if ((this.valueSeparatorCombo.getSelectedItem() == "Other") && 
       (this.valueOtherField.getText().length() != 1)) {
       this.errorMessages += "\nValue separator must be a single char.";
       noErrors = false;
     }
 
     if (noErrors) {
       char valueSep = getValueSeperator();
       char unitSep = getUnitSeperator();
       if (valueSep == unitSep) {
         this.errorMessages = "\nUnit and Value separator must be different.";
         noErrors = false;
       }
     }
 
     return noErrors;
   }
 
   private boolean isRowsForCoders()
   {
     return !this.rowsForUnitsButton.isSelected();
   }
 
   private void parseData() throws Exception
   {
     this.dataMatrix = ReliabilityDataFactory.loadReliabilityDataFromCSV(this.filenameTextField.getText(), getUnitSeperator(), getValueSeperator(), getFromRow(), getFromColumn(), isRowsForCoders());
   }
 
   protected void okButtonActionPerformed(ActionEvent evt)
   {
     if (!isOptionsValid())
     {
       LoadErrorDialogue led = new LoadErrorDialogue();
       led.setText(this.errorMessages);
       led.setVisible(true);
     }
     else {
       try {
         parseData();
         this.okPressed = true;
         setVisible(false);
       }
       catch (Exception e)
       {
         LoadErrorDialogue led = new LoadErrorDialogue();
         led.setText(e.getMessage());
         led.setVisible(true);
       }
     }
   }
 
   protected void cancelButtonActionPerformed(ActionEvent evt)
   {
     this.okPressed = false;
     setVisible(false);
   }
 
   public boolean isOkPressed()
   {
     return this.okPressed;
   }
 
   protected void fileBrowseButtonActionPerformed(ActionEvent evt)
   {
     JFileChooser fileChooser = new JFileChooser();
     fileChooser.setDialogTitle("Open Reliability Data");
 
     if (this.prevFilepath != "") {
       try {
         File f = new File(this.prevFilepath);
         if (f.exists()) fileChooser.setCurrentDirectory(f);
       }
       catch (Exception e)
       {
       }
     }
     int fileDialogResult = fileChooser.showOpenDialog(this);
 
     if (fileDialogResult == 0)
     {
       String filename = fileChooser.getSelectedFile().getAbsolutePath();
       this.filenameTextField.setText(filename);
     }
   }
 
   public ReliabilityDataMatrix getDataMatrix()
   {
     return this.dataMatrix;
   }
 
   protected void fromColumnSpinnerStateChanged(ChangeEvent evt)
   {
     int value = ((SpinnerNumberModel)this.fromColumnSpinner.getModel()).getNumber().intValue();
 
     if (value < 1)
       ((SpinnerNumberModel)this.fromColumnSpinner.getModel()).setValue(new Integer(1));
   }
 
   protected void fromRowSpinnerStateChanged(ChangeEvent evt)
   {
     int value = ((SpinnerNumberModel)this.fromColumnSpinner.getModel()).getNumber().intValue();
 
     if (value < 1)
       ((SpinnerNumberModel)this.fromColumnSpinner.getModel()).setValue(new Integer(1));
   }
 
   protected ReliabilityDataFileAndFormat getDataFileAndFormat()
   {
     File dataFile = new File(this.filenameTextField.getText());
     String filepath = dataFile.getParent();
     String name = dataFile.getAbsolutePath();
 
     ReliabilityDataFileAndFormat rdff = new ReliabilityDataFileAndFormat(name, filepath, getUnitSeperator(), getValueSeperator(), getFromRow(), getFromColumn(), logCalculations(), isRowsForCoders());
 
     return rdff;
   }
 
   public boolean logCalculations() {
     return this.logCalculationsBox.isSelected();
   }
 
   public void setPreviousPath(String prevPath) {
     this.prevFilepath = prevPath;
   }
 
   protected void unitSeparatorComboActionPerformed(ActionEvent evt)
   {
     this.unitOtherField.setEnabled(this.unitSeparatorCombo.getSelectedItem().equals("Other"));
   }
 
   protected void valueSeparatorComboActionPerformed(ActionEvent evt)
   {
     this.valueOtherField.setEnabled(this.valueSeparatorCombo.getSelectedItem().equals("Other"));
   }
 }

/* Location:           C:\dev\from jar\
 * Qualified Name:     multiValuedNominalAlpha.gui.OpenFileDialog
 * JD-Core Version:    0.6.2
 */