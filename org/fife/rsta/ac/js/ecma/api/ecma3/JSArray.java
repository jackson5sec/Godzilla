package org.fife.rsta.ac.js.ecma.api.ecma3;

import org.fife.rsta.ac.js.ecma.api.ecma3.functions.JSArrayFunctions;

public abstract class JSArray implements JSArrayFunctions {
  public JSNumber length;
  
  public JSArray prototype;
  
  protected JSFunction constructor;
  
  public JSArray() {}
  
  public JSArray(JSNumber size) {}
  
  public JSArray(JSObject element0, JSObject elementn) {}
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\js\ecma\api\ecma3\JSArray.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */