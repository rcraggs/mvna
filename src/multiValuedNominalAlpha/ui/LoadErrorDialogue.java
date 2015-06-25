package multiValuedNominalAlpha.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextPane;

public class LoadErrorDialogue extends JDialog
{
  private JButton okButton;
  private JTextPane errorText;
  private JLabel errorHeader;
  private JLabel header;

  public LoadErrorDialogue()
  {
     initGUI();
  }

  public void initGUI()
  {
    try
    {
       preInitGUI();

       this.header = new JLabel();
       this.errorHeader = new JLabel();
       this.errorText = new JTextPane();
       this.okButton = new JButton();

       GridBagLayout thisLayout = new GridBagLayout();
       getContentPane().setLayout(thisLayout);
       thisLayout.columnWidths = new int[] { 1, 1, 1 };
       thisLayout.rowHeights = new int[] { 1, 1, 1, 1 };
       thisLayout.columnWeights = new double[] { 0.1D, 0.1D, 0.1D };
       thisLayout.rowWeights = new double[] { 0.1D, 0.1D, 0.1D, 0.1D };
       setSize(new Dimension(300, 155));

       this.header.setText("Failed to load reliability data");
       this.header.setFont(new Font("Dialog", 0, 12));
       getContentPane().add(this.header, new GridBagConstraints(1, 0, 1, 1, 0.0D, 0.0D, 16, 0, new Insets(0, 0, 0, 0), 0, 0));

       this.errorHeader.setText("The following error message was returned :");
       this.errorHeader.setFont(new Font("Dialog", 0, 12));
       getContentPane().add(this.errorHeader, new GridBagConstraints(1, 1, 1, 1, 0.0D, 0.0D, 21, 0, new Insets(0, 0, 0, 0), 0, 0));

       this.errorText.setEditable(false);
       this.errorText.setBackground(new Color(204, 204, 204));
       this.errorText.setFont(new Font("Dialog", 1, 12));
       this.errorText.setFocusable(false);
       getContentPane().add(this.errorText, new GridBagConstraints(1, 2, 1, 1, 0.0D, 0.6D, 10, 1, new Insets(0, 0, 0, 0), 0, 0));

       this.okButton.setText("OK");
       this.okButton.setSize(new Dimension(81, 26));
       getContentPane().add(this.okButton, new GridBagConstraints(1, 3, 1, 1, 0.0D, 0.2D, 10, 0, new Insets(0, 0, 0, 0), 0, 0));
       this.okButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent evt) {
           LoadErrorDialogue.this.okButtonActionPerformed(evt);
        }
      });
       postInitGUI();
    } catch (Exception e) {
    }
  }

  public void preInitGUI()
  {
  }

  public void postInitGUI() {
     setTitle("Error");
     setModal(true);
  }

  public void setText(String text) {
     this.errorText.setText(text);
  }

  protected void okButtonActionPerformed(ActionEvent evt)
  {
     setVisible(false);
  }
  
  public void setVisible(boolean b){
      this.setVisible(b);
  }
}