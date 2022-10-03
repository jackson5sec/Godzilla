package org.fife.rsta.ac.js.completion;

public interface JSCompletion extends JSCompletionUI {
  String getLookupName();
  
  String getType(boolean paramBoolean);
  
  String getEnclosingClassName(boolean paramBoolean);
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\js\completion\JSCompletion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */