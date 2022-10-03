package org.fife.rsta.ac.js.ecma.api.dom;

import org.fife.rsta.ac.js.ecma.api.ecma3.JSFunction;
import org.fife.rsta.ac.js.ecma.api.ecma5.functions.JS5ObjectFunctions;
import org.w3c.dom.Element;

public abstract class JSElement implements Element, JS5ObjectFunctions {
  public JSElement protype;
  
  protected JSFunction constructor;
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\js\ecma\api\dom\JSElement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */