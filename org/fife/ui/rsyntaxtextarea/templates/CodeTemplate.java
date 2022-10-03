package org.fife.ui.rsyntaxtextarea.templates;

import java.io.Serializable;
import javax.swing.text.BadLocationException;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

public interface CodeTemplate extends Cloneable, Comparable<CodeTemplate>, Serializable {
  Object clone();
  
  String getID();
  
  void invoke(RSyntaxTextArea paramRSyntaxTextArea) throws BadLocationException;
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rsyntaxtextarea\templates\CodeTemplate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */