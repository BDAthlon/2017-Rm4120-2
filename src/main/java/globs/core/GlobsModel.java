package globs.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import org.apache.commons.math3.util.Precision;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.Species;

import edu.utah.ece.async.ibiosim.analysis.simulation.hierarchical.methods.HierarchicalODERKSimulator;
import edu.utah.ece.async.ibiosim.dataModels.util.exceptions.BioSimException;

public class GlobsModel {

  
  private String root;
  private String sbmlFileName;
  private SBMLDocument document;
  private HierarchicalODERKSimulator odeSimulator;
  private List<String> speciesList;
  private XY globs;
  private List<XY> simulationTraces;
  

  public GlobsModel()
  {
    speciesList = new ArrayList<String>();
  }

  
  public void simulate()
  {

    long randomSeed      = 0;
    try {
      int n = speciesList.size();
      simulationTraces = new ArrayList<XY>();
      for(int i = 0; i < n; i++)
      {
        simulationTraces.add(new XY());
      }
      
      odeSimulator = new HierarchicalODERKSimulator(sbmlFileName, root, 0);
      odeSimulator.initialize(randomSeed, 0);
      double timeLimit = 0;
      
      while(timeLimit <= 10)
      {
        odeSimulator.setTimeLimit(timeLimit);
        odeSimulator.simulate();
        
        for(int i = 0; i < n; i++)
        {
          double y = odeSimulator.getTopLevelValue(speciesList.get(i));
          simulationTraces.get(i).addXY(timeLimit, y);
        }
        timeLimit = Precision.round(timeLimit + 0.1,1);
      }
    } catch (IOException e) {
      e.printStackTrace();
    } catch (XMLStreamException e) {
      e.printStackTrace();
    } catch (BioSimException e) {
      e.printStackTrace();
    }
  }


  public void createPoints(int n)
  {
    globs = new XY();
    for(int i = n - 1; i >=0; --i)
    {
      double sample_x = Precision.round(Math.random()*10, 1);
      double sample_y = Precision.round(Math.random()*10, 1);

      globs.addXY(sample_x,sample_y);
    }
  }

  
  public XYDataset getDataset(String id) {

    XY data = null;

    XYSeries series = new XYSeries(id);
    if(id.equals("_Globs"))
    {
      data = globs;
    }
    else
    {
      int index = speciesList.indexOf(id);
      data = simulationTraces.get(index);
    }
    
    
    for(int i = 0; i < data.getSize(); ++i)
    {
      series.add(data.getX(i), data.getY(i));
    }

    XYSeriesCollection dataset = new XYSeriesCollection();
    dataset.addSeries(series);

    return dataset;
  }

  public void setSBMLFile(String sbmlFileName)
  {
    this.sbmlFileName = sbmlFileName;
  }
  
  public void setRoot(String root)
  {
    this.root = root;
  }
  
  public String getSBMLFile()
  {
    return sbmlFileName;
  }
  
  public String getRoot()
  {
    return root;
  }
  
  public List<String> getListOfSpecies()
  {
    return speciesList;
  }
  
  public void setSBMLDocument(SBMLDocument document)
  {
    speciesList.clear();

    this.document = document;
    Model model = document.getModel();
    for(Species species : model.getListOfSpecies())
    {
      speciesList.add(species.getId());
    }
    
  }
  
  public SBMLDocument getSBMLDocument()
  {
    return document;
  }
  

  public int computePoints()
  {
    int score = 0;
    for(int i = 0; i < globs.getSize(); i++)
    {
      double x = globs.getX(i);
      double y = globs.getY(i);
      int index = (int)(x*10);
      
      for(XY xy : simulationTraces)
      {
        if(Math.abs(xy.getY(index) - y) < 1e-1)
        {
          score++;
        }
      }
    }
    
    return score;
  }
  
  private class XY
  {
    private List<Double> x;
    private List<Double> y;
    
    public XY()
    {
      x = new ArrayList<Double>();
      y = new ArrayList<Double>();
    }
    
    void addXY(double x, double y)
    {
      this.x.add(x);
      this.y.add(y);
    }
    
    
    int getSize()
    {
      return x.size();
    }
    
    double getX(int index)
    {
      return x.get(index);
    }
    
    double getY(int index)
    {
      return y.get(index);
    }
  }
}
