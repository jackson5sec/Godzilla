package com.jgoodies.forms.layout;

import java.awt.Container;
import java.util.List;

public interface Size {
  int maximumSize(Container paramContainer, List paramList, FormLayout.Measure paramMeasure1, FormLayout.Measure paramMeasure2, FormLayout.Measure paramMeasure3);
  
  boolean compressible();
  
  String encode();
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jgoodies\forms\layout\Size.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */