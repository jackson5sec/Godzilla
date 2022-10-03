package org.fife.ui.rsyntaxtextarea;

import javax.swing.event.HyperlinkEvent;

public interface LinkGeneratorResult {
  HyperlinkEvent execute();
  
  int getSourceOffset();
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rsyntaxtextarea\LinkGeneratorResult.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */