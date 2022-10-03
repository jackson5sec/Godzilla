package org.mozilla.javascript.tools.debugger;

public interface GuiCallback {
  void updateSourceText(Dim.SourceInfo paramSourceInfo);
  
  void enterInterrupt(Dim.StackFrame paramStackFrame, String paramString1, String paramString2);
  
  boolean isGuiEventThread();
  
  void dispatchNextGuiEvent() throws InterruptedException;
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\tools\debugger\GuiCallback.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */