package com.jgoodies.forms.util;

import java.awt.Component;

public interface UnitConverter {
  int inchAsPixel(double paramDouble, Component paramComponent);
  
  int millimeterAsPixel(double paramDouble, Component paramComponent);
  
  int centimeterAsPixel(double paramDouble, Component paramComponent);
  
  int pointAsPixel(int paramInt, Component paramComponent);
  
  int dialogUnitXAsPixel(int paramInt, Component paramComponent);
  
  int dialogUnitYAsPixel(int paramInt, Component paramComponent);
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jgoodies\form\\util\UnitConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */