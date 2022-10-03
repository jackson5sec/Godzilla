package org.fife.rsta.ac.js.ecma.api.dom;

import org.fife.rsta.ac.js.ecma.api.ecma3.JSFunction;
import org.fife.rsta.ac.js.ecma.api.ecma5.functions.JS5ObjectFunctions;
import org.w3c.dom.DOMImplementation;

public abstract class JSDOMImplementation implements DOMImplementation, JS5ObjectFunctions {
  public JSDOMImplementation protype;
  
  protected JSFunction constructor;
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\js\ecma\api\dom\JSDOMImplementation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */