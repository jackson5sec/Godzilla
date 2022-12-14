package org.fife.rsta.ac.js.ecma.api.ecma3;

import org.fife.rsta.ac.js.ecma.api.ecma3.functions.JSRegExpFunctions;

public abstract class JSRegExp implements JSRegExpFunctions {
  public JSRegExp prototype;
  
  protected JSFunction constructor;
  
  protected JSString source;
  
  protected JSBoolean global;
  
  protected JSBoolean ignoreCase;
  
  protected JSBoolean multiline;
  
  protected JSNumber lastIndex;
  
  public JSRegExp(JSString pattern, JSString attributes) {}
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\js\ecma\api\ecma3\JSRegExp.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */