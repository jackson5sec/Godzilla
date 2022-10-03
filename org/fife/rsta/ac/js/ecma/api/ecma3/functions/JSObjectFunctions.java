package org.fife.rsta.ac.js.ecma.api.ecma3.functions;

import org.fife.rsta.ac.js.ecma.api.ecma3.JSBoolean;
import org.fife.rsta.ac.js.ecma.api.ecma3.JSObject;
import org.fife.rsta.ac.js.ecma.api.ecma3.JSString;

public interface JSObjectFunctions {
  String toString();
  
  JSString toLocaleString();
  
  JSObject valueOf();
  
  JSBoolean hasOwnProperty(String paramString);
  
  JSBoolean isPrototypeOf(JSObject paramJSObject);
  
  JSBoolean propertyIsEnumerable(JSObject paramJSObject);
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\js\ecma\api\ecma3\functions\JSObjectFunctions.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */