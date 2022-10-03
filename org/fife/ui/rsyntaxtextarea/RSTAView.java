package org.fife.ui.rsyntaxtextarea;

import java.awt.Rectangle;
import javax.swing.text.BadLocationException;

interface RSTAView {
  int yForLine(Rectangle paramRectangle, int paramInt) throws BadLocationException;
  
  int yForLineContaining(Rectangle paramRectangle, int paramInt) throws BadLocationException;
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rsyntaxtextarea\RSTAView.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */