package org.fife.rsta.ui.search;

import java.util.EventListener;

public interface SearchListener extends EventListener {
  void searchEvent(SearchEvent paramSearchEvent);
  
  String getSelectedText();
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rst\\ui\search\SearchListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */