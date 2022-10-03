package org.fife.rsta.ac.js.ecma.api.ecma3;

import org.fife.rsta.ac.js.ecma.api.ecma3.functions.JSObjectFunctions;

public abstract class JSError implements JSObjectFunctions {
  public JSError prototype;
  
  protected JSFunction constructor;
  
  protected JSString name;
  
  protected JSString message;
  
  public JSError() {}
  
  public JSError(JSString message) {}
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\js\ecma\api\ecma3\JSError.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */