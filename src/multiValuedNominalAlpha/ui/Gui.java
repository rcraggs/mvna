package multiValuedNominalAlpha.ui;

import multiValuedNominalAlpha.mvnaCalculator.MultiValuedAlphaCalculator;
import multiValuedNominalAlpha.mvnaCalculator.ReliabilityDataFactory;
import multiValuedNominalAlpha.mvnaCalculator.model.ReliabilityDataMatrix;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Gui extends JFrame {

    private JMenuItem calcAlphaMenuFunction;
    private JMenuItem viewDataStatsMenuItem;
    private JMenuItem genLog;
    private JMenu saveSuperMenu;
    private JButton expButton;
    private JButton obsButton;
    private JTextPane messageTextArea;
    private JSplitPane drmPanelSplitPane;
    private JCheckBox logCalculations;
    private JComboBox<String> orientCombo;
    private JTextField valText;
    private JComboBox<String> valCombo;
    private JTextField unitText;
    private JComboBox<String> unitCombo;
    private JSpinner colSpinner;
    private JSpinner rowSpinner;
    private JLabel resultLabel;
    private JPanel toolPanel;
    private JTable rdmTable;
    private JScrollPane dataScrollPane;
    private JMenuItem openLast;
    private JTable observedDeltaTable;
    private JScrollPane observedDeltaMatrix;
    private JTable coinMatrixTable;
    private JTextPane calculationsText;
    private JTextPane resultsText;
    private JScrollPane coinMatrixScroll;
    private JTabbedPane tabbedPane;
    private UISettings lastDataFormat;
    private int messageCount = 0;
    private String fileBasename;

    private final String ALPHA_FORMAT_STRING = "#.###";
    private boolean lastChangeWasValid;
    private UISettings lastValidFileFormat;
    String errorMessages;
    private ReliabilityDataMatrix rdm;
    private MultiValuedAlphaCalculator mvnaCalculator;
    private File rdmFile;
    private long calculationDuration;

    private static final String VERSION = "v1.0";
    public static final String APP_TITLE = "Multiple Valued Nominal Alpha " + Gui.VERSION;

    public Gui() {

        initGUI();
    }

    public void initGUI() {
        try {
            JToolBar toolbar = new JToolBar();
            JButton openFile = new JButton();
            this.obsButton = new JButton();
            this.expButton = new JButton();
            this.tabbedPane = new JTabbedPane();
            JPanel rdmPanel = new JPanel();
            this.toolPanel = new JPanel();
            JLabel fromLabel = new JLabel();
            JLabel colLabel = new JLabel();
            JLabel unitLabel = new JLabel();
            JLabel valLabel = new JLabel();
            JLabel orientLabel = new JLabel();
            this.rowSpinner = new JSpinner();
            this.colSpinner = new JSpinner();
            this.unitCombo = new JComboBox<>();
            this.unitText = new JTextField();
            this.valCombo = new JComboBox<>();
            this.valText = new JTextField();
            this.orientCombo = new JComboBox<>();
            this.logCalculations = new JCheckBox();
            JTextArea messageArea = new JTextArea();
            this.drmPanelSplitPane = new JSplitPane();
            this.dataScrollPane = new JScrollPane();
            this.rdmTable = new JTable();
            JScrollPane messageScrollPane = new JScrollPane();
            this.messageTextArea = new JTextPane();
            this.coinMatrixScroll = new JScrollPane();
            this.coinMatrixTable = new JTable();
            this.observedDeltaMatrix = new JScrollPane();
            this.observedDeltaTable = new JTable();
            JScrollPane resultsScroll = new JScrollPane();
            this.resultsText = new JTextPane();
            JScrollPane calculationsScroll = new JScrollPane();
            this.calculationsText = new JTextPane();

            BorderLayout thisLayout = new BorderLayout();
            getContentPane().setLayout(thisLayout);
            thisLayout.setHgap(0);
            thisLayout.setVgap(0);
            setSize(new Dimension(727, 445));

            getContentPane().add(toolbar, "North");

            openFile.setText("Open");
            openFile.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/Open16.gif")));
            toolbar.add(openFile);
            openFile.addActionListener(Gui.this::openFileActionPerformed);
            this.expButton.setText("Calc. Alpha");
            this.expButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/FastForward16.gif")));
            toolbar.add(this.expButton);
            this.expButton.addActionListener(Gui.this::expButtonActionPerformed);
            this.resultLabel = new JLabel("");
            toolbar.add(resultLabel);

            this.logCalculations.setText("Log Calculations (slower)");
            this.logCalculations.setFont(new Font("Dialog", 0, 12));
            toolbar.add(this.logCalculations);

            this.tabbedPane.setTabPlacement(3);
            this.tabbedPane.setEnabled(true);
            this.tabbedPane.setFont(new Font("Dialog", 0, 12));
            this.tabbedPane.setPreferredSize(new Dimension(674, 379));
            getContentPane().add(this.tabbedPane, "Center");

            BorderLayout rdmPanelLayout = new BorderLayout();
            rdmPanel.setLayout(rdmPanelLayout);
            rdmPanelLayout.setHgap(0);
            rdmPanelLayout.setVgap(0);
            rdmPanel.setVisible(false);
            rdmPanel.setPreferredSize(new Dimension(597, 352));
            this.tabbedPane.add(rdmPanel);
            this.tabbedPane.setTitleAt(0, "Reliability Data");

            GridBagLayout toolPanelLayout = new GridBagLayout();
            this.toolPanel.setLayout(toolPanelLayout);
            toolPanelLayout.columnWidths = new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1};
            toolPanelLayout.rowHeights = new int[]{1, 1, 1};
            toolPanelLayout.columnWeights = new double[]{0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.5D};
            toolPanelLayout.rowWeights = new double[]{0.0D, 0.0D, 0.1D};
            this.toolPanel.setVisible(true);
            rdmPanel.add(this.toolPanel, "North");

            fromLabel.setText("Start Row");
            fromLabel.setFont(new Font("Dialog", 0, 12));
            this.toolPanel.add(fromLabel, new GridBagConstraints(0, 0, 1, 1, 0.0D, 0.0D, 21, 0, new Insets(0, 10, 0, 0), 0, 0));

            colLabel.setText("Start Column");
            colLabel.setFont(new Font("Dialog", 0, 12));
            this.toolPanel.add(colLabel, new GridBagConstraints(1, 0, 1, 1, 0.0D, 0.0D, 21, 0, new Insets(0, 10, 0, 0), 0, 0));

            unitLabel.setText("Unit Separator");
            unitLabel.setFont(new Font("Dialog", 0, 12));
            this.toolPanel.add(unitLabel, new GridBagConstraints(2, 0, 3, 1, 0.0D, 0.0D, 21, 0, new Insets(0, 10, 0, 0), 0, 0));

            valLabel.setText("Value Separator");
            valLabel.setFont(new Font("Dialog", 0, 12));
            this.toolPanel.add(valLabel, new GridBagConstraints(4, 0, 3, 1, 0.0D, 0.0D, 21, 0, new Insets(0, 10, 0, 0), 0, 0));

            orientLabel.setText("Data Orientation");
            orientLabel.setFont(new Font("Dialog", 0, 12));
            this.toolPanel.add(orientLabel, new GridBagConstraints(6, 0, 1, 1, 0.0D, 0.0D, 21, 0, new Insets(0, 10, 0, 0), 0, 0));

            this.toolPanel.add(this.rowSpinner, new GridBagConstraints(0, 1, 1, 1, 0.0D, 0.0D, 21, 0, new Insets(0, 10, 0, 0), 0, 0));
            this.rowSpinner.addChangeListener(Gui.this::rowSpinnerStateChanged);
            this.toolPanel.add(this.colSpinner, new GridBagConstraints(1, 1, 1, 1, 0.0D, 0.0D, 21, 0, new Insets(0, 10, 0, 0), 0, 0));
            this.colSpinner.addChangeListener(Gui.this::colSpinnerStateChanged);
            this.toolPanel.add(this.unitCombo, new GridBagConstraints(2, 1, 1, 1, 0.0D, 0.0D, 10, 2, new Insets(0, 10, 0, 10), 0, 0));
            this.unitCombo.addActionListener(Gui.this::unitComboActionPerformed);
            this.unitText.setPreferredSize(new Dimension(26, 19));
            this.unitText.setMinimumSize(new Dimension(26, 19));
            this.unitText.setSize(new Dimension(26, 19));
            this.toolPanel.add(this.unitText, new GridBagConstraints(3, 1, 1, 1, 0.0D, 0.0D, 21, 0, new Insets(0, 0, 0, 0), 0, 0));
            this.unitText.addKeyListener(new KeyAdapter() {
                public void keyReleased(KeyEvent evt) {
                    Gui.this.unitTextKeyReleased(evt);
                }
            });
            this.toolPanel.add(this.valCombo, new GridBagConstraints(4, 1, 1, 1, 0.0D, 0.0D, 21, 2, new Insets(0, 10, 0, 10), 0, 0));
            this.valCombo.addActionListener(Gui.this::valComboActionPerformed);
            this.valText.setPreferredSize(new Dimension(26, 19));
            this.valText.setMinimumSize(new Dimension(26, 19));
            this.valText.setSize(new Dimension(26, 19));
            this.toolPanel.add(this.valText, new GridBagConstraints(5, 1, 1, 1, 0.0D, 0.0D, 21, 0, new Insets(0, 0, 0, 0), 0, 0));
            this.valText.addKeyListener(new KeyAdapter() {
                public void keyReleased(KeyEvent evt) {
                    Gui.this.valTextKeyReleased(evt);
                }
            });
            this.toolPanel.add(this.orientCombo, new GridBagConstraints(6, 1, 1, 1, 0.0D, 0.0D, 21, 2, new Insets(0, 10, 0, 0), 0, 0));
            this.orientCombo.addActionListener(Gui.this::orientComboActionPerformed);

            messageArea.setEnabled(false);
            this.toolPanel.add(messageArea, new GridBagConstraints(-1, -1, 1, 1, 0.0D, 0.0D, 10, 0, new Insets(0, 0, 0, 0), 0, 0));

            this.drmPanelSplitPane.setOrientation(0);
            this.drmPanelSplitPane.setDividerLocation(200);
            this.drmPanelSplitPane.setDividerSize(6);
            this.drmPanelSplitPane.setEnabled(true);
            rdmPanel.add(this.drmPanelSplitPane, "Center");

            this.dataScrollPane.setVerticalScrollBarPolicy(20);
            this.dataScrollPane.setVisible(true);
            this.dataScrollPane.setEnabled(false);
            this.dataScrollPane.setPreferredSize(new Dimension(585, 296));
            this.dataScrollPane.setVerifyInputWhenFocusTarget(false);
            this.drmPanelSplitPane.add(this.dataScrollPane, "top");

            this.rdmTable.setEnabled(false);
            this.dataScrollPane.add(this.rdmTable);
            this.dataScrollPane.setViewportView(this.rdmTable);

            Object[][] data = {{"0", "1"}, {"2", "3"}};

            Object[] columns = {"One", "Two"};
            TableModel dataModel = new DefaultTableModel(data, columns);
            this.rdmTable.setModel(dataModel);

            this.drmPanelSplitPane.add(messageScrollPane, "bottom");

            this.messageTextArea.setEditable(false);
            this.messageTextArea.setEnabled(true);
            this.messageTextArea.setPreferredSize(new Dimension(710, 89));
            this.messageTextArea.setSize(new Dimension(710, 89));
            messageScrollPane.add(this.messageTextArea);
            messageScrollPane.setViewportView(this.messageTextArea);

            this.coinMatrixScroll.setVisible(true);
            this.coinMatrixScroll.setName("Coincidence Matrix");
            this.tabbedPane.add(this.coinMatrixScroll);
            this.tabbedPane.setTitleAt(1, "Coincidence Matrix");

            this.coinMatrixTable.setEnabled(false);
            this.coinMatrixScroll.add(this.coinMatrixTable);
            this.coinMatrixScroll.setViewportView(this.coinMatrixTable);

            TableModel dataModel2 = new DefaultTableModel(data, columns);
            this.coinMatrixTable.setModel(dataModel2);

            this.observedDeltaMatrix.setVisible(true);
            this.tabbedPane.add(this.observedDeltaMatrix);
            this.tabbedPane.setTitleAt(2, "Delta Matrix");

            this.observedDeltaTable.setEnabled(false);
            this.observedDeltaMatrix.add(this.observedDeltaTable);
            this.observedDeltaMatrix.setViewportView(this.observedDeltaTable);

            Object[][] data3 = {{"0", "1"}, {"2", "3"}};

            Object[] columns3 = {"One", "Two"};
            TableModel dataModel3 = new DefaultTableModel(data3, columns3);
            this.observedDeltaTable.setModel(dataModel3);

            resultsScroll.setPreferredSize(new Dimension(597, 352));
            this.tabbedPane.add(resultsScroll);
            this.tabbedPane.setTitleAt(3, "Data Statistics");

            this.resultsText.setEditable(false);
            resultsScroll.add(this.resultsText);
            resultsScroll.setViewportView(this.resultsText);

            calculationsScroll.setFont(new Font("Dialog", 0, 11));
            this.tabbedPane.add(calculationsScroll);
            this.tabbedPane.setTitleAt(4, "Calculations");

            this.calculationsText.setEditable(false);
            this.calculationsText.setPreferredSize(new Dimension(594, 349));
            calculationsScroll.add(this.calculationsText);
            calculationsScroll.setViewportView(this.calculationsText);
            JMenuBar jMenuBar1 = new JMenuBar();
            JMenu jMenu3 = new JMenu();
            JMenuItem openFileMenuItem = new JMenuItem();
            this.openLast = new JMenuItem();
            JSeparator jSeparator1 = new JSeparator();
            this.saveSuperMenu = new JMenu();
            JMenuItem saveResults = new JMenuItem();
            JMenuItem saveCoin = new JMenuItem();
            JMenuItem saveDelta = new JMenuItem();
            JMenuItem saveLog = new JMenuItem();
            JMenuItem saveReliabilityData = new JMenuItem();
            JSeparator jSeparator3 = new JSeparator();
            JMenuItem autoSaveAll = new JMenuItem();
            JSeparator jSeparator2 = new JSeparator();
            JMenuItem exitMenuItem = new JMenuItem();
            this.genLog = new JMenuItem();
            this.viewDataStatsMenuItem = new JMenuItem();
            this.calcAlphaMenuFunction = new JMenuItem();

            setJMenuBar(jMenuBar1);

            jMenu3.setText("File");
            jMenu3.setVisible(true);
            jMenuBar1.add(jMenu3);

            openFileMenuItem.setText("Open");
            openFileMenuItem.setVisible(true);
            openFileMenuItem.setPreferredSize(new Dimension(55, 28));
            openFileMenuItem.setBounds(new Rectangle(6, 7, 55, 28));
            jMenu3.add(openFileMenuItem);
            openFileMenuItem.addActionListener(evt -> Gui.this.openFileMenuItemActionPerformed());
            this.openLast.setText("Open Previous");
            this.openLast.setVisible(true);
            jMenu3.add(this.openLast);
            this.openLast.addActionListener(Gui.this::openLastActionPerformed);
            jSeparator1.setVisible(true);
            jMenu3.add(jSeparator1);

            this.saveSuperMenu.setText("Save...");
            this.saveSuperMenu.setVisible(true);
            jMenu3.add(this.saveSuperMenu);

            saveResults.setText("Save Data Statistics...");
            saveResults.setVisible(true);
            this.saveSuperMenu.add(saveResults);
            saveResults.addActionListener(Gui.this::saveResultsActionPerformed);
            saveCoin.setText("Save Coincidence Matrix...");
            saveCoin.setVisible(true);
            this.saveSuperMenu.add(saveCoin);
            saveCoin.addActionListener(Gui.this::saveCoinActionPerformed);
            saveDelta.setText("Save Observed Delta Matrix...");
            saveDelta.setVisible(true);
            this.saveSuperMenu.add(saveDelta);
            saveDelta.addActionListener(Gui.this::saveDeltaActionPerformed);
            saveLog.setText("Save Calculations...");
            saveLog.setVisible(true);
            saveLog.setBounds(new Rectangle(5, 5, 60, 30));
            this.saveSuperMenu.add(saveLog);
            saveLog.addActionListener(Gui.this::saveLogActionPerformed);

            saveReliabilityData.setText("Save Reliability Data...");
            saveReliabilityData.setVisible(true);
            saveReliabilityData.setBounds(new Rectangle(5, 5, 60, 30));
            this.saveSuperMenu.add(saveReliabilityData);
            saveReliabilityData.addActionListener(Gui.this::saveReliabilityActionPerformed);

            this.saveSuperMenu.add(jSeparator3);

            autoSaveAll.setText("Save all to single file...");
            this.saveSuperMenu.add(autoSaveAll);
            autoSaveAll.addActionListener(Gui.this::autoSaveAllActionPerformed);
            jSeparator2.setVisible(true);
            jSeparator2.setBounds(new Rectangle(5, 5, 60, 30));
            jMenu3.add(jSeparator2);

            exitMenuItem.setText("Exit");
            exitMenuItem.setVisible(true);
            exitMenuItem.setBounds(new Rectangle(5, 5, 60, 30));
            jMenu3.add(exitMenuItem);
            exitMenuItem.addActionListener(Gui.this::exitMenuItemActionPerformed);
            postInitGUI();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void postInitGUI() {

        setTitle(APP_TITLE);

        // TODO: can we move these options into a config file rather than hard coded
        this.unitCombo.addItem("Comma");
        this.unitCombo.addItem("Tab");
        this.unitCombo.addItem("Space");
        this.unitCombo.addItem("Other");

        this.valCombo.addItem("None");
        this.valCombo.addItem("Vertical Bar '|'");
        this.valCombo.addItem("Comma");
        this.valCombo.addItem("Other");

        this.orientCombo.addItem("Rows for Coders");
        this.orientCombo.addItem("Rows for Units");

        resetConsoleForNoRDM();
        setDefaultValues();

        this.messageTextArea.setText("\n\n\n\n\n");

        this.openLast.setEnabled(false);
        File lastOpenedFile = new File("lastOpened");
        if (lastOpenedFile.exists()) {
            try {
                this.lastDataFormat = new UISettings("lastOpened");
                this.openLast.setEnabled(true);
            } catch (IOException e) {
            }
        }
        this.toolPanel.setVisible(true);
        this.drmPanelSplitPane.setResizeWeight(1.0D);
    }

    //todo: can we work out the values?
    private void setDefaultValues() {

        this.colSpinner.getModel().setValue(UISettings.DEFAULT.getFromCol());
        this.rowSpinner.getModel().setValue(UISettings.DEFAULT.getFromRow());
        this.unitCombo.setSelectedItem(UISettings.DEFAULT.getUnitSeperator());
        this.valCombo.setSelectedItem(UISettings.DEFAULT.getValueSeperator());
        this.orientCombo.setSelectedItem(UISettings.DEFAULT.isRowForCoders());
    }

    public static void main(String[] args) {

        try {
            Gui inst = new Gui();
            inst.setVisible(true);

            // if a file path is in command line, load it
            if (args.length == 1) {
                File f = new File(args[0]);
                inst.loadRdFileWIthDefaultValues(f);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void exitMenuItemActionPerformed(ActionEvent evt) {
        try {
            if (this.lastValidFileFormat != null)
                this.lastValidFileFormat.writeFormatFile("lastOpened");
        } catch (IOException e) {
        }
        setVisible(false);
        System.exit(0);
    }

    private ReliabilityDataMatrix loadReliabilityData(UISettings ff)
            throws Exception {
        return ReliabilityDataFactory.loadReliabilityDataFromCSV(ff.getFilename(), ff);
    }

    private void setupDoDetails() {
        String[] headers = this.mvnaCalculator.getCoincidenceMatrix().headerOrderedLabelStrings();

        TableModel coinModel = new DefaultTableModel(this.mvnaCalculator.getCoincidenceMatrix().getOrderedTableData(), headers);

        this.coinMatrixTable.setModel(coinModel);
        formatJTable(this.coinMatrixTable, this.coinMatrixScroll, null);

        TableModel obsDeltaModel = new DefaultTableModel(this.mvnaCalculator.getObservedDeltaMatrix().getOrderedTableData(), this.mvnaCalculator.getObservedDeltaMatrix().headerOrderedLabelStrings());

        this.observedDeltaTable.setModel(obsDeltaModel);
        formatJTable(this.observedDeltaTable, this.observedDeltaMatrix, null);

        this.resultsText.setText(this.mvnaCalculator.getValues());
        this.calculationsText.setText(this.mvnaCalculator.getCalculations());
    }

    private static Object[][] switchOrientation(Object[][] data) {
        Object[][] result = new Object[data[0].length][data.length];
        for (int x = 0; x < data.length; x++) {
            for (int y = 0; y < data[x].length; y++) {
                result[y][x] = data[x][y];
            }
        }
        return result;
    }

    private void formatJTable(JTable table, JScrollPane pane, String[] rowHeaderStrings) {
        int widestColumn = 0;
        table.setAutoResizeMode(0);
        DefaultTableModel rowHeaderModel = new DefaultTableModel(0, 1);
        FontMetrics metrics = getGraphics().getFontMetrics();

        // Create the column widths and names
        for (int col = 0; col < table.getColumnCount(); col++) {
            String colName = table.getColumnName(col);
            int textWidth = metrics.stringWidth(colName) + 2 * table.getColumnModel().getColumnMargin();
            int colWidth = Math.max(60, textWidth + 20);
            table.getColumnModel().getColumn(col).setPreferredWidth(colWidth);
            widestColumn = Math.max(widestColumn, colWidth);
            rowHeaderModel.addRow(new Object[]{colName});
        }

        // Display the row headers
        if (rowHeaderStrings != null) {
            rowHeaderModel = new DefaultTableModel(0, 1);
            for (int i = 0; i < rowHeaderStrings.length; i++) {
                rowHeaderModel.addRow(new Object[]{rowHeaderStrings[i]});
            }
        }

        // create and set the row header
        JTable rowHeader = new JTable(rowHeaderModel);
        rowHeader.setIntercellSpacing(new Dimension(0, 0));
        rowHeader.setBackground(getBackground());
        Dimension d = rowHeader.getPreferredScrollableViewportSize();

        d.width = widestColumn;
        rowHeader.setPreferredScrollableViewportSize(d);
        rowHeader.setAutoResizeMode(4);
        rowHeader.setDefaultRenderer(Object.class, new DefaultTableCellRenderer());

        pane.setRowHeaderView(rowHeader);
    }

    protected void openFileMenuItemActionPerformed() {
        openNewRDMFile();
    }

    protected UISettings getDataFileAndFormat(File rdmFile) {

        if (rdmFile == null) {
            return null;
        }
        String filepath = rdmFile.getParent();
        String name = rdmFile.getAbsolutePath();

        return new UISettings(name, filepath, getUnitSeperator(), getValueSeperator(), getFromRow(), getFromColumn(), logCalculations(), isRowsForCoders());
    }

    private void setFormatControlsValues(UISettings rdff) {
        this.rowSpinner.setValue(new Integer(rdff.getFromRow()));
        this.colSpinner.setValue(new Integer(rdff.getFromCol()));

        this.unitText.setText("" + rdff.getUnitSeperator());
        this.unitCombo.setSelectedItem("Other");

        if (rdff.getValueSeperator() != '\b') {
            this.valText.setText("" + rdff.getValueSeperator());
            this.valCombo.setSelectedItem("Other");
        }
    }

    protected void openLastActionPerformed(ActionEvent evt) {
        try {
            UISettings rdff = new UISettings("lastOpened");

            this.rdmFile = new File(rdff.getFilename());
            if (!this.rdmFile.exists()) {
                throw new Exception("File: " + rdff.getFilename() + " not found.");
            }
            this.lastValidFileFormat = rdff;
            updateRDM(rdff);

            setFormatControlsValues(rdff);
        } catch (Exception e) {
            LoadErrorDialogue led = new LoadErrorDialogue();
            led.setText(e.getMessage());
            led.setVisible(true);
        }
    }

    private void saveText(String filename, String text) {
        try {
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(filename)), true);
            out.println(text);
        } catch (IOException e) {
            LoadErrorDialogue led = new LoadErrorDialogue();
            led.setText(e.getMessage());
            led.setVisible(true);
        }
    }

    /**
     * Update the data matrix for calculation based on the UI properties.
     *
     * @param format
     */
    private void updateRDM(UISettings format) {
        if (this.rdmFile != null) {
            if (!isOptionsValid()) {
                printMessage("Error creating reliability data matrix:\n" + this.errorMessages + "\n");
                resetConsoleForNoRDM();
            } else {
                try {
                    this.fileBasename = new File(format.getFilename()).getName();
                    this.fileBasename = this.fileBasename.substring(0, this.fileBasename.indexOf(46));
                    this.fileBasename = (new File(format.getFilename()).getParent() + System.getProperty("file.separator") + this.fileBasename);

                    // todo could this be a repeat of something just done?
                    this.rdm = loadReliabilityData(format);

                    // Set the data according to the orientation
                    String[] colHeaders;
                    String[] rowHeaders;
                    Object[][] gridDataForDisplay;

                    if (this.isRowsForCoders()) {
                        colHeaders = this.rdm.getUnitHeaders();
                        rowHeaders = this.rdm.getCoderHeaders();
                        gridDataForDisplay = switchOrientation(this.rdm.getLabels());
                    } else {
                        colHeaders = this.rdm.getCoderHeaders();
                        rowHeaders = this.rdm.getUnitHeaders();
                        gridDataForDisplay = this.rdm.getLabels();
                    }

                    TableModel rdmModel = new DefaultTableModel(gridDataForDisplay, colHeaders);
                    this.rdmTable.setModel(rdmModel);
                    formatJTable(this.rdmTable, this.dataScrollPane, rowHeaders);
                    this.rdmTable.setVisible(true);
                    setTitle(APP_TITLE + " " + new File(format.getFilename()).getName());

                    resetConsoleForNewValidRDM();
                } catch (Exception e) {
                    printMessage("Error creating reliability data matrix:\n" + e);
                    resetConsoleForNoRDM();
                }
            }
        }

        this.tabbedPane.setSelectedIndex(0);
    }

    private void resetConsoleForNoRDM() {
        this.rdmTable.setVisible(false);
        this.rdm = null;

        while (this.rdmTable.getColumnCount() > 0) {
            TableColumn tc = this.rdmTable.getColumnModel().getColumn(0);
            this.rdmTable.getColumnModel().removeColumn(tc);
        }

        this.dataScrollPane.setRowHeader(null);

        this.saveSuperMenu.setEnabled(false);

        this.tabbedPane.setEnabled(true);
        for (int i = 0; i < this.tabbedPane.getTabCount(); i++) {
            this.tabbedPane.setEnabledAt(i, false);
        }

        this.obsButton.setEnabled(false);
        this.expButton.setEnabled(false);
        this.viewDataStatsMenuItem.setEnabled(false);
        this.calcAlphaMenuFunction.setEnabled(false);
        this.genLog.setEnabled(false);

        this.lastChangeWasValid = false;
    }

    private void resetConsoleForNewValidRDM() {
        this.tabbedPane.setEnabled(true);
        for (int i = 0; i < this.tabbedPane.getTabCount(); i++) {
            this.tabbedPane.setEnabledAt(i, false);
        }

        this.obsButton.setEnabled(true);
        this.expButton.setEnabled(true);
        this.viewDataStatsMenuItem.setEnabled(true);
        this.calcAlphaMenuFunction.setEnabled(true);

        this.saveSuperMenu.setEnabled(false);
        this.genLog.setEnabled(false);

        if (!this.lastChangeWasValid) {
            printMessage("Valid reliability data loaded.");
        }
        this.lastChangeWasValid = true;
    }

    private void calculateDoAndUpdate() {
        if (this.rdm != null) {
            this.mvnaCalculator = new MultiValuedAlphaCalculator(this.rdm, logCalculations());

            this.mvnaCalculator.calculateDo();
            setupDoDetails();

            this.tabbedPane.setEnabledAt(0, true);
            this.tabbedPane.setEnabledAt(1, true);
            this.tabbedPane.setEnabledAt(2, true);
            this.tabbedPane.setEnabledAt(3, true);
            this.tabbedPane.setEnabledAt(4, true);

            this.saveSuperMenu.setEnabled(true);
            this.lastValidFileFormat = getDataFileAndFormat(this.rdmFile);
        }
    }

    protected void saveLogActionPerformed(ActionEvent evt) {
        saveSomething("Save Calculations Log", this.mvnaCalculator.getCalculationsText(), this.fileBasename + "_calculations-log.txt");
    }

    protected void rowSpinnerStateChanged(ChangeEvent evt) {
        int value = ((SpinnerNumberModel) this.rowSpinner.getModel()).getNumber().intValue();

        if (value < 1) {
            ((SpinnerNumberModel) this.rowSpinner.getModel()).setValue(new Integer(1));
        }

        updateRDM(getDataFileAndFormat(this.rdmFile));
    }

    protected void colSpinnerStateChanged(ChangeEvent evt) {
        int value = ((SpinnerNumberModel) this.colSpinner.getModel()).getNumber().intValue();

        if (value < 1) {
            ((SpinnerNumberModel) this.colSpinner.getModel()).setValue(new Integer(1));
        }

        updateRDM(getDataFileAndFormat(this.rdmFile));
    }

    protected void valComboActionPerformed(ActionEvent evt) {
        this.valText.setEnabled(this.valCombo.getSelectedItem().equals("Other"));

        if (!this.valCombo.getSelectedItem().equals("Other"))
            this.valText.setText("");
        UISettings dataFileAndFormat;
        try {
            dataFileAndFormat = getDataFileAndFormat(this.rdmFile);
        } catch (StringIndexOutOfBoundsException e) {
            dataFileAndFormat = null;
        }

        updateRDM(dataFileAndFormat);
    }

    protected void unitComboActionPerformed(ActionEvent evt) {
        this.unitText.setEnabled(this.unitCombo.getSelectedItem().equals("Other"));

        if (!this.unitCombo.getSelectedItem().equals("Other"))
            this.unitText.setText("");
        UISettings dataFileAndFormat;
        try {
            dataFileAndFormat = getDataFileAndFormat(this.rdmFile);
        } catch (StringIndexOutOfBoundsException e) {
            dataFileAndFormat = null;
        }

        updateRDM(dataFileAndFormat);
    }

    protected void orientComboActionPerformed(ActionEvent evt) {
        updateRDM(getDataFileAndFormat(this.rdmFile));
    }

    public char getUnitSeperator() {
        switch (this.unitCombo.getSelectedIndex()) {
            case 0:
                return ',';
            case 1:
                return '\t';
            case 2:
                return ' ';
        }

        return this.unitText.getText().charAt(0);
    }

    public char getValueSeperator() {
        switch (this.valCombo.getSelectedIndex()) {
            case 0:
                return '\b';
            case 1:
                return '|';
            case 2:
                return ',';
            case 3:
                return this.valText.getText().charAt(0);
        }

        return '-';
    }

    public int getFromRow() {
        int value = ((SpinnerNumberModel) this.rowSpinner.getModel()).getNumber().intValue();

        return value;
    }

    public int getFromColumn() {
        int value = ((SpinnerNumberModel) this.colSpinner.getModel()).getNumber().intValue();

        return value;
    }

    private boolean isOptionsValid() {
        this.errorMessages = "";
        boolean noErrors = true;

        if ((this.unitCombo.getSelectedItem() == "Other") &&
                (this.unitText.getText().length() != 1)) {
            this.errorMessages += "\nUnit separator must be a single char.";
            noErrors = false;
        }
        if ((this.valCombo.getSelectedItem() == "Other") &&
                (this.valText.getText().length() != 1)) {
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

    private boolean logCalculations() {
        return this.logCalculations.isSelected();
    }

    private boolean isRowsForCoders() {
        return this.orientCombo.getSelectedItem() == "Rows for Coders";
    }

    private void printMessage(String message) {
        this.messageCount += 1;
        this.messageTextArea.setText(this.messageTextArea.getText() + "\n\n[" + this.messageCount + "] " + message);

        this.messageTextArea.setSelectionStart(2147483647);
    }

    protected void unitTextKeyReleased(KeyEvent evt) {
        updateRDM(getDataFileAndFormat(this.rdmFile));
    }

    protected void valTextKeyReleased(KeyEvent evt) {
        updateRDM(getDataFileAndFormat(this.rdmFile));
    }

    protected void openFileActionPerformed(ActionEvent evt) {
        openNewRDMFile();
    }

    private void saveSomething(String dialogueTitle, String toSave, String defaultName) {
        JFileChooser fileSaver = new JFileChooser(dialogueTitle);
        fileSaver.setSelectedFile(new File(defaultName));
        int fileDialogResult = fileSaver.showSaveDialog(this);

        if (fileDialogResult == 0) {
            String filename = fileSaver.getSelectedFile().getAbsolutePath();

            toSave = this.getCommonOutput() + toSave;
            saveText(filename, toSave);
        }
    }

    private String getCommonOutput() {

        String output = "";

        DecimalFormat df = new DecimalFormat(this.ALPHA_FORMAT_STRING);
        String durationSeconds = df.format(calculationDuration / 1E9);
        String alphaString = df.format(this.mvnaCalculator.getAlpha());

        DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        output += "Results from calculating mvnα on " + dateFormat.format(new Date())
                + " (took " + durationSeconds + " seconds)" + System.getProperty("line.separator");

        output += "Input Data File: " + this.fileBasename + System.getProperty("line.separator");
        output += "mvnα: " + alphaString + System.getProperty("line.separator");
        output += System.getProperty("line.separator");
        return output;
    }

    private void openNewRDMFile() {

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Open Reliability Data");

        if (this.lastDataFormat != null) {
            try {
                File f = new File(this.lastDataFormat.getFilepath());
                if (f.exists()) fileChooser.setCurrentDirectory(f);
            } catch (Exception ignored) {
            }
        }

        int fileDialogResult = fileChooser.showOpenDialog(this);

        if (fileDialogResult == 0) {
            File file = fileChooser.getSelectedFile();
            loadRdFileWIthDefaultValues(file);
        }
    }

    /**
     * Load a file setting the default values beforehand
     * Used when loading through the UI but also when providing a parameter
     *
     * @param file the file to load
     */
    private void loadRdFileWIthDefaultValues(File file) {
        setDefaultValues();
        this.lastDataFormat = getDataFileAndFormat(file);
        this.rdmFile = file;
        updateRDM(this.lastDataFormat);
        resultLabel.setText("");
    }

    private void calculateDeAndUpdate() {
        long startTime = System.nanoTime();

        calculateDoAndUpdate();

        setCursor(new Cursor(3));
        this.mvnaCalculator.calculateAlpha();
        setCursor(new Cursor(0));
        this.genLog.setEnabled(true);

        this.mvnaCalculator.addResultsText("\nAlpha = " + this.mvnaCalculator.getAlpha());
        this.resultsText.setText(this.mvnaCalculator.getValues());
        this.calculationsText.setText(this.mvnaCalculator.getCalculations());

        DecimalFormat df = new DecimalFormat(this.ALPHA_FORMAT_STRING);
        String formattedAlpha = df.format(this.mvnaCalculator.getAlpha());

        printMessage("Alpha = " + formattedAlpha);
        resultLabel.setText("   mvnα = " + formattedAlpha);

        this.calculationDuration = System.nanoTime() - startTime;

    }

    protected void expButtonActionPerformed(ActionEvent evt) {
        calculateDeAndUpdate();
    }

    protected void saveCoinActionPerformed(ActionEvent evt) {
        saveSomething("Save Coincidence Matrix",
                this.mvnaCalculator.getCoincidenceMatrix().getCSVText(),
                this.fileBasename + "_coincidence-matrix.txt");
    }

    protected void saveResultsActionPerformed(ActionEvent evt) {
        saveSomething("Save Data Statistics",
                this.mvnaCalculator.getResultsText(),
                this.fileBasename + "_data-statistics.txt");
    }

    protected void saveDeltaActionPerformed(ActionEvent evt) {
        saveSomething("Save Difference Matrix",
                this.mvnaCalculator.getObservedDeltaMatrix().getCSVText(),
                this.fileBasename + "_difference-matrix.txt");
    }

    private void saveReliabilityActionPerformed(ActionEvent evt) {

        saveSomething("Save Reliability Data",
                this.rdm.toCSV(),
                this.fileBasename + "_reliability-data.txt");
    }

    protected void autoSaveAllActionPerformed(ActionEvent evt) {
        String allText = this.mvnaCalculator.getResultsText() + "\n\nCoincidence Matrix\n" +
                this.mvnaCalculator.getCoincidenceMatrix().getCSVText() + "\n\nDelta Matrix\n" +
                this.mvnaCalculator.getObservedDeltaMatrix().getCSVText() + "\n\n" +
                this.mvnaCalculator.getCalculations();
        saveSomething("Save all results", allText, this.fileBasename + "_all_results.txt");
    }

}