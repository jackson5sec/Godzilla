package com.intellij.uiDesigner.lw;

import com.intellij.uiDesigner.shared.BorderType;

public interface IContainer extends IComponent {
  int getComponentCount();
  
  IComponent getComponent(int paramInt);
  
  int indexOfComponent(IComponent paramIComponent);
  
  boolean isXY();
  
  StringDescriptor getBorderTitle();
  
  BorderType getBorderType();
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\intelli\\uiDesigner\lw\IContainer.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */