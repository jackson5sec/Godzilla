package com.intellij.uiDesigner.lw;

import org.jdom.Element;

public abstract class LayoutSerializer {
  abstract void readLayout(Element paramElement, LwContainer paramLwContainer);
  
  abstract void readChildConstraints(Element paramElement, LwComponent paramLwComponent);
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\intelli\\uiDesigner\lw\LayoutSerializer.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */