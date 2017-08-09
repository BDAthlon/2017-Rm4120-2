package globs;

import javax.swing.SwingUtilities;

import globs.core.GlobsController;

public class GlobsRunner {

  public static void main(String[] args) {
    // TODO Auto-generated method stub
    SwingUtilities.invokeLater(() -> {
      GlobsController ex = new GlobsController();
      ex.init();
  });
  }
}
