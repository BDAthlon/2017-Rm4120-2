package globs.core;
 

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.xml.stream.XMLStreamException;

import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLException;
import org.sbml.jsbml.SBMLWriter;
import org.sbml.jsbml.text.parser.ParseException;

import com.google.common.io.Files;

import globs.panels.ModelPanel;
import globs.panels.SimulationPanel;
import globs.util.GlobsUtilities;
import globs.util.Rm4120Exception;

public class GlobsController {
  
  private GlobsModel model;
  private GlobsView view;
  private SimulationPanel simPanel;
  private ModelPanel modelPanel;

  private boolean isLoadingModels = false;
  private final JFileChooser filechooser = new JFileChooser();
  private final  String[] templateOptions = new String[]{"", "Simple Reaction", "Enzymatic Reaction", "Complex Reaction", "Repression Reaction"};
  private String[] modelOptions = GlobsUtilities.getSBMLFiles(".");
  private File tmp;
  
  public GlobsController()
  {
    model = new GlobsModel();
    view = new GlobsView();
    tmp = Files.createTempDir();
  }
  
  public void init()
  {
    model.createPoints(20);
    modelPanel = new ModelPanel();
    simPanel = new SimulationPanel();
    simPanel.initUI(model.getDataset("_Globs"));
    view.init(simPanel, modelPanel);
    filechooser.setCurrentDirectory(new java.io.File("."));
    
    JComboBox<String> templates = modelPanel.getTemplates();
    JTextArea editor = modelPanel.getEditor();
    JButton browser = modelPanel.getBrowser();

    JButton run = modelPanel.getRun();
    //JButton clear = modelPanel.getClear();
    
    JTextField directory = modelPanel.getDirectory();
    JComboBox<String> list = modelPanel.getList();
    
    for(String item : modelOptions)
    {
      list.addItem(item);
    }
    
    for(String item : templateOptions)
    {
      templates.addItem(item);
    }
    
    
    
    templates.addActionListener(new ActionListener()
    {

      @Override
      public void actionPerformed(ActionEvent e) {
        String type =  (String) templates.getSelectedItem();
        if(type.equals(templateOptions[1]))
        {
          String filename = getClass().getClassLoader().getResource("simpleReaction.txt").getFile();
          modelPanel.getEditor().setText(GlobsUtilities.fileToString(filename));
        }
        else if(type.equals(templateOptions[2]))
        {
          String filename = getClass().getClassLoader().getResource("enzymaticReaction.txt").getFile();
          editor.setText(GlobsUtilities.fileToString(filename));
        }
        else if(type.equals(templateOptions[3]))
        {
          String filename = getClass().getClassLoader().getResource("complexReaction.txt").getFile();
          editor.setText(GlobsUtilities.fileToString(filename));
        }
        else if(type.equals(templateOptions[4]))
        {
          String filename = getClass().getClassLoader().getResource("repressionReaction.txt").getFile();
          editor.setText(GlobsUtilities.fileToString(filename));
        }
        else
        {
          editor.setText("");
        }
        templates.setSelectedItem(templateOptions[0]);
      }

    });   

    browser.addActionListener(new ActionListener()
    {

      @Override
      public void actionPerformed(ActionEvent e) { // start at application current directory
        filechooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (filechooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
          String path = filechooser.getSelectedFile().getAbsolutePath();
          directory.setText(path);
          modelOptions = GlobsUtilities.getSBMLFiles(path);
          isLoadingModels = true;
          list.removeAllItems();
          for(String item : modelOptions)
          {
            list.addItem(item);
          }
          isLoadingModels = false;
        }
      }
    });

    list.addActionListener(new ActionListener()
    {

      @Override
      public void actionPerformed(ActionEvent e) {
        if(!isLoadingModels)
        {

          String select = (String) list.getSelectedItem();
          if(!select.equals(""))
          {
            editor.setText(GlobsUtilities.convertSBMLToText(directory.getText(), select));
          }
          list.setSelectedItem(modelOptions[0]);
        }

      }

    });
    
    run.addActionListener(new ActionListener(){

      @Override
      public void actionPerformed(ActionEvent e) {
        String sbmlFile = tmp.getPath() + File.separator + "Globs.xml";
        SBMLDocument doc;
        try {
          simPanel.resetChart();
          doc = GlobsUtilities.convertToSBML("Globs", editor.getText());
          SBMLWriter.write(doc, new File(sbmlFile), ' ', (short)4);
          model.setSBMLDocument(doc);
          model.setRoot(tmp.getPath());
          model.setSBMLFile(sbmlFile);
          model.simulate();
          for(int i = 0; i < model.getListOfSpecies().size(); i++)
          {
            String species = model.getListOfSpecies().get(i);
            simPanel.plotSimulationTrace(species, i+1, model.getDataset(species));
          }
          
          int points = model.computePoints();
          modelPanel.getScore().setText(String.valueOf(points));
          
        } catch (Rm4120Exception e1) {
          e1.printStackTrace();
        } catch (ParseException e1) {
          e1.printStackTrace();
        } catch (SBMLException e1) {
          e1.printStackTrace();
        } catch (XMLStreamException e1) {
          e1.printStackTrace();
        } catch (IOException e1) {
          e1.printStackTrace();
        }
        
      }
      
    });
  }
  
}
