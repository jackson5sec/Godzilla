package org.fife.rsta.ac.js.ecma.api.ecma3;

import org.fife.rsta.ac.js.ecma.api.ecma3.functions.JSFunctionFunctions;

public abstract class JSFunction implements JSFunctionFunctions {
  protected JSNumber length;
  
  public JSFunction prototype;
  
  protected JSFunction constructor;
  
  public JSFunction(JSString argument_names, JSString body) {}
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\js\ecma\api\ecma3\JSFunction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */