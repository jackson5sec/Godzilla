package org.fife.rsta.ac.js.ecma.api.client;

import org.fife.rsta.ac.js.ecma.api.client.funtions.HistoryFunctions;
import org.fife.rsta.ac.js.ecma.api.ecma3.JSFunction;
import org.fife.rsta.ac.js.ecma.api.ecma3.JSNumber;

public abstract class History implements HistoryFunctions {
  protected JSFunction constructor;
  
  public History prototype;
  
  public JSNumber length;
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\js\ecma\api\client\History.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */