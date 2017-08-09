package src.test.java;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.text.parser.ParseException;

import globs.util.Rm4120Exception;
import globs.util.GlobsUtilities;
import junit.framework.Assert;


public class ModelTest {

  @Before
  public void setUp() throws Exception {
  }


  @Test
  public void test01() {
    SBMLDocument doc = null;
    try {
      doc = GlobsUtilities.convertToSBML("Test", " Parameter    p =  0.1; ");
      assertTrue(doc.getModel() != null);
      assertTrue(doc.getModel().getParameterCount() == 1);
      assertTrue(doc.getModel().getParameter("p") != null);
      assertTrue(doc.getModel().getParameter("p").getValue() == 0.1);
    } catch (Rm4120Exception e) {
      fail();
    } catch (ParseException e) {
      fail();
    }
  }
  
  @Test
  public void test02() {
    String filename = getClass().getClassLoader().getResource("repressionReaction.txt").getFile();
    String model = GlobsUtilities.fileToString(filename);
    SBMLDocument doc = null;
    try {
      doc = GlobsUtilities.convertToSBML("Test", model);
    } catch (Rm4120Exception e) {
      fail();
    } catch (ParseException e) {
      fail();
    }
    assert(doc != null);
  }
  
  @Test
  public void test03() {
    String filename = getClass().getClassLoader().getResource("complexReaction.txt").getFile();
    String model = GlobsUtilities.fileToString(filename);
    SBMLDocument doc = null;
    try {
      doc = GlobsUtilities.convertToSBML("Test", model);
    } catch (Rm4120Exception e) {
      fail();
    } catch (ParseException e) {
      fail();
    }
    assert(doc != null);
  }
  
  @Test
  public void test04() {
    String filename = getClass().getClassLoader().getResource("enzymaticReaction.txt").getFile();
    String model = GlobsUtilities.fileToString(filename);
    SBMLDocument doc = null;
    try {
      doc = GlobsUtilities.convertToSBML("Test", model);
    } catch (Rm4120Exception e) {
      fail();
    } catch (ParseException e) {
      fail();
    }
    assert(doc != null);
  }
  
  @Test
  public void test05() {
    String filename = getClass().getClassLoader().getResource("simpleReaction.txt").getFile();
    String model = GlobsUtilities.fileToString(filename);
    SBMLDocument doc = null;
    try {
      doc = GlobsUtilities.convertToSBML("Test", model);
    } catch (Rm4120Exception e) {
      fail();
    } catch (ParseException e) {
      fail();
    }
    assert(doc != null);
  }
}
