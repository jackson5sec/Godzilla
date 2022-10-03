package com.jgoodies.forms.factories;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;

public interface ComponentFactory {
  JButton createButton(Action paramAction);
  
  JLabel createLabel(String paramString);
  
  JLabel createReadOnlyLabel(String paramString);
  
  JLabel createTitle(String paramString);
  
  JLabel createHeaderLabel(String paramString);
  
  JComponent createSeparator(String paramString, int paramInt);
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jgoodies\forms\factories\ComponentFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */