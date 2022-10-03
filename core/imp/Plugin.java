package core.imp;

import core.shell.ShellEntity;
import javax.swing.JPanel;

public interface Plugin {
  void init(ShellEntity paramShellEntity);
  
  JPanel getView();
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\core\imp\Plugin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */