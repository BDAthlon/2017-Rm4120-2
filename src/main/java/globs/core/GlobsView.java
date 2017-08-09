package globs.core;

import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import globs.panels.ModelPanel;
import globs.panels.SimulationPanel;

public class GlobsView {
  
  private JFrame frame;

  public GlobsView()
  {
    frame = new JFrame();
    frame.setTitle("Rm-4120 Green Globs");
    frame.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
          System.exit(0);
      }
  });
  }
  
  public void init(SimulationPanel simPanel, ModelPanel modelPanel)
  {
    frame.setLayout(new GridLayout(1, 2));
    frame.add(modelPanel);
    frame.add(simPanel);
    frame.pack();
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
  }
 
}
