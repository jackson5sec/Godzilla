package org.mozilla.javascript.debug;

public interface DebuggableScript {
  boolean isTopLevel();
  
  boolean isFunction();
  
  String getFunctionName();
  
  int getParamCount();
  
  int getParamAndVarCount();
  
  String getParamOrVarName(int paramInt);
  
  String getSourceName();
  
  boolean isGeneratedScript();
  
  int[] getLineNumbers();
  
  int getFunctionCount();
  
  DebuggableScript getFunction(int paramInt);
  
  DebuggableScript getParent();
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\debug\DebuggableScript.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */