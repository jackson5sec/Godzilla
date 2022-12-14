package org.fife.rsta.ac.js.ecma.api.e4x;

import org.fife.rsta.ac.js.ecma.api.ecma3.JSFunction;
import org.fife.rsta.ac.js.ecma.api.ecma3.JSString;
import org.fife.rsta.ac.js.ecma.api.ecma3.functions.JSObjectFunctions;

public abstract class E4XQName implements JSObjectFunctions {
  public E4XQName protype;
  
  protected JSFunction constructor;
  
  protected JSString localName;
  
  protected JSString uri;
  
  public E4XQName() {}
  
  public E4XQName(JSString name) {}
  
  public E4XQName(E4XNamespace namespace, JSString name) {}
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\js\ecma\api\e4x\E4XQName.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */