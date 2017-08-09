package globs.panels;

import java.awt.Component;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class ModelPanel extends JPanel {

  /**
   * 
   */
  private static final long serialVersionUID = 1702540132957290204L;
  
  private JComboBox<String> list = new JComboBox<String>();
  private JComboBox<String> templates = new JComboBox<String>();
  private JTextField directory = new JTextField(".",20);
  private JButton browser = new JButton("Browser");
  private JTextArea editor = new JTextArea();
  private JButton run = new JButton("Run");
  private JButton clear = new JButton("Clear");
  
  public JComboBox<String> getList() {
    return list;
  }

  public ModelPanel()
  {
    setLayout(new GridLayout(3,1));
    initModelOptions();
    initRunnerOptions();
  }
  
  public void initModelOptions()
  {
    directory.setEditable(false);
    JScrollPane scroll = new JScrollPane (editor, 
    JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

 

    JPanel optionsPanel = new JPanel();
    optionsPanel.setLayout(new GridLayout(3, 1));
    addComponentTriple(optionsPanel, "Directory: ", directory,browser);
    addComponentPair(optionsPanel, "List Of Models: ", list);
    addComponentPair(optionsPanel, "Load Template: ", templates);
    add(optionsPanel);
    add(scroll);
  }

  public void initRunnerOptions()
  {
    JPanel panel = new JPanel();
    panel.setLayout(new GridLayout(1, 3));
    JPanel scorePanel = new JPanel();
    scorePanel.setLayout(new GridLayout(2,1));
    scorePanel.add(new JLabel("Score"));
    scorePanel.add(new JLabel());
    panel.add(clear);
    panel.add(scorePanel);
    panel.add(run);
    add(panel);
  }
  
  private void addComponentPair(JPanel panel, String label, Component component)
  {
    JPanel p = new JPanel();
    p.setLayout(new GridLayout(1, 2));
    p.add(new JLabel(label));
    p.add(component);
    panel.add(p);
  }

  private void addComponentTriple(JPanel panel, String label, Component component1, Component component2)
  {
    JPanel p = new JPanel();
    p.setLayout(new GridLayout(1, 3));
    p.add(new JLabel(label));
    p.add(component1);
    p.add(component2);
    panel.add(p);
  }

  public JComboBox<String> getTemplates() {
    return templates;
  }

  
  public JTextField getDirectory() {
    return directory;
  }

  
  public JButton getBrowser() {
    return browser;
  }
  
  public JButton getRun() {
    return run;
  }

  public JButton getClear() {
    return clear;
  }
  
  public JTextArea getEditor() {
    return editor;
  }

}
