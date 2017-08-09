package globs.util;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Scanner;

import javax.xml.stream.XMLStreamException;

import org.apache.commons.io.FileUtils;
import org.sbml.jsbml.ASTNode;
import org.sbml.jsbml.Compartment;
import org.sbml.jsbml.KineticLaw;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.ModifierSpeciesReference;
import org.sbml.jsbml.Parameter;
import org.sbml.jsbml.Reaction;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLReader;
import org.sbml.jsbml.Species;
import org.sbml.jsbml.SpeciesReference;
import org.sbml.jsbml.text.parser.ParseException;

public class GlobsUtilities {


  public static String fileToString(String filename)
  {
    File file = new File(filename);
    String convert;
    try 
    {
      convert = FileUtils.readFileToString(file);
    } 
    catch (IOException e) 
    {
      convert = "";
    }
    return convert;

  }
  public static SBMLDocument convertToSBML(String modelId, String modelBody) throws Rm4120Exception, ParseException
  {
    SBMLDocument doc = new SBMLDocument(3,1);
    Model model = doc.createModel(modelId);
    Compartment cell = model.createCompartment("Cell");
    cell.setValue(1);
    cell.setSpatialDimensions(3);
    cell.setConstant(true);
    Scanner scanner = new Scanner(modelBody);
    while(scanner.hasNextLine())
    {
      String line = scanner.nextLine().trim();
      if(line.isEmpty())
      {
        continue;
      }
      if(!line.endsWith(";"))
      {
        scanner.close();
        throw new Rm4120Exception();
      }
      parseLine(model, cell, line);

    }
    scanner.close();
    return doc;

  }

  private static void parseLine(Model model, Compartment cell, String line) throws Rm4120Exception, ParseException
  {
    String[] parse = line.substring(0, line.length()-1).split("\\s+");

    if(parse.length == 0)
    {
      return;
    }
    
   
    Parameter param;
    Species species;
    Reaction reaction;
    SpeciesReference ref;
    String id, source, dest, formula; 
    double value;
    switch(parse[0])
    {
    case "Parameter":
      if(parse.length != 4)
      {
        throw new Rm4120Exception();
      }
      id = parse[1];
      value = Double.parseDouble(parse[3]);
      param = model.createParameter(id);
      param.setValue(value);
      param.setConstant(true);
      break;
    case "Species":
      if(parse.length != 4)
      {
        throw new Rm4120Exception();
      }
      id = parse[1];
      value = Double.parseDouble(parse[3]);
      species = model.createSpecies(id);
      species.setValue(value);
      species.setConstant(false);
      species.setBoundaryCondition(false);
      species.setHasOnlySubstanceUnits(false);
      species.setCompartment(cell);
      break;
    case "Reaction":
      if(parse.length != 4)
      {
        throw new Rm4120Exception();
      }
      id = parse[1];
      formula = parse[3];
      reaction = model.createReaction(id);
      KineticLaw law = reaction.createKineticLaw();
      law.setMath(ASTNode.parseFormula(formula));
      reaction.setReversible(false);
      break;
    case "Reactant":
      if(parse.length != 5)
      {
        throw new Rm4120Exception();
      }
      source = parse[2];
      dest = parse[4];
      reaction = model.getReaction(dest);
      ref = reaction.createReactant(model.getSpecies(source));
      ref.setConstant(true);
      ref.setStoichiometry(Double.parseDouble(parse[1]));
      break;
    case "Product":
      if(parse.length != 5)
      {
        throw new Rm4120Exception();
      }
      source = parse[2];
      dest = parse[4];
      reaction = model.getReaction(dest);
      ref = reaction.createProduct(model.getSpecies(source));
      ref.setConstant(true);
      ref.setStoichiometry(Double.parseDouble(parse[1]));
      break;
    case "Modifier":
      if(parse.length != 4)
      {
        throw new Rm4120Exception();
      }
      source = parse[1];
      dest = parse[3];
      reaction = model.getReaction(dest);
      reaction.createModifier(model.getSpecies(source));
      break;
    default:
      throw new Rm4120Exception();
    }

  }
  
  public static String convertSBMLToText(String path, String sbmlDoc)
  {
    StringBuilder builder = new StringBuilder();
    
    try {
      SBMLDocument doc = SBMLReader.read(new File(path + File.separator + sbmlDoc));
      Model model = doc.getModel();
      
      for(Parameter param : model.getListOfParameters())
      {
        builder.append("Parameter ");
        builder.append(param.getId());
        builder.append(" = ");
        builder.append(param.getValue());
        builder.append("\n");
      }
      for(Species species : model.getListOfSpecies())
      {
        builder.append("Species ");
        builder.append(species.getId());
        builder.append(" = ");
        builder.append(species.getValue());
        builder.append("\n");
      }
      for(Reaction reaction : model.getListOfReactions())
      {
        builder.append("Reaction ");
        builder.append(reaction.getId());
        builder.append(" = ");
        builder.append(reaction.getKineticLaw().getMath().toFormula());
        builder.append("\n");
        for(SpeciesReference specRef : reaction.getListOfReactants())
        {
          builder.append("Reactant ");
          builder.append(specRef.getStoichiometry());
          builder.append(' ');
          builder.append(specRef.getSpecies());
          builder.append(" -> ");
          builder.append(reaction.getId());
          builder.append("\n");
        }
        for(SpeciesReference specRef : reaction.getListOfProducts())
        {
          builder.append("Product ");
          builder.append(specRef.getStoichiometry());
          builder.append(' ');
          builder.append(specRef.getSpecies());
          builder.append(" -> ");
          builder.append(reaction.getId());
          builder.append("\n");
        }
        for(ModifierSpeciesReference specRef : reaction.getListOfModifiers())
        {
          builder.append("Modifier ");
          builder.append(specRef.getSpecies());
          builder.append(" -> ");
          builder.append(reaction.getId());
          builder.append("\n");
        }
      }
      
      
    } catch (XMLStreamException e) {
      return "";
    } catch (IOException e) {
      return "";
    }
    return builder.toString();
  }
  
  public static String[] getSBMLFiles(String path)
  {
    File file = new File(path);

    File[] xmlFiles = file.listFiles(new FilenameFilter() 
    {

      @Override
      public boolean accept(File dir, String name) {
        return name.toLowerCase().endsWith(".xml"); 
      } 
    });
    String[] models = new String[xmlFiles.length+1];

    for(int i = xmlFiles.length - 1; i >= 0; i--)
    {
      models[i+1] = xmlFiles[i].getName();
    }
    models[0]="";
    return models;
  }
  

}
