package com.jediterm.terminal.ui;

import java.awt.Component;
import javax.swing.JComponent;

public interface AbstractTabs<T extends Component> {
  int getTabCount();
  
  void addTab(String paramString, T paramT);
  
  String getTitleAt(int paramInt);
  
  int getSelectedIndex();
  
  void setSelectedIndex(int paramInt);
  
  void setTabComponentAt(int paramInt, Component paramComponent);
  
  int indexOfComponent(Component paramComponent);
  
  int indexOfTabComponent(Component paramComponent);
  
  void removeAll();
  
  void remove(T paramT);
  
  void setTitleAt(int paramInt, String paramString);
  
  void setSelectedComponent(T paramT);
  
  JComponent getComponent();
  
  T getComponentAt(int paramInt);
  
  void addChangeListener(TabChangeListener paramTabChangeListener);
  
  public static interface TabChangeListener {
    void tabRemoved();
    
    void selectionChanged();
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jediterm\termina\\ui\AbstractTabs.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */