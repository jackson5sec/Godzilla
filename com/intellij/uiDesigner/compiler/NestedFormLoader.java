package com.intellij.uiDesigner.compiler;

import com.intellij.uiDesigner.lw.LwRootContainer;

public interface NestedFormLoader {
  LwRootContainer loadForm(String paramString) throws Exception;
  
  String getClassToBindName(LwRootContainer paramLwRootContainer);
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\intelli\\uiDesigner\compiler\NestedFormLoader.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */